package squawker.mentions.events;

import org.springframework.context.ApplicationEvent;
import squawker.Message;

public class MentionEvent extends ApplicationEvent {

  private final String username;
  private final Message message;

  public MentionEvent(Object source, String username, Message message) {
    super(source);
    this.username = username;
    this.message = message;
  }

  public String getMentionedUsername() {
    return username;
  }

  public Message getMessage() {
    return message;
  }
}
