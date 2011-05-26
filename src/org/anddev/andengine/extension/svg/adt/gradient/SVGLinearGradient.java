package org.anddev.andengine.extension.svg.adt.gradient;

import android.graphics.Matrix;
import android.graphics.Shader;

/**
 * @author Nicolas Gramlich
 * @since 18:30:44 - 21.05.2011
 */
public class SVGLinearGradient extends SVGGradient {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final float mX1;
	private final float mY1;
	private final float mX2;
	private final float mY2;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SVGLinearGradient(final String pID, final float pX1, final float pX2, final float pY1, final float pY2, final Matrix pMatrix, final String pXLink) {
		super(pID, pMatrix, pXLink);
		this.mX1 = pX1;
		this.mX2 = pX2;
		this.mY1 = pY1;
		this.mY2 = pY2;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected SVGGradient copy(final String pID, final Matrix pMatrix, final String pXLink) {
		return new SVGLinearGradient(pID, this.mX1, this.mX2, this.mY1, this.mY2, pMatrix, pXLink);
	}

	@Override
	public Shader createShader() {
		final android.graphics.LinearGradient shader = new android.graphics.LinearGradient(this.mX1, this.mY1, this.mX2, this.mY2, this.getColorArray(), this.getPositionArray(), Shader.TileMode.CLAMP);
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
