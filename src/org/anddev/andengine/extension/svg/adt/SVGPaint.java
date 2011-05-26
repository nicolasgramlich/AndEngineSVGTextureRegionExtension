package org.anddev.andengine.extension.svg.adt;

import java.util.HashMap;

import org.anddev.andengine.extension.svg.adt.gradient.SVGGradient;
import org.anddev.andengine.extension.svg.adt.gradient.SVGGradient.Stop;
import org.anddev.andengine.extension.svg.adt.gradient.SVGLinearGradient;
import org.anddev.andengine.extension.svg.adt.gradient.SVGRadialGradient;
import org.anddev.andengine.extension.svg.exception.SVGParseException;
import org.anddev.andengine.extension.svg.util.SAXHelper;
import org.anddev.andengine.extension.svg.util.SVGParserUtils;
import org.anddev.andengine.extension.svg.util.SVGTransformParser;
import org.anddev.andengine.extension.svg.util.constants.ColorUtils;
import org.anddev.andengine.extension.svg.util.constants.ISVGConstants;
import org.xml.sax.Attributes;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;


/**
 * TODO Add ColorMapping - maybe a HashMap<Integer,Integer> and make us of it in
 * parseColor(...). Constructor should take such a ColorMapping object then.
 * 
 * @author Nicolas Gramlich
 * @since 22:01:39 - 23.05.2011
 */
public class SVGPaint implements ISVGConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Paint mPaint = new Paint();

	/** Multi purpose dummy rectangle. */
	private final RectF mRect = new RectF();
	private final RectF mComputedBounds = new RectF(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);

	private final HashMap<String, Shader> mSVGGradientShaderMap = new HashMap<String, Shader>();
	private final HashMap<String, SVGGradient> mSVGGradientMap = new HashMap<String, SVGGradient>();
	private final ISVGColorMapper mSVGColorMapper;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SVGPaint(final ISVGColorMapper pSVGColorMapper) {
		this.mSVGColorMapper = pSVGColorMapper;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public Paint getPaint() {
		return this.mPaint;
	}

	public RectF getComputedBounds() {
		return this.mComputedBounds;
	}

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

	public boolean setFill(final SVGProperties pSVGProperties) {
		if(this.isDisplayNone(pSVGProperties) || this.isFillNone(pSVGProperties)) {
			return false;
		}

		this.resetPaint(Paint.Style.FILL);

		final String fillProperty = pSVGProperties.getStringProperty(ATTRIBUTE_FILL);
		if(fillProperty == null) {
			if(pSVGProperties.getStringProperty(ATTRIBUTE_STROKE) == null) {
				/* Default is black fill. */
				this.mPaint.setColor(0xFF000000); // TODO Respect color mapping?
				return true;
			} else {
				return false;
			}
		} else {
			return this.setPaintProperties(pSVGProperties, true);
		}
	}

	public boolean setStroke(final SVGProperties pSVGProperties) {
		if(this.isDisplayNone(pSVGProperties) || this.isStrokeNone(pSVGProperties)) {
			return false;
		}

		this.resetPaint(Paint.Style.STROKE);

		return this.setPaintProperties(pSVGProperties, false);
	}

	private boolean isDisplayNone(final SVGProperties pSVGProperties) {
		return VALUE_NONE.equals(pSVGProperties.getStringProperty(ATTRIBUTE_DISPLAY));
	}

	private boolean isFillNone(final SVGProperties pSVGProperties) {
		return VALUE_NONE.equals(pSVGProperties.getStringProperty(ATTRIBUTE_FILL));
	}

	private boolean isStrokeNone(final SVGProperties pSVGProperties) {
		return VALUE_NONE.equals(pSVGProperties.getStringProperty(ATTRIBUTE_STROKE));
	}

	public boolean setPaintProperties(final SVGProperties pSVGProperties, final boolean pModeFill) {
		if(this.applyColorProperties(pSVGProperties, pModeFill)) {
			if(pModeFill) {
				return this.applyFillProperties(pSVGProperties);
			} else {
				return this.applyStrokeProperties(pSVGProperties);
			}
		} else {
			return false;
		}
	}

	private boolean applyColorProperties(final SVGProperties pSVGProperties, final boolean pModeFill) {
		final String colorProperty = pSVGProperties.getStringProperty(pModeFill ? ATTRIBUTE_FILL : ATTRIBUTE_STROKE);
		if(colorProperty == null) {
			return false;
		}

		if(SVGProperties.isURLProperty(colorProperty)) {
			final String id = SVGParserUtils.extractIDFromURLProperty(colorProperty);

			this.mPaint.setShader(this.getGradientShader(id));
			return true;
		} else {
			final Integer color = this.parseColor(colorProperty);
			if(color != null) {
				this.applyColor(pSVGProperties, color, pModeFill);
				return true;
			} else {
				return false;
			}
		}
	}

	private boolean applyFillProperties(final SVGProperties pSVGProperties) {
		return true;
	}

	private boolean applyStrokeProperties(final SVGProperties pSVGProperties) {
		final Float width = pSVGProperties.getFloatProperty(ATTRIBUTE_STROKE_WIDTH);
		if (width != null) {
			this.mPaint.setStrokeWidth(width);
		}
		final String linecap = pSVGProperties.getStringProperty(ATTRIBUTE_STROKE_LINECAP);
		if (ATTRIBUTE_STROKE_LINECAP_VALUE_ROUND.equals(linecap)) {
			this.mPaint.setStrokeCap(Paint.Cap.ROUND);
		} else if (ATTRIBUTE_STROKE_LINECAP_VALUE_SQUARE.equals(linecap)) {
			this.mPaint.setStrokeCap(Paint.Cap.SQUARE);
		} else if (ATTRIBUTE_STROKE_LINECAP_VALUE_BUTT.equals(linecap)) {
			this.mPaint.setStrokeCap(Paint.Cap.BUTT);
		}
		final String linejoin = pSVGProperties.getStringProperty(ATTRIBUTE_STROKE_LINEJOIN_VALUE_);
		if (ATTRIBUTE_STROKE_LINEJOIN_VALUE_MITER.equals(linejoin)) {
			this.mPaint.setStrokeJoin(Paint.Join.MITER);
		} else if (ATTRIBUTE_STROKE_LINEJOIN_VALUE_ROUND.equals(linejoin)) {
			this.mPaint.setStrokeJoin(Paint.Join.ROUND);
		} else if (ATTRIBUTE_STROKE_LINEJOIN_VALUE_BEVEL.equals(linejoin)) {
			this.mPaint.setStrokeJoin(Paint.Join.BEVEL);
		}
		return true;
	}

	private void applyColor(final SVGProperties pSVGProperties, final Integer pColor, final boolean pModeFill) {
		final int c = (ColorUtils.COLOR_MASK_32BIT_ARGB_RGB & pColor) | ColorUtils.COLOR_MASK_32BIT_ARGB_ALPHA;
		this.mPaint.setColor(c);
		this.mPaint.setAlpha(SVGPaint.parseAlpha(pSVGProperties, pModeFill));
	}

	private static int parseAlpha(final SVGProperties pSVGProperties, final boolean pModeFill) {
		Float opacity = pSVGProperties.getFloatProperty(ATTRIBUTE_OPACITY);
		if(opacity == null) {
			opacity = pSVGProperties.getFloatProperty(pModeFill ? ATTRIBUTE_FILL_OPACITY : ATTRIBUTE_STROKE_OPACITY);
		}
		if(opacity == null) {
			return 255;
		} else {
			return (int) (255 * opacity);
		}
	}

	private void registerGradientShader(final SVGGradient pSVGGradient) {
		final String gradientID = pSVGGradient.getID();
		if(pSVGGradient.hasHref()) {
			final SVGGradient parent = this.mSVGGradientMap.get(pSVGGradient.getHref());
			if(parent == null) {
				throw new SVGParseException("Could not resolve href: '" + pSVGGradient.getHref() + "' of gradient: '" + gradientID + "'.");
			} else {
				if(parent.hasHref() && !this.hasGradientShader(parent)) {
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

	public void ensureComputedBoundsInclude(final float pX, final float pY) {
		if (pX < this.mComputedBounds.left) {
			this.mComputedBounds.left = pX;
		}
		if (pX > this.mComputedBounds.right) {
			this.mComputedBounds.right = pX;
		}
		if (pY < this.mComputedBounds.top) {
			this.mComputedBounds.top = pY;
		}
		if (pY > this.mComputedBounds.bottom) {
			this.mComputedBounds.bottom = pY;
		}
	}

	public void ensureComputedBoundsInclude(final float pX, final float pY, final float pWidth, final float pHeight) {
		this.ensureComputedBoundsInclude(pX, pY);
		this.ensureComputedBoundsInclude(pX + pWidth, pY + pHeight);
	}

	public void ensureComputedBoundsInclude(final Path pPath) {
		pPath.computeBounds(this.mRect, false);
		this.ensureComputedBoundsInclude(this.mRect.left, this.mRect.top);
		this.ensureComputedBoundsInclude(this.mRect.right, this.mRect.bottom);
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
		/* TODO Test if explicit pattern matching is faster:
		 * 
		 * RGB:		/^rgb\((\d{1,3}),\s*(\d{1,3}),\s*(\d{1,3})\)$/
		 * #RRGGBB:	/^(\w{2})(\w{2})(\w{2})$/
		 * #RGB:	/^(\w{1})(\w{1})(\w{1})$/
		 */

		final Integer parsedColor;
		if(pString == null) {
			parsedColor = null;
		} else if(SVGProperties.isHexProperty(pString)) {
			parsedColor = SVGParserUtils.extractColorFromHexProperty(pString);
		} else if(SVGProperties.isRGBProperty(pString)) {
			parsedColor = SVGParserUtils.extractColorFromRGBProperty(pString);
		} else {
			final Integer colorByName = ColorUtils.getColorByName(pString.trim());
			if(colorByName != null) {
				parsedColor = colorByName;
			} else {
				parsedColor = SVGParserUtils.extraColorIntegerProperty(pString);
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

	public SVGFilter parseFilter(final Attributes pAttributes) {
		return null; // TODO pretty much like parseGradient
	}

	public SVGGradient parseGradient(final Attributes pAttributes, final boolean pLinear) {
		final String id = SAXHelper.getStringAttribute(pAttributes, ATTRIBUTE_ID);
		if(id == null) {
			return null;
		}

		String href = SAXHelper.getStringAttribute(pAttributes, ATTRIBUTE_HREF);
		if(href != null) {
			if(href.startsWith("#")) {
				href = href.substring(1);
			}
		}

		/* TODO it might be better to simply put a SVGProperties object into the SVGGradient with a linear/radial boolean flag. (Subclasses of SVGGradient then not really needed anymore.)
		 * Parameters like x1,x1,y1,y2,... would be extracted from the SVGProperties when they are actually needed.
		 * This would cover arbitrary deep inheritance of properties that are not covered by the ones extracted here (id,x1,x2,y1,y2,matrix,href). */
		final Matrix matrix = SVGTransformParser.parseTransform(SAXHelper.getStringAttribute(pAttributes, ATTRIBUTE_GRADIENT_TRANSFORM));
		final SVGGradient svgGradient;
		if(pLinear) {
			final float x1 = SAXHelper.getFloatAttribute(pAttributes, ATTRIBUTE_X1, 0f);
			final float x2 = SAXHelper.getFloatAttribute(pAttributes, ATTRIBUTE_X2, 0f);
			final float y1 = SAXHelper.getFloatAttribute(pAttributes, ATTRIBUTE_Y1, 0f);
			final float y2 = SAXHelper.getFloatAttribute(pAttributes, ATTRIBUTE_Y2, 0f);
			svgGradient = new SVGLinearGradient(id, x1, x2, y1, y2, matrix, href);
		} else {
			final float centerX = SAXHelper.getFloatAttribute(pAttributes, ATTRIBUTE_CENTER_X, 0f);
			final float centerY = SAXHelper.getFloatAttribute(pAttributes, ATTRIBUTE_CENTER_Y, 0f);
			final float radius = SAXHelper.getFloatAttribute(pAttributes, ATTRIBUTE_RADIUS, 0f);
			svgGradient = new SVGRadialGradient(id, centerX, centerY, radius, matrix, href);
		}
		this.mSVGGradientMap.put(id, svgGradient);
		return svgGradient;
	}

	public Stop parseGradientStop(final SVGProperties pSVGProperties) {
		final float offset = pSVGProperties.getFloatProperty(ATTRIBUTE_OFFSET, 0f);
		final String stopColor = pSVGProperties.getStringProperty(ATTRIBUTE_STOP_COLOR);
		final int rgb = this.parseColor(stopColor.trim(), Color.BLACK);
		final int alpha = this.parseGradientStopAlpha(pSVGProperties);
		return new Stop(offset, alpha | rgb);
	}

	private int parseGradientStopAlpha(final SVGProperties pSVGProperties) {
		final String opacityStyle = pSVGProperties.getStringProperty(ATTRIBUTE_STOP_OPACITY);
		if(opacityStyle != null) {
			final float alpha = Float.parseFloat(opacityStyle);
			final int alphaInt = Math.round(255 * alpha);
			return (alphaInt << 24);
		} else {
			return ColorUtils.COLOR_MASK_32BIT_ARGB_ALPHA;
		}
	}

	private Shader getGradientShader(final String id) {
		final Shader gradientShader = this.mSVGGradientShaderMap.get(id);
		if(gradientShader == null) {
			final SVGGradient svgGradient = this.mSVGGradientMap.get(id);
			if(svgGradient == null) {
				throw new SVGParseException("No gradient found for id: '" + id + "'.");
			} else {
				if(!this.hasGradientShader(svgGradient)) {
					this.registerGradientShader(svgGradient);
				}
				return this.mSVGGradientShaderMap.get(id);
			}
		} else {
			return gradientShader;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
