package squawker.cleanup.v5;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.spockframework.runtime.extension.ExtensionAnnotation;

// tag::annotation[]
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD}) // <1>
@ExtensionAnnotation(TruncateTablesExtension.class) // <2>
public @interface TruncateTables {
}
// end::annotation[]
