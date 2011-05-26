package org.anddev.andengine.extension.svg.util;

import org.anddev.andengine.extension.svg.adt.SVGPaint;
import org.anddev.andengine.extension.svg.adt.SVGProperties;
import org.anddev.andengine.extension.svg.util.SVGNumberParser.SVGNumberParserFloatResult;
import org.anddev.andengine.extension.svg.util.constants.ISVGConstants;

import android.graphics.Canvas;
import android.graphics.Path;


/**
 * @author Nicolas Gramlich
 * @since 19:23:07 - 24.05.2011
 */
public class SVGPolylineParser implements ISVGConstants {
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

	public static void parse(final SVGProperties pSVGProperties, final Canvas pCanvas, final SVGPaint pSVGPaint) {
		final SVGNumberParserFloatResult svgNumberParserFloatResult = SVGNumberParser.parseFloats(pSVGProperties.getStringAttribute(ATTRIBUTE_POINTS));
		if (svgNumberParserFloatResult != null) {
			final float[] points = svgNumberParserFloatResult.getNumbers();
			if (points.length >= 2) {
				final Path path = SVGPolylineParser.parse(points);
				if (pSVGPaint.setFill(pSVGProperties)) {
					pSVGPaint.ensureComputedBoundsInclude(path);
					pCanvas.drawPath(path, pSVGPaint.getPaint());
				}
				if (pSVGPaint.setStroke(pSVGProperties)) {
					// TODO are we missing a this.ensureComputedBoundsInclude(...); here?
					pCanvas.drawPath(path, pSVGPaint.getPaint());
				}
			}
		}
	}

	static Path parse(final float[] pPoints) {
		final Path path = new Path();
		path.moveTo(pPoints[0], pPoints[1]);
		for (int i = 2; i < pPoints.length; i += 2) {
			final float x = pPoints[i];
			final float y = pPoints[i + 1];
			path.lineTo(x, y);
		}
		return path;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
