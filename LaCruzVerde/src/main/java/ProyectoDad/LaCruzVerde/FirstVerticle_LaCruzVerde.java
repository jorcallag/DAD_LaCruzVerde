package ProyectoDad.LaCruzVerde;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class FirstVerticle_LaCruzVerde extends AbstractVerticle {

	@SuppressWarnings("deprecation")
	@Override
	public void start(Future<Void> startFuture) {

		//API Rest
//		vertx.deployVerticle(DatabaseVerticle_LaCruzVerde.class.getName());
		
		//MQTT Server
		vertx.deployVerticle(MqttServerVerticle_LaCruzVerde.class.getName());
		
		//MQTT Client (con todos los topics en la misma clase, error, solo ejecuta el ultimo topic creado 
//		vertx.deployVerticle(MqttClientVerticle_LaCruzVerde.class.getName());
		
		//MQTT Client (con todos los topics separados en distintas clases)
		vertx.deployVerticle(MqttClientVerticle_Planta_LaCruzVerde.class.getName());
		vertx.deployVerticle(MqttClientVerticle_Dispositivo_LaCruzVerde.class.getName());
		vertx.deployVerticle(MqttClientVerticle_Sensor_LaCruzVerde.class.getName());
		vertx.deployVerticle(MqttClientVerticle_Sensor_valor_LaCruzVerde.class.getName());
		vertx.deployVerticle(MqttClientVerticle_Actuador_LaCruzVerde.class.getName());
		vertx.deployVerticle(MqttClientVerticle_Actuador_valor_LaCruzVerde.class.getName());
		
	}
}