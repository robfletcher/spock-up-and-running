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
package squawker.jdbi;

import java.sql.*;
import java.time.*;
import org.skife.jdbi.v2.*;
import org.skife.jdbi.v2.tweak.*;
import squawker.*;

class UserMapper implements ResultSetMapper<User> {

  private final Handle handle;

  UserMapper(Handle handle) {
    this.handle = handle;
  }

  public User map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    return new PersistentUser(handle.attach(DataStore.class), r.getString("username"), Instant.ofEpochMilli(r.getTimestamp("registered")
                                                                                                             .getTime()));
  }
}
