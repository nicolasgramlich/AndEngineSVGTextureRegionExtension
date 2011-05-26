package org.anddev.andengine.extension.svg.util;

import org.anddev.andengine.extension.svg.adt.SVGPaint;
import org.anddev.andengine.extension.svg.adt.SVGProperties;
import org.anddev.andengine.extension.svg.util.constants.ISVGConstants;

import android.graphics.Canvas;
import android.graphics.RectF;


/**
 * @author Nicolas Gramlich
 * @since 19:57:25 - 25.05.2011
 */
public class SVGEllipseParser implements ISVGConstants {
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
		final Float centerX = pSVGProperties.getFloatAttribute(ATTRIBUTE_CENTER_X);
		final Float centerY = pSVGProperties.getFloatAttribute(ATTRIBUTE_CENTER_Y);
		final Float radiusX = pSVGProperties.getFloatAttribute(ATTRIBUTE_RADIUS_X);
		final Float radiusY = pSVGProperties.getFloatAttribute(ATTRIBUTE_RADIUS_Y);
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
