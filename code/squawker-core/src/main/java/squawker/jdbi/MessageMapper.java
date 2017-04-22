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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import squawker.Message;
import squawker.User;

public class MessageMapper implements ResultSetMapper<Message> {

  private final Handle handle;

  public MessageMapper(Handle handle) {
    this.handle = handle;
  }

  public Message map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    User user = new PersistentUser(
      r.getLong("id"),
      handle,
      r.getString("username"),
      Instant.ofEpochMilli(r.getTimestamp("registered").getTime())
    );
    return new Message(
      r.getLong("id"),
      user,
      r.getString("text"),
      Instant.ofEpochMilli(r.getTimestamp("posted_at").getTime())
    );
  }
}
