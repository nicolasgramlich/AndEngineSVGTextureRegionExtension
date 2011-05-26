package org.anddev.andengine.extension.svg.util;

import org.anddev.andengine.extension.svg.util.constants.ISVGConstants;


/**
 * @author Nicolas Gramlich
 * @since 17:43:24 - 22.05.2011
 */
public class SVGParserUtils implements ISVGConstants {
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
				if (pString.endsWith(UNIT_PX)) {
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
