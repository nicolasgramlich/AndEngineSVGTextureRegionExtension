package org.anddev.andengine.extension.svg.opengl.texture.source;

import org.anddev.andengine.opengl.texture.source.PictureTextureSource;
import org.anddev.andengine.util.Debug;

import com.larvalabs.svgandroid.adt.SVG;

/**
 * @author Nicolas Gramlich
 * @since 13:34:55 - 21.05.2011
 */
public class SVGBaseTextureSource extends PictureTextureSource {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final SVG mSVG;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SVGBaseTextureSource(final SVG pSVG) {
		super(pSVG.getPicture());
		this.mSVG = pSVG;
	}

	public SVGBaseTextureSource(final SVG pSVG, final int pWidth, final int pHeight) {
		super(pSVG.getPicture(), pWidth, pHeight);
		this.mSVG = pSVG;
	}

	@Override
	public SVGBaseTextureSource clone() {
		Debug.w("SVGBaseTextureSource.clone() does not actually clone the SVG!");
		return new SVGBaseTextureSource(this.mSVG, this.mWidth, this.mHeight);
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
