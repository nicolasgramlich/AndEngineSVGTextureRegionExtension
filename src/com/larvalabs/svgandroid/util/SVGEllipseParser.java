package com.larvalabs.svgandroid.util;

import android.graphics.Canvas;
import android.graphics.RectF;

import com.larvalabs.svgandroid.adt.SVGPaint;
import com.larvalabs.svgandroid.adt.SVGProperties;

/**
 * @author Nicolas Gramlich
 * @since 19:57:25 - 25.05.2011
 */
public class SVGEllipseParser {
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

	public static void parse(final SVGProperties pSVGProperties, final Canvas pCanvas, final SVGPaint pSVGPaint, final RectF pRect) {
		final Float centerX = pSVGProperties.getFloatAttribute("cx");
		final Float centerY = pSVGProperties.getFloatAttribute("cy");
		final Float radiusX = pSVGProperties.getFloatAttribute("rx");
		final Float radiusY = pSVGProperties.getFloatAttribute("ry");
		if (centerX != null && centerY != null && radiusX != null && radiusY != null) {
			pRect.set(centerX - radiusX, centerY - radiusY, centerX + radiusX, centerY + radiusY);
			if (pSVGPaint.setFill(pSVGProperties)) {
				pSVGPaint.ensureComputedBoundsInclude(centerX - radiusX, centerY - radiusY);
				pSVGPaint.ensureComputedBoundsInclude(centerX + radiusX, centerY + radiusY);
				pCanvas.drawOval(pRect, pSVGPaint.getPaint());
			}
			if (pSVGPaint.setStroke(pSVGProperties)) {
				// TODO are we missing a this.ensureComputedBoundsInclude(...); here?
				pCanvas.drawOval(pRect, pSVGPaint.getPaint());
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
