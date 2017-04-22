package squawker.api;

import java.time.Clock;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import squawker.Message;
import squawker.User;
import squawker.jdbi.MessageStore;
import squawker.jdbi.UserStore;
import static java.util.Collections.singletonMap;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/api")
public class ApiController extends ApiControllerBase {

  private final Clock clock;

  @Autowired
  public ApiController(UserStore userStore, MessageStore messageStore, Clock clock) {
    super(userStore, messageStore);
    this.clock = clock;
  }

  @RequestMapping(path = "/{username}/user", method = GET)
  public User getUser(@PathVariable("username") String username) {
    return withUser(username, user -> user);
  }

  @RequestMapping(path = "/{username}/timeline", method = GET)
  public List<Message> timeline(@PathVariable("username") String username) {
    return withUser(username, messageStore::timeline);
  }

  // tag::get-user-messages[]
  @RequestMapping(path = "/{username}/messages", method = GET)
  public List<Message> list(@PathVariable("username") String username) {
    return withUser(username, messageStore::postsBy);
  }
  // end::get-user-messages[]

  @RequestMapping(path = "/{username}/messages", method = POST)
  @ResponseStatus(CREATED)
  public Message postMessage(@PathVariable("username") String username, @RequestBody String text) {
    return withUser(username, (user) -> {
      if (text == null || text.isEmpty()) {
        throw new EmptyText();
      }
      return messageStore.insert(user, text, clock.instant());
    });
  }

  @Secured("ROLE_USER")
  @RequestMapping(path = "/messages", method = POST)
  @ResponseStatus(CREATED)
  public Message postMessage(@RequestBody String text) {
    return getAuthenticatedUser()
      .map(user -> {
        if (text == null || text.isEmpty()) {
          throw new EmptyText();
        }
        return messageStore.insert(user, text, clock.instant());
      })
      // TODO: this is unreachable
      .orElseThrow(NoAuthenticatedUser::new);
  }

  @RequestMapping(path = "/messages/{id}", method = GET)
  public Message getMessage(@PathVariable("id") Long id) {
    Message message = messageStore.find(id);
    if (message == null) {
      throw new NoSuchMessage(id);
    }
    return message;
  }

  @Secured("ROLE_USER")
  @RequestMapping(path = "/messages/{id}", method = DELETE)
  public void deleteMessage(@PathVariable("id") Long id) {
    getAuthenticatedUser()
      .ifPresent(user -> {
        Message message = messageStore.find(id);
        if (message == null) {
          throw new NoSuchMessage(id);
        } else if (message.getPostedBy().equals(user)) {
          messageStore.delete(id);
        } else {
          throw new CannotModifyMessage(user, message);
        }
      });
  }

  /**
   * We don't actually have any roles so failing @Secured means user is not
   * authenticated at all. This promotes the error code to 401 instead of 403.
   */
  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(UNAUTHORIZED)
  public Map<String, String> notAuthorized(AccessDeniedException exception) {
    return singletonMap("message", exception.getLocalizedMessage());
  }
}
