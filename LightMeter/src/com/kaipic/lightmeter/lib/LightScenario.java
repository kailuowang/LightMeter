package com.kaipic.lightmeter.lib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;


public class LightScenario {
  private final String description;
  private final LightScenarioCategory category;
  private final List<ExposureValue> lightValues;
  private static final List<LightScenario> scenarios = new ArrayList<LightScenario>(); 

  public LightScenario(String description, LightScenarioCategory category, ExposureValue... lightValues) {
    this.description = description;
    this.category = category;
    this.lightValues = Arrays.asList(lightValues);
    scenarios.add(this);
  }

  public static Collection<LightScenario> find(final ExposureValue ev) {
    return CollectionUtils.select(scenarios, new Predicate() {
      public boolean evaluate(Object o) {
        return ((LightScenario) o).lightValues.contains(ev);
      }
    });


  }
}
