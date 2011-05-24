package com.larvalabs.svgandroid.adt.gradient;

import android.graphics.Matrix;
import android.graphics.Shader;

/**
 * @author Nicolas Gramlich
 * @since 18:34:54 - 21.05.2011
 */
public class SVGRadialGradient extends SVGGradient {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final float mCenterX;
	private final float mCenterY;
	private final float mRadius;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SVGRadialGradient(final String pID, final float pCenterX, final float pCenterY, final float pRadius, final Matrix pMatrix, final String pXLink) {
		super(pID, pMatrix, pXLink);
		this.mCenterX = pCenterX;
		this.mCenterY = pCenterY;
		this.mRadius = pRadius;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected SVGGradient copy(final String pID, final Matrix pMatrix, final String pXLink) {
		return new SVGRadialGradient(pID, this.mCenterX, this.mCenterY, this.mRadius, pMatrix, pXLink);
	}

	@Override
	public Shader createShader() {
		final android.graphics.RadialGradient shader = new android.graphics.RadialGradient(this.mCenterX, this.mCenterY, this.mRadius, this.getColorArray(), this.getPositionArray(), Shader.TileMode.CLAMP);
		if (this.mMatrix != null) {
			shader.setLocalMatrix(this.mMatrix);
		}
		return shader;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
