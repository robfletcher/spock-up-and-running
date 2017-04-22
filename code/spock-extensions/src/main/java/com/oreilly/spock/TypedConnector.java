package com.oreilly.spock;

import java.sql.Connection;
import org.spockframework.util.WrappedException;
import static java.lang.String.format;

/**
 * Base class for implementations of {@link Connector} that deal with a specific
 * type of connection source.
 *
 * @param <T> The connection source type. The {@link TruncateTables} annotation
 *            may be applied to fields of this type when using this `Connector`
 *            implementations.
 */
public abstract class TypedConnector<T> implements Connector {

  private final Class<T> type;

  protected TypedConnector(Class<T> type) {
    this.type = type;
  }

  public final Connection connectionFrom(Object source) {
    if (type.isAssignableFrom(source.getClass())) {
      try {
        return apply((T) source);
      } catch (Exception e) {
        throw new WrappedException("Error acquiring connection", e);
      }
    }
    throw new IllegalArgumentException(
      format("Connection source of class %s is not an instance of %s", source.getClass(), type)
    );
  }

  protected abstract Connection apply(T source) throws Exception;
}
