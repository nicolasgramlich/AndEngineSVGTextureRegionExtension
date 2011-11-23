package org.andengine.extension.svg.opengl.texture.atlas.bitmap.source;

import org.andengine.extension.svg.adt.SVG;
import org.andengine.opengl.texture.atlas.bitmap.source.PictureBitmapTextureAtlasSource;
import org.andengine.util.debug.Debug;


/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 13:34:55 - 21.05.2011
 */
public class SVGBaseBitmapTextureAtlasSource extends PictureBitmapTextureAtlasSource {
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

	public SVGBaseBitmapTextureAtlasSource(final SVG pSVG) {
		this(pSVG, 0, 0);
	}

	public SVGBaseBitmapTextureAtlasSource(final SVG pSVG, final float pScale) {
		this(pSVG, 0, 0, pScale);
	}

	public SVGBaseBitmapTextureAtlasSource(final SVG pSVG, final int pTexturePositionX, final int pTexturePositionY, final float pScale) {
		super(pSVG.getPicture(), pTexturePositionX, pTexturePositionY, pScale);
		this.mSVG = pSVG;
	}

	public SVGBaseBitmapTextureAtlasSource(final SVG pSVG, final int pWidth, final int pHeight) {
		this(pSVG, 0, 0, pWidth, pHeight);
	}

	public SVGBaseBitmapTextureAtlasSource(final SVG pSVG, final int pTexturePositionX, final int pTexturePositionY, final int pWidth, final int pHeight) {
		super(pSVG.getPicture(), pTexturePositionX, pTexturePositionY, pWidth, pHeight);
		this.mSVG = pSVG;
	}

	@Override
	public SVGBaseBitmapTextureAtlasSource deepCopy() {
		Debug.w("SVGBaseBitmapTextureAtlasSource.deepCopy() does not actually deepCopy the SVG!");
		return new SVGBaseBitmapTextureAtlasSource(this.mSVG, this.mTexturePositionX, this.mTexturePositionY, this.mWidth, this.mHeight);
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
