package com.larvalabs.svgandroid.gradient;

import java.util.ArrayList;

import android.graphics.Matrix;
import android.graphics.Shader;

/**
 * @author Larva Labs, LLC
 * @author Nicolas Gramlich
 * @since 16:50:09 - 21.05.2011
 */
public abstract class Gradient {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected String mID;
	protected Matrix mMatrix = null;
	protected String mXLink;

	protected ArrayList<Float> mOffsets = new ArrayList<Float>();
	protected ArrayList<Integer> mColors = new ArrayList<Integer>();

	// ===========================================================
	// Constructors
	// ===========================================================

	public Gradient(final String pID, final Matrix pMatrix, final String pXLink) {
		this.mID = pID;
		this.mMatrix = pMatrix;
		this.mXLink = pXLink;
	}

	public static Gradient deriveChild(final Gradient pParent, final Gradient pGradient) {
		final Matrix childMatrix;
		if (pGradient.mMatrix == null) {
			/* Child inherits parent matrix. */
			childMatrix = pParent.mMatrix;
		} else {
			/* Child inherits gradient matrix. */
			childMatrix = pGradient.mMatrix;
		}

		final Gradient child = pGradient.copy(pGradient.mID, childMatrix, pParent.mXLink);
		if(pGradient.mOffsets.size() > 0) {
			child.mOffsets = pGradient.mOffsets;
			child.mColors = pGradient.mColors;
		} else {
			child.mOffsets = pParent.mOffsets;
			child.mColors = pParent.mColors;
		}
		return child;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean hasXLink() {
		return this.mXLink != null;
	}

	public String getXLink() {
		return this.mXLink;
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

	protected abstract Gradient copy(final String pID, final Matrix pMatrix, final String pXLink);
	public abstract Shader createShader();

	// ===========================================================
	// Methods
	// ===========================================================

	protected int[] getColorArray() {
		final int[] colors = new int[this.mColors.size()];
		for (int i = 0; i < colors.length; i++) {
			colors[i] = this.mColors.get(i);
		}
		return colors;
	}

	protected float[] getPositionArray() {
		final float[] positions = new float[this.mOffsets.size()];
		for (int i = 0; i < positions.length; i++) {
			positions[i] = this.mOffsets.get(i);
		}
		return positions;
	}

	public void addStop(final float pOffset, final int pColor) {
		this.mOffsets.add(pOffset);
		this.mColors.add(pColor);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}