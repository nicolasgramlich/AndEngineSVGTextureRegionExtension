package com.larvalabs.svgandroid.adt;

import java.util.HashMap;
import java.util.regex.Matcher;

import org.xml.sax.Attributes;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;

import com.larvalabs.svgandroid.adt.gradient.SVGGradient;
import com.larvalabs.svgandroid.adt.gradient.SVGGradient.Stop;
import com.larvalabs.svgandroid.adt.gradient.SVGLinearGradient;
import com.larvalabs.svgandroid.adt.gradient.SVGRadialGradient;
import com.larvalabs.svgandroid.exception.SVGParseException;
import com.larvalabs.svgandroid.util.SAXHelper;
import com.larvalabs.svgandroid.util.SVGTransformParser;
import com.larvalabs.svgandroid.util.constants.ColorUtils;

/**
 * TODO Add ColorMapping - maybe a HashMap<Integer,Integer> and make us of it in
 * parseColor(...). Constructor should take such a ColorMapping object then.
 * 
 * @author Nicolas Gramlich
 * @since 22:01:39 - 23.05.2011
 */
public class SVGPaint {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Paint mPaint;

	private final HashMap<String, Shader> mSVGGradientShaderMap = new HashMap<String, Shader>();
	private final HashMap<String, SVGGradient> mSVGGradientMap = new HashMap<String, SVGGradient>();

	// ===========================================================
	// Constructors
	// ===========================================================

	public SVGPaint(final Paint pPaint) {
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

	public boolean setColor(final SVGProperties pSVGProperties, final String pColorProperty) {
		if(pColorProperty == null) {
			return false;
		}

		if(pColorProperty.startsWith("url(#")) {
			final String id = pColorProperty.substring("url(#".length(), pColorProperty.length() - 1);

			Shader gradientShader = this.mSVGGradientShaderMap.get(id);
			if(gradientShader == null) {
				final SVGGradient svgGradient = this.mSVGGradientMap.get(id);
				if(svgGradient == null) {
					throw new SVGParseException("No gradient found for id: '" + id + "'.");
				} else {
					this.registerGradientShader(svgGradient);
					gradientShader = this.mSVGGradientShaderMap.get(id);
				}
			}
			this.mPaint.setShader(gradientShader);
			return true;
		} else {
			final Integer color = this.parseColor(pColorProperty);
			if(color != null) {
				this.setColor(pSVGProperties, color, true);
				return true;
			} else {
				return false;
			}
		}
	}

	private void setColor(final SVGProperties pSVGProperties, final Integer pColor, final boolean pFillMode) {
		final int c = (ColorUtils.COLOR_MASK_RGB & pColor) | ColorUtils.COLOR_MASK_ALPHA;
		this.mPaint.setColor(c);
		Float opacity = pSVGProperties.getFloatProperty("opacity");
		if(opacity == null) {
			opacity = pSVGProperties.getFloatProperty(pFillMode ? "fill-opacity" : "stroke-opacity");
		}
		if(opacity == null) {
			this.mPaint.setAlpha(255);
		} else {
			this.mPaint.setAlpha((int) (255 * opacity));
		}
	}

	private void registerGradientShader(final SVGGradient pSVGGradient) {
		final String gradientID = pSVGGradient.getID();
		if(this.hasGradientShader(pSVGGradient)) {
			/* Nothing to do, as Shader was already created. */
		} else if(pSVGGradient.hasXLink()) {
			final SVGGradient parent = this.mSVGGradientMap.get(pSVGGradient.getXLink());
			if(parent == null) {
				throw new SVGParseException("Could not resolve xlink: '" + pSVGGradient.getXLink() + "' of gradient: '" + gradientID + "'.");
			} else {
				if(parent.hasXLink() && !this.hasGradientShader(parent)) {
					this.registerGradientShader(parent);
				}
				final SVGGradient svgGradient = SVGGradient.deriveChild(parent, pSVGGradient);

				this.mSVGGradientMap.put(gradientID, svgGradient);
				this.mSVGGradientShaderMap.put(gradientID, svgGradient.createShader());
			}
		} else {
			this.mSVGGradientShaderMap.put(gradientID, pSVGGradient.createShader());
		}
	}

	private boolean hasGradientShader(final SVGGradient pSVGGradient) {
		return this.mSVGGradientShaderMap.containsKey(pSVGGradient.getID());
	}

	public void clearGradientShaders() {
		this.mSVGGradientShaderMap.clear();
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

	public SVGGradient registerGradient(final Attributes pAttributes, final boolean pLinear) {
		final String id = SAXHelper.getStringAttribute(pAttributes, "id");
		if(id == null) {
			return null;
		}
		final Matrix matrix = SVGTransformParser.parseTransform(SAXHelper.getStringAttribute(pAttributes, "gradientTransform"));
		String xlink = SAXHelper.getStringAttribute(pAttributes, "href");
		if(xlink != null) {
			if(xlink.startsWith("#")) {
				xlink = xlink.substring(1);
			}
		}
		final SVGGradient svgGradient;
		if(pLinear) {
			final float x1 = SAXHelper.getFloatAttribute(pAttributes, "x1", 0f);
			final float x2 = SAXHelper.getFloatAttribute(pAttributes, "x2", 0f);
			final float y1 = SAXHelper.getFloatAttribute(pAttributes, "y1", 0f);
			final float y2 = SAXHelper.getFloatAttribute(pAttributes, "y2", 0f);
			svgGradient = new SVGLinearGradient(id, x1, x2, y1, y2, matrix, xlink);
		} else {
			final float centerX = SAXHelper.getFloatAttribute(pAttributes, "cx", 0f);
			final float centerY = SAXHelper.getFloatAttribute(pAttributes, "cy", 0f);
			final float radius = SAXHelper.getFloatAttribute(pAttributes, "r", 0f);
			svgGradient = new SVGRadialGradient(id, centerX, centerY, radius, matrix, xlink);
		}
		this.mSVGGradientMap.put(id, svgGradient);
		return svgGradient;
	}

	public Stop parseGradientStop(final SVGProperties pSVGProperties) {
		final float offset = pSVGProperties.getFloatProperty("offset", 0f);
		final String stopColor = pSVGProperties.getStringProperty("stop-color");
		int color = this.parseColor(stopColor.trim(), Color.BLACK);
		final String opacityStyle = pSVGProperties.getStringProperty("stop-opacity");
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
