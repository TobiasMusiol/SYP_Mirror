package de.thkoeln.syp.iot_etage.mqtt;

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
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

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

    options.setServerURIs(new String[] { "ssl://" + this.hostname + ":" + this.port, });
    options.setUserName(this.username);
    options.setPassword(this.password.toCharArray());
    factory.setConnectionOptions(options);
    return factory;
  }

  // inbound-Adapter = subscribe Topic sensordata

  @Bean
  public MessageProducer inboundSendorData() {
    String clientId = "Java_" + UUID.randomUUID().toString();
    MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientId,
        this.mqttClientFactory(), "/iot_etage/sensordata");
    adapter.setCompletionTimeout(5000);
    adapter.setConverter(new DefaultPahoMessageConverter());
    adapter.setQos(1);
    adapter.setOutputChannel(mqttInputChannelSensorData());

    return adapter;
  }

  @Bean
  public MessageChannel mqttInputChannelSensorData() {
    return new DirectChannel();
  }

  @Bean
  @ServiceActivator(inputChannel = "mqttInputChannelSensorData")
  public MessageHandler handlerSensorData() {
    return new MqttSubSensorHandler();
  }

  // inbound-Adapter = subscribe Topic eventdata

  @Bean
  public MessageProducer inboundEventdata() {
    String clientId = "Java_" + UUID.randomUUID().toString();
    MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientId,
        this.mqttClientFactory(), "/iot_etage/eventdata");
    adapter.setCompletionTimeout(5000);
    adapter.setConverter(new DefaultPahoMessageConverter());
    adapter.setQos(1);
    adapter.setOutputChannel(mqttInputChannelEventData());

    return adapter;
  }

  @Bean
  public MessageChannel mqttInputChannelEventData() {
    return new DirectChannel();
  }

  @Bean
  @ServiceActivator(inputChannel = "mqttInputChannelEventData")
  public MessageHandler handlerEventData() {

    return new MqttSubEventHandler();
  }

  // inbound-Adpater = subscribe Topic instructions/response
  @Bean
  public MessageProducer inboundInstructionResponse() {
    String clientId = "Java_" + UUID.randomUUID().toString();
    MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientId,
        this.mqttClientFactory(), "/iot_etage/instructions/response");
    adapter.setCompletionTimeout(5000);
    adapter.setConverter(new DefaultPahoMessageConverter());
    adapter.setQos(1);
    adapter.setOutputChannel(mqttInputChannelInstructionResponse());

    return adapter;
  }

  @Bean
  public MessageChannel mqttInputChannelInstructionResponse() {
    return new DirectChannel();
  }

  @Bean
  @ServiceActivator(inputChannel = "mqttInputChannelInstructionResponse")
  public MessageHandler handlerInstructionResponse() {

    return new MqttInstructionResponseHandler();
  }

  // outbound = publish

  @Bean
  @ServiceActivator(inputChannel = "mqttOutboundChannel")
  public MessageHandler mqttOutbound() {
    String clientId = "Java_" + UUID.randomUUID().toString();
    MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(clientId, mqttClientFactory());
    messageHandler.setAsync(true);
    messageHandler.setDefaultTopic("/iot_etage/instructions");
    return messageHandler;
  }

  @Bean
  public MessageChannel mqttOutboundChannel() {
    return new DirectChannel();
  }

  @MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
  public interface InstructionTopicGateway {

    void sendToMqtt(String data);
  }
}
