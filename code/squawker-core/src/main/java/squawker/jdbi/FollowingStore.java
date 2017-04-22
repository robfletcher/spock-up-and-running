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

import java.util.List;
import org.skife.jdbi.v2.PrimitivesColumnMapperFactory;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterColumnMapperFactory;
import org.skife.jdbi.v2.sqlobject.mixins.GetHandle;
import squawker.User;

public abstract class FollowingStore implements GetHandle {

  @SqlUpdate("schema/following")
  public abstract void createFollowingTable();

  @SqlUpdate("insert into following (follower_id, following_id)"
    + "                      select u1.id, u2.id from user u1, user u2"
    + "                       where u1.username = :u1.username and u2.username = :u2.username")
  public abstract void follow(@BindBean(value = "u1", type = User.class) User follower,
                              @BindBean(value = "u2", type = User.class) User following);

  public List<User> findFollowing(User user) {
    return getHandle()
      .createQuery("select id, username, registered from user"
        + "          where id in"
        + "                (select following_id"
        + "                 from following"
        + "                where follower_id = (select id from user where username = :username))")
      .bind("username", user.getUsername())
      .map(new UserMapper(getHandle()))
      .list();
  }

  public List<User> findFollowers(User user) {
    return getHandle()
      .createQuery("select id, username, registered from user"
        + "          where id in"
        + "                (select follower_id from following"
        + "                  where following_id = (select id from user where username = :username))")
      .bind("username", user.getUsername())
      .map(new UserMapper(getHandle()))
      .list();
  }

  @SqlQuery("select count(1) from following f, user u"
    + "       where u.username = :username"
    + "         and f.following_id = u.id")
  @RegisterColumnMapperFactory(PrimitivesColumnMapperFactory.class)
  public abstract int countFollowers(User user);

  public abstract void close();
}
