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

import java.io.Serializable;
import java.time.Clock;
import java.time.Instant;
import java.util.Iterator;
import org.skife.jdbi.v2.GeneratedKeys;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.mixins.GetHandle;
import org.skife.jdbi.v2.util.LongColumnMapper;
import squawker.User;

public abstract class UserStore implements GetHandle {

  @SqlUpdate("schema/user")
  public abstract void createUserTable();

  public User insert(String username, Clock clock) {
    Instant registered = clock.instant();
    GeneratedKeys<Long> ids = getHandle()
      .createStatement("insert into user (username, registered) values (:username, :registered)")
      .bind("username", username)
      .bind("registered", registered)
      .executeAndReturnGeneratedKeys(LongColumnMapper.PRIMITIVE);
    return new PersistentUser(ids.first(), getHandle(), username, registered);
  }

  public User insert(String username) {
    return insert(username, Clock.systemDefaultZone());
  }

  public User find(String username) {
    return getHandle()
      .createQuery("select id, username, registered from user where username = :username")
      .map(new UserMapper(getHandle()))
      .bind("username", username)
      .first();
  }

  public User get(Serializable id) {
    return getHandle()
      .createQuery("select id, username, registered from user where id = :id")
      .map(new UserMapper(getHandle()))
      .bind("id", id)
      .first();
  }

  public Iterator<User> findAllUsers() {
    return getHandle()
      .createQuery("select id, username, registered from user")
      .map(new UserMapper(getHandle()))
      .iterator();
  }

  @SqlQuery("select 1 from user where upper(username) = upper(:username)")
  public abstract boolean usernameInUse(@Bind("username") String username);

  public abstract void close();
}
