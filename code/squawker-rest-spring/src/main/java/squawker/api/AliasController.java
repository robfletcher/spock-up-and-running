package squawker.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import squawker.Message;
import squawker.jdbi.MessageStore;
import squawker.jdbi.UserStore;
import static java.lang.String.format;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("/api")
public class AliasController extends ApiControllerBase {
  @Autowired
  public AliasController(UserStore userStore, MessageStore messageStore) {
    super(userStore, messageStore);
  }

  @RequestMapping(path = "/{username}/messages/latest", method = GET)
  public String latestMessage(@PathVariable("username") String username) {
    return withUser(username, user -> {
      Message message = messageStore.latestPostBy(user);
      if (message == null) {
        throw new NoMessages(user);
      }
      return format("redirect:/api/messages/%s", message.getId());
    });
  }
}
