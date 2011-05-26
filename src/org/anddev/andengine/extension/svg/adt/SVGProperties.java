package org.anddev.andengine.extension.svg.adt;

import org.anddev.andengine.extension.svg.util.SAXHelper;
import org.anddev.andengine.extension.svg.util.SVGNumberParser;
import org.anddev.andengine.extension.svg.util.SVGNumberParser.SVGNumberParserIntegerResult;
import org.anddev.andengine.extension.svg.util.SVGParserUtils;
import org.anddev.andengine.extension.svg.util.constants.ColorUtils;
import org.anddev.andengine.extension.svg.util.constants.ISVGConstants;
import org.xml.sax.Attributes;

import android.graphics.Color;


/**
 * @author Larva Labs, LLC
 * @author Nicolas Gramlich
 * @since 16:49:55 - 21.05.2011
 */
public class SVGProperties implements ISVGConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final SVGStyleSet mSVGStyleSet;
	private final Attributes mAttributes;
	private final SVGProperties mParentSVGProperties;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SVGProperties(final Attributes pAttributes, final SVGProperties pParentSVGProperties) {
		this.mAttributes = pAttributes;
		this.mParentSVGProperties = pParentSVGProperties;
		final String styleAttr = SAXHelper.getStringAttribute(pAttributes, ATTRIBUTE_STYLE);
		if (styleAttr != null) {
			this.mSVGStyleSet = new SVGStyleSet(styleAttr);
		} else {
			this.mSVGStyleSet = null;
		}
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

	public String getStringProperty(final String pPropertyName) {
		return this.getStringProperty(pPropertyName, true);
	}

	public String getStringProperty(final String pPropertyName, final boolean pAllowParentSVGProperties) {
		String s = null;
		if (this.mSVGStyleSet != null) {
			s = this.mSVGStyleSet.getStyle(pPropertyName);
		}
		if (s == null) {
			s = SAXHelper.getStringAttribute(this.mAttributes, pPropertyName);
		}
		if(s == null && pAllowParentSVGProperties) {
			if(this.mParentSVGProperties == null) {
				return null;
			} else {
				return this.mParentSVGProperties.getStringProperty(pPropertyName);
			}
		} else {
			return s;
		}
	}

	public String getStringProperty(final String pPropertyName, final String pDefaultValue) {
		final String s = this.getStringProperty(pPropertyName);
		if (s == null) {
			return pDefaultValue;
		} else {
			return s;
		}
	}

	public Float getFloatProperty(final String pPropertyName) {
		return SVGParserUtils.parseFloatAttribute(this.getStringProperty(pPropertyName));
	}

	public Float getFloatProperty(final String pPropertyName, final float pDefaultValue) {
		final Float f = this.getFloatProperty(pPropertyName);
		if (f == null) {
			return pDefaultValue;
		} else {
			return f;
		}
	}

	public String getStringAttribute(final String pAttributeName) {
		return SAXHelper.getStringAttribute(this.mAttributes, pAttributeName);
	}

	public String getStringAttribute(final String pAttributeName, final String pDefaultValue) {
		return SAXHelper.getStringAttribute(this.mAttributes, pAttributeName, pDefaultValue);
	}

	public Float getFloatAttribute(final String pAttributeName) {
		return SAXHelper.getFloatAttribute(this.mAttributes, pAttributeName);
	}

	public float getFloatAttribute(final String pAttributeName, final float pDefaultValue) {
		return SAXHelper.getFloatAttribute(this.mAttributes, pAttributeName, pDefaultValue);
	}

	// ===========================================================
	// Property-Testing-Methods
	// ===========================================================

	public static boolean isURLProperty(final String pProperty) {
		return pProperty.startsWith("url(#");
	}

	public static boolean isRGBProperty(final String pProperty) {
		return pProperty.startsWith("rgb(");
	}

	public static boolean isHexProperty(final String pProperty) {
		return pProperty.startsWith("#");
	}

	public static String extractIDFromURLProperty(final String pProperty) {
		return pProperty.substring("url(#".length(), pProperty.length() - 1);
	}

	public static Integer extractColorFromRGBProperty(final String pProperty) {
		final SVGNumberParserIntegerResult svgNumberParserIntegerResult = SVGNumberParser.parseInts(pProperty.substring("rgb(".length(), pProperty.indexOf(')')));
		if(svgNumberParserIntegerResult.getNumberCount() == 3) {
			return Color.argb(0, svgNumberParserIntegerResult.getNumber(0), svgNumberParserIntegerResult.getNumber(1), svgNumberParserIntegerResult.getNumber(2));
		} else {
			return null;
		}
	}

	public static Integer extraColorIntegerProperty(final String pProperty) {
		return Integer.parseInt(pProperty, 16);
	}

	public static Integer extractColorFromHexProperty(final String pProperty) {
		final String hexColorString = pProperty.substring(1).trim();
		if(hexColorString.length() == 3) {
			final int parsedInt = Integer.parseInt(hexColorString, 16);
			final int red = (parsedInt & ColorUtils.COLOR_MASK_12BIT_RGB_R) >> 8;
			final int green = (parsedInt & ColorUtils.COLOR_MASK_12BIT_RGB_G) >> 4;
			final int blue = (parsedInt & ColorUtils.COLOR_MASK_12BIT_RGB_B) >> 0;
			/* Generate color, duplicating the bits, so that i.e.: #F46 gets #FFAA66. */
			return Color.argb(0, (red << 4) | red, (green << 4) | green, (blue << 4) | blue);
		} else if(hexColorString.length() == 6) {
			return Integer.parseInt(hexColorString, 16);
		} else {
			return null;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}