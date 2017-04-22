/*
 * Copyright 2013 the original author or authors.
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
package squawker.jdbi

import co.freeside.jdbi.time.TimeTypesArgumentFactory
import co.freeside.jdbi.time.TimeTypesMapperFactory
import org.skife.jdbi.v2.DBI
import org.skife.jdbi.v2.Handle
import org.skife.jdbi.v2.util.IntegerColumnMapper
import spock.lang.Shared
import spock.lang.Specification

// tag::whole-spec[]
abstract class BasePersistenceSpec extends Specification {

  @Shared DataStore dataStore

  @Shared dbi = new DBI("jdbc:h2:mem:test")
  @Shared Handle handle

  def setupSpec() {
    dbi.registerArgumentFactory(new TimeTypesArgumentFactory())
    dbi.registerMapper(new TimeTypesMapperFactory())

    handle = dbi.open()
    dataStore = handle.attach(DataStore)
    dataStore.createUserTable()
  }

  def cleanupSpec() {
    handle.execute("drop table user if exists")
    handle.close()
  }

  def cleanup() {
    handle.execute("delete from user")
  }

  protected int rowCount(String table) {
    handle.createQuery("select count(*) from $table")
          .map(IntegerColumnMapper.PRIMITIVE)
          .first()
  }
}
