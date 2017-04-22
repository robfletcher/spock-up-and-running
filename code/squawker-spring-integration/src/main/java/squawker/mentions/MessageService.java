package squawker.mentions;

import java.time.Clock;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import squawker.Message;
import squawker.User;
import squawker.jdbi.MessageStore;
import squawker.mentions.events.MentionEvent;

@Service
public class MessageService {

  // tag::implementation[]
  private final MessageStore messageStore;
  private final MentionStore mentionStore;
  private final Clock clock;
  // end::implementation[]
  private final Optional<ApplicationEventPublisher> publisher;

  // transactional insert of message and index of mentions

  // fake clock test periodic posting of scheduled messages

  // back with datastore and test transactional behavior?

  @Autowired
  public MessageService(MessageStore messageStore,
                        MentionStore mentionStore,
                        Clock clock,
                        Optional<ApplicationEventPublisher> publisher) {
    this.messageStore = messageStore;
    this.mentionStore = mentionStore;
    this.clock = clock;
    this.publisher = publisher;
  }

  @VisibleForTesting
  public MessageService(MessageStore messageStore,
                        MentionStore mentionStore) {
    this(messageStore, mentionStore, Clock.systemDefaultZone(), Optional.empty());
  }

  private static final Pattern MENTION_PATTERN = Pattern.compile("@(\\w+)");

  // tag::implementation[]
  @Transactional
  public Message postMessage(User user, String text) {
    Message message = messageStore.insert(user, text, clock.instant());
    Matcher matcher = MENTION_PATTERN.matcher(text);
    while (matcher.find()) {
      String username = matcher.group(1);
      mentionStore.insert(username, message);
      // end::implementation[]
      publishEvent(username, message);
      // tag::implementation[]
    }
    return message;
  }
  // end::implementation[]

  // TODO: test to ensure nothing gets published if tx fails
  private void publishEvent(String username, Message message) {
    publisher.ifPresent(it ->
      it.publishEvent(new MentionEvent(this, username, message))
    );
  }

}
