package org.anddev.andengine.extension.svg.adt.filter.element;

import android.graphics.Paint;

/**
 * @author Nicolas Gramlich
 * @since 16:54:15 - 26.05.2011
 */
public interface ISVGFilterElement {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void apply(final Paint pPaint);
}
