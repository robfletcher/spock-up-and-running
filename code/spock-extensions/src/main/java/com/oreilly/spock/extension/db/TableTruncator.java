package com.oreilly.spock.extension.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Logger;
import org.spockframework.util.WrappedException;
import static java.lang.String.format;
import static java.util.logging.Logger.getLogger;

public class TableTruncator {

  private static final String[] TABLE_TYPE = {"TABLE"};

  private final Logger log = getLogger(getClass().getName());
  private final Connection connection;
  private final Collection<Table> remaining = new HashSet<>();

  public static void truncateTables(Connection connection) {
    new TableTruncator(connection).truncateTables();
  }

  private TableTruncator(Connection connection) {
    this.connection = connection;
  }

  private void truncateTables() {
    findTables();
    while (!remaining.isEmpty()) {
      deleteFrom(remaining.iterator().next());
    }
  }

  private void findTables() {
    try {
      DatabaseMetaData metaData = connection.getMetaData();
      try (ResultSet rs = metaData.getTables(connection.getCatalog(), null, null, TABLE_TYPE)) {
        while (rs.next()) {
          remaining.add(new Table(
            rs.getString("TABLE_SCHEM"),
            rs.getString("TABLE_NAME")
          ));
        }
      }
    } catch (SQLException e) {
      throw new WrappedException("Error finding database tables", e);
    }
  }

  private void deleteFrom(Table table) {
    deleteFromDependentTables(table);
    try {
      String sql = format("delete from %s.%s", table.getSchema(), table.getName());
      connection
        .createStatement()
        .execute(sql);
      log.info(format("Executed: %s", sql));
      remaining.remove(table);
    } catch (SQLException e) {
      throw new WrappedException(format("Error deleting data from %s", table), e);
    }
  }

  private void deleteFromDependentTables(Table table) {
    try {
      DatabaseMetaData metaData = connection.getMetaData();
      try (ResultSet fks = metaData.getExportedKeys(connection.getCatalog(), table.getSchema(), table.getName())) {
        while (fks.next()) {
          Table referencedTable = new Table(
            fks.getString("FKTABLE_SCHEM"),
            fks.getString("FKTABLE_NAME")
          );
          if (remaining.contains(referencedTable)) {
            deleteFrom(referencedTable);
          }
        }
      }
    } catch (SQLException e) {
      throw new WrappedException(format("Error analyzing exported keys of %s", table), e);
    }
  }
}
