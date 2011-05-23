package com.larvalabs.svgandroid.adt;

import org.xml.sax.Attributes;

import com.larvalabs.svgandroid.util.SAXHelper;

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

	// ===========================================================
	// Constructors
	// ===========================================================

	public SVGProperties(final Attributes pAttributes) {
		this.mAttributes = pAttributes;
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
		return s;
	}

	public Float getFloatProperty(final String pPropertyName, final float pDefaultValue) {
		final Float f = this.getFloatProperty(pPropertyName);
		if (f == null) {
			return pDefaultValue;
		} else {
			return f;
		}
	}

	public Float getFloatProperty(final String pPropertyName) {
		final String f = this.getStringProperty(pPropertyName);
		if (f == null) {
			return null;
		} else {
			try {
				if (f.endsWith("px")) {
					return Float.parseFloat(f.substring(0, f.length() - 2));
				} else {
					return Float.parseFloat(f);
				}
			} catch (final NumberFormatException nfe) {
				return null;
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}