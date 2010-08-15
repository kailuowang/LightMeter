package com.kaipic.lightmeter.lib;

import org.junit.Test;

import static com.kaipic.lightmeter.lib.LengthTest.assertLengthEquals;

public class DoFCalculatorTest {
  @Test
  public void shouldCalculateHyperfocalDistance() throws Exception {
    DoFCalculator calculator = createTestingCalculator();
    assertLengthEquals(new Length(5438), calculator.hyperFocalDistance(), 1f);
  }

  @Test
  public void shouldCalculateNearLimit() throws Exception {
    DoFCalculator calculator = createTestingCalculator();
    calculator.setSubjectDistance(new Length(1000f));
    assertLengthEquals(new Length(850), calculator.nearLimit(), 1f);
  }

  @Test
  public void shouldCalculateFarLimit() throws Exception {
    DoFCalculator calculator = createTestingCalculator();
    calculator.setSubjectDistance(new Length(1000f));
    assertLengthEquals(new Length(1214), calculator.farLimit(), 1f);
  }

  private DoFCalculator createTestingCalculator() {
    DoFCalculator calculator = new DoFCalculator();
    calculator.setFocalLength(new Length(50f));
    calculator.setAperture(new Aperture(16f));
    calculator.setCircleOfConfusion(CirclesOfConfusion.FULL_FRAME_35MM);
    return calculator;
  }


}
