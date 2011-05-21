package com.larvalabs.svgandroid;

/**
 * Parses numbers from SVG text. Based on the Batik Number Parser (Apache 2 License).
 *
 * @author Apache Software Foundation
 * @author Larva Labs LLC
 * @author Nicolas Gramlich
 */
public class ParserHelper {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final double[] POWERS_OF_10 = new double[128];
	static {
		for (int i = 0; i < POWERS_OF_10.length; i++) {
			POWERS_OF_10[i] = Math.pow(10, i);
		}
	}

	// ===========================================================
	// Fields
	// ===========================================================

	private final CharSequence pCharSequence;
	private final int mLength;
	private int mPosition;
	private char mCurrentChar;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ParserHelper(final CharSequence pCharSequence, final int pPosition) {
		this.pCharSequence = pCharSequence;
		this.mPosition = pPosition;
		this.mLength = pCharSequence.length();
		this.mCurrentChar = pCharSequence.charAt(pPosition);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getPosition() {
		return this.mPosition;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	private char read() {
		if (this.mPosition < this.mLength) {
			this.mPosition++;
		}
		if (this.mPosition == this.mLength) {
			return '\0';
		} else {
			return this.pCharSequence.charAt(this.mPosition);
		}
	}

	public void skipWhitespace() {
		while (this.mPosition < this.mLength) {
			if (Character.isWhitespace(this.pCharSequence.charAt(this.mPosition))) {
				this.advance();
			} else {
				break;
			}
		}
	}

	public void skipNumberSeparator() {
		while (this.mPosition < this.mLength) {
			final char c = this.pCharSequence.charAt(this.mPosition);
			switch (c) {
				case ' ':
				case ',':
				case '\n':
				case '\t':
					this.advance();
					break;
				default:
					return;
			}
		}
	}

	public void advance() {
		this.mCurrentChar = this.read();
	}

	/**
	 * Parses the content of the buffer and converts it to a float.
	 */
	private float parseFloat() {
		int     mant     = 0;
		int     mantissaDigit  = 0;
		boolean mantPos  = true;
		boolean mantissaRead = false;

		int     exp      = 0;
		int     expDig   = 0;
		int     expAdj   = 0;
		boolean expPos   = true;

		switch (this.mCurrentChar) {
			case '-':
				mantPos = false;
			case '+':
				this.mCurrentChar = this.read();
		}

		m1: switch (this.mCurrentChar) {
			default:
				return Float.NaN;

			case '.':
				break;

			case '0':
				mantissaRead = true;
				l: for (;;) {
					this.mCurrentChar = this.read();
					switch (this.mCurrentChar) {
						case '1': case '2': case '3': case '4':
						case '5': case '6': case '7': case '8': case '9':
							break l;
						case '.': case 'e': case 'E':
							break m1;
						default:
							return 0.0f;
						case '0':
					}
				}

			case '1': case '2': case '3': case '4':
			case '5': case '6': case '7': case '8': case '9':
				mantissaRead = true;
				l: for (;;) {
					if (mantissaDigit < 9) {
						mantissaDigit++;
						mant = mant * 10 + (this.mCurrentChar - '0');
					} else {
						expAdj++;
					}
					this.mCurrentChar = this.read();
					switch (this.mCurrentChar) {
						default:
							break l;
						case '0': case '1': case '2': case '3': case '4':
						case '5': case '6': case '7': case '8': case '9':
					}
				}
		}

		if (this.mCurrentChar == '.') {
			this.mCurrentChar = this.read();
			m2: switch (this.mCurrentChar) {
				default:
				case 'e': case 'E':
					if (!mantissaRead) {
						throw new IllegalArgumentException("Unexpected char '" + this.mCurrentChar + "'.");
					}
					break;

				case '0':
					if (mantissaDigit == 0) {
						l: for (;;) {
							this.mCurrentChar = this.read();
							expAdj--;
							switch (this.mCurrentChar) {
								case '1': case '2': case '3': case '4':
								case '5': case '6': case '7': case '8': case '9':
									break l;
								default:
									if (!mantissaRead) {
										return 0.0f;
									}
									break m2;
								case '0':
							}
						}
					}
				case '1': case '2': case '3': case '4':
				case '5': case '6': case '7': case '8': case '9':
					l: for (;;) {
						if (mantissaDigit < 9) {
							mantissaDigit++;
							mant = mant * 10 + (this.mCurrentChar - '0');
							expAdj--;
						}
						this.mCurrentChar = this.read();
						switch (this.mCurrentChar) {
							default:
								break l;
							case '0': case '1': case '2': case '3': case '4':
							case '5': case '6': case '7': case '8': case '9':
						}
					}
			}
		}

		switch (this.mCurrentChar) {
			case 'e': case 'E':
				this.mCurrentChar = this.read();
				switch (this.mCurrentChar) {
					default:
						throw new IllegalArgumentException("Unexpected char '" + this.mCurrentChar + "'.");
					case '-':
						expPos = false;
					case '+':
						this.mCurrentChar = this.read();
						switch (this.mCurrentChar) {
							default:
								throw new IllegalArgumentException("Unexpected char '" + this.mCurrentChar + "'.");
							case '0': case '1': case '2': case '3': case '4':
							case '5': case '6': case '7': case '8': case '9':
						}
					case '0': case '1': case '2': case '3': case '4':
					case '5': case '6': case '7': case '8': case '9':
				}

				en: switch (this.mCurrentChar) {
					case '0':
						l: for (;;) {
							this.mCurrentChar = this.read();
							switch (this.mCurrentChar) {
								case '1': case '2': case '3': case '4':
								case '5': case '6': case '7': case '8': case '9':
									break l;
								default:
									break en;
								case '0':
							}
						}

					case '1': case '2': case '3': case '4':
					case '5': case '6': case '7': case '8': case '9':
						l: for (;;) {
							if (expDig < 3) {
								expDig++;
								exp = exp * 10 + (this.mCurrentChar - '0');
							}
							this.mCurrentChar = this.read();
							switch (this.mCurrentChar) {
								default:
									break l;
								case '0': case '1': case '2': case '3': case '4':
								case '5': case '6': case '7': case '8': case '9':
							}
						}
				}
			default:
		}

		if (!expPos) {
			exp = -exp;
		}
		exp += expAdj;
		if (!mantPos) {
			mant = -mant;
		}

		return ParserHelper.buildFloat(mant, exp);
	}

	public float nextFloat() {
		this.skipWhitespace();
		final float f = this.parseFloat();
		this.skipNumberSeparator();
		return f;
	}

	public static float buildFloat(int pMantissa, final int pExponent) {
		if (pExponent < -125 || pMantissa == 0) {
			return 0.0f;
		}

		if (pExponent >=  128) {
			return (pMantissa > 0)
			? Float.POSITIVE_INFINITY
					: Float.NEGATIVE_INFINITY;
		}

		if (pExponent == 0) {
			return pMantissa;
		}

		if (pMantissa >= (1 << 26)) {
			pMantissa++;  // round up trailing bits if they will be dropped.
		}

		return (float) ((pExponent > 0) ? pMantissa * POWERS_OF_10[pExponent] : pMantissa / POWERS_OF_10[-pExponent]);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
