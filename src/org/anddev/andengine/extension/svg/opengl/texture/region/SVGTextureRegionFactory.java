package org.anddev.andengine.extension.svg.opengl.texture.region;

import org.anddev.andengine.extension.svg.opengl.texture.source.SVGAssetTextureSource;
import org.anddev.andengine.extension.svg.opengl.texture.source.SVGBaseTextureSource;
import org.anddev.andengine.extension.svg.opengl.texture.source.SVGResourceTextureSource;
import org.anddev.andengine.opengl.texture.BuildableTexture;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.opengl.texture.source.ITextureSource;

import android.content.Context;

import com.larvalabs.svgandroid.adt.SVG;

/**
 * TODO Add possibility to set the bounds/clipping to be rendered. Useful to render only a specific region of a big svg file.
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

	public static TextureRegion createFromSVG(final Texture pTexture, final SVG pSVG, final int pWidth, final int pHeight, final int pTexturePositionX, final int pTexturePositionY) {
		final ITextureSource textureSource = new SVGBaseTextureSource(pSVG, SVGTextureRegionFactory.applyScaleFactor(pWidth), SVGTextureRegionFactory.applyScaleFactor(pHeight));
		return TextureRegionFactory.createFromSource(pTexture, textureSource, pTexturePositionX, pTexturePositionY);
	}

	public static TiledTextureRegion createTiledFromSVG(final Texture pTexture, final SVG pSVG, final int pWidth, final int pHeight, final int pTexturePositionX, final int pTexturePositionY, final int pTileColumns, final int pTileRows) {
		final ITextureSource textureSource = new SVGBaseTextureSource(pSVG, SVGTextureRegionFactory.applyScaleFactor(pWidth), SVGTextureRegionFactory.applyScaleFactor(pHeight));
		return TextureRegionFactory.createTiledFromSource(pTexture, textureSource, pTexturePositionX, pTexturePositionY, pTileColumns, pTileRows);
	}


	public static TextureRegion createFromAsset(final Texture pTexture, final Context pContext, final String pAssetPath, final int pWidth, final int pHeight, final int pTexturePositionX, final int pTexturePositionY) {
		final ITextureSource textureSource = new SVGAssetTextureSource(pContext, SVGTextureRegionFactory.sAssetBasePath + pAssetPath, SVGTextureRegionFactory.applyScaleFactor(pWidth), SVGTextureRegionFactory.applyScaleFactor(pHeight));
		return TextureRegionFactory.createFromSource(pTexture, textureSource, pTexturePositionX, pTexturePositionY);
	}

	public static TiledTextureRegion createTiledFromAsset(final Texture pTexture, final Context pContext, final String pAssetPath, final int pWidth, final int pHeight, final int pTexturePositionX, final int pTexturePositionY, final int pTileColumns, final int pTileRows) {
		final ITextureSource textureSource = new SVGAssetTextureSource(pContext, SVGTextureRegionFactory.sAssetBasePath + pAssetPath, SVGTextureRegionFactory.applyScaleFactor(pWidth), SVGTextureRegionFactory.applyScaleFactor(pHeight));
		return TextureRegionFactory.createTiledFromSource(pTexture, textureSource, pTexturePositionX, pTexturePositionY, pTileColumns, pTileRows);
	}


	public static TextureRegion createFromResource(final Texture pTexture, final Context pContext, final int pRawResourceID, final int pWidth, final int pHeight, final int pTexturePositionX, final int pTexturePositionY) {
		final ITextureSource textureSource = new SVGResourceTextureSource(pContext, pRawResourceID, SVGTextureRegionFactory.applyScaleFactor(pWidth), SVGTextureRegionFactory.applyScaleFactor(pHeight));
		return TextureRegionFactory.createFromSource(pTexture, textureSource, pTexturePositionX, pTexturePositionY);
	}

	public static TiledTextureRegion createTiledFromResource(final Texture pTexture, final Context pContext, final int pRawResourceID, final int pWidth, final int pHeight, final int pTexturePositionX, final int pTexturePositionY, final int pTileColumns, final int pTileRows) {
		final ITextureSource textureSource = new SVGResourceTextureSource(pContext, pRawResourceID, SVGTextureRegionFactory.applyScaleFactor(pWidth), SVGTextureRegionFactory.applyScaleFactor(pHeight));
		return TextureRegionFactory.createTiledFromSource(pTexture, textureSource, pTexturePositionX, pTexturePositionY, pTileColumns, pTileRows);
	}

	// ===========================================================
	// Methods using BuildableTexture
	// ===========================================================

	public static TextureRegion createFromSVG(final BuildableTexture pBuildableTexture, final SVG pSVG, final int pWidth, final int pHeight) {
		final ITextureSource textureSource = new SVGBaseTextureSource(pSVG, SVGTextureRegionFactory.applyScaleFactor(pWidth), SVGTextureRegionFactory.applyScaleFactor(pHeight));
		return TextureRegionFactory.createFromSource(pBuildableTexture, textureSource);
	}

	public static TiledTextureRegion createTiledFromSVG(final BuildableTexture pBuildableTexture, final SVG pSVG, final int pWidth, final int pHeight, final int pTileColumns, final int pTileRows) {
		final ITextureSource textureSource = new SVGBaseTextureSource(pSVG, SVGTextureRegionFactory.applyScaleFactor(pWidth), SVGTextureRegionFactory.applyScaleFactor(pHeight));
		return TextureRegionFactory.createTiledFromSource(pBuildableTexture, textureSource, pTileColumns, pTileRows);
	}


	public static TextureRegion createFromAsset(final BuildableTexture pBuildableTexture, final Context pContext, final String pAssetPath, final int pWidth, final int pHeight) {
		final ITextureSource textureSource = new SVGAssetTextureSource(pContext, SVGTextureRegionFactory.sAssetBasePath + pAssetPath, SVGTextureRegionFactory.applyScaleFactor(pWidth), SVGTextureRegionFactory.applyScaleFactor(pHeight));
		return TextureRegionFactory.createFromSource(pBuildableTexture, textureSource);
	}

	public static TiledTextureRegion createTiledFromAsset(final BuildableTexture pBuildableTexture, final Context pContext, final String pAssetPath, final int pWidth, final int pHeight, final int pTileColumns, final int pTileRows) {
		final ITextureSource textureSource = new SVGAssetTextureSource(pContext, SVGTextureRegionFactory.sAssetBasePath + pAssetPath, SVGTextureRegionFactory.applyScaleFactor(pWidth), SVGTextureRegionFactory.applyScaleFactor(pHeight));
		return TextureRegionFactory.createTiledFromSource(pBuildableTexture, textureSource, pTileColumns, pTileRows);
	}


	public static TextureRegion createFromResource(final BuildableTexture pBuildableTexture, final Context pContext, final int pRawResourceID, final int pWidth, final int pHeight) {
		final ITextureSource textureSource = new SVGResourceTextureSource(pContext, pRawResourceID, SVGTextureRegionFactory.applyScaleFactor(pWidth), SVGTextureRegionFactory.applyScaleFactor(pHeight));
		return TextureRegionFactory.createFromSource(pBuildableTexture, textureSource);
	}

	public static TiledTextureRegion createTiledFromResource(final BuildableTexture pBuildableTexture, final Context pContext, final int pRawResourceID, final int pWidth, final int pHeight, final int pTileColumns, final int pTileRows) {
		final ITextureSource textureSource = new SVGResourceTextureSource(pContext, pRawResourceID, SVGTextureRegionFactory.applyScaleFactor(pWidth), SVGTextureRegionFactory.applyScaleFactor(pHeight));
		return TextureRegionFactory.createTiledFromSource(pBuildableTexture, textureSource, pTileColumns, pTileRows);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
