package squawker.cleanup.v5;

import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension;
import org.spockframework.runtime.model.FieldInfo;
import org.spockframework.runtime.model.SpecInfo;

// tag::extension-class[]
public class TruncateTablesExtension
  extends AbstractAnnotationDrivenExtension<TruncateTables> { // <1>

  @Override
  public void visitFieldAnnotation(TruncateTables annotation, FieldInfo field) {
    SpecInfo spec = field.getParent(); // <2>
    spec.addCleanupInterceptor(new TruncateTablesInterceptor(field));
  }
}
// end::extension-class[]
