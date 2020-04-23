package ProyectoDad.LaCruzVerde;

import java.util.ArrayList;
import java.util.List;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.SetMultimap;

import io.netty.handler.codec.mqtt.MqttConnectReturnCode;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.ClientAuth;
import io.vertx.mqtt.MqttEndpoint;
import io.vertx.mqtt.MqttServer;
import io.vertx.mqtt.MqttServerOptions;
import io.vertx.mqtt.MqttTopicSubscription;

public class MqttServerVerticle_LaCruzVerde extends AbstractVerticle{
	
	public static final String TOPIC_ACTUADOR = "actuador";
	public static final String TOPIC_ACTUADOR_VALOR = "actuador_valor";
	public static final String TOPIC_SENSOR = "sensor";
	public static final String TOPIC_SENSOR_VALOR = "sensor_valor";
	public static final String TOPIC_DISPOSITIVO = "dispositivo";
	public static final String TOPIC_PLANTA = "planta";
	
	private static final SetMultimap<String, MqttEndpoint> clients = LinkedHashMultimap.create();
	
	public void start (Promise<Void> promise) {
		
		MqttServerOptions options = new MqttServerOptions();
		options.setPort(1885);
		options.setClientAuth(ClientAuth.REQUIRED);
		MqttServer mqttServer = MqttServer.create(vertx, options);
		init(mqttServer);
		
	}
	
	public void init(MqttServer mqttSever) {
		mqttSever.endpointHandler(endpoint -> {
			System.out.println("Cliente MQTT [" + endpoint.clientIdentifier() + 
					"] solicita conexion. Sesion limpia = " + endpoint.isCleanSession());
			if(endpoint.auth().getUsername().contentEquals("mqttbroker") && endpoint.auth().getPassword().contentEquals("mqttbrokerpass")) {
				endpoint.accept();
				handleSubscription(endpoint);
				handleUnsubscription(endpoint);
				publishHandle(endpoint);
				handleClientDisconnect(endpoint);
			}else {
				endpoint.reject(MqttConnectReturnCode.CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD);
			}
		}).listen(ar -> {
			if(ar.succeeded()) {
				System.out.println("El servidor MQTT se esta ejecutando en el puerto: " + ar.result().actualPort());
			}else {
				System.out.println("Error al iniciar servidor MQTT");
				ar.cause().printStackTrace();
			}
		});
	}
	
	private void handleSubscription(MqttEndpoint endpoint) {
		endpoint.subscribeHandler(subscribe -> {
			List<MqttQoS> grantedQoSLevels = new ArrayList<>();
			for(MqttTopicSubscription s : subscribe.topicSubscriptions()) {
				System.out.println("Suscripcion de " + s.topicName() + " con QoS " + s.qualityOfService());
				grantedQoSLevels.add(s.qualityOfService());
				clients.put(s.topicName(), endpoint);
			}
			endpoint.subscribeAcknowledge(subscribe.messageId(), grantedQoSLevels);
		});
	}
	
	private void handleUnsubscription(MqttEndpoint endpoint) {
		endpoint.unsubscribeHandler(unsuscribe -> {
			for(String t : unsuscribe.topics()) {
				System.out.println("Desuscripcion de " + t);
				clients.remove(t, endpoint);
			}
			endpoint.unsubscribeAcknowledge(unsuscribe.messageId());
		});
	}
	
	private void publishHandle(MqttEndpoint endpoint) {
		endpoint.publishHandler(message -> {
			if(message.qosLevel() == MqttQoS.AT_LEAST_ONCE) {
				String topicName = message.topicName();
				System.out.println("Nuevo mensaje publicado en " + topicName);
				for(MqttEndpoint subscribed : clients.get(topicName)) {
					subscribed.publish(message.topicName(), message.payload(), message.qosLevel(), message.isDup(), message.isRetain());
				}
				endpoint.publishAcknowledge(message.messageId());
			}else if(message.qosLevel() == MqttQoS.EXACTLY_ONCE){
				endpoint.publishRelease(message.messageId());
			}
		}).publishReleaseHandler(messageId -> {
			endpoint.publishComplete(messageId);
		});
	}
	
	private void handleClientDisconnect(MqttEndpoint endpoint) {
		endpoint.disconnectHandler(h -> {
			System.out.println("El cliente remoto a cerrado la conexion");
		});
	}
	
}

