package gui;

import java.awt.*;

/**
 * Created by winston on 1/23/15.
 * Phase_01
 * CS 351 spring 2015
 * <p>
 * Global set of Color for all things gui
 */
public interface ColorsAndFonts
{

  Color REGION_NAME_FONT_C = new Color(0xFF0000);

  Color OCEANS = new Color(58, 56, 56);

  Color ACTIVE_REGION = Color.CYAN;

  /* changed from 0xE37956, now is same as PASSIVE_REGION_OUTLINE */
  Color ACTIVE_REGION_OUTLINE = new Color(58, 56, 56);

  Color SELECT_RECT_OUTLINE = new Color(94, 78, 114);
  Color SELECT_RECT_FILL = new Color(SELECT_RECT_OUTLINE.getRGB() & 0x50FFFFFF, true);


  Color PASSIVE_REGION = new Color(0xD1D1D1);
  Color PASSIVE_REGION_OUTLINE = new Color(58, 56, 56);

  Color XML_ERROR = new Color(255, 129, 78, 212);

  Color MAP_GRID = new Color(255, 255, 255, 70);

  Color GUI_BACKGROUND = new Color(0x626060);
  Color GUI_TEXT_COLOR = new Color(235, 235, 235);

  Font GUI_FONT = new Font("SansSerif", Font.PLAIN, 11);
  //  Font TOP_FONT = GUI_FONT.deriveFont(18f);
  Font TOP_FONT = new Font("SansSerif", Font.PLAIN, 14);
  Font HUD_TITLE = new Font("SansSerif", Font.PLAIN, 14);
  Font NAME_VIEW = new Font("SansSerif", Font.PLAIN, 12);


  Color MINI_BOX_REGION = new Color(222, 183, 106);

  Color[] PlantingZoneColors = {
    new Color(0x36F03A),
    new Color(0x67001f),
    new Color(0x67001f),
    new Color(0x67001f),
    new Color(0xb2182b),
    new Color(0xd6604d),
    new Color(0xf4a582),
    new Color(0xfddbc7),
    new Color(0xf7f7f7),
    new Color(0xd1e5f0),
    new Color(0x92c5de),
    new Color(0x4393c3),
    new Color(0x2166ac),
    new Color(0x053061),
    new Color(0x053061),
  };


  Color[] RAIN_FALL = {
    new Color(0xdeebf7),
    new Color(0xc6dbef),
    new Color(0x9ecae1),
    new Color(0x6baed6),
    new Color(0x4292c6),
    new Color(0x2171b5),
    new Color(0x08519c),
    new Color(0x08306b),
  };

  Color[] NOURISHMENT = {
      new Color(0x00A600),
      new Color(0x00cc00),
      new Color(0x33FF33),
      new Color(0x5CE62E),
      new Color(0xCCCC00),
      new Color(0xFF7519),
      new Color(0xFF3300),
      new Color(0xFF2100),
      new Color(0xFF0000),
  };

  Color[] SOIL = {
      new Color(0xFF4719), //clay
      new Color(0x808080), //silt
      new Color(0xFFFF99), //sand
      new Color(0xAD5C5C), //clay loam
      new Color(0x999966), //sand loam
      new Color(0xA3A385), //silt loam
      new Color(0x996633) //loam
  };

  Color[] POPULATION = {
      new Color(0xDDFFFF),
      new Color(0x80FFFF),
      new Color(0x00FFFF),
      new Color(0x00E6E6),
      new Color(0x00B2B2),
      new Color(0x2166ac)
  };

  Color[] AGE = {
      new Color(0x00DD00),
      new Color(0x4DFF4D),
      new Color(0x99FF99),
      new Color(0x4DFFFF),
      new Color(0x00FFFF),
      new Color(0x4DFFFF),
      new Color(0x88AAFF),
      new Color(0x9999FF),
      new Color(0x4D4DFF),
      new Color(0x0000FF),
      new Color(0x000099)
  };

  Color BAR_GRAPH_NEG = Color.cyan;

  class colorConverter
  {
    /**
     * get either black or white depending on the luminosity of the specified color.
     *
     * @param color to be 'inverted'
     * @return
     */
    public static Color getContrastColor(Color color)
    {
      double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
      return y >= 128 ? Color.black : Color.white;
    }
  }
}
