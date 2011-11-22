package org.anddev.andengine.extension.svg.opengl.texture.atlas.bitmap.source;

import org.anddev.andengine.extension.svg.SVGParser;
import org.anddev.andengine.extension.svg.adt.ISVGColorMapper;
import org.anddev.andengine.extension.svg.adt.SVG;
import org.anddev.andengine.util.debug.Debug;

import android.content.Context;


/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 13:22:48 - 21.05.2011
 */
public class SVGResourceBitmapTextureAtlasSource extends SVGBaseBitmapTextureAtlasSource {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Context mContext;
	private final int mRawResourceID;
	private final ISVGColorMapper mSVGColorMapper;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SVGResourceBitmapTextureAtlasSource(final Context pContext, final int pRawResourceID, final int pTexturePositionX, final int pTexturePositionY) {
		this(pContext, pRawResourceID, pTexturePositionX, pTexturePositionY, null);
	}

	public SVGResourceBitmapTextureAtlasSource(final Context pContext, final int pRawResourceID, final int pTexturePositionX, final int pTexturePositionY, final float pScale) {
		this(pContext, pRawResourceID, pTexturePositionX, pTexturePositionY, pScale, null);
	}

	public SVGResourceBitmapTextureAtlasSource(final Context pContext, final int pRawResourceID, final int pTexturePositionX, final int pTexturePositionY, final int pWidth, final int pHeight) {
		this(pContext, pRawResourceID, pTexturePositionX, pTexturePositionY, pWidth, pHeight, null);
	}

	public SVGResourceBitmapTextureAtlasSource(final Context pContext, final int pRawResourceID, final int pTexturePositionX, final int pTexturePositionY, final ISVGColorMapper pSVGColorMapper) {
		super(SVGResourceBitmapTextureAtlasSource.getSVG(pContext, pRawResourceID, pSVGColorMapper), pTexturePositionX, pTexturePositionY);
		this.mContext = pContext;
		this.mRawResourceID = pRawResourceID;
		this.mSVGColorMapper = pSVGColorMapper;
	}

	public SVGResourceBitmapTextureAtlasSource(final Context pContext, final int pRawResourceID, final int pTexturePositionX, final int pTexturePositionY, final float pScale, final ISVGColorMapper pSVGColorMapper) {
		super(SVGResourceBitmapTextureAtlasSource.getSVG(pContext, pRawResourceID, pSVGColorMapper), pTexturePositionX, pTexturePositionY, pScale);
		this.mContext = pContext;
		this.mRawResourceID = pRawResourceID;
		this.mSVGColorMapper = pSVGColorMapper;
	}

	public SVGResourceBitmapTextureAtlasSource(final Context pContext, final int pRawResourceID, final int pTexturePositionX, final int pTexturePositionY, final int pWidth, final int pHeight, final ISVGColorMapper pSVGColorMapper) {
		super(SVGResourceBitmapTextureAtlasSource.getSVG(pContext, pRawResourceID, pSVGColorMapper), pTexturePositionX, pTexturePositionY, pWidth, pHeight);
		this.mContext = pContext;
		this.mRawResourceID = pRawResourceID;
		this.mSVGColorMapper = pSVGColorMapper;
	}
	
	@Override
	public SVGResourceBitmapTextureAtlasSource deepCopy() {
		return new SVGResourceBitmapTextureAtlasSource(this.mContext, this.mRawResourceID, this.mTexturePositionX, this.mTexturePositionY, this.mWidth, this.mHeight, this.mSVGColorMapper);
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

	private static SVG getSVG(final Context pContext, final int pRawResourceID, final ISVGColorMapper pSVGColorMapper) {
		try {
			return SVGParser.parseSVGFromResource(pContext.getResources(), pRawResourceID, pSVGColorMapper);
		} catch (final Throwable t) {
			Debug.e("Failed loading SVG in SVGResourceBitmapTextureAtlasSource. RawResourceID: " + pRawResourceID, t);
			return null;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
