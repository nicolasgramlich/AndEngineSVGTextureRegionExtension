package com.larvalabs.svgandroid;

import org.xml.sax.Attributes;

/**
 * @author Larva Labs, LLC
 * @author Nicolas Gramlich
 * @since 16:49:55 - 21.05.2011
 */
public class Properties {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	private final StyleSet mStyles;
	private final Attributes mAttributes;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Properties(final Attributes pAttributes) {
		this.mAttributes = pAttributes;
		final String styleAttr = SAXHelper.getStringAttribute(pAttributes, "style");
		if (styleAttr != null) {
			this.mStyles = new StyleSet(styleAttr);
		} else {
			this.mStyles = null;
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

	public String getAttribute(final String pName) {
		String v = null;
		if (this.mStyles != null) {
			v = this.mStyles.getStyle(pName);
		}
		if (v == null) {
			v = SAXHelper.getStringAttribute(this.mAttributes, pName);
		}
		return v;
	}

	public String getString(final String pName) {
		return this.getAttribute(pName);
	}

	public Integer getHex(final String pName) {
		final String v = this.getAttribute(pName);
		if (v == null || !v.startsWith("#")) {
			return null;
		} else {
			try {
				return Integer.parseInt(v.substring(1), 16);
			} catch (final NumberFormatException nfe) {
				// todo - parse word-based color here
				return null;
			}
		}
	}

	public Float getFloat(final String pName, final float defaultValue) {
		final Float v = this.getFloat(pName);
		if (v == null) {
			return defaultValue;
		} else {
			return v;
		}
	}

	public Float getFloat(final String pName) {
		String v = this.getAttribute(pName);
		if (v == null) {
			return null;
		} else {
			try {
				if (v.endsWith("px")) {
					v = v.substring(0, v.length() - 2);
				}
				return Float.parseFloat(v);
			} catch (final NumberFormatException nfe) {
				return null;
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}