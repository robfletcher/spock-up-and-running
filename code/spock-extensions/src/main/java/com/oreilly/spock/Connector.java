package com.oreilly.spock;

import java.sql.Connection;

/**
 * A functional interface for acquiring a database connection from a source.
 */
public interface Connector {
  Connection connectionFrom(Object source);
}
