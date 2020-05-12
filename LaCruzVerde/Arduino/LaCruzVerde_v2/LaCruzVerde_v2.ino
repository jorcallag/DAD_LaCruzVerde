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

//Declaracion de pines de sensores
const int pinSensorLuz = 34;
const int pinSensorHumedad = 35;
const int pinSensorTemperatura = 32;
const int pinSensorLiquidos = 33;

//Declaracion de pines de actuadores
const int pinActuadorCalor = 25;
const int pinActuadorVentilacion = 26;
const int pinActuadorLuz = 12;
const int pinActuadorBomba = 27;
const int pinActuadorSonido = 14;

//Declaracion de variables necesarias para la alarma
const int frecuencia = 6000;
const int canalSonido = 0;
const int resolucion = 8;

//Declaracion de variables de medidas
float medidaLuz = 0;
float medidaHumedad = 0;
float medidaTemperatura = 0;
float medidaLiquidos = 0;

//Declaracion de variables de la planta
float humedadNecesaria = 0; 
float temperaturaNecesaria = 0; 

//Declaracion de variables para el correcto funcionamiento de la conexion
char responseBuffer[300];
WiFiClient client;
String SSID = "LAGUNAS";
String PASS = "PeloPicoPata1970";
String SERVER_IP = "192.168.1.120";
int SERVER_PORT = 8080;

//Declaracion funciones
void getPlanta();
void postMedidaSensor(float medida, int idSensor);
void postOnActuador(bool endendido, int idActuador);

//Declaracion de variables para timestamp
long tiempoValor = 0;
const long utcOffsetInSeconds = 7200;
WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP, "europe.pool.ntp.org", utcOffsetInSeconds);

void setup() {

  //Inicializacion del puerto Serial
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

  //Inicializacion de pines de sensores
  pinMode(pinSensorLuz, INPUT); //Seleccion de pin para sensor de luz
  pinMode(pinSensorHumedad, INPUT); //Seleccion de pin para sensor de humedad
  pinMode(pinSensorTemperatura, INPUT); //Seleccion de pin para sensor de temperatura
  pinMode(pinSensorLiquidos, INPUT); //Seleccion de pin para sensor de liquidos

  //Inicializacion de pines de actuadores
  pinMode(pinActuadorCalor, OUTPUT); //Seleccion de pin para actuador de calor (rele nº3)
  pinMode(pinActuadorVentilacion, OUTPUT); //Seleccion de pin para actuador de ventilacion (rele nº1)
  pinMode(pinActuadorLuz, OUTPUT); //Seleccion de pin para actuador de luz (rele nº4)
  pinMode(pinActuadorBomba, OUTPUT); //Seleccion de pin para actuador de bomba de agua (rele nº2)
  pinMode(pinActuadorSonido, OUTPUT); //Seleccion de pin para actuador de sonido (buzzer)

  //Inicializacion frecuencias para sonido
  ledcSetup(canalSonido, frecuencia, resolucion);
  ledcAttachPin(pinActuadorSonido, canalSonido);

  //Prueba inicial
  Serial.println("Prueba inicial");
  medidaLuz = (100*analogRead(pinSensorLuz))/4095; //Lectura en porcentaje
  medidaHumedad = (100-((100*analogRead(pinSensorHumedad))/4095)); //Lectura en porcentaje
  medidaTemperatura = analogRead(pinSensorTemperatura)*0,1220703125; //Lectura en grados
  medidaLiquidos = (100*analogRead(pinSensorLiquidos))/4095; //Lectura en porcentaje
  Serial.print("La humedad es del: ");
  Serial.println(medidaHumedad);
  delay(2000);
  Serial.print("La temperatura es de: ");
  Serial.print(medidaTemperatura);
  Serial.println("º");
  delay(2000);
  if(medidaLiquidos < 10){
    Serial.println("Tanque casi vacio");
  }else{
    Serial.println("Tanque lleno");
  }
  delay(2000);
  if(medidaLuz < 20){
    Serial.println("Es de noche!! Luces encendidas!!");
  }else{
    Serial.println("Es de dia!! Luces apagadas");
  } 
  delay(2000);
  Serial.println("CALEFACTOR ENCENDIDO");
  digitalWrite(25, LOW);
  delay(2000);
  Serial.println("CALEFACTOR APAGADO");
  digitalWrite(25, HIGH);
  delay(2000);
  Serial.println("REFRIGERADOR ENCENDIDO");
  digitalWrite(26, LOW);
  delay(2000);
  Serial.println("REFRIGERADOR APAGADO");
  digitalWrite(26, HIGH);
  delay(2000);
  Serial.println("LUZ ENCENDIDA");
  digitalWrite(12, LOW);
  delay(2000);
  Serial.println("LUZ APAGADA");
  digitalWrite(12, HIGH);
  delay(2000);
  Serial.println("BOMBA ENCENDIDA");
  digitalWrite(27, LOW);  
  delay(2000);
  Serial.println("BOMBA APAGADA");
  digitalWrite(27, HIGH);
  delay(2000);
  Serial.println("BUZZER ENCENDIDO");
  ledcWriteTone(canalSonido, 500);
  delay(2000);
  Serial.println("BUZZER APAGADO");
  ledcWriteTone(canalSonido, 0);
  delay(2000);
  Serial.println("");
}

void loop() {
  
  //Declaracion de medidas necesarias de la planta
  getPlanta();

  //Declaramos y calculamos el timestamp
  timeClient.update();
  String tiempo = String(timeClient.getDay()) + String(timeClient.getHours()) + String(timeClient.getMinutes()) + String(timeClient.getSeconds());
  tiempoValor = tiempo.toInt();
  
  //Declaracion de variables de medidas (sensores)
  medidaLuz = (100*analogRead(pinSensorLuz))/4095; //Lectura en porcentaje
  medidaHumedad = (100-((100*analogRead(pinSensorHumedad))/4095)); //Lectura en porcentaje
  medidaTemperatura = analogRead(pinSensorTemperatura)*0,1220703125; //Lectura en grados
  medidaLiquidos = (100*analogRead(pinSensorLiquidos))/4095; //Lectura en porcentaje

  //Imprimimos por el monitor Serial
  Serial.print("La humedad es del: ");
  Serial.println(medidaHumedad);
  Serial.print("La temperatura es de: ");
  Serial.print(medidaTemperatura);
  Serial.println("º");

  //Enviamos datos de los sensores a la BBDD
  postMedidaSensor(medidaTemperatura, 1);
  postMedidaSensor(medidaHumedad, 2);
  postMedidaSensor(medidaLiquidos, 3);
  postMedidaSensor(medidaLuz, 4);
  
  //Parte funcional del sistema de riego
  if(medidaLiquidos > 10){
    ledcWriteTone(canalSonido, 0);
    postOnActuador(5, 0);
    Serial.println("Tanque lleno");
    if(medidaHumedad < humedadNecesaria){
      Serial.println("La bomba esta ENCENDIDA");
      digitalWrite(pinActuadorBomba, LOW);
      delay(1000);
      digitalWrite(pinActuadorBomba, HIGH);
      postOnActuador(1, 1);
    }else{
      Serial.println("La bomba esta APAGADA");
      digitalWrite(pinActuadorBomba, HIGH);
      postOnActuador(1, 0); //
    }
    postOnActuador(5, 0);
  }else{
    Serial.println("Hay que rellenar el tanque de agua");
    ledcWriteTone(canalSonido, 500);
    postOnActuador(5, 1);
  }

  //Parte funcional del sistema de control de temperatura
  if(medidaTemperatura < temperaturaNecesaria){
    Serial.println("El habitaculo se esta calefactando");
    digitalWrite(pinActuadorVentilacion, HIGH);
    digitalWrite(pinActuadorCalor, LOW);
    postOnActuador(3, 1);
    postOnActuador(4, 0);
  }else if(medidaTemperatura > temperaturaNecesaria){
    Serial.println("El habitaculo se esta refrigerando");
    digitalWrite(pinActuadorVentilacion, LOW);
    digitalWrite(pinActuadorCalor, HIGH);
    postOnActuador(3, 0);
    postOnActuador(4, 1);
  }

  //Parte funcional del sistema de luminosidad
  if(medidaLuz < 20){
    Serial.println("Es de noche!! Luces encendidas!!");
    digitalWrite(pinActuadorLuz, LOW);
    postOnActuador(2, 1);
  }else{
    Serial.println("Es de dia!! Luces apagadas");
    digitalWrite(pinActuadorLuz, HIGH);
    postOnActuador(2, 0);
  }
  Serial.println("");
  delay(5000);  
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
    
    float temperaturaNecesaria = planta["temp_amb_planta"].as<float>();
    float humedadNecesaria = planta["humed_tierra_planta"].as<float>();
  }
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
    doc["tiempo"] = 124123123345678; //Preguntar como tener un timestamp en tiempo real

    String output;
    serializeJson(doc, output);

    int httpCode = http.PUT(output);

    //Serial.print("Response code: ");
    //Serial.println(httpCode);

    String payload = http.getString();

    //Serial.print("Resultado: ");
    //Serial.println(payload);
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
    doc["tiempo"] = 12412312; //Preguntar como tener un timestamp en tiempo real

    String output;
    serializeJson(doc, output);

    int httpCode = http.PUT(output);

    //Serial.print("Response code: ");
    //Serial.println(httpCode);

    String payload = http.getString();

    //Serial.print("Resultado: ");
    //Serial.println(payload);
  }
}
