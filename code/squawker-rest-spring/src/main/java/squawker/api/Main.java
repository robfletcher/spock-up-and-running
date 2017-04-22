package squawker.api;

import java.time.Clock;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import squawker.Message;
import squawker.api.json.MessageDeserializer;

@SpringBootApplication(scanBasePackageClasses = Main.class, scanBasePackages = "squawker.jdbi")
public class Main {
  @Bean Clock clock() {
    return Clock.systemDefaultZone();
  }

  @Bean HttpMessageConverter<?> messageConverter(ObjectMapper mapper) {
    return new MappingJackson2HttpMessageConverter(mapper);
  }

  @Autowired
  public void configureJackson(Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder) {
    jackson2ObjectMapperBuilder.deserializerByType(Message.class, new MessageDeserializer());
  }

  public static void main(String... args) {
    SpringApplication.run(Main.class, args);
  }
}
