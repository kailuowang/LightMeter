package com.kaipic.lightmeter.lib;

import org.junit.Test;

import static com.kaipic.lightmeter.lib.LengthTest.assertLengthEquals;

public class DoFCalculatorTest {
  @Test
  public void shouldCalculateHyperfocalDistance() throws Exception {
    DoFCalculator calculator = new DoFCalculator();
    calculator.setFocalLength(new Length(50f));
    calculator.setAperture(new Aperture(16f));
    calculator.setCircleOfConfusion(new Length(0.03f));
    assertLengthEquals(new Length(5258), calculator.hyperFocalDistance(), 1f);
  }

  @Test
  public void shouldCalculateNearLimit() throws Exception {
    DoFCalculator calculator = new DoFCalculator();
    calculator.setFocalLength(new Length(50f));
    calculator.setAperture(new Aperture(16f));
    calculator.setCircleOfConfusion(new Length(0.03f));
    calculator.setSubjectDistance(new Length(1000f));
    assertLengthEquals(new Length(846), calculator.nearLimit(), 1f);
  }

  @Test
  public void shouldCalculateFarLimit() throws Exception {
    DoFCalculator calculator = new DoFCalculator();
    calculator.setFocalLength(new Length(50f));
    calculator.setAperture(new Aperture(16f));
    calculator.setCircleOfConfusion(new Length(0.03f));
    calculator.setSubjectDistance(new Length(1000f));
    assertLengthEquals(new Length(1223), calculator.farLimit(), 1f);
  }


}
