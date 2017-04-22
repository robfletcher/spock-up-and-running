package squawker.spring;

import java.time.Clock;
import javax.sql.DataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

// tag::main-class[]
@SpringBootApplication(scanBasePackages = {"squawker.jdbi", "squawker.mentions"})
public class Main {
  // end::main-class[]
  @Bean Clock clock() {
    return Clock.systemDefaultZone();
  }

  @Bean
  @Profile({"test", "integration"})
  public DataSource dataSource() {
    EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
    EmbeddedDatabase db = builder
      .setType(EmbeddedDatabaseType.H2)
      .generateUniqueName(true)
      .build();
    return db;
  }

  // tag::main-class[]
  public static void main(String... args) {
    SpringApplication.run(Main.class, args);
  }
}
// end::main-class[]
