package org.anddev.andengine.extension.svg.opengl.texture.region;

import org.anddev.andengine.extension.svg.adt.ISVGColorMapper;
import org.anddev.andengine.extension.svg.adt.SVG;
import org.anddev.andengine.extension.svg.opengl.texture.source.SVGAssetBitmapTextureSource;
import org.anddev.andengine.extension.svg.opengl.texture.source.SVGBaseBitmapTextureSource;
import org.anddev.andengine.extension.svg.opengl.texture.source.SVGResourceBitmapTextureSource;
import org.anddev.andengine.opengl.texture.bitmap.BitmapTexture;
import org.anddev.andengine.opengl.texture.bitmap.BitmapTextureRegionFactory;
import org.anddev.andengine.opengl.texture.bitmap.BuildableBitmapTexture;
import org.anddev.andengine.opengl.texture.bitmap.source.IBitmapTextureSource;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.content.Context;


/**
 * TODO Add possibility to set the bounds/clipping to be rendered. Useful to render only a specific region of a big svg file, which could be a spritesheet.
 * 
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 12:47:31 - 21.05.2011
 */
public class SVGTextureRegionFactory {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static String sAssetBasePath = "";
	private static float sScaleFactor = 1;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * @param pAssetBasePath must end with '<code>/</code>' or have <code>.length() == 0</code>.
	 */
	public static void setAssetBasePath(final String pAssetBasePath) {
		if(pAssetBasePath.endsWith("/") || pAssetBasePath.length() == 0) {
			SVGTextureRegionFactory.sAssetBasePath = pAssetBasePath;
		} else {
			throw new IllegalArgumentException("pAssetBasePath must end with '/' or be lenght zero.");
		}
	}

	/**
	 * @param pScaleFactor must be > 0;
	 */
	public static void setScaleFactor(final float pScaleFactor) {
		if(pScaleFactor > 0) {
			SVGTextureRegionFactory.sScaleFactor = pScaleFactor;
		} else {
			throw new IllegalArgumentException("pScaleFactor must be greater than zero.");
		}
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	private static int applyScaleFactor(final int pInt) {
		return Math.round(pInt * SVGTextureRegionFactory.sScaleFactor);
	}

	// ===========================================================
	// Methods using Texture
	// ===========================================================

	public static TextureRegion createFromSVG(final BitmapTexture pBitmapTexture, final SVG pSVG, final int pWidth, final int pHeight, final int pTexturePositionX, final int pTexturePositionY) {
		final IBitmapTextureSource textureSource = new SVGBaseBitmapTextureSource(pSVG, SVGTextureRegionFactory.applyScaleFactor(pWidth), SVGTextureRegionFactory.applyScaleFactor(pHeight));
		return BitmapTextureRegionFactory.createFromSource(pBitmapTexture, textureSource, pTexturePositionX, pTexturePositionY);
	}

	public static TiledTextureRegion createTiledFromSVG(final BitmapTexture pBitmapTexture, final SVG pSVG, final int pWidth, final int pHeight, final int pTexturePositionX, final int pTexturePositionY, final int pTileColumns, final int pTileRows) {
		final IBitmapTextureSource textureSource = new SVGBaseBitmapTextureSource(pSVG, SVGTextureRegionFactory.applyScaleFactor(pWidth), SVGTextureRegionFactory.applyScaleFactor(pHeight));
		return BitmapTextureRegionFactory.createTiledFromSource(pBitmapTexture, textureSource, pTexturePositionX, pTexturePositionY, pTileColumns, pTileRows);
	}


	public static TextureRegion createFromAsset(final BitmapTexture pBitmapTexture, final Context pContext, final String pAssetPath, final int pWidth, final int pHeight, final int pTexturePositionX, final int pTexturePositionY) {
		return SVGTextureRegionFactory.createFromAsset(pBitmapTexture, pContext, pAssetPath, pWidth, pHeight, null, pTexturePositionX, pTexturePositionY);
	}

	public static TiledTextureRegion createTiledFromAsset(final BitmapTexture pBitmapTexture, final Context pContext, final String pAssetPath, final int pWidth, final int pHeight, final int pTexturePositionX, final int pTexturePositionY, final int pTileColumns, final int pTileRows) {
		return SVGTextureRegionFactory.createTiledFromAsset(pBitmapTexture, pContext, pAssetPath, pWidth, pHeight, null, pTexturePositionX, pTexturePositionY, pTileColumns, pTileRows);
	}

	public static TextureRegion createFromAsset(final BitmapTexture pBitmapTexture, final Context pContext, final String pAssetPath, final int pWidth, final int pHeight, final ISVGColorMapper pSVGColorMapper, final int pTexturePositionX, final int pTexturePositionY) {
		final IBitmapTextureSource textureSource = new SVGAssetBitmapTextureSource(pContext, SVGTextureRegionFactory.sAssetBasePath + pAssetPath, SVGTextureRegionFactory.applyScaleFactor(pWidth), SVGTextureRegionFactory.applyScaleFactor(pHeight), pSVGColorMapper);
		return BitmapTextureRegionFactory.createFromSource(pBitmapTexture, textureSource, pTexturePositionX, pTexturePositionY);
	}

	public static TiledTextureRegion createTiledFromAsset(final BitmapTexture pBitmapTexture, final Context pContext, final String pAssetPath, final int pWidth, final int pHeight, final ISVGColorMapper pSVGColorMapper, final int pTexturePositionX, final int pTexturePositionY, final int pTileColumns, final int pTileRows) {
		final IBitmapTextureSource textureSource = new SVGAssetBitmapTextureSource(pContext, SVGTextureRegionFactory.sAssetBasePath + pAssetPath, SVGTextureRegionFactory.applyScaleFactor(pWidth), SVGTextureRegionFactory.applyScaleFactor(pHeight), pSVGColorMapper);
		return BitmapTextureRegionFactory.createTiledFromSource(pBitmapTexture, textureSource, pTexturePositionX, pTexturePositionY, pTileColumns, pTileRows);
	}


	public static TextureRegion createFromResource(final BitmapTexture pBitmapTexture, final Context pContext, final int pRawResourceID, final int pWidth, final int pHeight, final int pTexturePositionX, final int pTexturePositionY) {
		return SVGTextureRegionFactory.createFromResource(pBitmapTexture, pContext, pRawResourceID, pWidth, pHeight, null, pTexturePositionX, pTexturePositionY);
	}

	public static TiledTextureRegion createTiledFromResource(final BitmapTexture pBitmapTexture, final Context pContext, final int pRawResourceID, final int pWidth, final int pHeight, final int pTexturePositionX, final int pTexturePositionY, final int pTileColumns, final int pTileRows) {
		return SVGTextureRegionFactory.createTiledFromResource(pBitmapTexture, pContext, pRawResourceID, pWidth, pHeight, null, pTexturePositionX, pTexturePositionY, pTileColumns, pTileRows);
	}

	public static TextureRegion createFromResource(final BitmapTexture pBitmapTexture, final Context pContext, final int pRawResourceID, final int pWidth, final int pHeight, final ISVGColorMapper pSVGColorMapper, final int pTexturePositionX, final int pTexturePositionY) {
		final IBitmapTextureSource textureSource = new SVGResourceBitmapTextureSource(pContext, SVGTextureRegionFactory.applyScaleFactor(pHeight), pRawResourceID, SVGTextureRegionFactory.applyScaleFactor(pWidth), pSVGColorMapper);
		return BitmapTextureRegionFactory.createFromSource(pBitmapTexture, textureSource, pTexturePositionX, pTexturePositionY);
	}

	public static TiledTextureRegion createTiledFromResource(final BitmapTexture pBitmapTexture, final Context pContext, final int pRawResourceID, final int pWidth, final int pHeight, final ISVGColorMapper pSVGColorMapper, final int pTexturePositionX, final int pTexturePositionY, final int pTileColumns, final int pTileRows) {
		final IBitmapTextureSource textureSource = new SVGResourceBitmapTextureSource(pContext, SVGTextureRegionFactory.applyScaleFactor(pHeight), pRawResourceID, SVGTextureRegionFactory.applyScaleFactor(pWidth), pSVGColorMapper);
		return BitmapTextureRegionFactory.createTiledFromSource(pBitmapTexture, textureSource, pTexturePositionX, pTexturePositionY, pTileColumns, pTileRows);
	}

	// ===========================================================
	// Methods using BuildableTexture
	// ===========================================================

	public static TextureRegion createFromSVG(final BuildableBitmapTexture pBuildableBitmapTexture, final SVG pSVG, final int pWidth, final int pHeight) {
		final IBitmapTextureSource textureSource = new SVGBaseBitmapTextureSource(pSVG, SVGTextureRegionFactory.applyScaleFactor(pWidth), SVGTextureRegionFactory.applyScaleFactor(pHeight));
		return TextureRegionFactory.createFromSource(pBuildableBitmapTexture, textureSource);
	}

	public static TiledTextureRegion createTiledFromSVG(final BuildableBitmapTexture pBuildableBitmapTexture, final SVG pSVG, final int pWidth, final int pHeight, final int pTileColumns, final int pTileRows) {
		final IBitmapTextureSource textureSource = new SVGBaseBitmapTextureSource(pSVG, SVGTextureRegionFactory.applyScaleFactor(pWidth), SVGTextureRegionFactory.applyScaleFactor(pHeight));
		return TextureRegionFactory.createTiledFromSource(pBuildableBitmapTexture, textureSource, pTileColumns, pTileRows);
	}


	public static TextureRegion createFromAsset(final BuildableBitmapTexture pBuildableBitmapTexture, final Context pContext, final String pAssetPath, final int pWidth, final int pHeight) {
		return SVGTextureRegionFactory.createFromAsset(pBuildableBitmapTexture, pContext, pAssetPath, pWidth, pHeight, null);
	}

	public static TiledTextureRegion createTiledFromAsset(final BuildableBitmapTexture pBuildableBitmapTexture, final Context pContext, final String pAssetPath, final int pWidth, final int pHeight, final int pTileColumns, final int pTileRows) {
		return SVGTextureRegionFactory.createTiledFromAsset(pBuildableBitmapTexture, pContext, pAssetPath, pWidth, pHeight, null, pTileColumns, pTileRows);
	}

	public static TextureRegion createFromAsset(final BuildableBitmapTexture pBuildableBitmapTexture, final Context pContext, final String pAssetPath, final int pWidth, final int pHeight, final ISVGColorMapper pSVGColorMapper) {
		final IBitmapTextureSource textureSource = new SVGAssetBitmapTextureSource(pContext, SVGTextureRegionFactory.sAssetBasePath + pAssetPath, SVGTextureRegionFactory.applyScaleFactor(pWidth), SVGTextureRegionFactory.applyScaleFactor(pHeight), pSVGColorMapper);
		return TextureRegionFactory.createFromSource(pBuildableBitmapTexture, textureSource);
	}

	public static TiledTextureRegion createTiledFromAsset(final BuildableBitmapTexture pBuildableBitmapTexture, final Context pContext, final String pAssetPath, final int pWidth, final int pHeight, final ISVGColorMapper pSVGColorMapper, final int pTileColumns, final int pTileRows) {
		final IBitmapTextureSource textureSource = new SVGAssetBitmapTextureSource(pContext, SVGTextureRegionFactory.sAssetBasePath + pAssetPath, SVGTextureRegionFactory.applyScaleFactor(pWidth), SVGTextureRegionFactory.applyScaleFactor(pHeight), pSVGColorMapper);
		return TextureRegionFactory.createTiledFromSource(pBuildableBitmapTexture, textureSource, pTileColumns, pTileRows);
	}


	public static TextureRegion createFromResource(final BuildableBitmapTexture pBuildableBitmapTexture, final Context pContext, final int pRawResourceID, final int pWidth, final int pHeight) {
		return SVGTextureRegionFactory.createFromResource(pBuildableBitmapTexture, pContext, pRawResourceID, pWidth, pHeight, null);
	}

	public static TiledTextureRegion createTiledFromResource(final BuildableBitmapTexture pBuildableBitmapTexture, final Context pContext, final int pRawResourceID, final int pWidth, final int pHeight, final int pTileColumns, final int pTileRows) {
		return SVGTextureRegionFactory.createTiledFromResource(pBuildableBitmapTexture, pContext, pRawResourceID, pWidth, pHeight, null, pTileColumns, pTileRows);
	}

	public static TextureRegion createFromResource(final BuildableBitmapTexture pBuildableBitmapTexture, final Context pContext, final int pRawResourceID, final int pWidth, final int pHeight, final ISVGColorMapper pSVGColorMapper) {
		final IBitmapTextureSource textureSource = new SVGResourceBitmapTextureSource(pContext, SVGTextureRegionFactory.applyScaleFactor(pHeight), pRawResourceID, SVGTextureRegionFactory.applyScaleFactor(pWidth), pSVGColorMapper);
		return TextureRegionFactory.createFromSource(pBuildableBitmapTexture, textureSource);
	}

	public static TiledTextureRegion createTiledFromResource(final BuildableBitmapTexture pBuildableBitmapTexture, final Context pContext, final int pRawResourceID, final int pWidth, final int pHeight, final ISVGColorMapper pSVGColorMapper, final int pTileColumns, final int pTileRows) {
		final IBitmapTextureSource textureSource = new SVGResourceBitmapTextureSource(pContext, SVGTextureRegionFactory.applyScaleFactor(pHeight), pRawResourceID, SVGTextureRegionFactory.applyScaleFactor(pWidth), pSVGColorMapper);
		return TextureRegionFactory.createTiledFromSource(pBuildableBitmapTexture, textureSource, pTileColumns, pTileRows);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
