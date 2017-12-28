package com.momnop.currency;

import org.lwjgl.opengl.GL11;

public class ColorHelper
{
  public static int getDecimalFromRGB(int red, int green, int blue)
  {
    int dec = Math.max(Math.min(255, red), 0);
    dec = (dec << 8) + Math.max(Math.min(255, green), 0);
    dec = (dec << 8) + Math.max(Math.min(255, blue), 0);
    return dec;
  }
  
  public static int getDecimalFromRGB(int[] rgb)
  {
    return getDecimalFromRGB(rgb[0], rgb[1], rgb[2]);
  }
  
  public static int[] getRGBFromDecimal(int dec)
  {
    int[] a = new int[3];
    int i = 0;
    
    float f = (dec >> 16 & 0xFF) / 255.0F;
    float f1 = (dec >> 8 & 0xFF) / 255.0F;
    float f2 = (dec & 0xFF) / 255.0F;
    i = (int)(i + Math.max(f, Math.max(f1, f2)) * 255.0F);
    a[0] = ((int)(a[0] + f * 255.0F));
    a[1] = ((int)(a[1] + f1 * 255.0F));
    a[2] = ((int)(a[2] + f2 * 255.0F));
    return a;
  }
  
  public static void glColor(int dec)
  {
    glColor(dec, 1.0D);
  }
  
  public static void glColor(int dec, double alpha)
  {
    int[] c = getRGBFromDecimal(dec);
    GL11.glColor4d(c[0] / 255.0D, c[1] / 255.0D, c[2] / 255.0D, alpha);
  }
  
  public static float[] getColorFromStage(float[] c0, float[] c1, int maxStages, float stage)
  {
    float[] ret = { 1.0F, 1.0F, 1.0F, 1.0F };
    float prog = 1.0F - stage / (maxStages - 1);
    ret[0] = (c0[0] * prog + c1[0] * (1.0F - prog));
    ret[1] = (c0[1] * prog + c1[1] * (1.0F - prog));
    ret[2] = (c0[2] * prog + c1[2] * (1.0F - prog));
    if ((c0.length == 4) && (c1.length == 4)) {
      ret[3] = (c0[3] * prog + c1[3] * (1.0F - prog));
    }
    return ret;
  }
}
