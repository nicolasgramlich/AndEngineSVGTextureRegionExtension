package com.larvalabs.svgandroid.adt;

import java.util.HashMap;

import org.xml.sax.Attributes;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Shader;

import com.larvalabs.svgandroid.adt.gradient.SVGGradient;
import com.larvalabs.svgandroid.adt.gradient.SVGGradient.Stop;
import com.larvalabs.svgandroid.adt.gradient.SVGLinearGradient;
import com.larvalabs.svgandroid.adt.gradient.SVGRadialGradient;
import com.larvalabs.svgandroid.exception.SVGParseException;
import com.larvalabs.svgandroid.util.SAXHelper;
import com.larvalabs.svgandroid.util.SVGNumberParser;
import com.larvalabs.svgandroid.util.SVGNumberParser.SVGNumberParserIntegerResult;
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
	private final ISVGColorMapper mSVGColorMapper;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SVGPaint(final Paint pPaint) {
		this(pPaint, null);
	}

	public SVGPaint(final Paint pPaint, final ISVGColorMapper pSVGColorMapper) {
		this.mPaint = pPaint;
		this.mSVGColorMapper = pSVGColorMapper;
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

	public void resetPaint(final Style pStyle) {
		this.mPaint.reset();
		this.mPaint.setAntiAlias(true); // TODO AntiAliasing could be made optional through some SVGOptions object.
		this.mPaint.setStyle(pStyle);
	}

	public boolean setColor(final SVGProperties pSVGProperties, final boolean pModeFill) {
		final String colorProperty = pSVGProperties.getStringProperty(pModeFill ? "fill" : "stroke");
		if(colorProperty == null) {
			return false;
		}

		if(colorProperty.startsWith("url(#")) {
			final String id = colorProperty.substring("url(#".length(), colorProperty.length() - 1);

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
			if(!pModeFill) {
				this.applyStrokeProperties(pSVGProperties);
			}
			return true;
		} else {
			final Integer color = this.parseColor(colorProperty);
			if(color != null) {
				this.applyColor(pSVGProperties, color, pModeFill);
				if(!pModeFill) {
					this.applyStrokeProperties(pSVGProperties);
				}
				return true;
			} else {
				return false;
			}
		}
	}

	private void applyStrokeProperties(final SVGProperties pSVGProperties) {
		final Float width = pSVGProperties.getFloatProperty("stroke-width");
		if (width != null) {
			this.mPaint.setStrokeWidth(width);
		}
		final String linecap = pSVGProperties.getStringProperty("stroke-linecap");
		if ("round".equals(linecap)) {
			this.mPaint.setStrokeCap(Paint.Cap.ROUND);
		} else if ("square".equals(linecap)) {
			this.mPaint.setStrokeCap(Paint.Cap.SQUARE);
		} else if ("butt".equals(linecap)) {
			this.mPaint.setStrokeCap(Paint.Cap.BUTT);
		}
		final String linejoin = pSVGProperties.getStringProperty("stroke-linejoin");
		if ("miter".equals(linejoin)) {
			this.mPaint.setStrokeJoin(Paint.Join.MITER);
		} else if ("round".equals(linejoin)) {
			this.mPaint.setStrokeJoin(Paint.Join.ROUND);
		} else if ("bevel".equals(linejoin)) {
			this.mPaint.setStrokeJoin(Paint.Join.BEVEL);
		}
	}

	private void applyColor(final SVGProperties pSVGProperties, final Integer pColor, final boolean pModeFill) {
		final int c = (ColorUtils.COLOR_MASK_32BIT_ARGB_RGB & pColor) | ColorUtils.COLOR_MASK_32BIT_ARGB_ALPHA;
		this.mPaint.setColor(c);
		this.mPaint.setAlpha(this.getAlpha(pSVGProperties, pModeFill));
	}

	private int getAlpha(final SVGProperties pSVGProperties, final boolean pModeFill) {
		Float opacity = pSVGProperties.getFloatProperty("opacity");
		if(opacity == null) {
			opacity = pSVGProperties.getFloatProperty(pModeFill ? "fill-opacity" : "stroke-opacity");
		}
		if(opacity == null) {
			return 255;
		} else {
			return (int) (255 * opacity);
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
			return this.applySVGColorMapper(pDefault);
		} else {
			return color;
		}
	}

	private Integer parseColor(final String pString) {
		final Integer parsedColor;
		if(pString == null) {
			parsedColor = null;
		} else if(pString.startsWith("#")) {
			final String hexColorString = pString.substring(1).trim();
			if(hexColorString.length() == 3) {
				final int parsedInt = Integer.parseInt(hexColorString, 16);
				final int red = (parsedInt & ColorUtils.COLOR_MASK_12BIT_RGB_R) >> 8;
				final int green = (parsedInt & ColorUtils.COLOR_MASK_12BIT_RGB_G) >> 4;
				final int blue = (parsedInt & ColorUtils.COLOR_MASK_12BIT_RGB_B) >> 0;
				/* Generate color, duplicating the bits, so that i.e.: #F46 gets #FFAA66. */
				parsedColor = Color.argb(0, (red << 4) | red, (green << 4) | green, (blue << 4) | blue);
			} else if(hexColorString.length() == 6) {
				parsedColor = Integer.parseInt(hexColorString, 16);
			} else {
				parsedColor = null;
			}
		} else if(pString.startsWith("rgb(")) {
			final SVGNumberParserIntegerResult svgNumberParserIntegerResult = SVGNumberParser.parseInts(pString.substring("rgb(".length(), pString.indexOf(')')));
			if(svgNumberParserIntegerResult.getNumberCount() == 3) {
				parsedColor = Color.argb(0, svgNumberParserIntegerResult.getNumber(0), svgNumberParserIntegerResult.getNumber(1), svgNumberParserIntegerResult.getNumber(2));
			} else {
				parsedColor = null;
			}
		} else {
			final Integer colorByName = ColorUtils.getColorByName(pString.trim());
			if(colorByName != null) {
				parsedColor = colorByName;
			} else {
				parsedColor = Integer.parseInt(pString, 16);
			}
		}
		return this.applySVGColorMapper(parsedColor);
	}

	private Integer applySVGColorMapper(final Integer parsedColor) {
		if(this.mSVGColorMapper == null) {
			return parsedColor;
		} else {
			return this.mSVGColorMapper.mapColor(parsedColor);
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
		final int rgb = this.parseColor(stopColor.trim(), Color.BLACK);
		final int alpha = this.parseGradientStopAlpha(pSVGProperties);
		return new Stop(offset, alpha | rgb);
	}

	private int parseGradientStopAlpha(final SVGProperties pSVGProperties) {
		final String opacityStyle = pSVGProperties.getStringProperty("stop-opacity");
		if(opacityStyle != null) {
			final float alpha = Float.parseFloat(opacityStyle);
			final int alphaInt = Math.round(255 * alpha);
			return (alphaInt << 24);
		} else {
			return ColorUtils.COLOR_MASK_32BIT_ARGB_ALPHA;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
