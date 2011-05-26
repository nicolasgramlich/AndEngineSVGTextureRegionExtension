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
public class SVGResourceTextureSource extends SVGBaseTextureSource {
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

	public SVGResourceTextureSource(final Context pContext, final int pRawResourceID) {
		this(pContext, pRawResourceID, null);
	}

	public SVGResourceTextureSource(final Context pContext, final int pRawResourceID, final int pWidth, final int pHeight) {
		this(pContext, pRawResourceID, pWidth, pHeight, null);
	}

	public SVGResourceTextureSource(final Context pContext, final int pRawResourceID, final ISVGColorMapper pSVGColorMapper) {
		super(SVGResourceTextureSource.getSVG(pContext, pRawResourceID, pSVGColorMapper));
		this.mContext = pContext;
		this.mRawResourceID = pRawResourceID;
		this.mSVGColorMapper = pSVGColorMapper;
	}

	public SVGResourceTextureSource(final Context pContext, final int pRawResourceID, final int pWidth, final int pHeight, final ISVGColorMapper pSVGColorMapper) {
		super(SVGResourceTextureSource.getSVG(pContext, pRawResourceID, pSVGColorMapper), pWidth, pHeight);
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
	public SVGResourceTextureSource clone() {
		return new SVGResourceTextureSource(this.mContext, this.mRawResourceID, this.mWidth, this.mHeight, this.mSVGColorMapper);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private static SVG getSVG(final Context pContext, final int pRawResourceID, final ISVGColorMapper pSVGColorMapper) {
		try {
			return SVGParser.parseSVGFromResource(pContext.getResources(), pRawResourceID, pSVGColorMapper);
		} catch (final Throwable t) {
			Debug.e("Failed loading SVG in SVGResourceTextureSource. RawResourceID: " + pRawResourceID, t);
			return null;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
