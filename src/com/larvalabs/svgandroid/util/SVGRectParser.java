package com.larvalabs.svgandroid.util;

import android.graphics.Canvas;

import com.larvalabs.svgandroid.adt.SVGPaint;
import com.larvalabs.svgandroid.adt.SVGProperties;

/**
 * @author Nicolas Gramlich
 * @since 19:27:42 - 25.05.2011
 */
public class SVGRectParser {
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
		final float x = pSVGProperties.getFloatAttribute("x", 0f);
		final float y = pSVGProperties.getFloatAttribute("y", 0f);
		final float width = pSVGProperties.getFloatAttribute("width", 0f);
		final float height = pSVGProperties.getFloatAttribute("height", 0f);
		// TODO Support rounded corners "rx"/"ry". --> this.mCanvas.drawRoundRect(rect, rx, ry, paint) 
		if (pSVGPaint.setFill(pSVGProperties)) {
			pSVGPaint.ensureComputedBoundsInclude(x, y, width, height);
			pCanvas.drawRect(x, y, x + width, y + height, pSVGPaint.getPaint());
		}
		if (pSVGPaint.setStroke(pSVGProperties)) {
			// TODO are we missing a this.ensureComputedBoundsInclude(...); here?
			pCanvas.drawRect(x, y, x + width, y + height, pSVGPaint.getPaint());
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
