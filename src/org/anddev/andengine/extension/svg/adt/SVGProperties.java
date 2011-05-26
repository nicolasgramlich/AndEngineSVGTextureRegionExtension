package org.anddev.andengine.extension.svg.adt;

import org.anddev.andengine.extension.svg.util.SAXHelper;
import org.anddev.andengine.extension.svg.util.SVGParserUtils;
import org.xml.sax.Attributes;


/**
 * @author Larva Labs, LLC
 * @author Nicolas Gramlich
 * @since 16:49:55 - 21.05.2011
 */
public class SVGProperties {
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
		final String styleAttr = SAXHelper.getStringAttribute(pAttributes, "style");
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
		String s = null;
		if (this.mSVGStyleSet != null) {
			s = this.mSVGStyleSet.getStyle(pPropertyName);
		}
		if (s == null) {
			s = SAXHelper.getStringAttribute(this.mAttributes, pPropertyName);
		}
		if(s == null) {
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
	// Inner and Anonymous Classes
	// ===========================================================
}