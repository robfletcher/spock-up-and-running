package squawker.mentions;

import java.util.List;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.mixins.GetHandle;
import squawker.Message;
import squawker.User;
import squawker.jdbi.MessageMapper;

public abstract class MentionStore implements GetHandle {
  @SqlUpdate("schema/mention")
  public abstract void createMentionTable();

  public void insert(User user, Message message) {
    insert(user.getUsername(), message);
  }

  @SqlUpdate("commands/insert_mention")
  public abstract void insert(@Bind("username") String username,
                              @BindBean("message") Message message);

  public List<Message> mentionsOf(User user) {
    return getHandle()
      .createQuery("select m.id, m.posted_by_id, m.text, m.posted_at,"
        + "                u.username, u.registered"
        + "           from user u, message m, mention mn"
        + "          where u.id = m.posted_by_id"
        + "            and mn.message_id = m.id"
        + "            and mn.user_id = :user_id"
        + "       order by m.posted_at desc")
      .map(new MessageMapper(getHandle()))
      .bind("user_id", user.getId())
      .list();
  }
}
