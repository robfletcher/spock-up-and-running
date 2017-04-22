/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package primarykeys;

import java.sql.*;

public class SchemaBuilder {

  private final Connection connection;
  private final String[] tableNames = {"foo", "bar", "baz"};

  public SchemaBuilder(Connection connection) {
    this.connection = connection;
  }

  public void createSchema() throws SQLException {
    for (String name : tableNames) {
      connection.createStatement().execute(String.format("create table %s (id char(32) primary key, name varchar(255))", name));
    }
  }

  public void destroySchema() throws SQLException {
    for (String name : tableNames) {
      connection.createStatement().execute(String.format("drop table %s", name));
    }
  }
}
