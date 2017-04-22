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

import java.time.*;
import java.util.*;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.mixins.*;
import squawker.*;

public abstract class DataStore implements GetHandle {

  @SqlUpdate("create table user (id int primary key auto_increment,"
      + "                   username varchar(32),"
      + "                   registered timestamp)")
  public abstract void createUserTable();

  @SqlUpdate("create table following (follower_id int, following_id int)")
  public abstract void createFollowingTable();

  @SqlUpdate("create table message (id int primary key auto_increment,"
      + "                      posted_by_id int,"
      + "                      text varchar(140),"
      + "                      posted_at timestamp)")
  public abstract void createMessageTable();

  @SqlUpdate("insert into user (username, registered)"
      + " values (:u.username, :u.registered)")
  public abstract void insert(@BindBean("u") User user);

  public Iterator<User> findAllUsers() {
    return getHandle().createQuery("select username, registered from user")
        .map(new UserMapper(getHandle()))
        .iterator();
  }

  public User newUser(String username, Instant registered) {
    return new PersistentUser(this, username, registered);
  }

  @SqlUpdate("insert into following (follower_id, following_id)"
      + " select u1.id, u2.id from user u1, user u2"
      + " where u1.username = :u1.username and u2.username = :u2.username")
  public abstract void follow(@BindBean("u1") User follower, @BindBean("u2") User following);

  public List<User> findFollowing(User user) {
    return getHandle()
        .createQuery("select username, registered from user"
            + " where id in"
            + " (select following_id"
            + "  from following"
            + "  where follower_id = (select id from user where username = :username))")
        .bind("username", user.getUsername())
        .map(new UserMapper(getHandle()))
        .list();
  }

  public List<User> findFollowers(User user) {
    return getHandle()
        .createQuery("select username, registered from user"
            + " where id in"
            + " (select follower_id from following"
            + "  where following_id = (select id from user where username = :username))")
        .bind("username", user.getUsername())
        .map(new UserMapper(getHandle()))
        .list();
  }

  @SqlUpdate("insert into message (posted_by_id, text, posted_at)"
      + " select id, :text, :postedAt from user"
      + " where username = :username")
  public abstract void insert(@BindMessage Message message);

  public List<Message> postsBy(User user) {
    return getHandle()
        .createQuery("select m.id, m.posted_by_id, m.text, m.posted_at,"
            + " u.username, u.registered"
            + " from message m, user u"
            + " where u.username = :username"
            + " and m.posted_by_id = u.id")
        .map(new MessageMapper(getHandle()))
        .bind("username", user.getUsername())
        .list();
  }

  public List<Message> timeline(User user) {
    return getHandle()
        .createQuery("select m.id, m.posted_by_id, m.text, m.posted_at,"
            + " u.username, u.registered"
            + " from message m, user u"
            + " where m.posted_by_id = u.id"
            + " and (u.id in"
            + "      (select following_id from following"
            + "       where follower_id = (select id from user where username = :username))"
            + "      or u.id = (select id from user where username = :username))"
            + " order by m.posted_at desc")
        .map(new MessageMapper(getHandle()))
        .bind("username", user.getUsername())
        .list();
  }

  public abstract void close();
}
