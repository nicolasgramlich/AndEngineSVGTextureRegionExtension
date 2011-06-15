package org.anddev.andengine.extension.svg.opengl.texture.source;

import org.anddev.andengine.extension.svg.SVGParser;
import org.anddev.andengine.extension.svg.adt.ISVGColorMapper;
import org.anddev.andengine.extension.svg.adt.SVG;
import org.anddev.andengine.util.Debug;

import android.content.Context;


/**
 * @author Nicolas Gramlich
 * @since 13:22:48 - 21.05.2011
 */
public class SVGAssetTextureSource extends SVGBaseTextureSource {
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

	public SVGAssetTextureSource(final Context pContext, final String pAssetPath) {
		this(pContext, pAssetPath, null);
	}

	public SVGAssetTextureSource(final Context pContext, final String pAssetPath, final int pWidth, final int pHeight) {
		this(pContext, pAssetPath, pWidth, pHeight, null);
	}

	public SVGAssetTextureSource(final Context pContext, final String pAssetPath, final float pScale) {
		this(pContext, pAssetPath, pScale, null);
	}

	public SVGAssetTextureSource(final Context pContext, final String pAssetPath, final ISVGColorMapper pSVGColorMapper) {
		super(SVGAssetTextureSource.getSVG(pContext, pAssetPath, pSVGColorMapper));
		this.mContext = pContext;
		this.mAssetPath = pAssetPath;
		this.mSVGColorMapper = pSVGColorMapper;
	}

	public SVGAssetTextureSource(final Context pContext, final String pAssetPath, final float pScale, final ISVGColorMapper pSVGColorMapper) {
		super(SVGAssetTextureSource.getSVG(pContext, pAssetPath, pSVGColorMapper), pScale);
		this.mContext = pContext;
		this.mAssetPath = pAssetPath;
		this.mSVGColorMapper = pSVGColorMapper;
	}

	public SVGAssetTextureSource(final Context pContext, final String pAssetPath, final int pWidth, final int pHeight, final ISVGColorMapper pSVGColorMapper) {
		super(SVGAssetTextureSource.getSVG(pContext, pAssetPath, pSVGColorMapper), pWidth, pHeight);
		this.mContext = pContext;
		this.mAssetPath = pAssetPath;
		this.mSVGColorMapper = pSVGColorMapper;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public SVGAssetTextureSource clone() {
		return new SVGAssetTextureSource(this.mContext, this.mAssetPath, this.mWidth, this.mHeight, this.mSVGColorMapper);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private static SVG getSVG(final Context pContext, final String pAssetPath, final ISVGColorMapper pSVGColorMapper) {
		try {
			return SVGParser.parseSVGFromAsset(pContext.getAssets(), pAssetPath, pSVGColorMapper);
		} catch (final Throwable t) {
			Debug.e("Failed loading SVG in SVGAssetTextureSource. AssetPath: " + pAssetPath, t);
			return null;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
