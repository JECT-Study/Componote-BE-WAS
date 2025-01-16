package ject.componote.infra.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Bean
    public Queue imageMoveQueue() {
        return new Queue("imageMoveQueue");
    }

    @Bean
    public Queue imageDeleteQueue() {
        return new Queue("imageDeleteQueue");
    }

    @Bean
    public Exchange imageExchange() {
        return new TopicExchange("was-storage");
    }

    @Bean
    public Binding imageMoveBinding(final Queue imageMoveQueue, final Exchange imageExchange) {
        return BindingBuilder.bind(imageMoveQueue)
                .to(imageExchange)
                .with("storage.image.move")
                .noargs();
    }

    @Bean
    public Binding imageDeleteBinding(final Queue imageDeleteQueue, final Exchange imageExchange) {
        return BindingBuilder.bind(imageDeleteQueue)
                .to(imageExchange)
                .with("storage.image.delete")
                .noargs();
    }

    @Bean
    public Queue notificationCreateQueue() {
        return new Queue("notificationCreateQueue");
    }

    @Bean
    public Exchange notificationExchange() {
        return new TopicExchange("was-notification");
    }

    @Bean
    public Binding notificationCreateBinding(final Queue notificationCreateQueue, final Exchange notificationExchange) {
        return BindingBuilder.bind(notificationCreateQueue)
                .to(notificationExchange)
                .with("notification.create")
                .noargs();
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory, final MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}
