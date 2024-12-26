package com.megatronix.eden.messaging;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.megatronix.eden.constant.RabbitConstant;
import com.megatronix.eden.util.MailUtil;

import jakarta.annotation.Resource;

@Component
public class CaptchaMessage {
  @Resource
  private MailUtil mailUtil;

  @RabbitListener(bindings = @QueueBinding(value = @Queue(value = RabbitConstant.CAPTCHA_QUEUE, durable = "true"), exchange = @Exchange(value = RabbitConstant.CAPTCHA_EXCHANGE), key = RabbitConstant.CAPTCHA_ROUTING_KEY))
  public void messageHandler(@Payload Map<String, String> stringMap) {
    String captcha = stringMap.get("captcha");
    String email = stringMap.get("email");
    mailUtil.sendSimpleMessage(email, "captcha", "Your captcha is:" + captcha);
  }
}
