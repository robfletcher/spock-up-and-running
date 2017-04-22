package squawker.api;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ResponseStatus;
import squawker.Message;
import squawker.User;
import squawker.jdbi.MessageStore;
import squawker.jdbi.UserStore;
import static java.lang.String.format;
import static org.springframework.http.HttpStatus.*;

public abstract class ApiControllerBase {

  protected final UserStore userStore;
  protected final MessageStore messageStore;

  protected ApiControllerBase(UserStore userStore, MessageStore messageStore) {
    this.userStore = userStore;
    this.messageStore = messageStore;
  }

  protected final Optional<User> getAuthenticatedUser() {
    return Optional.ofNullable((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
  }

  protected final <T> T withUser(String username, Function<User, T> closure) {
    User user = userStore.find(username);
    if (user == null) {
      throw new NoSuchUser(username);
    }
    return closure.apply(user);
  }

  @ResponseStatus(NOT_FOUND)
  protected class NoSuchUser extends RuntimeException {
    public NoSuchUser(String username) {
      super(format("No user %s found", username));
    }
  }

  @ResponseStatus(NOT_FOUND)
  protected class NoMessages extends RuntimeException {
    public NoMessages(User user) {
      super(format("User %s has posted no messages", user));
    }
  }

  @ResponseStatus(UNPROCESSABLE_ENTITY)
  protected class EmptyText extends RuntimeException {
    public EmptyText() {
      super("Message text is required");
    }
  }

  @ResponseStatus(NOT_FOUND)
  protected class NoSuchMessage extends RuntimeException {
    public NoSuchMessage(Serializable id) {
      super(format("No message with id %s found", id));
    }
  }

  @ResponseStatus(UNAUTHORIZED)
  protected class NoAuthenticatedUser extends RuntimeException {
    public NoAuthenticatedUser() {
      super("No authenticated user");
    }
  }

  @ResponseStatus(FORBIDDEN)
  protected class CannotModifyMessage extends RuntimeException {
    public CannotModifyMessage(User user, Message message) {
      super(format("User %s cannot modify message %s", user, message.getId()));
    }
  }
}
