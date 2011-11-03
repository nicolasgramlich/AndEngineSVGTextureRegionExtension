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
public class SVGAssetBitmapTextureAtlasSource extends SVGBaseBitmapTextureAtlasSource {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Context mContext;
	private final String mAssetPath;
	private final ISVGColorMapper mSVGColorMapper;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SVGAssetBitmapTextureAtlasSource(final Context pContext, final String pAssetPath, final int pTexturePositionX, final int pTexturePositionY) {
		this(pContext, pAssetPath, pTexturePositionX, pTexturePositionY, null);
	}

	public SVGAssetBitmapTextureAtlasSource(final Context pContext, final String pAssetPath, final int pTexturePositionX, final int pTexturePositionY, final int pWidth, final int pHeight) {
		this(pContext, pAssetPath, pTexturePositionX, pTexturePositionY, pWidth, pHeight, null);
	}

	public SVGAssetBitmapTextureAtlasSource(final Context pContext, final String pAssetPath, final int pTexturePositionX, final int pTexturePositionY, final float pScale) {
		this(pContext, pAssetPath, pTexturePositionX, pTexturePositionY, pScale, null);
	}

	public SVGAssetBitmapTextureAtlasSource(final Context pContext, final String pAssetPath, final int pTexturePositionX, final int pTexturePositionY, final ISVGColorMapper pSVGColorMapper) {
		super(SVGAssetBitmapTextureAtlasSource.getSVG(pContext, pAssetPath, pSVGColorMapper), pTexturePositionX, pTexturePositionY);
		this.mContext = pContext;
		this.mAssetPath = pAssetPath;
		this.mSVGColorMapper = pSVGColorMapper;
	}

	public SVGAssetBitmapTextureAtlasSource(final Context pContext, final String pAssetPath, final int pTexturePositionX, final int pTexturePositionY, final float pScale, final ISVGColorMapper pSVGColorMapper) {
		super(SVGAssetBitmapTextureAtlasSource.getSVG(pContext, pAssetPath, pSVGColorMapper), pTexturePositionX, pTexturePositionY, pScale);
		this.mContext = pContext;
		this.mAssetPath = pAssetPath;
		this.mSVGColorMapper = pSVGColorMapper;
	}

	public SVGAssetBitmapTextureAtlasSource(final Context pContext, final String pAssetPath, final int pTexturePositionX, final int pTexturePositionY, final int pWidth, final int pHeight, final ISVGColorMapper pSVGColorMapper) {
		super(SVGAssetBitmapTextureAtlasSource.getSVG(pContext, pAssetPath, pSVGColorMapper), pTexturePositionX, pTexturePositionY, pWidth, pHeight);
		this.mContext = pContext;
		this.mAssetPath = pAssetPath;
		this.mSVGColorMapper = pSVGColorMapper;
	}
	
	@Override
	public SVGAssetBitmapTextureAtlasSource deepCopy() {
		return new SVGAssetBitmapTextureAtlasSource(this.mContext, this.mAssetPath, this.mTexturePositionX, this.mTexturePositionY, this.mWidth, this.mHeight, this.mSVGColorMapper);
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

	private static SVG getSVG(final Context pContext, final String pAssetPath, final ISVGColorMapper pSVGColorMapper) {
		try {
			return SVGParser.parseSVGFromAsset(pContext.getAssets(), pAssetPath, pSVGColorMapper);
		} catch (final Throwable t) {
			Debug.e("Failed loading SVG in SVGAssetBitmapTextureAtlasSource. AssetPath: " + pAssetPath, t);
			return null;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
