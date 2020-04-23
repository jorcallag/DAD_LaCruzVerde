package ProyectoDad.LaCruzVerde;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class FirstVerticle_LaCruzVerde extends AbstractVerticle {

	@SuppressWarnings("deprecation")
	@Override
	public void start(Future<Void> startFuture) {

		//API Rest
//		vertx.deployVerticle(DatabaseVerticle_LaCruzVerde.class.getName());
		
		//MQTT
		vertx.deployVerticle(MqttServerVerticle_LaCruzVerde.class.getName());
		vertx.deployVerticle(MqttClientVerticle_LaCruzVerde.class.getName());

	}
}