package com.cloud.admin.notifier;

import de.codecentric.boot.admin.server.config.AdminServerNotifierAutoConfiguration.CompositeNotifierConfiguration;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(
        prefix = "spring.boot.admin.notify.wechatwork",
        name = {"webhook-token"}
)
@AutoConfigureBefore({WechatWorkNotifierConfiguration.class, CompositeNotifierConfiguration.class})
public class WechatWorkNotifierConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties(prefix = "spring.boot.admin.notify.wechatwork")
    public WechatWorkNotifier dingTalkNotifier(InstanceRepository repository) {
        return new WechatWorkNotifier(repository);
    }

}
