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

	private final SVGGroup mSVGroupParent;
	private final SVGProperties mSVGProperties;
	private final boolean mHasTransform;
	private final boolean mHidden;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SVGGroup(final SVGGroup pSVGroupParent, final SVGProperties pSVGProperties, final boolean pHasTransform) {
		this.mSVGroupParent = pSVGroupParent;
		this.mSVGProperties = pSVGProperties;
		this.mHasTransform = pHasTransform;
		this.mHidden = this.mSVGroupParent.isHidden() || "none".equals(this.mSVGProperties.getStringProperty("display", false));
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

	public boolean isHidden() {
		return this.mHidden;
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
