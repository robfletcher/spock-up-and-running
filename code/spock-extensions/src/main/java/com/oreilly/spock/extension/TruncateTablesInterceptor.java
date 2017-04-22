package com.oreilly.spock.extension;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.oreilly.spock.Connector;
import com.oreilly.spock.TruncateTables;
import org.spockframework.runtime.extension.ExtensionUtil;
import org.spockframework.runtime.extension.IMethodInterceptor;
import org.spockframework.runtime.extension.IMethodInvocation;
import org.spockframework.runtime.model.FieldInfo;
import static com.oreilly.spock.extension.db.TableTruncator.truncateTables;

public class TruncateTablesInterceptor implements IMethodInterceptor {
  private final List<FieldInfo> fields = new ArrayList<FieldInfo>();

  public void add(FieldInfo field) {
    fields.add(field);
  }

  public void install(List<IMethodInterceptor> interceptors) {
    if (fields.isEmpty()) return;

    Collections.reverse(fields);
    interceptors.add(this);
  }

  public void intercept(IMethodInvocation invocation) throws Throwable {
    List<Throwable> exceptions = new ArrayList<>();

    try {
      invocation.proceed();
    } catch (Throwable t) {
      exceptions.add(t);
    }

    for (FieldInfo field : fields) {
      TruncateTables annotation = field.getAnnotation(TruncateTables.class);

      try {
        Connector connector = annotation.value().newInstance();
        Object fieldValue = field.readValue(invocation.getInstance());

        if (fieldValue == null) continue;

        try (Connection connection = connector.connectionFrom(fieldValue)) {
          // TODO: allow schema, etc. to be parameterized
          truncateTables(connection);
        }
      } catch (Throwable t) {
        if (!annotation.quiet()) exceptions.add(t);
      }
    }

    ExtensionUtil.throwAll(exceptions);
  }
}

