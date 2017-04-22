package squawker.api.security;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import squawker.User;
import squawker.jdbi.ApiTokenStore;
import static java.util.Collections.singleton;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class TokenAuthFilter extends GenericFilterBean {

  private static final Pattern TOKEN_MATCHER = Pattern.compile("^Token (.+)$");

  private final ApiTokenStore apiTokenStore;

  @Autowired
  public TokenAuthFilter(ApiTokenStore apiTokenStore) {
    this.apiTokenStore = apiTokenStore;
  }

  private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
    getAuthToken(request)
      .map(apiTokenStore::find)
      .ifPresent(this::authenticateUser);
    chain.doFilter(request, response);
  }

  private void authenticateUser(User user) {
    Authentication authentication = new UsernamePasswordAuthenticationToken(
      user, null, singleton(new SimpleGrantedAuthority("ROLE_USER"))
    );
    SecurityContextHolder
      .getContext()
      .setAuthentication(authentication);
  }

  private Optional<String> getAuthToken(HttpServletRequest request) {
    return Optional
      .ofNullable(request.getHeader(AUTHORIZATION))
      .map(this::extractToken);
  }

  private String extractToken(String authHeader) {
    Matcher matcher = TOKEN_MATCHER.matcher(authHeader);
    return matcher.find() ? matcher.group(1) : null;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
  }
}
