package de.thkoeln.syp.iot_etage;

import java.util.UUID;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

@Configuration
@ConfigurationProperties(prefix = "application.mqtt")
public class MqttConfiguration {

  @Value("${application.mqtt.hostname}")
  private String hostname;

  @Value("${application.mqtt.port}")
  private String port;

  @Value("${application.mqtt.username}")
  private String username;

  @Value("${application.mqtt.password}")
  private String password;

  // Setting für die MQTT-Verbindung
  @Bean
  public MqttPahoClientFactory mqttClientFactory() {
    DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
    MqttConnectOptions options = new MqttConnectOptions();
    
    options.setServerURIs(new String[] 
    { 
      "tcp://"+this.hostname+":"+this.port, 
    }
    );
    options.setUserName(this.username);
    options.setPassword(this.password.toCharArray());
    factory.setConnectionOptions(options);
    
    return factory;
  }

  //inbound-Adapter = subscribe
  @Bean
  public MessageChannel mqttInputChannel() {
      return new DirectChannel();
  }

  @Bean
  public MessageProducer inbound() {
    String clientId = "Java_" + UUID.randomUUID().toString();
    MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
      clientId,
      this.mqttClientFactory(),
      "testin"
    );
    adapter.setCompletionTimeout(5000);
    adapter.setConverter(new DefaultPahoMessageConverter());
    adapter.setQos(1);
    adapter.setOutputChannel(mqttInputChannel());

    return adapter;
  }

  @Bean
  @ServiceActivator(inputChannel = "mqttInputChannel")
  public MessageHandler handler() {

    return new MessageHandler() {

      @Override
      public void handleMessage(Message<?> message) throws MessagingException {
        //hier überschreiben
        System.out.println(message.getPayload());
      }
    };
  }
  
  //outbound = publish

  @Bean
  @ServiceActivator(inputChannel = "mqttOutboundChannel")
  public MessageHandler mqttOutbound() {
    String clientId = "Java_" + UUID.randomUUID().toString();
    MqttPahoMessageHandler messageHandler =                    new MqttPahoMessageHandler(
      clientId,
      mqttClientFactory()
    );
    messageHandler.setAsync(true);
    messageHandler.setDefaultTopic("testout");
    return messageHandler;
  }

  @Bean
  public MessageChannel mqttOutboundChannel() {
    return new DirectChannel();
  }

  @MessagingGateway(
    defaultRequestChannel = "mqttOutboundChannel"
  )
  public interface MyGateway {

    void sendToMqtt(String data);

  }
}
