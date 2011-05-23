package com.larvalabs.svgandroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.anddev.andengine.util.Debug;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.RectF;
import android.graphics.Shader;

import com.larvalabs.svgandroid.NumberParser.NumberParserResult;

/**
 * @author Larva Labs, LLC
 * @author Nicolas Gramlich
 * @since 16:50:02 - 21.05.2011
 */
public class SVGHandler extends DefaultHandler {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int COLOR_MASK_RGB = 0xFFFFFF;
	private static final int COLOR_MASK_ALPHA = 0xFF000000;

	private static final Pattern RGB_PATTERN = Pattern.compile("rgb\\((.*[\\d]+),.*([\\d]+),.*([\\d]+).*\\)");

	// ===========================================================
	// Fields
	// ===========================================================

	private final Picture mPicture;
	private Canvas mCanvas;
	private final Paint mPaint;
	private final RectF mRect = new RectF();
	private RectF mBounds;
	private final RectF mLimits = new RectF(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);

	private boolean mPushed;

	private final HashMap<String, Shader> mGradientShaderMap = new HashMap<String, Shader>();
	private final HashMap<String, Gradient> mGradientMap = new HashMap<String, Gradient>();
	private Gradient mCurrentGradient;

	private boolean mHidden;
	private int mHiddenLevel;
	private boolean mBoundsMode;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SVGHandler(final Picture pPicture) {
		this.mPicture = pPicture;
		this.mPaint = new Paint();
		this.mPaint.setAntiAlias(true);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public RectF getBounds() {
		return this.mBounds;
	}

	public RectF getLimits() {
		return this.mLimits;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void startElement(final String pNamespace, final String pLocalName, final String pQualifiedName, final Attributes pAttributes) throws SAXException {
		/* Ignore everything but rectangles in bounds mode. */
		if (this.mBoundsMode) {
			if (pLocalName.equals("rect")) {
				final float x = SVGParser.getFloatAttribute(pAttributes, "x", 0f);
				final float y = SVGParser.getFloatAttribute(pAttributes, "y", 0f);
				final float width = SVGParser.getFloatAttribute(pAttributes, "width", 0f);
				final float height = SVGParser.getFloatAttribute(pAttributes, "height", 0f);
				this.mBounds = new RectF(x, y, x + width, y + height);
			}
			return;
		}
		if (pLocalName.equals("svg")) {
			final int width = (int) Math.ceil(SVGParser.getFloatAttribute(pAttributes, "width", 0f));
			final int height = (int) Math.ceil(SVGParser.getFloatAttribute(pAttributes, "height", 0f));
			this.mCanvas = this.mPicture.beginRecording(width, height);
		} else if (pLocalName.equals("defs")) {
			// Ignore
		} else if (pLocalName.equals("linearGradient")) {
			this.mCurrentGradient = GradientParser.parse(pAttributes, true);
		} else if (pLocalName.equals("radialGradient")) {
			this.mCurrentGradient = GradientParser.parse(pAttributes, false);
		} else if (pLocalName.equals("stop")) {
			this.parseStop(pAttributes);
		} else if (pLocalName.equals("g")) {
			// Check to see if this is the "bounds" layer
			if ("bounds".equalsIgnoreCase(SAXHelper.getStringAttribute(pAttributes, "id"))) {
				this.mBoundsMode = true;
			}
			if (this.mHidden) {
				this.mHiddenLevel++;
			}
			// Go in to hidden mode if display is "none"
			if ("none".equals(SAXHelper.getStringAttribute(pAttributes, "display"))) {
				if (!this.mHidden) {
					this.mHidden = true;
					this.mHiddenLevel = 1;
				}
			}
		} else if (!this.mHidden && pLocalName.equals("rect")) {
			final float x = SVGParser.getFloatAttribute(pAttributes, "x", 0f);
			final float y = SVGParser.getFloatAttribute(pAttributes, "y", 0f);
			final float width = SVGParser.getFloatAttribute(pAttributes, "width", 0f);
			final float height = SVGParser.getFloatAttribute(pAttributes, "height", 0f);
			this.pushTransform(pAttributes);
			final Properties properties = new Properties(pAttributes);
			if (this.setFill(properties)) {
				this.setLimits(x, y, width, height);
				this.mCanvas.drawRect(x, y, x + width, y + height, this.mPaint);
			}
			if (this.setStroke(properties)) {
				this.mCanvas.drawRect(x, y, x + width, y + height, this.mPaint);
			}
			this.popTransform();
		} else if (!this.mHidden && pLocalName.equals("line")) {
			final float x1 = SVGParser.getFloatAttribute(pAttributes, "x1", 0f);
			final float x2 = SVGParser.getFloatAttribute(pAttributes, "x2", 0f);
			final float y1 = SVGParser.getFloatAttribute(pAttributes, "y1", 0f);
			final float y2 = SVGParser.getFloatAttribute(pAttributes, "y2", 0f);
			final Properties properties = new Properties(pAttributes);
			if (this.setStroke(properties)) {
				this.pushTransform(pAttributes);
				this.setLimits(x1, y1);
				this.setLimits(x2, y2);
				this.mCanvas.drawLine(x1, y1, x2, y2, this.mPaint);
				this.popTransform();
			}
		} else if (!this.mHidden && pLocalName.equals("circle")) {
			final Float centerX = SVGParser.getFloatAttribute(pAttributes, "cx");
			final Float centerY = SVGParser.getFloatAttribute(pAttributes, "cy");
			final Float radius = SVGParser.getFloatAttribute(pAttributes, "r");
			if (centerX != null && centerY != null && radius != null) {
				this.pushTransform(pAttributes);
				final Properties properties = new Properties(pAttributes);
				if (this.setFill(properties)) {
					this.setLimits(centerX - radius, centerY - radius);
					this.setLimits(centerX + radius, centerY + radius);
					this.mCanvas.drawCircle(centerX, centerY, radius, this.mPaint);
				}
				if (this.setStroke(properties)) {
					this.mCanvas.drawCircle(centerX, centerY, radius, this.mPaint);
				}
				this.popTransform();
			}
		} else if (!this.mHidden && pLocalName.equals("ellipse")) {
			final Float centerX = SVGParser.getFloatAttribute(pAttributes, "cx");
			final Float centerY = SVGParser.getFloatAttribute(pAttributes, "cy");
			final Float radiusX = SVGParser.getFloatAttribute(pAttributes, "rx");
			final Float radiusY = SVGParser.getFloatAttribute(pAttributes, "ry");
			if (centerX != null && centerY != null && radiusX != null && radiusY != null) {
				this.pushTransform(pAttributes);
				final Properties properties = new Properties(pAttributes);
				this.mRect.set(centerX - radiusX, centerY - radiusY, centerX + radiusX, centerY + radiusY);
				if (this.setFill(properties)) {
					this.setLimits(centerX - radiusX, centerY - radiusY);
					this.setLimits(centerX + radiusX, centerY + radiusY);
					this.mCanvas.drawOval(this.mRect, this.mPaint);
				}
				if (this.setStroke(properties)) {
					this.mCanvas.drawOval(this.mRect, this.mPaint);
				}
				this.popTransform();
			}
		} else if (!this.mHidden && (pLocalName.equals("polygon") || pLocalName.equals("polyline"))) {
			final NumberParserResult numberParserResult = NumberParser.parseFromAttributes(pAttributes, "points");
			if (numberParserResult != null) {
				final Path p = new Path();
				final ArrayList<Float> points = numberParserResult.getNumbers();
				if (points.size() > 1) {
					this.pushTransform(pAttributes);
					final Properties properties = new Properties(pAttributes);
					p.moveTo(points.get(0), points.get(1));
					for (int i = 2; i < points.size(); i += 2) {
						final float x = points.get(i);
						final float y = points.get(i + 1);
						p.lineTo(x, y);
					}
					if (!pLocalName.equals("polyline")) {
						p.close();
					}
					if (this.setFill(properties)) {
						this.setLimits(p);
						this.mCanvas.drawPath(p, this.mPaint);
					}
					if (this.setStroke(properties)) {
						this.mCanvas.drawPath(p, this.mPaint);
					}
					this.popTransform();
				}
			}
		} else if (!this.mHidden && pLocalName.equals("path")) {
			final Path p = new PathParser().parse(SAXHelper.getStringAttribute(pAttributes, "d"));
			this.pushTransform(pAttributes);
			final Properties properties = new Properties(pAttributes);
			if (this.setFill(properties)) {
				this.setLimits(p);
				this.mCanvas.drawPath(p, this.mPaint);
			}
			if (this.setStroke(properties)) {
				this.mCanvas.drawPath(p, this.mPaint);
			}
			this.popTransform();
		} else if (!this.mHidden) {
			Debug.d("Unexpected SVG tag: '" + pLocalName +"'!");
		}
	}

	@Override
	public void characters(final char pCharacters[], final int pStart, final int pLength) {
		/* Nothing. */
	}

	@Override
	public void endElement(final String pNamespace, final String pLocalName, final String pQualifiedName)
	throws SAXException {
		if (pLocalName.equals("svg")) {
			this.mPicture.endRecording();
		} else if (pLocalName.equals("linearGradient") || pLocalName.equals("radialGradient")) {
			if (this.mCurrentGradient.getID() != null) {
				this.mGradientMap.put(this.mCurrentGradient.getID(), this.mCurrentGradient);
			}
		} else if (pLocalName.equals("g")) {
			if (this.mBoundsMode) {
				this.mBoundsMode = false;
			}
			// Break out of hidden mode
			if (this.mHidden) {
				this.mHiddenLevel--;
				if (this.mHiddenLevel == 0) {
					this.mHidden = false;
				}
			}
			/* Clear shader map. */
			this.mGradientShaderMap.clear();
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private boolean setFill(final Properties pProperties) {
		if(this.isDisplayNone(pProperties) || this.isFillNone(pProperties)) {
			return false;
		}

		this.resetPaint();

		final String fillString = pProperties.getString("fill");
		if (fillString != null && fillString.startsWith("url(#")) {
			final String id = fillString.substring("url(#".length(), fillString.length() - 1);

			Shader gradientShader = this.mGradientShaderMap.get(id);
			if (gradientShader == null) {
				final Gradient gradient = this.mGradientMap.get(id);
				if(gradient == null) {
					throw new SVGParseException("No gradient found for id: '" + id + "'.");
				} else {
					this.registerGradientShader(gradient);
					gradientShader = this.mGradientShaderMap.get(id);
				}
			}
			this.mPaint.setShader(gradientShader);
			this.mPaint.setStyle(Paint.Style.FILL);
			return true;
		} else {
			this.mPaint.setShader(null);
			final Integer color = pProperties.getHex("fill");
			if (color != null) {
				this.setPaintColor(pProperties, color, true);
				this.mPaint.setStyle(Paint.Style.FILL);
				return true;
			} else if (pProperties.getString("fill") == null && pProperties.getString("stroke") == null) {
				/* Default is black fill. */
				this.mPaint.setStyle(Paint.Style.FILL);
				this.mPaint.setColor(0xFF000000);
				return true;
			}
		}
		return false;
	}

	private void resetPaint() {
		this.mPaint.reset();
		this.mPaint.setAntiAlias(true);
	}

	private boolean setStroke(final Properties pProperties) {
		if(this.isDisplayNone(pProperties) || this.isStrokeNone(pProperties)) {
			return false;
		}

		this.resetPaint();

		final Integer color = pProperties.getHex("stroke");
		if (color != null) {
			// TODO No Shaders for stroke? Could be extracted from setFill to a common method.
			this.setPaintColor(pProperties, color, false);
			final Float width = pProperties.getFloat("stroke-width");
			if (width != null) {
				this.mPaint.setStrokeWidth(width);
			}
			final String linecap = pProperties.getString("stroke-linecap");
			if ("round".equals(linecap)) {
				this.mPaint.setStrokeCap(Paint.Cap.ROUND);
			} else if ("square".equals(linecap)) {
				this.mPaint.setStrokeCap(Paint.Cap.SQUARE);
			} else if ("butt".equals(linecap)) {
				this.mPaint.setStrokeCap(Paint.Cap.BUTT);
			}
			final String linejoin = pProperties.getString("stroke-linejoin");
			if ("miter".equals(linejoin)) {
				this.mPaint.setStrokeJoin(Paint.Join.MITER);
			} else if ("round".equals(linejoin)) {
				this.mPaint.setStrokeJoin(Paint.Join.ROUND);
			} else if ("bevel".equals(linejoin)) {
				this.mPaint.setStrokeJoin(Paint.Join.BEVEL);
			}
			this.mPaint.setStyle(Paint.Style.STROKE);
			return true;
		} else {
			return false;
		}
	}

	private void setPaintColor(final Properties pProperties, final Integer pColor, final boolean pFillMode) {
		final int c = (COLOR_MASK_RGB & pColor) | COLOR_MASK_ALPHA;
		this.mPaint.setColor(c);
		Float opacity = pProperties.getFloat("opacity");
		if (opacity == null) {
			opacity = pProperties.getFloat(pFillMode ? "fill-opacity" : "stroke-opacity");
		}
		if (opacity == null) {
			this.mPaint.setAlpha(255);
		} else {
			this.mPaint.setAlpha((int) (255 * opacity));
		}
	}

	private void setLimits(final float pX, final float pY) {
		if (pX < this.mLimits.left) {
			this.mLimits.left = pX;
		}
		if (pX > this.mLimits.right) {
			this.mLimits.right = pX;
		}
		if (pY < this.mLimits.top) {
			this.mLimits.top = pY;
		}
		if (pY > this.mLimits.bottom) {
			this.mLimits.bottom = pY;
		}
	}

	private void setLimits(final float x, final float y, final float width, final float height) {
		this.setLimits(x, y);
		this.setLimits(x + width, y + height);
	}

	private void setLimits(final Path pPath) {
		pPath.computeBounds(this.mRect, false);
		this.setLimits(this.mRect.left, this.mRect.top);
		this.setLimits(this.mRect.right, this.mRect.bottom);
	}

	private void pushTransform(final Attributes pAttributes) {
		final String transform = SAXHelper.getStringAttribute(pAttributes, "transform");
		this.mPushed = transform != null;
		if (this.mPushed) {
			final Matrix matrix = TransformParser.parseTransform(transform);
			this.mCanvas.save();
			this.mCanvas.concat(matrix);
		}
	}

	private void popTransform() {
		if (this.mPushed) {
			this.mCanvas.restore();
		}
	}

	private void registerGradientShader(final Gradient pGradient) {
		final String gradientID = pGradient.getID();
		if(this.hasGradientShader(pGradient)) {
			/* Nothing to do, as Shader was already created. */
		} else if(pGradient.hasXLink()) {
			final Gradient parent = this.mGradientMap.get(pGradient.getXLink());
			if (parent == null) {
				throw new SVGParseException("Could not resolve xlink: '" + pGradient.getXLink() + "' of gradient: '" + gradientID + "'.");
			} else {
				if(parent.hasXLink() && !this.hasGradientShader(parent)) {
					this.registerGradientShader(parent);
				}
				final Gradient gradient = Gradient.deriveChild(parent, pGradient);

				this.mGradientMap.put(gradientID, gradient);
				this.mGradientShaderMap.put(gradientID, gradient.createShader());
			}
		} else {
			this.mGradientShaderMap.put(gradientID, pGradient.createShader());
		}
	}

	private boolean hasGradientShader(final Gradient pGradient) {
		return this.mGradientShaderMap.containsKey(pGradient.getID());
	}

	private boolean isDisplayNone(final Properties pProperties) {
		return "none".equals(pProperties.getString("display"));
	}

	private boolean isFillNone(final Properties pProperties) {
		return "none".equals(pProperties.getString("fill"));
	}

	private boolean isStrokeNone(final Properties pProperties) {
		return "none".equals(pProperties.getString("stroke"));
	}

	private void parseStop(final Attributes pAttributes) {
		if (this.mCurrentGradient != null) {
			final float offset = SVGParser.getFloatAttribute(pAttributes, "offset", 0f);
			final String styles = SAXHelper.getStringAttribute(pAttributes, "style");
			final StyleSet styleSet = new StyleSet(styles);
			String stopColor = styleSet.getStyle("stop-color");
			int color = Color.BLACK;
			if (stopColor != null) {
				stopColor = stopColor.trim();
				if (stopColor.startsWith("#")) {
					color = Integer.parseInt(stopColor.substring(1), 16);
				} else if(stopColor.startsWith("rgb")) {
					final Matcher matcher = RGB_PATTERN.matcher(stopColor);
					if(matcher.matches() && matcher.groupCount() == 3) {
						final int red = Integer.parseInt(matcher.group(1));
						final int green = Integer.parseInt(matcher.group(2));
						final int blue = Integer.parseInt(matcher.group(3));
						color = Color.rgb(red, green, blue);
					}
				} else {
					color = Integer.parseInt(stopColor, 16);
				}
			}
			final String opacityStyle = styleSet.getStyle("stop-opacity");
			if (opacityStyle != null) {
				final float alpha = Float.parseFloat(opacityStyle);
				final int alphaInt = Math.round(255 * alpha);
				color |= (alphaInt << 24);
			} else {
				color |= 0xFF000000;
			}
			this.mCurrentGradient.addStop(offset, color);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}