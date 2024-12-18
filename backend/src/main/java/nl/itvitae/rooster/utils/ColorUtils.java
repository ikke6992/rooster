package nl.itvitae.rooster.utils;

public final class ColorUtils {

  private ColorUtils(){}

  public static final int COLOR_DISTANCE_THRESHOLD = 50;
  public static final double BRIGHTNESS_THRESHOLD = 128;

  public static int[] hexToRgb(String hexColour){
    int hexR = Integer.valueOf(hexColour.substring(1, 3), 16);
    int hexG = Integer.valueOf(hexColour.substring(3, 5), 16);
    int hexB = Integer.valueOf(hexColour.substring(5, 7), 16);
    return new int[]{hexR, hexG, hexB};
  }

  public static boolean calculateBrightness(int r, int g, int b){
    double brightness = Math.sqrt(0.299*r*r + 0.587*g*g + 0.114*b*b);

    return brightness < BRIGHTNESS_THRESHOLD;
  }
}
