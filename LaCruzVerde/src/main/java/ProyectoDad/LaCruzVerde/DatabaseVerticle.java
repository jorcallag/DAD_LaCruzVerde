package ProyectoDad.LaCruzVerde;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.mysqlclient.MySQLClient;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

import types.sensor_valor;
import types.planta;
import types.sensor;
import types.dispositivo;

public class DatabaseVerticle extends AbstractVerticle {

	private MySQLPool mySQLPool;

	@Override
	public void start(Promise<Void> startPromise) {
		MySQLConnectOptions mySQLConnectOptions = new MySQLConnectOptions().setPort(3306).setHost("localhost")
				.setDatabase("daddatabase").setUser("root").setPassword("root");
		PoolOptions poolOptions = new PoolOptions().setMaxSize(10);
		mySQLPool = MySQLPool.pool(vertx, mySQLConnectOptions, poolOptions);

		Router router = Router.router(vertx);
		router.route("/api/*").handler(BodyHandler.create());
		

		vertx.createHttpServer().requestHandler(router::handle).listen(8080, result -> {
			if (result.succeeded()) {
				startPromise.complete();
				System.out.println("Conexion con la BBDD satisfactoria");
			} else {
				startPromise.fail(result.cause());
				System.out.println("Conexion con la BBDD fallida");
			}
		});

		//Get y Put sensor_valor
		router.get("/api/sensor_valor/:id_sensor").handler(this::get_sensor_valor);
		router.put("/api/sensor_valor").handler(this::put_sensor_valor);
		
		//Get y Put planta
		router.get("/api/planta/:id_planta").handler(this::get_planta);
		router.put("/api/planta").handler(this::put_planta);
		
		//Get y Put sensor
		router.get("/api/sensor/:id_sensor").handler(this::get_sensor);
		router.put("/api/sensor").handler(this::put_sensor);
		//Get y Put dispositivo
		router.get("/api/dispositivo/:id_dispositivo").handler(this::get_dispositivo);
		router.put("/api/dispositivo").handler(this::put_dispositivo);
		
	}

	//Metodo Put para sensor_valor
	private void put_sensor_valor(RoutingContext routingContext) {
		sensor_valor sensor_valor = Json.decodeValue(routingContext.getBodyAsString(), sensor_valor.class);
		System.out.println(sensor_valor.toString());
		mySQLPool.preparedQuery(
				"INSERT INTO daddatabase.sensor_valor (id_sensor, valor, precision_valor, tiempo) VALUES (?,?,?,?)",
				Tuple.of(sensor_valor.getId_sensor(), sensor_valor.getValor(), sensor_valor.getPrecision_valor(),
						sensor_valor.getTiempo()),
				handler -> {
					if (handler.succeeded()) {
						System.out.println("Añadida correctamente");

						long id = handler.result().property(MySQLClient.LAST_INSERTED_ID);
						sensor_valor.setId_sensor_valor((int) id);

						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end(JsonObject.mapFrom(sensor_valor).encodePrettily());
					} else {
						System.out.println("Algo salió mal");
						System.out.println(handler.cause().toString());
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
					}
				});
	}

	//Metodo Get para sensor_valor
	private void get_sensor_valor(RoutingContext routingContext) {		
		mySQLPool.query("SELECT * FROM daddatabase.sensor_valor WHERE id_sensor = "
				+ routingContext.request().getParam("id_sensor"), res -> {
					if (res.succeeded()) {
						RowSet<Row> resultSet = res.result();
						System.out.println("Consulta satisfactoria");
						JsonArray result = new JsonArray();
						for (Row row : resultSet) {
							result.add(JsonObject.mapFrom(new sensor_valor(row.getInteger("id_sensor_valor"),
									row.getInteger("id_sensor"), row.getFloat("valor"), row.getFloat("precision_valor"),
									row.getLong("tiempo"))));
						}
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end(result.encodePrettily());
					} else {
						System.out.println("Consulta fallida");
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
					}
				});
	}

	//Metodo Put para planta
//	private void post_planta(RoutingContext routingContext) {
//		JsonObject planta = routingContext.getBodyAsJson();
//		System.out.println(planta);
//		mySQLPool.query("INSERT INTO daddatabase.dispositivo VALUES (" + planta.getInteger("id_planta") + "," + planta.getString("nombre_planta") + "," + planta.getFloat("temp_amb_planta") + "," +
//				planta.getFloat("humed_tierra_planta") + "," + planta.getFloat("humed_amb_planta") +")" , 
//				handler -> {
//					if (handler.succeeded()) {
//						System.out.println("Añadida correctamente");
//						System.out.println(handler.result().rowCount());
//
//						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
//						.end(planta.encodePrettily());
//					} else {
//						System.out.println("Algo salió mal");
//						System.out.println(handler.cause().toString());
//						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
//						.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
//					}
//				});
//	}
	
	private void put_planta(RoutingContext routingContext) {
		planta planta = Json.decodeValue(routingContext.getBodyAsString(), planta.class);
		System.out.println(planta.toString());
		mySQLPool.preparedQuery(
				"INSERT INTO daddatabase.planta (nombre_planta, temp_amb_planta, humed_tierra_planta, humed_amb_planta) VALUES (?,?,?,?)",
				Tuple.of(planta.getNombre_planta(), planta.getTemp_amb_planta(), planta.getHumed_tierra_planta(), planta.getHumed_amb_planta()),
				handler -> {
					if (handler.succeeded()) {
						System.out.println("Añadida correctamente");

						long id = handler.result().property(MySQLClient.LAST_INSERTED_ID);
						planta.setId_planta((int) id);

						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end(JsonObject.mapFrom(planta).encodePrettily());
					} else {
						System.out.println("Algo salió mal");
						System.out.println(handler.cause().toString());
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
					}
				});
	}

	//Metodo Get para planta
	private void get_planta(RoutingContext routingContext) {		
		mySQLPool.query("SELECT * FROM daddatabase.planta WHERE id_planta = "
				+ routingContext.request().getParam("id_planta"), res -> {
					if (res.succeeded()) {
						RowSet<Row> resultSet = res.result();
						System.out.println("Consulta satisfactoria");
						JsonArray result = new JsonArray();
						for (Row row : resultSet) {
							result.add(JsonObject.mapFrom(new planta(row.getInteger("id_planta"),
									row.getString("nombre_planta"), row.getFloat("temp_amb_planta"), row.getFloat("humed_tierra_planta"),
									row.getFloat("humed_amb_planta"))));
						}
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end(result.encodePrettily());
					} else {
						System.out.println("Consulta fallida");
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
					}
				});
	}

	//Metodo Put para sensor
	private void put_sensor(RoutingContext routingContext) {
		sensor sensor = Json.decodeValue(routingContext.getBodyAsString(), sensor.class);
		System.out.println(sensor.toString());
		mySQLPool.preparedQuery(
				"INSERT INTO daddatabase.sensor (tipo, nombre, id_dispositivo) VALUES (?,?,?)",
				Tuple.of(sensor.getTipo(), sensor.getNombre(), sensor.getId_dispositivo()),
				handler -> {
					if (handler.succeeded()) {
						System.out.println("Añadida correctamente");

						long id = handler.result().property(MySQLClient.LAST_INSERTED_ID);
						sensor.setId_sensor((int) id);

						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end(JsonObject.mapFrom(sensor).encodePrettily());
					} else {
						System.out.println("Algo salió mal");
						System.out.println(handler.cause().toString());
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
					}
				});
	}

	//Metodo Get para sensor
	private void get_sensor(RoutingContext routingContext) {		
		mySQLPool.query("SELECT * FROM daddatabase.sensor WHERE id_sensor = "
				+ routingContext.request().getParam("id_sensor"), res -> {
					if (res.succeeded()) {
						RowSet<Row> resultSet = res.result();
						System.out.println("Consulta satisfactoria");
						JsonArray result = new JsonArray();
						for (Row row : resultSet) {
							result.add(JsonObject.mapFrom(new sensor(row.getInteger("id_sensor"),
									row.getString("tipo"), row.getString("nombre"), row.getInteger("id_dispositivo"))));
						}
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end(result.encodePrettily());
					} else {
						System.out.println("Consulta fallida");
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
					}
				});
	}

	//Metodo Put para dispositivo
//	private void post_dispositivo(RoutingContext routingContext) {
//		JsonObject dispositivo = routingContext.getBodyAsJson();
//		System.out.println(dispositivo);
//		mySQLPool.query("INSERT INTO daddatabase.dispositivo VALUES (" + dispositivo.getInteger("id_dispositivo") + "," + dispositivo.getString("ip") + "," + dispositivo.getString("nombre") + "," +
//				dispositivo.getInteger("id_planta") + "," + dispositivo.getLong("tiempoInicial") +")" , 
//				handler -> {
//					if (handler.succeeded()) {
//						System.out.println("Añadida correctamente");
//						System.out.println(handler.result().rowCount());
//
//						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
//						.end(dispositivo.encodePrettily());
//					} else {
//						System.out.println("Algo salió mal");
//						System.out.println(handler.cause().toString());
//						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
//						.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
//					}
//				});
//	}
	
	private void put_dispositivo(RoutingContext routingContext) {
		dispositivo dispositivo = Json.decodeValue(routingContext.getBodyAsString(), dispositivo.class);
		System.out.println(dispositivo.toString());
		mySQLPool.preparedQuery(
				"INSERT INTO daddatabase.dispositivo (id_dispositivo, ip, nombre, id_planta, tiempoInicial) VALUES (?,?,?,?,?)",
				Tuple.of(dispositivo.getId_dispositivo(), dispositivo.getIp(), dispositivo.getNombre(), dispositivo.getId_planta(), dispositivo.getTiempoInicial()),
				handler -> {
					if (handler.succeeded()) {
						System.out.println("Añadida correctamente");

						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end(JsonObject.mapFrom(dispositivo).encodePrettily());
					} else {
						System.out.println("Algo salió mal");
						System.out.println(handler.cause().toString());
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
					}
				});
	}

	//Metodo Get para dispositivo
	private void get_dispositivo(RoutingContext routingContext) {		
		mySQLPool.query("SELECT * FROM daddatabase.dispositivo WHERE id_dispositivo = "
				+ routingContext.request().getParam("id_dispositivo"), res -> {
					if (res.succeeded()) {
						RowSet<Row> resultSet = res.result();
						System.out.println("Consulta satisfactoria");
						JsonArray result = new JsonArray();
						for (Row row : resultSet) {
							result.add(JsonObject.mapFrom(new dispositivo(row.getInteger("id_dispositivo"),
									row.getString("ip"), row.getString("nombre"), row.getInteger("id_planta"), row.getLong("tiempoInicial"))));
						}
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end(result.encodePrettily());
					} else {
						System.out.println("Consulta fallida");
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
					}
				});
	}
	
	//Las entidades actuador y actuador_valor, no tienen ninguna utilidad, estan diseñados sus metodos pero no seran necesarios.

	//Metodo Put para actuador_valor
	/*private void put_actuador_valor(RoutingContext routingContext) {
		actuador_valor actuador_valor = Json.decodeValue(routingContext.getBodyAsString(), actuador_valor.class);
		mySQLPool.preparedQuery(
				"INSERT INTO daddatabase.actuador_valor (id_actuador, on, tiempo) VALUES (?,?,?)",
				Tuple.of(actuador_valor.getId_actuador(), actuador_valor.getOn(), actuador_valor.getTiempo()),
				handler -> {
					if (handler.succeeded()) {
						System.out.println("Añadida correctamente");

						long id = handler.result().property(MySQLClient.LAST_INSERTED_ID);
						actuador_valor.setId_actuador_valor((int) id);

						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end(JsonObject.mapFrom(actuador_valor).encodePrettily());
					} else {
						System.out.println("Algo salió mal");
						System.out.println(handler.cause().toString());
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
					}
				});
	}*/

	//Metodo Get para actuador_valor
	/*private void get_actuador_valor(RoutingContext routingContext) {		
		mySQLPool.query("SELECT * FROM daddatabase.actuador_valor WHERE id_actuador = "
				+ routingContext.request().getParam("id_actuador"), res -> {
					if (res.succeeded()) {
						RowSet<Row> resultSet = res.result();
						System.out.println("Consulta satisfactoria");
						JsonArray result = new JsonArray();
						for (Row row : resultSet) {
							result.add(JsonObject.mapFrom(new actuador_valor(row.getInteger("id_actuador_valor"),
									row.getInteger("id_actuador"), row.getBoolean("on"), row.getLong("tiempo"))));
						}
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end(result.encodePrettily());
					} else {
						System.out.println("Consulta fallida");
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
					}
				});
	}*/

	//Metodo Put para actuador
	/*private void put_actuador(RoutingContext routingContext) {
		actuador actuador = Json.decodeValue(routingContext.getBodyAsString(), actuador.class);
		mySQLPool.preparedQuery(
				"INSERT INTO daddatabase.actuador (tipo, nombre, id_dispositivo) VALUES (?,?,?)",
				Tuple.of(actuador.getTipo(), actuador.getNombre(), actuador.getId_dispositivo()),
				handler -> {
					if (handler.succeeded()) {
						System.out.println("Añadida correctamente");

						long id = handler.result().property(MySQLClient.LAST_INSERTED_ID);
						actuador.setId_actuador((int) id);

						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end(JsonObject.mapFrom(actuador).encodePrettily());
					} else {
						System.out.println("Algo salió mal");
						System.out.println(handler.cause().toString());
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
					}
				});
	}*/

	//Metodo Get para actuador
	/*private void get_actuador(RoutingContext routingContext) {		
		mySQLPool.query("SELECT * FROM daddatabase.sensor WHERE id_actuador = "
				+ routingContext.request().getParam("id_actuador"), res -> {
					if (res.succeeded()) {
						RowSet<Row> resultSet = res.result();
						System.out.println("Consulta satisfactoria");
						JsonArray result = new JsonArray();
						for (Row row : resultSet) {
							result.add(JsonObject.mapFrom(new actuador(row.getInteger("id_actuador"),
									row.getString("tipo"), row.getString("nombre"), row.getInteger("id_dispositivo"))));
						}
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end(result.encodePrettily());
					} else {
						System.out.println("Consulta fallida");
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
					}
				});
	}*/

}
