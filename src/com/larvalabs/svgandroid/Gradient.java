package com.larvalabs.svgandroid;

import java.util.ArrayList;

import android.graphics.Matrix;

/**
 * @author Larva Labs, LLC
 * @author Nicolas Gramlich
 * @since 16:50:09 - 21.05.2011
 */
public class Gradient {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	String mID;
	private boolean mXLinkUnresolved;
	String mXLink;
	boolean isLinear;
	float x1, y1, x2, y2;
	float x, y, radius;
	ArrayList<Float> mPositions = new ArrayList<Float>();
	ArrayList<Integer> mColors = new ArrayList<Integer>();
	Matrix mMatrix = null;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Gradient createChild(final Gradient pGradient) {
		final Gradient child = new Gradient();
		child.mID = pGradient.mID;
		child.mXLink = this.mID;
		child.isLinear = pGradient.isLinear;
		child.x1 = pGradient.x1;
		child.x2 = pGradient.x2;
		child.y1 = pGradient.y1;
		child.y2 = pGradient.y2;
		child.x = pGradient.x;
		child.y = pGradient.y;
		child.radius = pGradient.radius;
		child.mPositions = this.mPositions;
		child.mColors = this.mColors;
		child.mMatrix = this.mMatrix;
		if (pGradient.mMatrix != null) {
			if (this.mMatrix == null) {
				child.mMatrix = pGradient.mMatrix;
			} else {
				final Matrix m = new Matrix(this.mMatrix);
				m.preConcat(pGradient.mMatrix);
				child.mMatrix = m;
			}
		}
		return child;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void markXLinkUnresolved() {
		this.mXLinkUnresolved = true;
	}

	public boolean isXLinkUnresolved() {
		return this.mXLinkUnresolved;
	}

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