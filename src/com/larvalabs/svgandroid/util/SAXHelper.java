package com.larvalabs.svgandroid.util;

import org.xml.sax.Attributes;

/**
 * @author Nicolas Gramlich
 * @since 14:22:28 - 22.05.2011
 */
public class SAXHelper {
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

	public static String getStringAttribute(final Attributes pAttributes, final String pAttributeName) {
		final int attributeCount = pAttributes.getLength();
		for (int i = 0; i < attributeCount; i++) {
			if (pAttributes.getLocalName(i).equals(pAttributeName)) {
				return pAttributes.getValue(i);
			}
		}
		return null;
	}

	public static Float getFloatAttribute(final Attributes pAttributes, final String pAttributeName) {
		return SVGParserUtils.parseFloatAttribute(SAXHelper.getStringAttribute(pAttributes, pAttributeName));
	}

	public static float getFloatAttribute(final Attributes pAttributes, final String pAttributeName, final float pDefaultValue) {
		final Float f = getFloatAttribute(pAttributes, pAttributeName);
		if(f == null) {
			return pDefaultValue;
		} else {
			return f;
		}
	}

	//	private static Integer getHexAttribute(final Attributes pAttributes, final String pAttributeName)  {
	//		final String v = SAXHelper.getStringAttribute(pAttributes, pAttributeName, null);
	//		if (v == null) {
	//			return null;
	//		} else {
	//			try {
	//				return Integer.parseInt(v.substring(1), 16);
	//			} catch (final NumberFormatException nfe) {
	//				// TODO - parse word-based color here
	//				return null;
	//			}
	//		}
	//	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
