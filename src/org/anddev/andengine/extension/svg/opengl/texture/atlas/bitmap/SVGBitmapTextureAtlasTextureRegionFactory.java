package org.anddev.andengine.extension.svg.opengl.texture.atlas.bitmap;

import org.anddev.andengine.extension.svg.adt.ISVGColorMapper;
import org.anddev.andengine.extension.svg.adt.SVG;
import org.anddev.andengine.extension.svg.opengl.texture.atlas.bitmap.source.SVGAssetBitmapTextureAtlasSource;
import org.anddev.andengine.extension.svg.opengl.texture.atlas.bitmap.source.SVGBaseBitmapTextureAtlasSource;
import org.anddev.andengine.extension.svg.opengl.texture.atlas.bitmap.source.SVGResourceBitmapTextureAtlasSource;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.anddev.andengine.opengl.texture.atlas.buildable.BuildableTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.ITextureRegion;
import org.anddev.andengine.opengl.texture.region.ITiledTextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

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
public class SVGBitmapTextureAtlasTextureRegionFactory {
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
			SVGBitmapTextureAtlasTextureRegionFactory.sAssetBasePath = pAssetBasePath;
		} else {
			throw new IllegalArgumentException("pAssetBasePath must end with '/' or be lenght zero.");
		}
	}

	/**
	 * @param pScaleFactor must be > 0;
	 */
	public static void setScaleFactor(final float pScaleFactor) {
		if(pScaleFactor > 0) {
			SVGBitmapTextureAtlasTextureRegionFactory.sScaleFactor = pScaleFactor;
		} else {
			throw new IllegalArgumentException("pScaleFactor must be greater than zero.");
		}
	}

	public static void reset() {
		SVGBitmapTextureAtlasTextureRegionFactory.setAssetBasePath("");
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	private static int applyScaleFactor(final int pInt) {
		return Math.round(pInt * SVGBitmapTextureAtlasTextureRegionFactory.sScaleFactor);
	}

	// ===========================================================
	// Methods using Texture
	// ===========================================================

	public static ITextureRegion createFromSVG(final BitmapTextureAtlas pBitmapTextureAtlas, final SVG pSVG, final int pWidth, final int pHeight, final int pTexturePositionX, final int pTexturePositionY) {
		final IBitmapTextureAtlasSource bitmapTextureAtlasSource = new SVGBaseBitmapTextureAtlasSource(pSVG, SVGBitmapTextureAtlasTextureRegionFactory.applyScaleFactor(pWidth), SVGBitmapTextureAtlasTextureRegionFactory.applyScaleFactor(pHeight));
		return TextureRegionFactory.createFromSource(pBitmapTextureAtlas, bitmapTextureAtlasSource, pTexturePositionX, pTexturePositionY);
	}

	public static ITiledTextureRegion createTiledFromSVG(final BitmapTextureAtlas pBitmapTextureAtlas, final SVG pSVG, final int pWidth, final int pHeight, final int pTexturePositionX, final int pTexturePositionY, final int pTileColumns, final int pTileRows) {
		final IBitmapTextureAtlasSource textureSource = new SVGBaseBitmapTextureAtlasSource(pSVG, SVGBitmapTextureAtlasTextureRegionFactory.applyScaleFactor(pWidth), SVGBitmapTextureAtlasTextureRegionFactory.applyScaleFactor(pHeight));
		return TextureRegionFactory.createTiledFromSource(pBitmapTextureAtlas, textureSource, pTexturePositionX, pTexturePositionY, pTileColumns, pTileRows);
	}


	public static ITextureRegion createFromAsset(final BitmapTextureAtlas pBitmapTextureAtlas, final Context pContext, final String pAssetPath, final int pWidth, final int pHeight, final int pTexturePositionX, final int pTexturePositionY) {
		return SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(pBitmapTextureAtlas, pContext, pAssetPath, pWidth, pHeight, null, pTexturePositionX, pTexturePositionY);
	}

	public static ITiledTextureRegion createTiledFromAsset(final BitmapTextureAtlas pBitmapTextureAtlas, final Context pContext, final String pAssetPath, final int pWidth, final int pHeight, final int pTexturePositionX, final int pTexturePositionY, final int pTileColumns, final int pTileRows) {
		return SVGBitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(pBitmapTextureAtlas, pContext, pAssetPath, pWidth, pHeight, null, pTexturePositionX, pTexturePositionY, pTileColumns, pTileRows);
	}

	public static ITextureRegion createFromAsset(final BitmapTextureAtlas pBitmapTextureAtlas, final Context pContext, final String pAssetPath, final int pWidth, final int pHeight, final ISVGColorMapper pSVGColorMapper, final int pTexturePositionX, final int pTexturePositionY) {
		final IBitmapTextureAtlasSource textureSource = new SVGAssetBitmapTextureAtlasSource(pContext, SVGBitmapTextureAtlasTextureRegionFactory.sAssetBasePath + pAssetPath, SVGBitmapTextureAtlasTextureRegionFactory.applyScaleFactor(pWidth), SVGBitmapTextureAtlasTextureRegionFactory.applyScaleFactor(pHeight), pSVGColorMapper);
		return TextureRegionFactory.createFromSource(pBitmapTextureAtlas, textureSource, pTexturePositionX, pTexturePositionY);
	}

	public static ITiledTextureRegion createTiledFromAsset(final BitmapTextureAtlas pBitmapTextureAtlas, final Context pContext, final String pAssetPath, final int pWidth, final int pHeight, final ISVGColorMapper pSVGColorMapper, final int pTexturePositionX, final int pTexturePositionY, final int pTileColumns, final int pTileRows) {
		final IBitmapTextureAtlasSource textureSource = new SVGAssetBitmapTextureAtlasSource(pContext, SVGBitmapTextureAtlasTextureRegionFactory.sAssetBasePath + pAssetPath, SVGBitmapTextureAtlasTextureRegionFactory.applyScaleFactor(pWidth), SVGBitmapTextureAtlasTextureRegionFactory.applyScaleFactor(pHeight), pSVGColorMapper);
		return TextureRegionFactory.createTiledFromSource(pBitmapTextureAtlas, textureSource, pTexturePositionX, pTexturePositionY, pTileColumns, pTileRows);
	}


	public static ITextureRegion createFromResource(final BitmapTextureAtlas pBitmapTextureAtlas, final Context pContext, final int pRawResourceID, final int pWidth, final int pHeight, final int pTexturePositionX, final int pTexturePositionY) {
		return SVGBitmapTextureAtlasTextureRegionFactory.createFromResource(pBitmapTextureAtlas, pContext, pRawResourceID, pWidth, pHeight, null, pTexturePositionX, pTexturePositionY);
	}

	public static ITiledTextureRegion createTiledFromResource(final BitmapTextureAtlas pBitmapTextureAtlas, final Context pContext, final int pRawResourceID, final int pWidth, final int pHeight, final int pTexturePositionX, final int pTexturePositionY, final int pTileColumns, final int pTileRows) {
		return SVGBitmapTextureAtlasTextureRegionFactory.createTiledFromResource(pBitmapTextureAtlas, pContext, pRawResourceID, pWidth, pHeight, null, pTexturePositionX, pTexturePositionY, pTileColumns, pTileRows);
	}

	public static ITextureRegion createFromResource(final BitmapTextureAtlas pBitmapTextureAtlas, final Context pContext, final int pRawResourceID, final int pWidth, final int pHeight, final ISVGColorMapper pSVGColorMapper, final int pTexturePositionX, final int pTexturePositionY) {
		final IBitmapTextureAtlasSource textureSource = new SVGResourceBitmapTextureAtlasSource(pContext, SVGBitmapTextureAtlasTextureRegionFactory.applyScaleFactor(pHeight), pRawResourceID, SVGBitmapTextureAtlasTextureRegionFactory.applyScaleFactor(pWidth), pSVGColorMapper);
		return TextureRegionFactory.createFromSource(pBitmapTextureAtlas, textureSource, pTexturePositionX, pTexturePositionY);
	}

	public static ITiledTextureRegion createTiledFromResource(final BitmapTextureAtlas pBitmapTextureAtlas, final Context pContext, final int pRawResourceID, final int pWidth, final int pHeight, final ISVGColorMapper pSVGColorMapper, final int pTexturePositionX, final int pTexturePositionY, final int pTileColumns, final int pTileRows) {
		final IBitmapTextureAtlasSource textureSource = new SVGResourceBitmapTextureAtlasSource(pContext, SVGBitmapTextureAtlasTextureRegionFactory.applyScaleFactor(pHeight), pRawResourceID, SVGBitmapTextureAtlasTextureRegionFactory.applyScaleFactor(pWidth), pSVGColorMapper);
		return TextureRegionFactory.createTiledFromSource(pBitmapTextureAtlas, textureSource, pTexturePositionX, pTexturePositionY, pTileColumns, pTileRows);
	}

	// ===========================================================
	// Methods using BuildableTexture
	// ===========================================================

	public static ITextureRegion createFromSVG(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final SVG pSVG, final int pWidth, final int pHeight) {
		return createFromSVG(pBuildableBitmapTextureAtlas, pSVG, pWidth, pHeight, false);
	}
	
	public static ITextureRegion createFromSVG(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final SVG pSVG, final int pWidth, final int pHeight, final boolean pRotated) {
		final IBitmapTextureAtlasSource textureSource = new SVGBaseBitmapTextureAtlasSource(pSVG, SVGBitmapTextureAtlasTextureRegionFactory.applyScaleFactor(pWidth), SVGBitmapTextureAtlasTextureRegionFactory.applyScaleFactor(pHeight));
		return BuildableTextureAtlasTextureRegionFactory.createFromSource(pBuildableBitmapTextureAtlas, textureSource, pRotated);
	}

	public static ITiledTextureRegion createTiledFromSVG(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final SVG pSVG, final int pWidth, final int pHeight, final int pTileColumns, final int pTileRows) {
		final IBitmapTextureAtlasSource textureSource = new SVGBaseBitmapTextureAtlasSource(pSVG, SVGBitmapTextureAtlasTextureRegionFactory.applyScaleFactor(pWidth), SVGBitmapTextureAtlasTextureRegionFactory.applyScaleFactor(pHeight));
		return BuildableTextureAtlasTextureRegionFactory.createTiledFromSource(pBuildableBitmapTextureAtlas, textureSource, pTileColumns, pTileRows);
	}


	public static ITextureRegion createFromAsset(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final Context pContext, final String pAssetPath, final int pWidth, final int pHeight) {
		return SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(pBuildableBitmapTextureAtlas, pContext, pAssetPath, pWidth, pHeight, null);
	}

	public static ITiledTextureRegion createTiledFromAsset(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final Context pContext, final String pAssetPath, final int pWidth, final int pHeight, final int pTileColumns, final int pTileRows) {
		return SVGBitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(pBuildableBitmapTextureAtlas, pContext, pAssetPath, pWidth, pHeight, null, pTileColumns, pTileRows);
	}

	public static ITextureRegion createFromAsset(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final Context pContext, final String pAssetPath, final int pWidth, final int pHeight, final ISVGColorMapper pSVGColorMapper) {
		return createFromAsset(pBuildableBitmapTextureAtlas, pContext, pAssetPath, pWidth, pHeight, false, pSVGColorMapper);
	}
	
	public static ITextureRegion createFromAsset(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final Context pContext, final String pAssetPath, final int pWidth, final int pHeight, final boolean pRotated, final ISVGColorMapper pSVGColorMapper) {
		final IBitmapTextureAtlasSource textureSource = new SVGAssetBitmapTextureAtlasSource(pContext, SVGBitmapTextureAtlasTextureRegionFactory.sAssetBasePath + pAssetPath, SVGBitmapTextureAtlasTextureRegionFactory.applyScaleFactor(pWidth), SVGBitmapTextureAtlasTextureRegionFactory.applyScaleFactor(pHeight), pSVGColorMapper);
		return BuildableTextureAtlasTextureRegionFactory.createFromSource(pBuildableBitmapTextureAtlas, textureSource, pRotated);
	}

	public static ITiledTextureRegion createTiledFromAsset(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final Context pContext, final String pAssetPath, final int pWidth, final int pHeight, final ISVGColorMapper pSVGColorMapper, final int pTileColumns, final int pTileRows) {
		final IBitmapTextureAtlasSource textureSource = new SVGAssetBitmapTextureAtlasSource(pContext, SVGBitmapTextureAtlasTextureRegionFactory.sAssetBasePath + pAssetPath, SVGBitmapTextureAtlasTextureRegionFactory.applyScaleFactor(pWidth), SVGBitmapTextureAtlasTextureRegionFactory.applyScaleFactor(pHeight), pSVGColorMapper);
		return BuildableTextureAtlasTextureRegionFactory.createTiledFromSource(pBuildableBitmapTextureAtlas, textureSource, pTileColumns, pTileRows);
	}


	public static ITextureRegion createFromResource(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final Context pContext, final int pRawResourceID, final int pWidth, final int pHeight) {
		return SVGBitmapTextureAtlasTextureRegionFactory.createFromResource(pBuildableBitmapTextureAtlas, pContext, pRawResourceID, pWidth, pHeight, null);
	}

	public static ITiledTextureRegion createTiledFromResource(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final Context pContext, final int pRawResourceID, final int pWidth, final int pHeight, final int pTileColumns, final int pTileRows) {
		return SVGBitmapTextureAtlasTextureRegionFactory.createTiledFromResource(pBuildableBitmapTextureAtlas, pContext, pRawResourceID, pWidth, pHeight, null, pTileColumns, pTileRows);
	}

	public static ITextureRegion createFromResource(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final Context pContext, final int pRawResourceID, final int pWidth, final int pHeight, final ISVGColorMapper pSVGColorMapper) {
		return createFromResource(pBuildableBitmapTextureAtlas, pContext, pRawResourceID, pWidth, pHeight, false, pSVGColorMapper);
	}
	
	public static ITextureRegion createFromResource(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final Context pContext, final int pRawResourceID, final int pWidth, final int pHeight, final boolean pRotated, final ISVGColorMapper pSVGColorMapper) {
		final IBitmapTextureAtlasSource textureSource = new SVGResourceBitmapTextureAtlasSource(pContext, SVGBitmapTextureAtlasTextureRegionFactory.applyScaleFactor(pHeight), pRawResourceID, SVGBitmapTextureAtlasTextureRegionFactory.applyScaleFactor(pWidth), pSVGColorMapper);
		return BuildableTextureAtlasTextureRegionFactory.createFromSource(pBuildableBitmapTextureAtlas, textureSource, pRotated);
	}

	public static ITiledTextureRegion createTiledFromResource(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final Context pContext, final int pRawResourceID, final int pWidth, final int pHeight, final ISVGColorMapper pSVGColorMapper, final int pTileColumns, final int pTileRows) {
		final IBitmapTextureAtlasSource textureSource = new SVGResourceBitmapTextureAtlasSource(pContext, SVGBitmapTextureAtlasTextureRegionFactory.applyScaleFactor(pHeight), pRawResourceID, SVGBitmapTextureAtlasTextureRegionFactory.applyScaleFactor(pWidth), pSVGColorMapper);
		return BuildableTextureAtlasTextureRegionFactory.createTiledFromSource(pBuildableBitmapTextureAtlas, textureSource, pTileColumns, pTileRows);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
