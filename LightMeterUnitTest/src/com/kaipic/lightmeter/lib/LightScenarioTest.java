package com.kaipic.lightmeter.lib;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LightScenarioTest {

  @Test
  public void testFindLightScenarioByExposureValue(){
    LightScenario scene14 = new LightScenario("testScene14", null, new ExposureValue(14));
    new LightScenario("testScene15", null, new ExposureValue(15));
    assertEquals(1, LightScenario.find(new ExposureValue(14)).size());
    assertTrue(LightScenario.find(new ExposureValue(14)).contains(scene14));
  }

  @Test
  public void testToString() {
    assertEquals("EV 15, testScene15", new LightScenario("testScene15", null, new ExposureValue(15)).toString());
    assertEquals("EV 15-16, testScene15", new LightScenario("testScene15", null, new ExposureValue(15), new ExposureValue(16)).toString());

  }
}
