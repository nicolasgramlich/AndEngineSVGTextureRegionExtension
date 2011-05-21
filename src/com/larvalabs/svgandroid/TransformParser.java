package com.larvalabs.svgandroid;

import android.graphics.Matrix;

import com.larvalabs.svgandroid.NumberParser.NumberParserResult;

/**
 * @author Larva Labs, LLC
 * @author Nicolas Gramlich
 * @since 16:56:54 - 21.05.2011
 */
public class TransformParser {
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

	public static Matrix parseTransform(final String s) {
		if (s.startsWith("matrix(")) {
			return TransformParser.parseTransformMatrix(s);
		} else if (s.startsWith("translate(")) {
			return TransformParser.parseTransformTranslate(s);
		} else if (s.startsWith("scale(")) {
			return 	TransformParser.parseTransformScale(s);
		} else if (s.startsWith("skewX(")) {
			return TransformParser.parseTransformSkewX(s);
		} else if (s.startsWith("skewY(")) {
			return TransformParser.parseTransformSkewY(s);
		} else if (s.startsWith("rotate(")) {
			return TransformParser.parseTransformRotate(s);
		} else {
			return null;
		}
	}

	public static Matrix parseTransformRotate(final String s) {
		final NumberParserResult numberParserResult = NumberParser.parse(s.substring("rotate(".length()));
		if (numberParserResult.getNumbers().size() > 0) {
			final float angle = numberParserResult.getNumber(0);
			float cx = 0;
			float cy = 0;
			if (numberParserResult.getNumbers().size() > 2) {
				cx = numberParserResult.getNumber(1);
				cy = numberParserResult.getNumber(2);
			}
			final Matrix matrix = new Matrix();
			matrix.postTranslate(cx, cy);
			matrix.postRotate(angle);
			matrix.postTranslate(-cx, -cy);
			return matrix;
		}
		return null;
	}

	public static Matrix parseTransformSkewY(final String s) {
		final NumberParserResult numberParserResult = NumberParser.parse(s.substring("skewY(".length()));
		if (numberParserResult.getNumbers().size() > 0) {
			final float angle = numberParserResult.getNumber(0);
			final Matrix matrix = new Matrix();
			matrix.postSkew(0, (float) Math.tan(angle));
			return matrix;
		}
		return null;
	}

	static Matrix parseTransformSkewX(final String s) {
		final NumberParserResult numberParserResult = NumberParser.parse(s.substring("skewX(".length()));
		if (numberParserResult.getNumbers().size() > 0) {
			final float angle = numberParserResult.getNumber(0);
			final Matrix matrix = new Matrix();
			matrix.postSkew((float) Math.tan(angle), 0);
			return matrix;
		}
		return null;
	}

	public static Matrix parseTransformScale(final String s) {
		final NumberParserResult numberParserResult = NumberParser.parse(s.substring("scale(".length()));
		if (numberParserResult.getNumbers().size() > 0) {
			final float sx = numberParserResult.getNumber(0);
			float sy = 0;
			if (numberParserResult.getNumbers().size() > 1) {
				sy = numberParserResult.getNumber(1);
			}
			final Matrix matrix = new Matrix();
			matrix.postScale(sx, sy);
			return matrix;
		}
		return null;
	}

	public static Matrix parseTransformTranslate(final String s) {
		final NumberParserResult numberParserResult = NumberParser.parse(s.substring("translate(".length()));
		if (numberParserResult.getNumbers().size() > 0) {
			final float tx = numberParserResult.getNumber(0);
			float ty = 0;
			if (numberParserResult.getNumbers().size() > 1) {
				ty = numberParserResult.getNumber(1);
			}
			final Matrix matrix = new Matrix();
			matrix.postTranslate(tx, ty);
			return matrix;
		}
		return null;
	}

	public static Matrix parseTransformMatrix(final String s) {
		final NumberParserResult numberParserResult = NumberParser.parse(s.substring("matrix(".length()));
		if (numberParserResult.getNumbers().size() == 6) {
			final Matrix matrix = new Matrix();
			matrix.setValues(new float[]{
					// Row 1
					numberParserResult.getNumber(0),
					numberParserResult.getNumbers().get(2),
					numberParserResult.getNumber(4),
					// Row 2
					numberParserResult.getNumber(1),
					numberParserResult.getNumber(3),
					numberParserResult.getNumber(5),
					// Row 3
					0,
					0,
					1,
			});
			return matrix;
		}
		return null;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
