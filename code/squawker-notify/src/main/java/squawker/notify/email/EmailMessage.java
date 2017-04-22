package squawker.notify.email;

import java.util.Objects;

public class EmailMessage {

  private final String from;
  private final String subject;
  private final String template;
  private final String follower;

  public EmailMessage(String from, String subject, String template, String follower) {
    this.from = from;
    this.subject = subject;
    this.template = template;
    this.follower = follower;
  }

  public String getFrom() {
    return from;
  }

  public String getSubject() {
    return subject;
  }

  public String getTemplate() {
    return template;
  }

  public String getFollower() {
    return follower;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    EmailMessage that = (EmailMessage) o;
    return Objects.equals(from, that.from) &&
      Objects.equals(subject, that.subject) &&
      Objects.equals(template, that.template) &&
      Objects.equals(follower, that.follower);
  }

  @Override public int hashCode() {
    return Objects.hash(from, subject, template, follower);
  }
}
