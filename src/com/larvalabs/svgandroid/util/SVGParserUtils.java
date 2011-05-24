package com.larvalabs.svgandroid.util;


/**
 * @author Nicolas Gramlich
 * @since 17:43:24 - 22.05.2011
 */
public class SVGParserUtils {
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

	public static Float parseFloatAttribute(final String pString) {
		if (pString == null) {
			return null;
		} else {
			try {
				if (pString.endsWith("px")) {
					return Float.parseFloat(pString.substring(0, pString.length() - 2));
				} else {
					return Float.parseFloat(pString);
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
