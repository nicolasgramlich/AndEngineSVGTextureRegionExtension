package com.larvalabs.svgandroid;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Picture;

import com.larvalabs.svgandroid.exception.SVGParseException;

/**
 * TODO Eventually add support for ".svgz" format. (Not totally useful as the apk itself gets zipped anyway. But might be useful, when loading from an external source.) 
 * 
 * @author Larva Labs, LLC
 * @author Nicolas Gramlich
 * @since 17:00:16 - 21.05.2011
 */
public class SVGParser {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public static SVG parseSVGFromString(final String pString) throws SVGParseException {
		return SVGParser.parse(new ByteArrayInputStream(pString.getBytes()));
	}

	public static SVG parseSVGFromResource(final Resources pResources, final int pRawResourceID) throws SVGParseException {
		return SVGParser.parse(pResources.openRawResource(pRawResourceID));
	}

	public static SVG parseSVGFromAsset(final AssetManager pAssetManager, final String pAssetPath) throws SVGParseException, IOException {
		final InputStream inputStream = pAssetManager.open(pAssetPath);
		final SVG svg = SVGParser.parseSVGFromInputStream(inputStream);
		inputStream.close();
		return svg;
	}

	public static SVG parseSVGFromInputStream(final InputStream pInputStream) throws SVGParseException {
		return SVGParser.parse(pInputStream);
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
			final SVGHandler svgHandler = new SVGHandler(picture);
			xr.setContentHandler(svgHandler);
			xr.parse(new InputSource(pInputStream));
			final SVG svg = new SVG(picture, svgHandler.getBounds(), svgHandler.getComputedBounds());
			return svg;
		} catch (final Exception e) {
			throw new SVGParseException(e);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
