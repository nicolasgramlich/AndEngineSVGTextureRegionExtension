package com.larvalabs.svgandroid;

import java.util.LinkedList;
import java.util.Queue;

import android.graphics.Path;

/**
 * Parses a single SVG path and returns it as a <code>android.graphics.Path</code> object.
 * An example path is <code>M250,150L150,350L350,350Z</code>, which draws a triangle.
 *
 * @see <a href="http://www.w3.org/TR/SVG/paths.html">Specification</a>.
 * 
 * @author Nicolas Gramlich
 * @since 17:16:39 - 21.05.2011
 */
public class PathParser {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private Path mPath;
	private final Queue<Float> mCommandParameters = new LinkedList<Float>();

	private Character mCommand = null;
	private int mCurrentCommandStart = 0;
	private final StringBuilder mCommandParameterStringBuilder = new StringBuilder();
	private float mLastX;
	private float mLastY;
	private float mLastCubicBezierX2;
	private float mLastCubicBezierY2;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * Uppercase rules are absolute positions, lowercase are relative.
	 * Types of path rules:
	 * <p/>
	 * <ol>
	 * <li>M/m - (x y)+ - Move to (without drawing)
	 * <li>Z/z - (no params) - Close path (back to starting point)
	 * <li>L/l - (x y)+ - Line to
	 * <li>H/h - x+ - Horizontal ine to
	 * <li>V/v - y+ - Vertical line to
	 * <li>C/c - (x1 y1 x2 y2 x y)+ - Cubic bezier to
	 * <li>S/s - (x2 y2 x y)+ - Smooth cubic bezier to (shorthand that assumes the x2, y2 from previous C/S is the x1, y1 of this bezier)
	 * <li>Q/q - (x1 y1 x y)+ - Quadratic bezier to
	 * <li>T/t - (x y)+ - Smooth quadratic bezier to (assumes previous control point is "reflection" of last one w.r.t. to current point)
	 * </ol>
	 * <p/>
	 * Numbers are separate by whitespace, comma or nothing at all (!) if they are self-delimiting, (ie. begin with a - sign)
	 */
	public Path parse(final String pString) {
		this.mLastX = 0;
		this.mLastY = 0;
		this.mLastCubicBezierX2 = 0;
		this.mLastCubicBezierY2 = 0;
		this.mCommand = null;
		this.mCommandParameters.clear();
		this.mCommandParameterStringBuilder.setLength(0);
		this.mPath = new Path();

		final int length = pString.length();
		for (int i = 0; i < length; i++) {
			try {
				final char c = pString.charAt(i);
				if (Character.isLetter(c) && (c != 'e')) {
					this.processCommand();

					this.mCommand = c;
					this.mCurrentCommandStart = i;
				} else if ((Character.isDigit(c)) || (c == '-') || (c == '.') || (c == 'e')) {
					// TODO Track if '-' has been read already, as it can be used as a separator too
					// TODO Scientific notation most likely won't work here!
					// TODO --> Solution: make use of ParserHelper.parseFloat? See how ParserHelper was used before.
					this.mCommandParameterStringBuilder.append(c);
				} else {
					this.addParameter();
				}
			} catch(final Throwable t) {
				throw new IllegalArgumentException("Error parsing: " + pString.substring(this.mCurrentCommandStart, i) + " - " + this.mCommand + " - " + this.mCommandParameters.size(), t);
			}
		}
		this.processCommand();
		return this.mPath;
	}

	private void addParameter() {
		if (this.mCommandParameterStringBuilder.length() > 0) {
			this.mCommandParameters.add(Float.parseFloat(this.mCommandParameterStringBuilder.toString()));
			this.mCommandParameterStringBuilder.delete(0, this.mCommandParameterStringBuilder.length());
		}
	}

	private void processCommand() {
		this.addParameter();
		if (this.mCommand != null) {
			// Process command
			this.generatePathElement();
		}
		this.mCommandParameters.clear();
	}

	private void generatePathElement() {
		boolean wasCubicBezierCurve = false;
		switch (this.mCommand) {
			case 'm':
				this.generateMove(false);
				break;
			case 'M':
				this.generateMove(true);
				break;
			case 'l':
				this.generateLine(false);
				break;
			case 'L':
				this.generateLine(true);
				break;
			case 'h':
				this.generateHorizontalLine(false);
				break;
			case 'H':
				this.generateHorizontalLine(true);
				break;
			case 'v':
				this.generateVerticalLine(false);
				break;
			case 'V':
				this.generateVerticalLine(true);
				break;
			case 'c':
				this.generateCubicBezierCurve(false);
				wasCubicBezierCurve = true;
				break;
			case 'C':
				this.generateCubicBezierCurve(true);
				wasCubicBezierCurve = true;
				break;
			case 's':
				this.generateSmoothCubicBezierCurve(false);
				wasCubicBezierCurve = true;
				break;
			case 'S':
				this.generateSmoothCubicBezierCurve(true);
				wasCubicBezierCurve = true;
				break;
			case 'q':
				this.generateQuadraticBezierCurve(false);
				break;
			case 'Q':
				this.generateQuadraticBezierCurve(true);
				break;
			case 't':
				this.generateSmoothQuadraticBezierCurve(false);
				break;
			case 'T':
				this.generateSmoothQuadraticBezierCurve(true);
				break;
			case 'a':
				this.generateArcTo(false);
				break;
			case 'A':
				this.generateArcTo(true);
				break;
			case 'z':
			case 'Z':
				this.generateClosePath();
				break;
			default:
				throw new RuntimeException("Unexpected SVG command: " + this.mCommand);
		}
		if (!wasCubicBezierCurve) {
			this.mLastCubicBezierX2 = this.mLastX;
			this.mLastCubicBezierY2 = this.mLastY;
		}
	}

	private void assertParameterCountMinimum(final int pParameterCount) {
		if (this.mCommandParameters.size() < pParameterCount) {
			throw new RuntimeException("Incorrect parameter count: '" + this.mCommandParameters.size() + "'. Expected at least: '" + pParameterCount + "'.");
		}
	}

	private void assertParameterCount(final int pParameterCount) {
		if (this.mCommandParameters.size() != pParameterCount) {
			throw new RuntimeException("Incorrect parameter count: '" + this.mCommandParameters.size() + "'. Expected: '" + pParameterCount + "'.");
		}
	}

	private void generateMove(final boolean pAbsolute) {
		this.assertParameterCountMinimum(2);
		final float x = this.mCommandParameters.poll();
		final float y = this.mCommandParameters.poll();
		/** Moves the line from mLastX,mLastY to x,y. */
		if (pAbsolute) {
			this.mPath.moveTo(x, y);
			this.mLastX = x;
			this.mLastY = y;
		} else {
			this.mPath.rMoveTo(x, y);
			this.mLastX += x;
			this.mLastY += y;
		}
		if(this.mCommandParameters.size() >= 2) {
			this.generateLine(pAbsolute);
		}
		this.mCommandParameters.clear();
	}

	private void generateLine(final boolean pAbsolute) {
		this.assertParameterCountMinimum(2);
		/** Draws a line from mLastX,mLastY to x,y. */
		if(pAbsolute) {
			while(this.mCommandParameters.size() >= 2) {
				final float x = this.mCommandParameters.poll();
				final float y = this.mCommandParameters.poll();
				this.mPath.lineTo(x, y);
				this.mLastX = x;
				this.mLastY = y;
			}
		} else {
			while(this.mCommandParameters.size() >= 2) {
				final float x = this.mCommandParameters.poll();
				final float y = this.mCommandParameters.poll();
				this.mPath.rLineTo(x, y);
				this.mLastX += x;
				this.mLastY += y;
			}
		}
		this.mCommandParameters.clear();
	}

	private void generateHorizontalLine(final boolean pAbsolute) {
		this.assertParameterCount(1);
		/** Draws a horizontal line to the point defined by mLastY and x. */
		final float x = this.mCommandParameters.poll();
		if(pAbsolute) {
			this.mPath.lineTo(x, this.mLastY);
			this.mLastX = x;
		} else {
			this.mPath.rLineTo(x, 0);
			this.mLastX += x;
		}
	}

	private void generateVerticalLine(final boolean pAbsolute) {
		this.assertParameterCount(1);
		/** Draws a vertical line to the point defined by mLastX and y. */
		final float y = this.mCommandParameters.poll();
		if(pAbsolute) {
			this.mPath.lineTo(this.mLastX, y);
			this.mLastY = y;
		} else {
			this.mPath.rLineTo(0, y);
			this.mLastY += y;
		}
	}

	private void generateCubicBezierCurve(final boolean pAbsolute) {
		this.assertParameterCountMinimum(6);
		/** Draws a cubic bezier curve from current pen point to x,y.
		 * x1,y1 and x2,y2 are start and end control points of the curve. */
		if(pAbsolute) {
			while(this.mCommandParameters.size() >= 6) {
				final float x1 = this.mCommandParameters.poll();
				final float y1 = this.mCommandParameters.poll();
				final float x2 = this.mCommandParameters.poll();
				final float y2 = this.mCommandParameters.poll();
				final float x3 = this.mCommandParameters.poll();
				final float y3 = this.mCommandParameters.poll();
				this.mPath.cubicTo(x1, y1, x2, y2, x3, y3);
				this.mLastCubicBezierX2 = x2;
				this.mLastCubicBezierY2 = y2;
				this.mLastX = x3;
				this.mLastY = y3;
			}
		} else {
			while(this.mCommandParameters.size() >= 6) {
				final float x1 = this.mCommandParameters.poll();
				final float y1 = this.mCommandParameters.poll();
				final float x2 = this.mCommandParameters.poll();
				final float y2 = this.mCommandParameters.poll();
				final float x3 = this.mCommandParameters.poll();
				final float y3 = this.mCommandParameters.poll();
				this.mPath.rCubicTo(x1, y1, x2, y2, x3, y3);
				this.mLastCubicBezierX2 = x2 + this.mLastX;
				this.mLastCubicBezierY2 = y2 + this.mLastY;
				this.mLastX = x3 + this.mLastY;
				this.mLastY = y3 + this.mLastY;
			}
		}
	}

	private void generateSmoothCubicBezierCurve(final boolean pAbsolute) {
		this.assertParameterCountMinimum(4);
		/** Draws a cubic bezier curve from the last point to x,y.
		 * x2,y2 is the end control point.
		 * The start control point is is assumed to be the same as
		 * the end control point of the previous curve. */
		if(pAbsolute) {
			while(this.mCommandParameters.size() >= 4) {
				// TODO Check why x1,y1 where calculated like that? Was only for relative maybe?
				final float x1 = this.mLastCubicBezierX2; // 2 * this.mLastX - this.mLastCubicBezierX2
				final float y1 = this.mLastCubicBezierY2; // 2 * this.mLastY - this.mLastCubicBezierY2
				final float x2 = this.mCommandParameters.poll();
				final float y2 = this.mCommandParameters.poll();
				final float x = this.mCommandParameters.poll();
				final float y = this.mCommandParameters.poll();
				this.mPath.cubicTo(x1, y1, x2, y2, x, y);
				this.mLastCubicBezierX2 = x2;
				this.mLastCubicBezierY2 = y2;
				this.mLastX = x;
				this.mLastY = y;
			}
		} else {
			while(this.mCommandParameters.size() >= 4) {
				final float x1 = this.mLastCubicBezierX2 - this.mLastX; // 2 * this.mLastX - this.mLastCubicBezierX2
				final float y1 = this.mLastCubicBezierY2 - this.mLastY; // 2 * this.mLastY - this.mLastCubicBezierY2
				final float x2 = this.mCommandParameters.poll();
				final float y2 = this.mCommandParameters.poll();
				final float x = this.mCommandParameters.poll();
				final float y = this.mCommandParameters.poll();
				this.mPath.rCubicTo(x1, y1, x2, y2, x, y);
				this.mLastCubicBezierX2 = x2;
				this.mLastCubicBezierY2 = y2;
				this.mLastX = x;
				this.mLastY = y;
			}
		}
	}

	private void generateQuadraticBezierCurve(final boolean pAbsolute) {
		this.assertParameterCount(4);
		// TODO Loop?
		final float x1 = this.mCommandParameters.poll();
		final float y1 = this.mCommandParameters.poll();
		final float x2 = this.mCommandParameters.poll();
		final float y2 = this.mCommandParameters.poll();

		/** Draws a quadratic Bezier curve from mLastX,mLastY x,y. x1,y1 is the control point.. */
		if(pAbsolute) {
			this.mPath.quadTo(x1, y1, x2, y2);
			this.mLastX = x2;
			this.mLastY = y2;
		} else {
			this.mPath.rQuadTo(x1, y1, x2, y2); // TODO rQuadTo?
			this.mLastX += x2;
			this.mLastY += y2;
		}
	}

	private void generateSmoothQuadraticBezierCurve(final boolean pAbsolute) {
		// TODO Implement

		/** Draws a quadratic Bezier curve from mLastX,mLastY to x,y.
		 * The control point is assumed to be the same as the last control point used. */
	}

	private void generateArcTo(final boolean pAbsolute) {
		this.assertParameterCount(7);
		final float rx = this.mCommandParameters.poll();
		final float ry = this.mCommandParameters.poll();
		final float theta = this.mCommandParameters.poll();
		final int largeArc = this.mCommandParameters.poll().intValue();
		final int sweepArc = this.mCommandParameters.poll().intValue();
		final float x = this.mCommandParameters.poll();
		final float y = this.mCommandParameters.poll();
		if(pAbsolute) {
			// TODO Implement
			// lastX = x;
			// lastY = y;
		} else {
			// TODO Implement
			// lastX += x;
			// lastY += y;
		}
	}

	private void generateClosePath() {
		this.assertParameterCount(0);
		this.mPath.close();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
