package com.larvalabs.svgandroid.util;


/**
 * @author Larva Labs, LLC
 * @author Nicolas Gramlich
 * @since 16:50:17 - 21.05.2011
 */
public class SVGNumberParser {
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

	public static SVGNumberParserResult parse(final String pString) {
		if(pString == null) {
			return null;
		}

		final String[] parts = pString.split("[\\s,]+");
		final float[] numbers = new float[parts.length];
		for(int i = parts.length - 1; i >= 0; i--) {
			numbers[i] = Float.parseFloat(parts[i]);
		}
		
		return new SVGNumberParserResult(numbers);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class SVGNumberParserResult {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final float[] mNumbers;

		// ===========================================================
		// Constructors
		// ===========================================================

		public SVGNumberParserResult(final float[] pNumbers) {
			this.mNumbers = pNumbers;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================


		public float[] getNumbers() {
			return this.mNumbers;
		}

		public int getNumberCount() {
			return this.mNumbers.length;
		}

		public float getNumber(final int pIndex) {
			return this.mNumbers[pIndex];
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}