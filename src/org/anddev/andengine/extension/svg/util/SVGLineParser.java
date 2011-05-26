package org.anddev.andengine.extension.svg.util;

import org.anddev.andengine.extension.svg.adt.SVGPaint;
import org.anddev.andengine.extension.svg.adt.SVGProperties;

import android.graphics.Canvas;


/**
 * @author Nicolas Gramlich
 * @since 19:53:50 - 25.05.2011
 */
public class SVGLineParser {
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
		final float x1 = pSVGProperties.getFloatAttribute("x1", 0f);
		final float x2 = pSVGProperties.getFloatAttribute("x2", 0f);
		final float y1 = pSVGProperties.getFloatAttribute("y1", 0f);
		final float y2 = pSVGProperties.getFloatAttribute("y2", 0f);
		if (pSVGPaint.setStroke(pSVGProperties)) {
			pSVGPaint.ensureComputedBoundsInclude(x1, y1);
			pSVGPaint.ensureComputedBoundsInclude(x2, y2);
			pCanvas.drawLine(x1, y1, x2, y2, pSVGPaint.getPaint());
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
