package squawker.schedule;

import java.time.Instant;
import java.util.Collection;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.springframework.transaction.annotation.Transactional;
import squawker.User;

public abstract class ScheduledMessageStore {

  @SqlUpdate("schema/scheduled_message")
  public abstract void createScheduledMessageTable();

  @SqlUpdate("insert into scheduled_message "
    + "                   (posted_by_id, text, post_at) "
    + "            values (:user.id, :text, :post_at)")
  public abstract void scheduleMessage(@BindBean("user") User user,
                                       @Bind("text") String text,
                                       @Bind("post_at") Instant postAt);

  @Transactional
  public void scheduleMessages(User user,
                               Collection<ScheduledMessageRequest> messages) {
    messages.forEach(message ->
      scheduleMessage(user, message.text, message.postAt)
    );
  }

  static class ScheduledMessageRequest {
    private final String text;
    private final Instant postAt;

    ScheduledMessageRequest(String text, Instant postAt) {
      this.text = text;
      this.postAt = postAt;
    }
  }
}
