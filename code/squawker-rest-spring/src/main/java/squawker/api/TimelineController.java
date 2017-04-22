package squawker.api;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import squawker.Message;
import squawker.jdbi.MessageStore;
import squawker.jdbi.UserStore;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api/timeline")
public class TimelineController extends ApiControllerBase {
  @Autowired
  public TimelineController(UserStore userStore, MessageStore messageStore) {
    super(userStore, messageStore);
  }

  @Secured("ROLE_USER")
  @RequestMapping(method = GET)
  public List<Message> timeline() {
    return getAuthenticatedUser()
      .map(messageStore::timeline)
      // TODO: this is unreachable
      .orElseThrow(NoAuthenticatedUser::new);
  }
}
