package org.anddev.andengine.extension.svg.opengl.texture.source;

import org.anddev.andengine.util.Debug;

import android.content.Context;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

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

	// ===========================================================
	// Constructors
	// ===========================================================

	public SVGAssetTextureSource(final Context pContext, final String pAssetPath) {
		super(SVGAssetTextureSource.getSVG(pContext, pAssetPath));
		this.mContext = pContext;
		this.mAssetPath = pAssetPath;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public SVGAssetTextureSource clone() {
		return new SVGAssetTextureSource(this.mContext, this.mAssetPath);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private static SVG getSVG(final Context pContext, final String pAssetPath) {
		try {
			return SVGParser.getSVGFromAsset(pContext.getAssets(), pAssetPath);
		} catch (final Throwable t) {
			Debug.e("Failed loading SVG in SVGAssetTextureSource. AssetPath: " + pAssetPath, t);
			return null;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
