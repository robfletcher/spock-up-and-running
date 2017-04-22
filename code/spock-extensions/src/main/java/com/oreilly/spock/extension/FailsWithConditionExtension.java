package com.oreilly.spock.extension;

import com.oreilly.spock.FailsWithCondition;
import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension;
import org.spockframework.runtime.model.FeatureInfo;
import org.spockframework.runtime.model.MethodInfo;
import org.spockframework.runtime.model.SpecInfo;

public class FailsWithConditionExtension extends AbstractAnnotationDrivenExtension<FailsWithCondition> {
  public void visitSpecAnnotation(FailsWithCondition FailsWithCondition, SpecInfo spec) {
    for (FeatureInfo feature : spec.getFeatures())
      if (!feature.getFeatureMethod().getReflection().isAnnotationPresent(FailsWithCondition.class))
        feature.getFeatureMethod().addInterceptor(new FailsWithConditionInterceptor(FailsWithCondition));
  }

  public void visitFeatureAnnotation(FailsWithCondition FailsWithCondition, FeatureInfo feature) {
    feature.getFeatureMethod().addInterceptor(new FailsWithConditionInterceptor(FailsWithCondition));
  }

  public void visitFixtureAnnotation(FailsWithCondition FailsWithCondition, MethodInfo fixtureMethod) {
    fixtureMethod.addInterceptor(new FailsWithConditionInterceptor(FailsWithCondition));
  }
}

