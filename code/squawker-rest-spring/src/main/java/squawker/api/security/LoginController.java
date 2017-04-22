package squawker.api.security;

import java.util.Map;
import java.util.function.Consumer;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import squawker.jdbi.ApiTokenStore;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/api/auth")
public class LoginController {

  private final ApiTokenStore apiTokenStore;

  @Autowired
  public LoginController(ApiTokenStore apiTokenStore) {
    this.apiTokenStore = apiTokenStore;
  }

  @RequestMapping(method = POST)
  @ResponseBody
  public Map<String, String> login(@RequestParam("username") String username) {
    String token = apiTokenStore.getToken(username);
    if (token == null) {
      throw new InvalidCredentials();
    } else {
      return map(builder -> builder
        .put("username", username)
        .put("token", token)
      );
    }
  }

  private <K, V> Map<K, V> map(Consumer<Builder<K, V>> consumer) {
    Builder<K, V> builder = ImmutableMap.builder();
    consumer.accept(builder);
    return builder.build();
  }

  @ResponseStatus(UNAUTHORIZED)
  private static class InvalidCredentials extends RuntimeException {
    InvalidCredentials() {
      super("Username or password is incorrect");
    }
  }
}
