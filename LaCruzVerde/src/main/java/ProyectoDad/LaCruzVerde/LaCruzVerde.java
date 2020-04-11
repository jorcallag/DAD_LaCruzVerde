package ProyectoDad.LaCruzVerde;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class LaCruzVerde extends AbstractVerticle {

	@SuppressWarnings("deprecation")
	@Override
	public void start(Future<Void> startFuture) {
		//		vertx.createHttpServer().requestHandler(
		//				request ->{
		//					request.response().end("<h1>Bienvenido a mi pagina</h1> Hola mundo");
		//		}).listen(8081, result->{
		//			if(result.succeeded()) {
		//				System.out.println("Todo correcto");
		//			}else {
		//				System.out.println(result.cause());
		//			}
		//		});

		//		vertx.deployVerticle(MySecondVerticle.class.getName());
		//		vertx.deployVerticle(MyThirdVerticle.class.getName());

		//		vertx.deployVerticle(BlockingThread.class.getName());

		vertx.deployVerticle(DatabaseVerticle.class.getName());

		//		EventBus eventBus=vertx.eventBus();
		//		vertx.setPeriodic(4000, action->{
		//			eventBus.send("mensaje_p2p", "Hola, esto es un mensaje �te llega?",reply->{
		//				if(reply.succeeded()) {
		//					String replyMessage = 
		//							(String) reply.result().body();
		//					System.out.println("Respuesta: "+replyMessage);
		//				}else {
		//					System.out.println("No ha habido respuesta");
		//				}
		//			});
		//		});
		//		
		//		vertx.setPeriodic(4000, action->{
		//			eventBus.publish("mensaje_broadcast","Esto es un mensaje broadcast");
		//		});
	}
}