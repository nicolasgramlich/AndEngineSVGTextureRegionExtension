package org.anddev.andengine.extension.svg.opengl.texture.source;

import org.anddev.andengine.extension.svg.SVGParser;
import org.anddev.andengine.extension.svg.adt.ISVGColorMapper;
import org.anddev.andengine.extension.svg.adt.SVG;
import org.anddev.andengine.util.Debug;

import android.content.Context;


/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 13:22:48 - 21.05.2011
 */
public class SVGResourceBitmapTextureSource extends SVGBaseBitmapTextureSource {
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

	public SVGResourceBitmapTextureSource(final Context pContext, final int pRawResourceID, final int pTexturePositionX, final int pTexturePositionY) {
		this(pContext, pRawResourceID, pTexturePositionX, pTexturePositionY, null);
	}

	public SVGResourceBitmapTextureSource(final Context pContext, final int pRawResourceID, final int pTexturePositionX, final int pTexturePositionY, final float pScale) {
		this(pContext, pRawResourceID, pTexturePositionX, pTexturePositionY, pScale, null);
	}

	public SVGResourceBitmapTextureSource(final Context pContext, final int pRawResourceID, final int pTexturePositionX, final int pTexturePositionY, final int pWidth, final int pHeight) {
		this(pContext, pRawResourceID, pTexturePositionX, pTexturePositionY, pWidth, pHeight, null);
	}

	public SVGResourceBitmapTextureSource(final Context pContext, final int pRawResourceID, final int pTexturePositionX, final int pTexturePositionY, final ISVGColorMapper pSVGColorMapper) {
		super(SVGResourceBitmapTextureSource.getSVG(pContext, pRawResourceID, pSVGColorMapper), pTexturePositionX, pTexturePositionY);
		this.mContext = pContext;
		this.mRawResourceID = pRawResourceID;
		this.mSVGColorMapper = pSVGColorMapper;
	}

	public SVGResourceBitmapTextureSource(final Context pContext, final int pRawResourceID, final int pTexturePositionX, final int pTexturePositionY, final float pScale, final ISVGColorMapper pSVGColorMapper) {
		super(SVGResourceBitmapTextureSource.getSVG(pContext, pRawResourceID, pSVGColorMapper), pTexturePositionX, pTexturePositionY, pScale);
		this.mContext = pContext;
		this.mRawResourceID = pRawResourceID;
		this.mSVGColorMapper = pSVGColorMapper;
	}

	public SVGResourceBitmapTextureSource(final Context pContext, final int pRawResourceID, final int pTexturePositionX, final int pTexturePositionY, final int pWidth, final int pHeight, final ISVGColorMapper pSVGColorMapper) {
		super(SVGResourceBitmapTextureSource.getSVG(pContext, pRawResourceID, pSVGColorMapper), pTexturePositionX, pTexturePositionY, pWidth, pHeight);
		this.mContext = pContext;
		this.mRawResourceID = pRawResourceID;
		this.mSVGColorMapper = pSVGColorMapper;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public SVGResourceBitmapTextureSource clone() {
		return new SVGResourceBitmapTextureSource(this.mContext, this.mRawResourceID, this.mTexturePositionX, this.mTexturePositionY, this.mWidth, this.mHeight, this.mSVGColorMapper);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private static SVG getSVG(final Context pContext, final int pRawResourceID, final ISVGColorMapper pSVGColorMapper) {
		try {
			return SVGParser.parseSVGFromResource(pContext.getResources(), pRawResourceID, pSVGColorMapper);
		} catch (final Throwable t) {
			Debug.e("Failed loading SVG in SVGResourceBitmapTextureSource. RawResourceID: " + pRawResourceID, t);
			return null;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
