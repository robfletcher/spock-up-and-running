package squawker.jdbi;

import java.util.List;
import javax.sql.DataSource;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.ResultSetMapperFactory;
import org.skife.jdbi.v2.spring.DBIFactoryBean;
import org.skife.jdbi.v2.tweak.ArgumentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import static java.util.Collections.emptyList;

/**
 * Type-safe version of {@link DBIFactoryBean} that can also auto-wire instances
 * of {@link ArgumentFactory} and {@link ResultSetMapperFactory}.
 */
class TypedDBIFactoryBean extends AbstractFactoryBean<DBI> {

  private final DBIFactoryBean delegate;
  private List<ArgumentFactory> argumentFactories = emptyList();
  private List<ResultSetMapperFactory> resultSetMapperFactories = emptyList();

  public TypedDBIFactoryBean(DataSource dataSource) {
    delegate = new DBIFactoryBean(dataSource);
  }

  @Override public Class<?> getObjectType() {
    return DBI.class;
  }

  @Override protected DBI createInstance() throws Exception {
    DBI dbi = (DBI) delegate.getObject();
    argumentFactories.forEach(dbi::registerArgumentFactory);
    resultSetMapperFactories.forEach(dbi::registerMapper);
    return dbi;
  }

  @Autowired(required = false)
  public void setArgumentFactories(List<ArgumentFactory> argumentFactories) {
    this.argumentFactories = argumentFactories;
  }

  @Autowired(required = false)
  public void setResultSetMapperFactories(List<ResultSetMapperFactory> resultSetMapperFactories) {
    this.resultSetMapperFactories = resultSetMapperFactories;
  }
}
