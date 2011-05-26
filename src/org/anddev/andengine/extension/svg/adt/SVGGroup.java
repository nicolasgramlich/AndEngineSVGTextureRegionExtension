package org.anddev.andengine.extension.svg.adt;

/**
 * @author Nicolas Gramlich
 * @since 12:58:32 - 24.05.2011
 */
public class SVGGroup {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final SVGProperties mSVGProperties;
	private final boolean mHasTransform;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SVGGroup(final SVGProperties pSVGProperties, final boolean pHasTransform) {
		this.mSVGProperties = pSVGProperties;
		this.mHasTransform = pHasTransform;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean hasTransform() {
		return this.mHasTransform;
	}

	public SVGProperties getSVGProperties() {
		return this.mSVGProperties;
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
