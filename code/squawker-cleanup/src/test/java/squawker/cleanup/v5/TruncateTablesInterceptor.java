package squawker.cleanup.v5;

import java.sql.Connection;
import org.skife.jdbi.v2.DBI;
import org.spockframework.runtime.extension.IMethodInterceptor;
import org.spockframework.runtime.extension.IMethodInvocation;
import org.spockframework.runtime.model.FieldInfo;
import static com.oreilly.spock.extension.db.TableTruncator.truncateTables;

// tag::interceptor[]
public class TruncateTablesInterceptor implements IMethodInterceptor {

  private final FieldInfo field;

  TruncateTablesInterceptor(FieldInfo field) {
    this.field = field;
  }

  @Override
  public void intercept(IMethodInvocation invocation) throws Throwable {
    try {
      invocation.proceed(); // <1>
    } finally {
      DBI dbi = (DBI) field.readValue(invocation.getInstance()); // <2>
      if (dbi != null) {
        try (Connection connection = dbi.open().getConnection()) {
          truncateTables(connection);
        }
      }
    }
  }
}
// end::interceptor[]
