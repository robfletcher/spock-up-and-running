package squawker.jdbi.async;

import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import squawker.Message;
import squawker.User;
import squawker.jdbi.MessageStore;

// tag::async-message-store[]
public class AsyncMessageStore {

  private final MessageStore delegate; // <1>
  private final ExecutorService executor; // <2>

  public AsyncMessageStore(MessageStore delegate, ExecutorService executor) {
    this.delegate = delegate;
    this.executor = executor;
  }

  public void latestPostBy(String username, Consumer<Message> callback) { // <3>
    executor.submit(() -> { // <4>
      Message result = delegate.latestPostBy(username); // <5>
      callback.accept(result); // <6>
    });
  }
// end::async-message-store[]

  // tag::multiple-callback-method[]
  public void latestPostsByFollowed(User user, Consumer<Message> callback) {
    user
      .following()
      .forEach(followed -> latestPostBy(followed.getUsername(), callback));
  }
// end::multiple-callback-method[]

// tag::async-message-store[]
}
// end::async-message-store[]
