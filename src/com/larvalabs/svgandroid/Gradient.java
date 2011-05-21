package com.larvalabs.svgandroid;

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

	protected boolean mXLinkUnresolved;
	
	protected ArrayList<Float> mOffsets = new ArrayList<Float>();
	protected ArrayList<Integer> mColors = new ArrayList<Integer>();

	// ===========================================================
	// Constructors
	// ===========================================================

	public Gradient(String pID, Matrix pMatrix, String pXLink) {
		this.mID = pID;
		this.mMatrix = pMatrix;
		this.mXLink = pXLink;
	}
	
	public Gradient deriveChild(final Gradient pGradient) {
		final Gradient child = pGradient.copy(pGradient.mID, mMatrix, mXLink);
		child.mOffsets = this.mOffsets;
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

	public boolean hasXLink() {
		return this.mXLink != null;
	}

	public String getXLink() {
		return this.mXLink;
	}

	public boolean isXLinkUnresolved() {
		return this.mXLinkUnresolved;
	}

	public void setXLinkUnresolved(final boolean pXLinkUnresolved) {
		this.mXLinkUnresolved = pXLinkUnresolved;
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