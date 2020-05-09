#include <ArduinoJson.h>
#include <WiFi.h>
#include <HTTPClient.h>

const int idSensorTemp = 1;
const int idSensorHumed = 2;
const int idSensorLiq = 3;
const int idSensorLuz = 4;

const int idActuadorBomba = 1;
const int idActuadorLuz = 2;
const int idActuadorResistencia = 3;
const int idActuadorVentilador = 4;
const int idActuadorAlarma = 5;

float medidaLuz = 20;
float medidaHumedad = 0;
float medidaTemperatura = 0;
float medidaLiquidos = 0;

char responseBuffer[300];
WiFiClient client;

String SSID = "LAGUNAS";
String PASS = "PeloPicoPata1970";

String SERVER_IP = "192.168.1.120";
int SERVER_PORT = 80;

void postMedidaSensor(float medida, int idSensor);
void postOnActuador(bool endendido, int idActuador);

void setup() {
  Serial.begin(9600);

  WiFi.begin(SSID.c_str(), PASS.c_str());

  Serial.printf("Conectando...");
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.printf(".");
  }
  Serial.print("Exito al conectar, IP address: ");
  Serial.println(WiFi.localIP());
}

void loop() {
  postMedidaSensor(medidaLuz, idSensorLuz);
  //postOnActuador(false, idActuadorBomba);
}

//Funciones POST

void postMedidaSensor(float medida, int idSensor){
  if (WiFi.status() == WL_CONNECTED){
    HTTPClient http;
    http.begin(client, SERVER_IP, SERVER_PORT, "/api/sensor_valor", true);
    http.addHeader("Content-Type", "application/json");

    const size_t capacity = JSON_OBJECT_SIZE(4) + JSON_ARRAY_SIZE(1) + 60;
    DynamicJsonDocument doc(capacity);
    doc["id_sensor"] = idSensor;
    doc["valor"]= medida;
    doc["precision_valor"] = 2;
    doc["timestamp"] = 124123123; //Preguntar como tener un timestamp en tiempo real

    String output;
    serializeJson(doc, output);

    int httpCode = http.PUT(output);

    Serial.print("Response code: ");
    Serial.println(httpCode);

    String payload = http.getString();

    Serial.print("Resultado: ");
    Serial.println(payload);
  }
}

void postOnActuador(bool encendido, int idActuador){
  if (WiFi.status() == WL_CONNECTED){
    HTTPClient http;
    http.begin(client, SERVER_IP, SERVER_PORT, "/api/sensor_valor", true);
    http.addHeader("Content-Type", "application/json");

    const size_t capacity = JSON_OBJECT_SIZE(3) + JSON_ARRAY_SIZE(2) + 60;
    DynamicJsonDocument doc(capacity);
    doc["id_sensor"] = idActuador;
    doc["on"]= encendido;
    doc["timestamp"] = 124123123; //Preguntar como tener un timestamp en tiempo real

    String output;
    serializeJson(doc, output);

    int httpCode = http.PUT(output);

    Serial.print("Response code: ");
    Serial.println(httpCode);

    String payload = http.getString();

    Serial.print("Resultado: ");
    Serial.println(payload);
  }
}
