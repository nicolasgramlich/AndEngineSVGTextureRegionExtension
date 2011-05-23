package com.larvalabs.svgandroid.util;

import java.util.HashMap;
import java.util.regex.Matcher;

import org.xml.sax.Attributes;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;

import com.larvalabs.svgandroid.SVGParser;
import com.larvalabs.svgandroid.adt.Properties;
import com.larvalabs.svgandroid.adt.StyleSet;
import com.larvalabs.svgandroid.exception.SVGParseException;
import com.larvalabs.svgandroid.gradient.Gradient;
import com.larvalabs.svgandroid.gradient.Gradient.Stop;
import com.larvalabs.svgandroid.gradient.LinearGradient;
import com.larvalabs.svgandroid.gradient.RadialGradient;

/**
 * @author Nicolas Gramlich
 * @since 22:01:39 - 23.05.2011
 */
public class PaintManager {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Paint mPaint;

	private final HashMap<String, Shader> mGradientShaderMap = new HashMap<String, Shader>();
	private final HashMap<String, Gradient> mGradientMap = new HashMap<String, Gradient>();

	// ===========================================================
	// Constructors
	// ===========================================================

	public PaintManager(final Paint pPaint) {
		this.mPaint = pPaint;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean setPaintColor(final Properties pProperties, final String pColorProperty) {
		if(pColorProperty.startsWith("url(#")) {
			final String id = pColorProperty.substring("url(#".length(), pColorProperty.length() - 1);

			Shader gradientShader = this.mGradientShaderMap.get(id);
			if(gradientShader == null) {
				final Gradient gradient = this.mGradientMap.get(id);
				if(gradient == null) {
					throw new SVGParseException("No gradient found for id: '" + id + "'.");
				} else {
					this.registerGradientShader(gradient);
					gradientShader = this.mGradientShaderMap.get(id);
				}
			}
			this.mPaint.setShader(gradientShader);
			return true;
		} else {
			final Integer color = this.parseColor(pColorProperty);
			if(color != null) {
				this.setPaintColor(pProperties, color, true);
				return true;
			} else {
				return false;
			}
		}
	}

	private void setPaintColor(final Properties pProperties, final Integer pColor, final boolean pFillMode) {
		final int c = (ColorUtils.COLOR_MASK_RGB & pColor) | ColorUtils.COLOR_MASK_ALPHA;
		this.mPaint.setColor(c);
		Float opacity = pProperties.getFloatProperty("opacity");
		if(opacity == null) {
			opacity = pProperties.getFloatProperty(pFillMode ? "fill-opacity" : "stroke-opacity");
		}
		if(opacity == null) {
			this.mPaint.setAlpha(255);
		} else {
			this.mPaint.setAlpha((int) (255 * opacity));
		}
	}

	public void registerGradient(final Gradient pGradient) {
		if (pGradient != null && pGradient.getID() != null) {
			this.mGradientMap.put(pGradient.getID(), pGradient);
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

	public void clearGradientShaders() {
		this.mGradientShaderMap.clear();
	}

	// ===========================================================
	// Methods for Color-Parsing
	// ===========================================================

	private Integer parseColor(final String pString, final Integer pDefault) {
		final Integer color = this.parseColor(pString);
		if(color == null) {
			return pDefault;
		} else {
			return color;
		}
	}

	private Integer parseColor(final String pString) {
		if(pString == null) {
			return null;
		} else if(pString.startsWith("#")) {
			return Integer.parseInt(pString.substring(1), 16);
		} else if(pString.startsWith("rgb")) {
			final Matcher matcher = ColorUtils.RGB_PATTERN.matcher(pString);
			if(matcher.matches() && matcher.groupCount() == 3) {
				final int red = Integer.parseInt(matcher.group(1));
				final int green = Integer.parseInt(matcher.group(2));
				final int blue = Integer.parseInt(matcher.group(3));
				return Color.rgb(red, green, blue);
			} else {
				return null;
			}
		} else {
			try {
				return Integer.parseInt(pString.substring(1), 16);
			} catch (final NumberFormatException nfe) {
				return ColorUtils.getColorByName(pString.trim());
			}
		}
	}

	// ===========================================================
	// Methods for Gradient-Parsing
	// ===========================================================

	public Gradient parseGradient(final Attributes pAttributes, final boolean pLinear) {
		final String id = SAXHelper.getStringAttribute(pAttributes, "id");
		if(id == null) {
			return null;
		}
		final Matrix matrix = TransformParser.parseTransform(SAXHelper.getStringAttribute(pAttributes, "gradientTransform"));
		String xlink = SAXHelper.getStringAttribute(pAttributes, "href");
		if(xlink != null) {
			if(xlink.startsWith("#")) {
				xlink = xlink.substring(1);
			}
		}

		if(pLinear) {
			final float x1 = SVGParser.getFloatAttribute(pAttributes, "x1", 0f);
			final float x2 = SVGParser.getFloatAttribute(pAttributes, "x2", 0f);
			final float y1 = SVGParser.getFloatAttribute(pAttributes, "y1", 0f);
			final float y2 = SVGParser.getFloatAttribute(pAttributes, "y2", 0f);
			return new LinearGradient(id, x1, x2, y1, y2, matrix, xlink);
		} else {
			final float centerX = SVGParser.getFloatAttribute(pAttributes, "cx", 0f);
			final float centerY = SVGParser.getFloatAttribute(pAttributes, "cy", 0f);
			final float radius = SVGParser.getFloatAttribute(pAttributes, "r", 0f);
			return new RadialGradient(id, centerX, centerY, radius, matrix, xlink);
		}
	}

	public Stop parseGradientStop(final Attributes pAttributes) {
		final float offset = SVGParser.getFloatAttribute(pAttributes, "offset", 0f);
		final String styles = SAXHelper.getStringAttribute(pAttributes, "style");
		final StyleSet styleSet = new StyleSet(styles);
		final String stopColor = styleSet.getStyle("stop-color");
		int color = this.parseColor(stopColor.trim(), Color.BLACK);
		final String opacityStyle = styleSet.getStyle("stop-opacity");
		if(opacityStyle != null) {
			final float alpha = Float.parseFloat(opacityStyle);
			final int alphaInt = Math.round(255 * alpha);
			color |= (alphaInt << 24);
		} else {
			color |= ColorUtils.COLOR_MASK_ALPHA;
		}
		return new Stop(offset, color);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
