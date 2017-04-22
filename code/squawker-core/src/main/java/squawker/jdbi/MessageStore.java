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

import java.time.Instant;
import java.util.List;
import org.skife.jdbi.v2.GeneratedKeys;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.mixins.GetHandle;
import org.skife.jdbi.v2.util.LongColumnMapper;
import squawker.Message;
import squawker.User;

public abstract class MessageStore implements GetHandle {

  @SqlUpdate("schema/message")
  public abstract void createMessageTable();

  public Message find(long id) {
    return getHandle()
      .createQuery("select m.id, m.posted_by_id, m.text, m.posted_at,"
        + "                u.username, u.registered"
        + "           from message m, user u"
        + "          where m.id = :id"
        + "            and m.posted_by_id = u.id")
      .map(new MessageMapper(getHandle()))
      .bind("id", id)
      .first();
  }

  @SqlUpdate("delete from message where id = :id")
  public abstract void delete(@Bind("id") long id);

  public Message insert(@BindBean(value = "postedBy", type = User.class) User postedBy,
                        @Bind("text") String text,
                        @Bind("postedAt") Instant postedAt) {
    GeneratedKeys<Long> ids = getHandle()
      .createStatement("insert into message (posted_by_id, text, posted_at)"
        + "                    select id, :text, :postedAt from user"
        + "                     where username = :username")
      .bind("username", postedBy.getUsername())
      .bind("text", text)
      .bind("postedAt", postedAt)
      .executeAndReturnGeneratedKeys(LongColumnMapper.PRIMITIVE);
    return new Message(ids.first(), postedBy, text, postedAt);
  }

  public List<Message> postsBy(User user) {
    return postsBy(user.getUsername());
  }

  public List<Message> postsBy(String username) {
    return getHandle()
      .createQuery("select m.id, m.posted_by_id, m.text, m.posted_at,"
        + "                u.username, u.registered"
        + "           from message m, user u"
        + "          where u.username = :username"
        + "            and m.posted_by_id = u.id"
        + "       order by m.posted_at desc")
      .map(new MessageMapper(getHandle()))
      .bind("username", username)
      .list();
  }

  public Message latestPostBy(User user) {
    return latestPostBy(user.getUsername());
  }

  public Message latestPostBy(String username) {
    return getHandle()
      .createQuery("select m.id, m.posted_by_id, m.text, m.posted_at,"
        + "                u.username, u.registered"
        + "           from message m, user u"
        + "          where u.username = :username"
        + "            and m.posted_by_id = u.id"
        + "       order by m.posted_at desc")
      .map(new MessageMapper(getHandle()))
      .bind("username", username)
      .first();
  }

  public List<Message> timeline(User user) {
    return getHandle()
      .createQuery("select m.id, m.posted_by_id, m.text, m.posted_at,"
        + "                u.username, u.registered"
        + "           from message m, user u"
        + "          where m.posted_by_id = u.id"
        + "            and (u.id in"
        + "                 (select following_id from following"
        + "                   where follower_id = (select id from user where username = :username))"
        + "                      or u.id = (select id from user where username = :username))"
        + "          order by m.posted_at desc")
      .map(new MessageMapper(getHandle()))
      .bind("username", user.getUsername())
      .list();
  }

  public abstract void close();
}
