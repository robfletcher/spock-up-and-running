package com.oreilly.spock

import java.sql.Connection
import javax.sql.DataSource
import groovy.sql.Sql
import org.h2.jdbcx.JdbcDataSource
import org.skife.jdbi.v2.DBI
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

@Stepwise
abstract class TruncateTablesSpec extends Specification {

  @Shared
  def dataSource = new JdbcDataSource(url: "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false")
  def sql = new Sql(dataSource)

  def setupSpec() {
    new Sql(dataSource.connection).with {
      execute("create table performer (id integer not null primary key, name varchar(50) not null)")
      execute("create table band (id integer not null primary key, name varchar(50) not null)")
      execute("create table band_member (band_id integer not null, performer_id integer not null, role varchar(50) not null)")
      execute("alter table band_member add constraint band_member_unique unique(band_id, performer_id)")
      execute("alter table band_member add foreign key (band_id) references band (id)")
      execute("alter table band_member add foreign key (performer_id) references performer (id)")
    }
  }

  def cleanupSpec() {
    new Sql(dataSource.connection).with {
      execute("drop table band_member")
      execute("drop table band")
      execute("drop table performer")
    }
  }

  def "create some data"() {
    given:
    sql.executeInsert("insert into band (id, name) values (1, 'Dead Kennedys')")
    sql.executeInsert("insert into performer (id, name) values (1, 'Jello Biafra')")
    sql.executeInsert("insert into band_member (band_id, performer_id, role) values (1, 1, 'vocals')")
    sql.executeInsert("insert into performer (id, name) values (2, 'East Bay Ray')")
    sql.executeInsert("insert into band_member (band_id, performer_id, role) values (1, 2, 'guitar')")

    expect:
    sql.firstRow("select count(*) as n from band").n == 1
    sql.firstRow("select count(*) as n from performer").n == 2
    sql.firstRow("select count(*) as n from band_member").n == 2
  }

  def "data should have been deleted"() {
    expect:
    sql.firstRow("select count(*) as n from band").n == 0
    sql.firstRow("select count(*) as n from performer").n == 0
    sql.firstRow("select count(*) as n from band_member").n == 0
  }

  static class TruncateTablesWithRawConnectionSpec extends TruncateTablesSpec {
    @TruncateTables Connection connection = dataSource.connection
  }

  static class TruncateTablesWithDataSourceSpec extends TruncateTablesSpec {
    @TruncateTables DataSource ds = dataSource
  }

  static class TruncateTablesWithGroovySqlSpec extends TruncateTablesSpec {
    @TruncateTables Sql groovySql = new Sql(dataSource)
  }

  static class TruncateTablesWithSingleConnectionGroovySqlSpec extends TruncateTablesSpec {
    @TruncateTables Sql groovySql = new Sql(dataSource.connection)
  }

  static class TruncateTablesWithJdbiSpec extends TruncateTablesSpec {
    @TruncateTables(DBIConnector) DBI dbi = new DBI(dataSource);
  }

  static class DBIConnector extends TypedConnector<DBI> {
    DBIConnector() { super(DBI) }

    @Override
    protected Connection apply(DBI source) {
      source.open().connection
    }
  }
}
