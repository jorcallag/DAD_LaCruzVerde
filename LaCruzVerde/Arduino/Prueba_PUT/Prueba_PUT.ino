#include <ArduinoJson.h>
#include <WiFi.h>
#include <HTTPClient.h>
#include <NTPClient.h>

//Declaracion de ids de sensores
const int idSensorTemp = 1;
const int idSensorHumed = 2;
const int idSensorLiq = 3;
const int idSensorLuz = 4;

//Declaracion de ids de actuadores
const int idActuadorBomba = 1;
const int idActuadorLuz = 2;
const int idActuadorResistencia = 3;
const int idActuadorVentilador = 4;
const int idActuadorAlarma = 5;

//Declaracion de variables de medidas
float medidaLuz = 20;
float medidaHumedad = 0;
float medidaTemperatura = 0;
float medidaLiquidos = 0;

//Declaracion de variables para el correcto funcionamiento de la conexion
char responseBuffer[300];
WiFiClient client;
String SSID = "LAGUNAS";
String PASS = "PeloPicoPata1970";
String SERVER_IP = "192.168.1.120";
int SERVER_PORT = 8080;

//Declaracion funciones
void postMedidaSensor(float medida, int idSensor);
void postOnActuador(bool endendido, int idActuador);

//Declaracion de variables para timestamp
long tiempoValor = 0;
const long utcOffsetInSeconds = 7200;
WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP, "europe.pool.ntp.org", utcOffsetInSeconds);

void setup() {

  //Iniciamos el puerto serial
  Serial.begin(9600);

  //Iniciamos la conexion
  WiFi.begin(SSID.c_str(), PASS.c_str());
  Serial.printf("Conectando...");
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.printf(".");
  }
  Serial.print("Exito al conectar, IP address: ");
  Serial.println(WiFi.localIP());

  //Iniciamos el reloj
  timeClient.begin();
}

void loop() {

  //Declaramos y calculamos el timestamp 
  timeClient.update();
  String tiempo = String(timeClient.getDay()) + String(timeClient.getHours()) + String(timeClient.getMinutes()) + String(timeClient.getSeconds());
  tiempoValor = tiempo.toInt();

  //Funciones post necesarias para el proyecto
  postMedidaSensor(medidaLuz, idSensorLuz); //Sube los datos de un sensor especifico
  delay(2000);
  postOnActuador(false, idActuadorBomba); //Sube los datos de un actuador especifico
  delay(2000);
}

//Funciones POST

void postMedidaSensor(float medida, int idSensor){
  if (WiFi.status() == WL_CONNECTED){
    HTTPClient http;
    http.begin(client, SERVER_IP, SERVER_PORT, "/api/sensor_valor", true);
    http.addHeader("Content-Type", "application/json");

    const size_t capacity = JSON_OBJECT_SIZE(4) + JSON_ARRAY_SIZE(0) + 60;
    DynamicJsonDocument doc(capacity);
    doc["id_sensor"] = idSensor;
    doc["valor"]= medida;
    doc["precision_valor"] = 2;
    doc["tiempo"] = tiempoValor; //Preguntar como tener un timestamp en tiempo real

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
    http.begin(client, SERVER_IP, SERVER_PORT, "/api/actuador_valor", true);
    http.addHeader("Content-Type", "application/json");

    const size_t capacity = JSON_OBJECT_SIZE(3) + JSON_ARRAY_SIZE(0) + 60;
    DynamicJsonDocument doc(capacity);
    doc["id_actuador"] = idActuador;
    doc["funcionamiento"]= encendido;
    doc["tiempo"] = tiempoValor; //Preguntar como tener un timestamp en tiempo real

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
