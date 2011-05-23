package org.anddev.andengine.extension.svg.opengl.texture.source;

import org.anddev.andengine.opengl.texture.source.PictureTextureSource;

import com.larvalabs.svgandroid.SVG;

/**
 * @author Nicolas Gramlich
 * @since 13:34:55 - 21.05.2011
 */
public abstract class SVGBaseTextureSource extends PictureTextureSource {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public SVGBaseTextureSource(final SVG pSVG) {
		super(pSVG.getPicture());
	}

	public SVGBaseTextureSource(final SVG pSVG, final int pWidth, final int pHeight) {
		super(pSVG.getPicture(), pWidth, pHeight);
	}

	@Override
	public abstract SVGBaseTextureSource clone();

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
