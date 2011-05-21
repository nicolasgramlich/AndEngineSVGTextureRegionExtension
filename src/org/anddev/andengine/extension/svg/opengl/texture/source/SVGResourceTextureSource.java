package org.anddev.andengine.extension.svg.opengl.texture.source;

import org.anddev.andengine.util.Debug;

import android.content.Context;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

/**
 * @author Nicolas Gramlich
 * @since 13:22:48 - 21.05.2011
 */
public class SVGResourceTextureSource extends SVGBaseTextureSource {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Context mContext;
	private final int mRawResourceID;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SVGResourceTextureSource(final Context pContext, final int pRawResourceID) {
		super(SVGResourceTextureSource.getSVG(pContext, pRawResourceID));
		this.mContext = pContext;
		this.mRawResourceID = pRawResourceID;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public SVGResourceTextureSource clone() {
		return new SVGResourceTextureSource(this.mContext, this.mRawResourceID);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private static SVG getSVG(final Context pContext, final int pRawResourceID) {
		try {
			return SVGParser.getSVGFromResource(pContext.getResources(), pRawResourceID);
		} catch (final Throwable t) {
			Debug.e("Failed loading SVG in SVGResourceTextureSource. RawResourceID: " + pRawResourceID, t);
			return null;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
