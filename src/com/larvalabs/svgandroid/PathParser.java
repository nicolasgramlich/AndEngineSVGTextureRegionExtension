package com.larvalabs.svgandroid;

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
	static Path parse(final String pString) {
		final int n = pString.length();
		final ParserHelper parserHelper = new ParserHelper(pString, 0);
		parserHelper.skipWhitespace();
		final Path p = new Path();
		float lastX = 0;
		float lastY = 0;
		float lastX1 = 0;
		float lastY1 = 0;
		while (parserHelper.getPosition() < n) {
			final char cmd = pString.charAt(parserHelper.getPosition());
			//Util.debug("* Commands remaining: '" + path + "'.");
			parserHelper.advance();
			boolean wasCurve = false;
			switch (cmd) {
				case 'M':
				case 'm': {
					final float x = parserHelper.nextFloat();
					final float y = parserHelper.nextFloat();
					if (cmd == 'm') {
						p.rMoveTo(x, y);
						lastX += x;
						lastY += y;
					} else {
						p.moveTo(x, y);
						lastX = x;
						lastY = y;
					}
					break;
				}
				case 'Z':
				case 'z': {
					p.close();
					break;
				}
				case 'L':
				case 'l': {
					final float x = parserHelper.nextFloat();
					final float y = parserHelper.nextFloat();
					if (cmd == 'l') {
						p.rLineTo(x, y);
						lastX += x;
						lastY += y;
					} else {
						p.lineTo(x, y);
						lastX = x;
						lastY = y;
					}
					break;
				}
				case 'H':
				case 'h': {
					final float x = parserHelper.nextFloat();
					if (cmd == 'h') {
						p.rLineTo(x, 0);
						lastX += x;
					} else {
						p.lineTo(x, lastY);
						lastX = x;
					}
					break;
				}
				case 'V':
				case 'v': {
					final float y = parserHelper.nextFloat();
					if (cmd == 'v') {
						p.rLineTo(0, y);
						lastY += y;
					} else {
						p.lineTo(lastX, y);
						lastY = y;
					}
					break;
				}
				case 'C':
				case 'c': {
					wasCurve = true;
					float x1 = parserHelper.nextFloat();
					float y1 = parserHelper.nextFloat();
					float x2 = parserHelper.nextFloat();
					float y2 = parserHelper.nextFloat();
					float x = parserHelper.nextFloat();
					float y = parserHelper.nextFloat();
					if (cmd == 'c') {
						x1 += lastX;
						x2 += lastX;
						x += lastX;
						y1 += lastY;
						y2 += lastY;
						y += lastY;
					}
					p.cubicTo(x1, y1, x2, y2, x, y);
					lastX1 = x2;
					lastY1 = y2;
					lastX = x;
					lastY = y;
					break;
				}
				case 'S':
				case 's': {
					wasCurve = true;
					float x2 = parserHelper.nextFloat();
					float y2 = parserHelper.nextFloat();
					float x = parserHelper.nextFloat();
					float y = parserHelper.nextFloat();
					if (cmd == 's') {
						x2 += lastX;
						x += lastX;
						y2 += lastY;
						y += lastY;
					}
					final float x1 = 2 * lastX - lastX1;
					final float y1 = 2 * lastY - lastY1;
					p.cubicTo(x1, y1, x2, y2, x, y);
					lastX1 = x2;
					lastY1 = y2;
					lastX = x;
					lastY = y;
					break;
				}
				case 'A':
				case 'a': {
//					final float rx = parserHelper.nextFloat();
//					final float ry = parserHelper.nextFloat();
//					final float theta = parserHelper.nextFloat();
//					final int largeArc = (int) parserHelper.nextFloat();
//					final int sweepArc = (int) parserHelper.nextFloat();
					final float x = parserHelper.nextFloat();
					final float y = parserHelper.nextFloat();
					// TODO - not implemented yet, may be very hard to do using Android drawing facilities.
					lastX = x;
					lastY = y;
					break;
				}
			}
			if (!wasCurve) {
				lastX1 = lastX;
				lastY1 = lastY;
			}
			parserHelper.skipWhitespace();
		}
		return p;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
