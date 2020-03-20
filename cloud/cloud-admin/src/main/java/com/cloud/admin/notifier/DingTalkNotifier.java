//package com.cloud.admin.notifier;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.context.expression.MapAccessor;
//import org.springframework.expression.Expression;
//import org.springframework.expression.ParserContext;
//import org.springframework.expression.spel.standard.SpelExpressionParser;
//import org.springframework.expression.spel.support.StandardEvaluationContext;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.web.client.RestTemplate;
//
//import de.codecentric.boot.admin.server.domain.entities.Instance;
//import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
//import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
//import de.codecentric.boot.admin.server.notify.AbstractStatusChangeNotifier;
//import reactor.core.publisher.Mono;
//
//public class DingTalkNotifier extends AbstractStatusChangeNotifier {
//    private static final String DEFAULT_MESSAGE =
//            "- 告警服务名称：*#{instance.registration.name}* \n " +
//                    "- 告警服务实例ID： (#{instance.id}) \n " +
//                    "- 告警主机IP：[ #{instance.registration.serviceUrl} ]( #{instance.registration.serviceUrl} ) \n " +
//                    "- 当前状态：  *#{event.statusInfo.status}* \n ";
//
//    private final SpelExpressionParser parser = new SpelExpressionParser();
//    private RestTemplate restTemplate = new RestTemplate();
//    private String webhookToken;
//    private String atMobiles;
//    private String msgtype = "markdown";
//    private String title = "故障告警";
//    private Expression message;
//
//    public DingTalkNotifier(InstanceRepository repository) {
//        super(repository);
//        this.message = parser.parseExpression(DEFAULT_MESSAGE, ParserContext.TEMPLATE_EXPRESSION);
//    }
//
//    @Override
//    protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
//        return Mono.fromRunnable(() -> restTemplate.postForEntity(webhookToken, createMessage(event, instance), Void.class));
//    }
//
//
//    private HttpEntity<Map<String, Object>> createMessage(InstanceEvent event, Instance instance) {
//        Map<String, Object> messageJson = new HashMap<>();
//        HashMap<String, String> params = new HashMap<>();
//        params.put("text", this.getMessage(event, instance).replace("OFFLINE", "下线啦").replace("UP", "上线啦"));
//        params.put("title", this.title);
//        messageJson.put("atMobiles", this.atMobiles);
//        messageJson.put("msgtype", this.msgtype);
//        messageJson.put(this.msgtype, params);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
//        return new HttpEntity<>(messageJson, headers);
//    }
//
//    private String getAtMobilesString(String s) {
//        StringBuilder atMobiles = new StringBuilder();
//        String[] mobiles = s.split(",");
//        for (String mobile : mobiles) {
//            atMobiles.append("@").append(mobile);
//        }
//        return atMobiles.toString();
//    }
//
//    private String getMessage(InstanceEvent event, Instance instance) {
//        Map<String, Object> root = new HashMap<>();
//        root.put("event", event);
//        root.put("instance", instance);
//        root.put("lastStatus", getLastStatus(event.getInstance()));
//        StandardEvaluationContext context = new StandardEvaluationContext(root);
//        context.addPropertyAccessor(new MapAccessor());
//        return message.getValue(context, String.class);
//    }
//
//    public void setRestTemplate(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//    public String getWebhookToken() {
//        return webhookToken;
//    }
//
//    public void setWebhookToken(String webhookToken) {
//        this.webhookToken = webhookToken;
//    }
//
//    public String getAtMobiles() {
//        return atMobiles;
//    }
//
//    public void setAtMobiles(String atMobiles) {
//        this.atMobiles = atMobiles;
//    }
//
//    public String getMsgtype() {
//        return msgtype;
//    }
//
//    public void setMsgtype(String msgtype) {
//        this.msgtype = msgtype;
//    }
//
//    public Expression getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = (Expression) this.parser.parseExpression(message, ParserContext.TEMPLATE_EXPRESSION);
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//
//}
//
