package org.anddev.andengine.extension.svg.opengl.texture.source;

import org.anddev.andengine.extension.svg.adt.SVG;
import org.anddev.andengine.opengl.texture.bitmap.source.PictureBitmapTextureSource;
import org.anddev.andengine.util.Debug;


/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 13:34:55 - 21.05.2011
 */
public class SVGBaseBitmapTextureSource extends PictureBitmapTextureSource {
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

	public SVGBaseBitmapTextureSource(final SVG pSVG) {
		this(pSVG, 0, 0);
	}

	public SVGBaseBitmapTextureSource(final SVG pSVG, final float pScale) {
		this(pSVG, 0, 0, pScale);
	}

	public SVGBaseBitmapTextureSource(final SVG pSVG, final int pTexturePositionX, final int pTexturePositionY, final float pScale) {
		super(pSVG.getPicture(), pTexturePositionX, pTexturePositionY, pScale);
		this.mSVG = pSVG;
	}

	public SVGBaseBitmapTextureSource(final SVG pSVG, final int pWidth, final int pHeight) {
		this(pSVG, 0, 0, pWidth, pHeight);
	}

	public SVGBaseBitmapTextureSource(final SVG pSVG, final int pTexturePositionX, final int pTexturePositionY, final int pWidth, final int pHeight) {
		super(pSVG.getPicture(), pTexturePositionX, pTexturePositionY, pWidth, pHeight);
		this.mSVG = pSVG;
	}

	@Override
	public SVGBaseBitmapTextureSource clone() {
		Debug.w("SVGBaseBitmapTextureSource.clone() does not actually clone the SVG!");
		return new SVGBaseBitmapTextureSource(this.mSVG, this.mTexturePositionX, this.mTexturePositionY, this.mWidth, this.mHeight);
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
