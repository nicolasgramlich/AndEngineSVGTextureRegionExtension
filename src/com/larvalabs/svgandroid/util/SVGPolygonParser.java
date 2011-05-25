package com.larvalabs.svgandroid.util;

import android.graphics.Canvas;
import android.graphics.Path;

import com.larvalabs.svgandroid.adt.SVGPaint;
import com.larvalabs.svgandroid.adt.SVGProperties;
import com.larvalabs.svgandroid.util.SVGNumberParser.SVGNumberParserFloatResult;

/**
 * @author Nicolas Gramlich
 * @since 19:47:02 - 25.05.2011
 */
public class SVGPolygonParser {
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
		final SVGNumberParserFloatResult svgNumberParserFloatResult = SVGNumberParser.parseFloats(pSVGProperties.getStringAttribute("points"));
		if (svgNumberParserFloatResult != null) {
			final float[] points = svgNumberParserFloatResult.getNumbers();
			if (points.length >= 2) {
				final Path path = SVGPolylineParser.parse(points);
				path.close();
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

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
