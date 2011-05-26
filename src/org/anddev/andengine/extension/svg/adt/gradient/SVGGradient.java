package org.anddev.andengine.extension.svg.adt.gradient;

import java.util.ArrayList;

import android.graphics.Matrix;
import android.graphics.Shader;

/**
 * @author Larva Labs, LLC
 * @author Nicolas Gramlich
 * @since 16:50:09 - 21.05.2011
 */
public abstract class SVGGradient {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected String mID;
	protected Matrix mMatrix = null;
	protected String mHref;

	protected ArrayList<Stop> mStops = new ArrayList<Stop>();

	// ===========================================================
	// Constructors
	// ===========================================================

	public SVGGradient(final String pID, final Matrix pMatrix, final String pHref) {
		this.mID = pID;
		this.mMatrix = pMatrix;
		this.mHref = pHref;
	}

	public static SVGGradient deriveChild(final SVGGradient pParent, final SVGGradient pSVGGradient) {
		final Matrix childMatrix;
		if (pSVGGradient.mMatrix == null) {
			/* Child inherits parent matrix. */
			childMatrix = pParent.mMatrix;
		} else {
			/* Child inherits gradient matrix. */
			childMatrix = pSVGGradient.mMatrix;
		}

		final SVGGradient child = pSVGGradient.copy(pSVGGradient.mID, childMatrix, pParent.mHref);
		if(pSVGGradient.mStops.size() > 0) {
			child.mStops = pSVGGradient.mStops;
		} else {
			child.mStops = pParent.mStops;
		}
		return child;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean hasHref() {
		return this.mHref != null;
	}

	public String getHref() {
		return this.mHref;
	}

	public void setMatrix(final Matrix pMatrix) {
		this.mMatrix = pMatrix;
	}

	public boolean hasID() {
		return this.mID != null;
	}

	public String getID() {
		return this.mID;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract SVGGradient copy(final String pID, final Matrix pMatrix, final String pHref);
	public abstract Shader createShader();

	// ===========================================================
	// Methods
	// ===========================================================

	protected int[] getColorArray() {
		final int[] colors = new int[this.mStops.size()];
		for (int i = 0; i < colors.length; i++) {
			colors[i] = this.mStops.get(i).mColor;
		}
		return colors;
	}

	protected float[] getPositionArray() {
		final float[] positions = new float[this.mStops.size()];
		for (int i = 0; i < positions.length; i++) {
			positions[i] = this.mStops.get(i).mOffset;
		}
		return positions;
	}

	public void addGradientStop(final Stop pStop) {
		this.mStops.add(pStop);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class Stop {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final float mOffset;
		private final int mColor;

		// ===========================================================
		// Constructors
		// ===========================================================

		public Stop(final float pOffset, final int pColor) {
			this.mOffset = pOffset;
			this.mColor = pColor;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}