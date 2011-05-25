package com.larvalabs.svgandroid.adt;

import java.util.HashMap;

/**
 * @author Nicolas Gramlich
 * @since 09:21:33 - 25.05.2011
 */
public class SVGDirectColorMapper implements ISVGColorMapper {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final HashMap<Integer, Integer> mColorMappings = new HashMap<Integer, Integer>();

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

	public void addColorMapping(final Integer pColorFrom, final Integer pColorTo) {
		this.mColorMappings.put(pColorFrom, pColorTo);
	}

	@Override
	public Integer mapColor(final Integer pColor) {
		final Integer mappedColor = this.mColorMappings.get(pColor);
		if(mappedColor == null) {
			return pColor;
		} else {
			return mappedColor;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
