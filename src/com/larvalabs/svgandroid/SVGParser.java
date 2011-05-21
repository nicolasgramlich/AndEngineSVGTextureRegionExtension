package com.larvalabs.svgandroid;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.anddev.andengine.util.SAXUtils;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Picture;

/**
 * @author Larva Labs, LLC
 * @author Nicolas Gramlich
 * @since 17:00:16 - 21.05.2011
 */
public class SVGParser {
	// ===========================================================
	// Constants
	// ===========================================================

	static final String TAG = "SVGAndroid";

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public static SVG getSVGFromInputStream(final InputStream pInputStream) throws SVGParseException {
		return SVGParser.parse(pInputStream);
	}

	public static SVG getSVGFromString(final String pString) throws SVGParseException {
		return SVGParser.parse(new ByteArrayInputStream(pString.getBytes()));
	}

	public static SVG getSVGFromResource(final Resources pResources, final int pRawResourceID) throws SVGParseException {
		return SVGParser.parse(pResources.openRawResource(pRawResourceID));
	}

	public static SVG getSVGFromAsset(final AssetManager pAssetManager, final String pAssetPath) throws SVGParseException, IOException {
		final InputStream inputStream = pAssetManager.open(pAssetPath);
		final SVG svg = SVGParser.getSVGFromInputStream(inputStream);
		inputStream.close();
		return svg;
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

	private static SVG parse(final InputStream pInputStream) throws SVGParseException {
		try {
			final SAXParserFactory spf = SAXParserFactory.newInstance();
			final SAXParser sp = spf.newSAXParser();
			final XMLReader xr = sp.getXMLReader();
			final Picture picture = new Picture();
			final SVGHandler handler = new SVGHandler(picture);
			xr.setContentHandler(handler);
			xr.parse(new InputSource(pInputStream));
			final SVG svg = new SVG(picture, handler.getBounds());
			/* Skip bounds if it was an empty picture. */
			if (!Float.isInfinite(handler.getLimits().top)) {
				svg.setLimits(handler.getLimits());
			}
			return svg;
		} catch (final Exception e) {
			throw new SVGParseException(e);
		}
	}

	public static Float getFloatAttribute(final Attributes pAttributes, final String pAttributeName) {
		String v = SAXUtils.getAttribute(pAttributes, pAttributeName, null);
		if (v == null) {
			return null;
		} else {
			if (v.endsWith("px")) {
				v = v.substring(0, v.length() - 2);
			}
			//            Log.d(TAG, "Float parsing '" + name + "=" + v + "'");
			return Float.parseFloat(v);
		}
	}

	public static float getFloatAttribute(final Attributes pAttributes, final String pAttributeName, final float pDefaultValue) {
		String v = SAXUtils.getAttribute(pAttributes, pAttributeName, null);
		if (v == null) {
			return pDefaultValue;
		} else {
			if (v.endsWith("px")) {
				v = v.substring(0, v.length() - 2);
			}
			//            Log.d(TAG, "Float parsing '" + name + "=" + v + "'");
			return Float.parseFloat(v);
		}
	}

//	private static Integer getHexAttribute(final Attributes pAttributes, final String pAttributeName)  {
//		final String v = SAXUtils.getAttribute(pAttributes, pAttributeName, null);
//		if (v == null) {
//			return null;
//		} else {
//			try {
//				return Integer.parseInt(v.substring(1), 16);
//			} catch (final NumberFormatException nfe) {
//				// TODO - parse word-based color here
//				return null;
//			}
//		}
//	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
