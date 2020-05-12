#include <ArduinoJson.h>
#include <WiFi.h>
#include <HTTPClient.h>

//Declaracion de variables para el correcto funcionamiento de la conexion
char responseBuffer[300];
WiFiClient client;
String SSID = "LAGUNAS";
String PASS = "PeloPicoPata1970";
String SERVER_IP = "192.168.1.120";
int SERVER_PORT = 8080;

//Declaracion funciones
void get_planta();

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
}

void loop() {

  //Funcion get necesaria para el proyecto
  getPlanta();
  delay(2000);
}

//Funciones GET

void getPlanta(){
  if(WiFi.status() == WL_CONNECTED){
    HTTPClient http;
    http.begin(client, SERVER_IP, SERVER_PORT, "/api/planta/1", true); //URL del recurso
    int httpCode = http.GET();

    Serial.print("Response code: ");
    Serial.println(httpCode);

    String payload = http.getString();

    Serial.println(payload);

    const size_t capacity = JSON_OBJECT_SIZE(4) + JSON_ARRAY_SIZE(1) + 60;
    DynamicJsonDocument doc(capacity);

    DeserializationError error = deserializeJson(doc, payload);
    //if(error){
    //  Serial.print("deserializeJson() failed: ");
    //  Serial.println(error.c_str());
    //  return;
    //}

    JsonObject planta = doc[0].as<JsonObject>();
    
    int id_planta = planta["id_planta"].as<int>();
    String nombre_planta = planta["nombre_planta"].as<char*>();
    float temp_amb_planta = planta["temp_amb_planta"].as<float>();
    float humed_tierra_planta = planta["humed_tierra_planta"].as<float>();

    Serial.print("Planta numero ");
    Serial.print(id_planta);
    Serial.print(": ");
    Serial.print(nombre_planta);
    Serial.print(". Temperatura necesaria: ");
    Serial.print(temp_amb_planta);
    Serial.print(". Humedad necesaria: ");
    Serial.println(humed_tierra_planta);
  }
}
