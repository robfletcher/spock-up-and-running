package com.oreilly.spock.extension.db;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import com.oreilly.spock.Connector;
import com.oreilly.spock.TypedConnector;
import groovy.sql.Sql;
import static java.lang.String.format;

public class DefaultConnector implements Connector {
  @Override public Connection connectionFrom(Object source) {
    if (DataSource.class.isAssignableFrom(source.getClass())) {
      return DATA_SOURCE_CONNECTOR.connectionFrom(source);
    } else if (Sql.class.isAssignableFrom(source.getClass())) {
      return GROOVY_SQL_CONNECTOR.connectionFrom(source);
    } else if (Connection.class.isAssignableFrom(source.getClass())) {
      return CONNECTION_CONNECTOR.connectionFrom(source);
    } else {
      return REFLECTION_CONNECTOR.connectionFrom(source);
    }
  }

  private static final TypedConnector<Connection> CONNECTION_CONNECTOR =
    new TypedConnector<Connection>(Connection.class) {
      @Override protected Connection apply(Connection source) {
        return source;
      }
    };

  private static final TypedConnector<DataSource> DATA_SOURCE_CONNECTOR =
    new TypedConnector<DataSource>(DataSource.class) {
      @Override
      protected Connection apply(DataSource source) throws SQLException {
        return source.getConnection();
      }
    };

  private static final TypedConnector<Sql> GROOVY_SQL_CONNECTOR =
    new TypedConnector<Sql>(Sql.class) {
      @Override protected Connection apply(Sql source) throws SQLException {
        if (source.getConnection() != null) {
          return CONNECTION_CONNECTOR.connectionFrom(source.getConnection());
        } else {
          return DATA_SOURCE_CONNECTOR.connectionFrom(source.getDataSource());
        }
      }
    };

  private static final Connector REFLECTION_CONNECTOR = source -> {
    try {
      Method method = source.getClass().getMethod("getConnection");
      if (!method.getReturnType().isAssignableFrom(Connection.class)) {
        throw new IllegalArgumentException(format(
          "%s has a getConnection method but it does not return %s",
          source.getClass(),
          Connection.class
        ));
      }
      method.setAccessible(true);
      return (Connection) method.invoke(source);
    } catch (NoSuchMethodException e) {
      throw new IllegalArgumentException(format(
        "Class %s has no getConnection method",
        source.getClass()
      ), e);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new IllegalStateException(format(
        "Unable to call getConnection on instance of %s",
        source.getClass()
      ), e);
    }
  };
}
