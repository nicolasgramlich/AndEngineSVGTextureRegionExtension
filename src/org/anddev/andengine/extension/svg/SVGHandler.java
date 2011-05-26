package org.anddev.andengine.extension.svg;

import java.util.Stack;

import org.anddev.andengine.extension.svg.adt.ISVGColorMapper;
import org.anddev.andengine.extension.svg.adt.SVGGroup;
import org.anddev.andengine.extension.svg.adt.SVGPaint;
import org.anddev.andengine.extension.svg.adt.SVGProperties;
import org.anddev.andengine.extension.svg.adt.gradient.SVGGradient;
import org.anddev.andengine.extension.svg.adt.gradient.SVGGradient.Stop;
import org.anddev.andengine.extension.svg.util.SAXHelper;
import org.anddev.andengine.extension.svg.util.SVGCircleParser;
import org.anddev.andengine.extension.svg.util.SVGEllipseParser;
import org.anddev.andengine.extension.svg.util.SVGLineParser;
import org.anddev.andengine.extension.svg.util.SVGPathParser;
import org.anddev.andengine.extension.svg.util.SVGPolygonParser;
import org.anddev.andengine.extension.svg.util.SVGPolylineParser;
import org.anddev.andengine.extension.svg.util.SVGRectParser;
import org.anddev.andengine.extension.svg.util.SVGTransformParser;
import org.anddev.andengine.extension.svg.util.constants.ISVGConstants;
import org.anddev.andengine.util.Debug;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Picture;
import android.graphics.RectF;


/**
 * @author Larva Labs, LLC
 * @author Nicolas Gramlich
 * @since 16:50:02 - 21.05.2011
 */
public class SVGHandler extends DefaultHandler implements ISVGConstants {
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

	private boolean mHidden;

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
		if (pLocalName.equals(TAG_SVG)) {
			final int width = (int) Math.ceil(SAXHelper.getFloatAttribute(pAttributes, ATTRIBUTE_WIDTH, 0f));
			final int height = (int) Math.ceil(SAXHelper.getFloatAttribute(pAttributes, ATTRIBUTE_HEIGHT, 0f));
			this.mCanvas = this.mPicture.beginRecording(width, height);
		} else if(pLocalName.equals(TAG_DEFS)) {
			// Ignore
		} else if(pLocalName.equals(TAG_LINEARGRADIENT)) {
			this.parseLinearGradient(pAttributes);
		} else if(pLocalName.equals(TAG_RADIALGRADIENT)) {
			this.parseRadialGradient(pAttributes);
		} else if(pLocalName.equals(TAG_STOP)) {
			this.parseGradientStop(pAttributes);
		} else if(pLocalName.equals(TAG_GROUP)) {
			this.parseGroup(pAttributes);
		} else if(!this.mHidden) {
			if(pLocalName.equals(TAG_RECTANGLE)) {
				this.parseRect(pAttributes);
			} else if(pLocalName.equals(TAG_LINE)) {
				this.parseLine(pAttributes);
			} else if(pLocalName.equals(TAG_CIRCLE)) {
				this.parseCircle(pAttributes);
			} else if(pLocalName.equals(TAG_ELLIPSE)) {
				this.parseEllipse(pAttributes);
			} else if(pLocalName.equals(TAG_POLYLINE)) {
				this.parsePolyline(pAttributes);
			} else if(pLocalName.equals(TAG_POLYGON)) {
				this.parsePolygon(pAttributes);
			} else if(pLocalName.equals(TAG_PATH)) {
				this.parsePath(pAttributes);
			} else {
				Debug.d("Unexpected SVG tag: '" + pLocalName + "'.");
			}
		} else {
			Debug.d("Unexpected SVG tag: '" + pLocalName + "'.");
		}
	}

	@Override
	public void characters(final char pCharacters[], final int pStart, final int pLength) {
		/* Nothing. */
	}

	@Override
	public void endElement(final String pNamespace, final String pLocalName, final String pQualifiedName)
	throws SAXException {
		if (pLocalName.equals(TAG_SVG)) {
			this.mPicture.endRecording();
		} else if (pLocalName.equals(TAG_GROUP)) {
			this.parseGroupEnd();
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void parseBounds(final String pLocalName, final Attributes pAttributes) {
		if (pLocalName.equals(TAG_RECTANGLE)) {
			final float x = SAXHelper.getFloatAttribute(pAttributes, ATTRIBUTE_X, 0f);
			final float y = SAXHelper.getFloatAttribute(pAttributes, ATTRIBUTE_Y, 0f);
			final float width = SAXHelper.getFloatAttribute(pAttributes, ATTRIBUTE_WIDTH, 0f);
			final float height = SAXHelper.getFloatAttribute(pAttributes, ATTRIBUTE_HEIGHT, 0f);
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
		if ("bounds".equalsIgnoreCase(SAXHelper.getStringAttribute(pAttributes, ATTRIBUTE_ID))) {
			this.mBoundsMode = true;
		}

		final SVGGroup parentSVGGroup = this.mSVGGroupStack.peek();
		final AttributesImpl attributesDeepCopy = new AttributesImpl(pAttributes);
		final boolean hasTransform = this.pushTransform(pAttributes);

		this.mSVGGroupStack.push(new SVGGroup(parentSVGGroup, this.getSVGPropertiesFromAttributes(attributesDeepCopy), hasTransform));

		this.updateHidden();
	}

	private void parseGroupEnd() {
		if (this.mBoundsMode) {
			this.mBoundsMode = false;
		}

		/* Pop group transform if there was one pushed. */
		if(this.mSVGGroupStack.pop().hasTransform()) {
			this.popTransform();
		}
		this.updateHidden();
		/* Clear shader map. */
		this.mSVGPaint.clearGradientShaders();
	}

	private void updateHidden() {
		if(this.mSVGGroupStack.size() == 0) {
			this.mHidden = false;
		} else {
			this.mSVGGroupStack.peek().isHidden();
		}
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
		SVGRectParser.parse(svgProperties, this.mCanvas, this.mSVGPaint, this.mRect);
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
		final String transform = SAXHelper.getStringAttribute(pAttributes, ATTRIBUTE_TRANSFORM);
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