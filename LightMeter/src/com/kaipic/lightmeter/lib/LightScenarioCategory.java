package com.kaipic.lightmeter.lib;

import java.util.ArrayList;
import java.util.List;

public class LightScenarioCategory {
  private final String name;
  private final List<LightScenario> scenarios = new ArrayList<LightScenario>();

  public LightScenarioCategory(String name) {
    this.name = name;
  }

  private void addScenario(LightScenario scenario){
    scenarios.add(scenario);
  }

  protected void addScenario(String description, int... exposureValues){
    ExposureValue[] evs = new ExposureValue[exposureValues.length];
    for(int i = 0; i < exposureValues.length; i++) {
      evs[i] = new ExposureValue(exposureValues[i]);
    }
    addScenario(new LightScenario(description, this, evs));
  }


}
