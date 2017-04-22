package squawker.jdbi;

import javax.sql.DataSource;
import co.freeside.jdbi.time.TimeTypesArgumentFactory;
import co.freeside.jdbi.time.TimeTypesMapperFactory;
import org.skife.jdbi.v2.DBI;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import squawker.mentions.MentionStore;
import squawker.schedule.ScheduledMessageStore;

@Configuration
public class DataStoreConfig {

  @Bean FactoryBean<DBI> dbi(DataSource dataSource) {
    return new TypedDBIFactoryBean(new TransactionAwareDataSourceProxy(dataSource));
  }

  @Bean TimeTypesArgumentFactory timeTypesArgumentFactory() {
    return new TimeTypesArgumentFactory();
  }

  @Bean TimeTypesMapperFactory timeTypesMapperFactory() {
    return new TimeTypesMapperFactory();
  }

  @Bean FactoryBean<UserStore> userStore() {
    return SqlObjectFactoryBean.create(UserStore.class);
  }

  @Bean FactoryBean<MessageStore> messageStore() {
    return SqlObjectFactoryBean.create(MessageStore.class);
  }

  @Bean FactoryBean<FollowingStore> followingStore() {
    return SqlObjectFactoryBean.create(FollowingStore.class);
  }

  @Bean FactoryBean<ApiTokenStore> apiTokenStore() {
    return SqlObjectFactoryBean.create(ApiTokenStore.class);
  }

  @Bean FactoryBean<MentionStore> mentionStore() {
    return SqlObjectFactoryBean.create(MentionStore.class);
  }

  @Bean FactoryBean<ScheduledMessageStore> scheduledMessageStore() {
    return SqlObjectFactoryBean.create(ScheduledMessageStore.class);
  }

}
