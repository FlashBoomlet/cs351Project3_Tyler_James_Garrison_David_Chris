package gui.hud.PieChart;

import java.awt.*;
import java.awt.geom.Arc2D;

/**
  * @author Tyler Lynch <lyncht@unm.edu>
  * @since 4.28.15
  */
public class Slice
{
  // Do not make static
  private double angle;
  private Color color;
  private String name;
  private double value;
  private Arc2D.Double arc;
  private double percent;

  /**
   * Get the Arc graphic for slice
   *
   * @return Arc2D.Double to draw
   */
  public Arc2D.Double getArc()
  {
    return arc;
  }

  /**
   * Set the Arc start angle
   *
   * @param startAngle of teh arc
   */
  public void setArcStartAngle(int startAngle)
  {
    arc.setAngleStart(startAngle);
  }

  /**
   * Set the frame that the arc will sit in
   *
   * @param area of which the arc may be in
   */
  public void setFrame(Rectangle area)
  {
    arc.setFrame(area.x, area.y, area.width, area.height);
  }

  /**
   * Constructor to assign the values of a new slice
   * @param color -of the slice
   * @param name -of the slice
   * @param value -of the slice being represented (Not a percentage, the percentage will be calculated separately)
   */
  public Slice(double value, Color color, String name) {
    this.color = color;
    this.name = name;
    this.value = value;
    arc = new Arc2D.Double(Arc2D.PIE);
  }

  /**
   * Update the crop
   *
   * @param color -of the slice
   * @param name -of the slice
   * @param value -of the slice being represented (Not a percentage, the percentage will be calculated separately)
   */
  public void updateSlice(double value, Color color, String name) {
    this.color = color;
    this.name = name;
    this.value = value;
    arc = new Arc2D.Double(Arc2D.PIE);
  }

  /**
   * Set the angle of the slice
   * @param angle -of a pie (double)
   */
  public void setAngle(double angle)
  {
    arc.setAngleExtent( angle );
    this.angle = angle;
  }

  /**
   * Set the percentage of the slice
   *
   * @return the angle of the slice in degrees
   */
  public void setPercent(double percent)
  {
    this.percent = percent;
  }

  /**
   * Get the slice angle
   *
   * @return the angle of the slice in degrees
   */
  public double getPercent()
  {
    return percent;
  }

  /**
   * Get the value of the slice nad not the percentage
   *
   * for the hover over feature to be implements in the future.
   *
   * @return the actual data of the slice, not percent
   */
  public double getValue()
  {
    return value;
  }

  /**
   * getColor is used to get the color of the slice
   *
   * @return the color of the slice
   */
  public Color getColor()
  {
    return color;
  }

  /**
   * Get the name of the slice
   * @return the name of the slice
   */
  public String getName()
  {
    return name;
  }

}