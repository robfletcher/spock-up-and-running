package com.oreilly.spock.extension;

import com.oreilly.spock.TruncateTables;
import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension;
import org.spockframework.runtime.model.FieldInfo;
import org.spockframework.runtime.model.SpecInfo;

public class TruncateTablesExtension extends AbstractAnnotationDrivenExtension<TruncateTables> {
  private final TruncateTablesInterceptor sharedFieldInterceptor = new TruncateTablesInterceptor();
  private final TruncateTablesInterceptor instanceFieldInterceptor = new TruncateTablesInterceptor();

  @Override
  public void visitFieldAnnotation(TruncateTables annotation, FieldInfo field) {
    if (field.isShared()) sharedFieldInterceptor.add(field);
    else instanceFieldInterceptor.add(field);
  }

  @Override
  public void visitSpec(SpecInfo spec) {
    sharedFieldInterceptor.install(spec.getCleanupSpecInterceptors());
    instanceFieldInterceptor.install(spec.getCleanupInterceptors());
  }
}
