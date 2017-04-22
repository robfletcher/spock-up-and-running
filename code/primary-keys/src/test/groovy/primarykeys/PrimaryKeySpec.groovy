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
package primarykeys

import java.sql.*
import spock.lang.*

// tag::primary-key-spec[]
class PrimaryKeySpec extends Specification {

  @Shared @AutoCleanup Connection connection // <1>
  @Shared @AutoCleanup("destroySchema") SchemaBuilder schemaBuilder

  def setupSpec() { // <2>
    Class.forName("org.h2.Driver")
    connection = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "")

    schemaBuilder = new SchemaBuilder(connection)
    schemaBuilder.createSchema()
  }

  private Iterable<String> readTableNames() { // <3>
    def list = []
    def tables = connection.metaData.getTables(null, null, "%", ["TABLE"] as String[])
    try {
      while (tables.next()) {
        list << tables.getString(3)
      }
    } finally {
      tables.close()
    }
    list.asImmutable()
  }

  @Unroll
  def "the #tableName table has a primary key"() { // <6>
    expect:
    keys.next() // <7>

    cleanup:
    keys.close()

    where:
    tableName << readTableNames() // <4>
    keys = connection.metaData.getPrimaryKeys(null, null, tableName) // <5>
  }
}
// end::primary-key-spec[]
