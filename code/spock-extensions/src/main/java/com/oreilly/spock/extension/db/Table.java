package com.oreilly.spock.extension.db;

import java.util.Objects;
import static java.lang.String.format;

class Table {
  private final String schema;
  private final String name;

  Table(String schema, String name) {
    this.schema = schema;
    this.name = name;
  }

  String getSchema() {
    return schema;
  }

  String getName() {
    return name;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Table table = (Table) o;
    return Objects.equals(schema, table.schema) &&
      Objects.equals(name, table.name);
  }

  @Override public int hashCode() {
    return Objects.hash(schema, name);
  }

  @Override public String toString() {
    return format("Table{schema='%s', name='%s'}", schema, name);
  }
}
