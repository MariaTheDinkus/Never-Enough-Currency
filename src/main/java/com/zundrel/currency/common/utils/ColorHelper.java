package com.zundrel.currency.common.utils;

import org.lwjgl.opengl.GL11;

public class ColorHelper {
	public static int getDecimalFromRGB(int red, int green, int blue) {
		int dec = Math.max(Math.min(0xFF, red), 0);
		dec = (dec << 8) + Math.max(Math.min(0xFF, green), 0);
		dec = (dec << 8) + Math.max(Math.min(0xFF, blue), 0);
		return dec;
	}

	public static int getDecimalFromRGB(int[] rgb) {
		return getDecimalFromRGB(rgb[0], rgb[1], rgb[2]);
	}

	public static int[] getRGBFromDecimal(int dec) {
		int[] a = new int[3];
		int i = 0;
		float f;
		float f1;
		f = (dec >> 16 & 255) / 255.0F;
		f1 = (dec >> 8 & 255) / 255.0F;
		float f2 = (dec & 255) / 255.0F;
		i = (int) (i + Math.max(f, Math.max(f1, f2)) * 255.0F);
		a[0] = (int) (a[0] + f * 255.0F);
		a[1] = (int) (a[1] + f1 * 255.0F);
		a[2] = (int) (a[2] + f2 * 255.0F);
		return a;
	}

	public static void glColor(int dec) {
		glColor(dec, 1);
	}

	public static void glColor(int dec, double alpha) {
		int[] c = getRGBFromDecimal(dec);
		GL11.glColor4d(c[0] / 255D, c[1] / 255D, c[2] / 255D, alpha);
	}

	/**
	 * Gets the color at a certain point in a gradient. Both colors should be
	 * the same format(RGB or RGBA).
	 * 
	 * @param c0
	 *            The first color.
	 * @param c1
	 *            The second color.
	 * @param maxStages
	 *            The amount of colors in the gradient.
	 * @param stage
	 *            The index of the color in the gradient.
	 * @return float[] RGB components
	 */
	public static float[] getColorFromStage(float[] c0, float[] c1, int maxStages, float stage) {
		float[] ret = new float[] { 1, 1, 1, 1 };
		float prog = 1F - (stage / (maxStages - 1));
		ret[0] = ((c0[0] * prog) + (c1[0] * (1 - prog)));
		ret[1] = ((c0[1] * prog) + (c1[1] * (1 - prog)));
		ret[2] = ((c0[2] * prog) + (c1[2] * (1 - prog)));
		if (c0.length == 4 && c1.length == 4)
			ret[3] = ((c0[3] * prog) + (c1[3] * (1 - prog)));
		return ret;
	}
}
