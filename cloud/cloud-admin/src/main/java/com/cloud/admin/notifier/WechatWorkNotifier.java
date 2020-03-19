package com.cloud.admin.notifier;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.notify.AbstractStatusChangeNotifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class WechatWorkNotifier extends AbstractStatusChangeNotifier {
    private static final String DEFAULT_MESSAGE =
            "服务上下线通知，请相关同事注意 \n" +
                    "> 告警服务名称：%s \n " +
                    "> 告警服务实例ID： (%s) \n " +
                    "> 告警主机IP：[%s](%s) \n " +
                    "> 当前状态：%s \n ";

    private RestTemplate restTemplate = new RestTemplate();
    private String webhookToken;
    private String msgtype = "markdown";
    private String title = "故障告警";

    public WechatWorkNotifier(InstanceRepository repository) {
        super(repository);
    }

    @Override
    protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
        return Mono.fromRunnable(() -> restTemplate.postForEntity(webhookToken, createMessage(event, instance), Void.class));
    }

    private HttpEntity<Map<String, Object>> createMessage(InstanceEvent event, Instance instance) {
        Map<String, Object> message = new HashMap<>();
        message.put("msgtype", this.msgtype);
        Map<String, Object> markdown = new HashMap<>();

        String content = String.format(DEFAULT_MESSAGE,
                instance.getRegistration().getName(),
                instance.getId(),
                instance.getRegistration().getServiceUrl(),
                instance.getRegistration().getServiceUrl(),
                getLastStatus(event.getInstance()));

        markdown.put("content", content);
        markdown.put("mentioned_list", Arrays.asList("qi.ren@paat.com"));
//        markdown.put("mentioned_mobile_list", this.atMobiles.split(","));
        message.put("markdown", markdown);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return new HttpEntity<>(message, headers);
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getWebhookToken() {
        return webhookToken;
    }

    public void setWebhookToken(String webhookToken) {
        this.webhookToken = webhookToken;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

}

