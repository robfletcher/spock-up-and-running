package com.oreilly.spock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Connection;
import javax.sql.DataSource;
import com.oreilly.spock.extension.TruncateTablesExtension;
import com.oreilly.spock.extension.db.DefaultConnector;
import groovy.sql.Sql;
import org.spockframework.runtime.extension.ExtensionAnnotation;

/**
 * A field in a Spock specification marked with `@TruncateTables` will be used
 * to acquire a database connection and delete data from all tables.
 *
 * By default the annotation can be used on a {@link Connection},
 * {@link DataSource} or {@link Sql} field.
 * To use a different field type simply implement {@link Connector} (or extend
 * {@link TypedConnector}) and specify {@link #value()}.
 *
 * After each feature method (or after all if the annotated field is also
 * annotated with `@Shared`) all data is deleted from any database tables
 * accessible via the connection.
 * Data is not deleted in any particular order but if foreign key constraints
 * exist deletion will be performed in an order that ensures constraints are not
 * violated.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@ExtensionAnnotation(TruncateTablesExtension.class)
public @interface TruncateTables {
  /**
   * The strategy used to connect to the database. If this annotation is applied
   * to anything other than a {@link Connection}, {@link DataSource} or Groovy
   * {@link Sql} field you need to supply your own implementation.
   */
  Class<? extends Connector> value() default DefaultConnector.class;

  /**
   * If `true` exceptions are ignored.
   */
  boolean quiet() default false;

  /**
   * If `true` SQL commands are logged to stdout.
   */
  boolean verbose() default false;

}
