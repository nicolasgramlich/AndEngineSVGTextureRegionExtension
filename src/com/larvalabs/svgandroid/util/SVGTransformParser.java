package com.larvalabs.svgandroid.util;

import android.graphics.Matrix;

import com.larvalabs.svgandroid.util.SVGNumberParser.SVGNumberParserResult;


/**
 * @author Larva Labs, LLC
 * @author Nicolas Gramlich
 * @since 16:56:54 - 21.05.2011
 */
public class SVGTransformParser {
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

	public static Matrix parseTransform(final String pString) {
		if(pString == null) {
			return null;
		}
		
		if (pString.startsWith("matrix(")) {
			return SVGTransformParser.parseTransformMatrix(pString);
		} else if (pString.startsWith("translate(")) {
			return SVGTransformParser.parseTransformTranslate(pString);
		} else if (pString.startsWith("scale(")) {
			return 	SVGTransformParser.parseTransformScale(pString);
		} else if (pString.startsWith("skewX(")) {
			return SVGTransformParser.parseTransformSkewX(pString);
		} else if (pString.startsWith("skewY(")) {
			return SVGTransformParser.parseTransformSkewY(pString);
		} else if (pString.startsWith("rotate(")) {
			return SVGTransformParser.parseTransformRotate(pString);
		} else {
			return null;
		}
	}

	public static Matrix parseTransformRotate(final String s) {
		final SVGNumberParserResult svgNumberParserResult = SVGNumberParser.parse(s.substring("rotate(".length()));
		if (svgNumberParserResult.getNumbers().size() > 0) {
			final float angle = svgNumberParserResult.getNumber(0);
			float cx = 0;
			float cy = 0;
			if (svgNumberParserResult.getNumbers().size() > 2) {
				cx = svgNumberParserResult.getNumber(1);
				cy = svgNumberParserResult.getNumber(2);
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
		final SVGNumberParserResult svgNumberParserResult = SVGNumberParser.parse(s.substring("skewY(".length()));
		if (svgNumberParserResult.getNumbers().size() > 0) {
			final float angle = svgNumberParserResult.getNumber(0);
			final Matrix matrix = new Matrix();
			matrix.postSkew(0, (float) Math.tan(angle));
			return matrix;
		}
		return null;
	}

	static Matrix parseTransformSkewX(final String s) {
		final SVGNumberParserResult svgNumberParserResult = SVGNumberParser.parse(s.substring("skewX(".length()));
		if (svgNumberParserResult.getNumbers().size() > 0) {
			final float angle = svgNumberParserResult.getNumber(0);
			final Matrix matrix = new Matrix();
			matrix.postSkew((float) Math.tan(angle), 0);
			return matrix;
		}
		return null;
	}

	public static Matrix parseTransformScale(final String s) {
		final SVGNumberParserResult svgNumberParserResult = SVGNumberParser.parse(s.substring("scale(".length()));
		if (svgNumberParserResult.getNumbers().size() > 0) {
			final float sx = svgNumberParserResult.getNumber(0);
			float sy = 0;
			if (svgNumberParserResult.getNumbers().size() > 1) {
				sy = svgNumberParserResult.getNumber(1);
			}
			final Matrix matrix = new Matrix();
			matrix.postScale(sx, sy);
			return matrix;
		}
		return null;
	}

	public static Matrix parseTransformTranslate(final String s) {
		final SVGNumberParserResult svgNumberParserResult = SVGNumberParser.parse(s.substring("translate(".length()));
		if (svgNumberParserResult.getNumbers().size() > 0) {
			final float tx = svgNumberParserResult.getNumber(0);
			float ty = 0;
			if (svgNumberParserResult.getNumbers().size() > 1) {
				ty = svgNumberParserResult.getNumber(1);
			}
			final Matrix matrix = new Matrix();
			matrix.postTranslate(tx, ty);
			return matrix;
		}
		return null;
	}

	public static Matrix parseTransformMatrix(final String s) {
		final SVGNumberParserResult svgNumberParserResult = SVGNumberParser.parse(s.substring("matrix(".length()));
		if (svgNumberParserResult.getNumbers().size() == 6) {
			final Matrix matrix = new Matrix();
			matrix.setValues(new float[]{
					// Row 1
					svgNumberParserResult.getNumber(0),
					svgNumberParserResult.getNumber(2),
					svgNumberParserResult.getNumber(4),
					// Row 2
					svgNumberParserResult.getNumber(1),
					svgNumberParserResult.getNumber(3),
					svgNumberParserResult.getNumber(5),
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
