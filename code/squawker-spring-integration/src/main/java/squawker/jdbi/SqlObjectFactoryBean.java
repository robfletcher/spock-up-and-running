package squawker.jdbi;

import org.skife.jdbi.v2.DBI;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

public class SqlObjectFactoryBean<T> implements FactoryBean<T> {

  private final Class<T> sqlObjectType;
  @Autowired DBI dbi;

  public static <T> FactoryBean<T> create(Class<T> sqlObjectType) {
    return new SqlObjectFactoryBean<>(sqlObjectType);
  }

  public SqlObjectFactoryBean(Class<T> sqlObjectType) {
    this.sqlObjectType = sqlObjectType;
  }

  @Override public T getObject() {
    return dbi.onDemand(sqlObjectType);
  }

  @Override public Class<T> getObjectType() {
    return sqlObjectType;
  }

  @Override public boolean isSingleton() {
    return false;
  }
}
