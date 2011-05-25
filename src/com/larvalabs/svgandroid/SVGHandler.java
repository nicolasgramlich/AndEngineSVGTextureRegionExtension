package com.larvalabs.svgandroid;

import java.util.Stack;

import org.anddev.andengine.util.Debug;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Picture;
import android.graphics.RectF;

import com.larvalabs.svgandroid.adt.ISVGColorMapper;
import com.larvalabs.svgandroid.adt.SVGGroup;
import com.larvalabs.svgandroid.adt.SVGPaint;
import com.larvalabs.svgandroid.adt.SVGProperties;
import com.larvalabs.svgandroid.adt.gradient.SVGGradient;
import com.larvalabs.svgandroid.adt.gradient.SVGGradient.Stop;
import com.larvalabs.svgandroid.util.SAXHelper;
import com.larvalabs.svgandroid.util.SVGCircleParser;
import com.larvalabs.svgandroid.util.SVGEllipseParser;
import com.larvalabs.svgandroid.util.SVGLineParser;
import com.larvalabs.svgandroid.util.SVGPathParser;
import com.larvalabs.svgandroid.util.SVGPolygonParser;
import com.larvalabs.svgandroid.util.SVGPolylineParser;
import com.larvalabs.svgandroid.util.SVGRectParser;
import com.larvalabs.svgandroid.util.SVGTransformParser;

/**
 * @author Larva Labs, LLC
 * @author Nicolas Gramlich
 * @since 16:50:02 - 21.05.2011
 */
public class SVGHandler extends DefaultHandler {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private Canvas mCanvas;
	private final Picture mPicture;
	private final SVGPaint mSVGPaint;

	private boolean mBoundsMode;
	private RectF mBounds;

	private final Stack<SVGGroup> mSVGGroupStack = new Stack<SVGGroup>();
	private final SVGPathParser mSVGPathParser = new SVGPathParser();

	private SVGGradient mCurrentSVGGradient;

	// TODO Put Hidden into SVGGroup?
	private boolean mHidden;
	private int mHiddenLevel;

	/** Multi purpose dummy rectangle. */
	private final RectF mRect = new RectF();

	// ===========================================================
	// Constructors
	// ===========================================================

	public SVGHandler(final Picture pPicture, final ISVGColorMapper pSVGColorMapper) {
		this.mPicture = pPicture;
		this.mSVGPaint = new SVGPaint(pSVGColorMapper);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public RectF getBounds() {
		return this.mBounds;
	}

	public RectF getComputedBounds() {
		return this.mSVGPaint.getComputedBounds();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void startElement(final String pNamespace, final String pLocalName, final String pQualifiedName, final Attributes pAttributes) throws SAXException {
		/* Ignore everything but rectangles in bounds mode. */
		if (this.mBoundsMode) {
			this.parseBounds(pLocalName, pAttributes);
			return;
		}
		if (pLocalName.equals("svg")) {
			final int width = (int) Math.ceil(SAXHelper.getFloatAttribute(pAttributes, "width", 0f));
			final int height = (int) Math.ceil(SAXHelper.getFloatAttribute(pAttributes, "height", 0f));
			this.mCanvas = this.mPicture.beginRecording(width, height);
		} else if (pLocalName.equals("defs")) {
			// Ignore
		} else if (pLocalName.equals("linearGradient")) {
			this.parseLinearGradient(pAttributes);
		} else if (pLocalName.equals("radialGradient")) {
			this.parseRadialGradient(pAttributes);
		} else if (pLocalName.equals("stop")) {
			this.parseGradientStop(pAttributes);
		} else if (pLocalName.equals("g")) {
			this.parseGroup(pAttributes);
		} else if (!this.mHidden && pLocalName.equals("rect")) {
			this.parseRect(pAttributes);
		} else if (!this.mHidden && pLocalName.equals("line")) {
			this.parseLine(pAttributes);
		} else if (!this.mHidden && pLocalName.equals("circle")) {
			this.parseCircle(pAttributes);
		} else if (!this.mHidden && pLocalName.equals("ellipse")) {
			this.parseEllipse(pAttributes);
		} else if (!this.mHidden && pLocalName.equals("polyline")) {
			this.parsePolyline(pAttributes);
		}  else if (!this.mHidden && pLocalName.equals("polygon")) {
			this.parsePolygon(pAttributes);
		} else if (!this.mHidden && pLocalName.equals("path")) {
			this.parsePath(pAttributes);
		} else if (!this.mHidden) {
			Debug.d("Unexpected SVG tag: '" + pLocalName +"'!");
		}
	}

	@Override
	public void characters(final char pCharacters[], final int pStart, final int pLength) {
		/* Nothing. */
	}

	@Override
	public void endElement(final String pNamespace, final String pLocalName, final String pQualifiedName)
	throws SAXException {
		if (pLocalName.equals("svg")) {
			this.mPicture.endRecording();
		} else if (pLocalName.equals("g")) {
			this.parseGroupEnd();
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void parseBounds(final String pLocalName, final Attributes pAttributes) {
		if (pLocalName.equals("rect")) {
			final float x = SAXHelper.getFloatAttribute(pAttributes, "x", 0f);
			final float y = SAXHelper.getFloatAttribute(pAttributes, "y", 0f);
			final float width = SAXHelper.getFloatAttribute(pAttributes, "width", 0f);
			final float height = SAXHelper.getFloatAttribute(pAttributes, "height", 0f);
			this.mBounds = new RectF(x, y, x + width, y + height);
		}
	}

	private void parseLinearGradient(final Attributes pAttributes) {
		this.mCurrentSVGGradient = this.mSVGPaint.registerGradient(pAttributes, true);
	}

	private void parseRadialGradient(final Attributes pAttributes) {
		this.mCurrentSVGGradient = this.mSVGPaint.registerGradient(pAttributes, false);
	}

	private void parseGradientStop(final Attributes pAttributes) {
		final Stop gradientStop = this.mSVGPaint.parseGradientStop(this.getSVGPropertiesFromAttributes(pAttributes));
		this.mCurrentSVGGradient.addGradientStop(gradientStop);
	}

	private void parseGroup(final Attributes pAttributes) {
		/* Check to see if this is the "bounds" layer. */
		if ("bounds".equalsIgnoreCase(SAXHelper.getStringAttribute(pAttributes, "id"))) {
			this.mBoundsMode = true;
		}
		final boolean hasTransform = this.pushTransform(pAttributes);
		final AttributesImpl attributesDeepCopy = new AttributesImpl(pAttributes);
		this.mSVGGroupStack.push(new SVGGroup(this.getSVGPropertiesFromAttributes(attributesDeepCopy), hasTransform));

		if (this.mHidden) {
			this.mHiddenLevel++;
		}
		/* Go in to hidden mode if display is "none". */
		if ("none".equals(SAXHelper.getStringAttribute(pAttributes, "display"))) {
			if (!this.mHidden) {
				this.mHidden = true;
				this.mHiddenLevel = 1;
			}
		}
	}

	private void parseGroupEnd() {
		if (this.mBoundsMode) {
			this.mBoundsMode = false;
		}
		/* Pop group transform if there was one pushed. */
		if(this.mSVGGroupStack.pop().hasTransform()) {
			this.popTransform();
		}
		/* Break out of hidden mode. */
		if (this.mHidden) {
			this.mHiddenLevel--;
			if (this.mHiddenLevel == 0) {
				this.mHidden = false;
			}
		}
		/* Clear shader map. */
		this.mSVGPaint.clearGradientShaders();
	}

	private void parsePath(final Attributes pAttributes) {
		final SVGProperties svgProperties = this.getSVGPropertiesFromAttributes(pAttributes);
		final boolean pushed = this.pushTransform(pAttributes);
		this.mSVGPathParser.parse(svgProperties, this.mCanvas, this.mSVGPaint);
		if(pushed) {
			this.popTransform();
		}
	}

	private void parsePolygon(final Attributes pAttributes) {
		final SVGProperties svgProperties = this.getSVGPropertiesFromAttributes(pAttributes);
		final boolean pushed = this.pushTransform(pAttributes);
		SVGPolygonParser.parse(svgProperties, this.mCanvas, this.mSVGPaint);
		if(pushed) {
			this.popTransform();
		}
	}

	private void parsePolyline(final Attributes pAttributes) {
		final SVGProperties svgProperties = this.getSVGPropertiesFromAttributes(pAttributes);
		final boolean pushed = this.pushTransform(pAttributes);
		SVGPolylineParser.parse(svgProperties, this.mCanvas, this.mSVGPaint);
		if(pushed) {
			this.popTransform();
		}
	}

	private void parseEllipse(final Attributes pAttributes) {
		final SVGProperties svgProperties = this.getSVGPropertiesFromAttributes(pAttributes);
		final boolean pushed = this.pushTransform(pAttributes);
		SVGEllipseParser.parse(svgProperties, this.mCanvas, this.mSVGPaint, this.mRect);
		if(pushed) {
			this.popTransform();
		}
	}

	private void parseCircle(final Attributes pAttributes) {
		final SVGProperties svgProperties = this.getSVGPropertiesFromAttributes(pAttributes);
		final boolean pushed = this.pushTransform(pAttributes);
		SVGCircleParser.parse(svgProperties, this.mCanvas, this.mSVGPaint);
		if(pushed) {
			this.popTransform();
		}
	}

	private void parseLine(final Attributes pAttributes) {
		final SVGProperties svgProperties = this.getSVGPropertiesFromAttributes(pAttributes);
		final boolean pushed = this.pushTransform(pAttributes);
		SVGLineParser.parse(svgProperties, this.mCanvas, this.mSVGPaint);
		if(pushed) {
			this.popTransform();
		}
	}

	private void parseRect(final Attributes pAttributes) {
		final SVGProperties svgProperties = this.getSVGPropertiesFromAttributes(pAttributes);
		final boolean pushed = this.pushTransform(pAttributes);
		SVGRectParser.parse(svgProperties, this.mCanvas, this.mSVGPaint);
		if(pushed) {
			this.popTransform();
		}
	}

	private SVGProperties getSVGPropertiesFromAttributes(final Attributes pAttributes) {
		if(this.mSVGGroupStack.size() > 0) {
			return new SVGProperties(pAttributes, this.mSVGGroupStack.peek().getSVGProperties());
		} else {
			return new SVGProperties(pAttributes, null);
		}
	}

	private boolean pushTransform(final Attributes pAttributes) {
		final String transform = SAXHelper.getStringAttribute(pAttributes, "transform");
		if(transform == null) {
			return false;
		} else {
			final Matrix matrix = SVGTransformParser.parseTransform(transform);
			this.mCanvas.save();
			this.mCanvas.concat(matrix);
			return true;
		}
	}

	private void popTransform() {
		this.mCanvas.restore();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}