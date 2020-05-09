const int pinSensorLuz = 34;
const int pinSensorHumedad = 35;
const int pinSensorTemperatura = 32;
const int pinSensorLiquidos = 33;

const int pinActuadorCalor = 25;
const int pinActuadorVentilacion = 26;
const int pinActuadorLuz = 12;
const int pinActuadorBomba = 27;
const int pinActuadorSonido = 14;

const int frecuencia = 6000;
const int canalSonido = 0;
const int resolucion = 8;

float medidaLuz = 0;
float medidaHumedad = 0;
float medidaTemperatura = 0;
float medidaLiquidos = 0;

float humedadNecesaria = 0; 
float temperaturaNecesaria = 0; 

void setup() {

  Serial.begin(9600);
  
  pinMode(pinSensorLuz, INPUT); //Seleccion de pin para sensor de luz
  pinMode(pinSensorHumedad, INPUT); //Seleccion de pin para sensor de humedad
  pinMode(pinSensorTemperatura, INPUT); //Seleccion de pin para sensor de temperatura
  pinMode(pinSensorLiquidos, INPUT); //Seleccion de pin para sensor de liquidos
  
  pinMode(pinActuadorCalor, OUTPUT); //Seleccion de pin para actuador de calor (rele nº3)
  pinMode(pinActuadorVentilacion, OUTPUT); //Seleccion de pin para actuador de ventilacion (rele nº1)
  pinMode(pinActuadorLuz, OUTPUT); //Seleccion de pin para actuador de luz (rele nº4)
  pinMode(pinActuadorBomba, OUTPUT); //Seleccion de pin para actuador de bomba de agua (rele nº2)
  pinMode(pinActuadorSonido, OUTPUT); //Seleccion de pin para actuador de sonido (buzzer)

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
  float humedadNecesaria = 20; //Peticion get de planta para obtener el parametro de humedad de la planta 
  float temperaturaNecesaria = 25; //Peticion get de planta para obtener el parametro de temperatura de la planta 
  
  //Declaracion de variables (sensores)
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
  
  //Subida de datos en tiempo real de los sensores a la bbdd

  //Subida de datos en tiempo real de los actuadores a la bbdd
  
  //Parte funcional del sistema de riego
  if(medidaLiquidos > 10){
    Serial.println("Tanque lleno");
    if(medidaHumedad < humedadNecesaria){
      Serial.println("La bomba esta ENCENDIDA");
      digitalWrite(pinActuadorBomba, LOW);
      delay(1000);
      digitalWrite(pinActuadorBomba, HIGH);
    }else{
      Serial.println("La bomba esta APAGADA");
      digitalWrite(pinActuadorBomba, HIGH);
    }
  }else{
    Serial.println("Hay que rellenar el tanque de agua");
    ledcWriteTone(canalSonido, 500);
    delay(3000);
    ledcWriteTone(canalSonido, 0);
    delay(1000);
  }

  //Parte funcional del sistema de control de temperatura
  if(medidaTemperatura < temperaturaNecesaria){
    Serial.println("El habitaculo se esta calefactando");
    digitalWrite(pinActuadorVentilacion, HIGH);
    digitalWrite(pinActuadorCalor, LOW);
  }else if(medidaTemperatura > temperaturaNecesaria){
    Serial.println("El habitaculo se esta refrigerando");
    digitalWrite(pinActuadorVentilacion, LOW);
    digitalWrite(pinActuadorCalor, HIGH);
  }

  //Parte funcional del sistema de luminosidad
  if(medidaLuz < 20){
    Serial.println("Es de noche!! Luces encendidas!!");
    digitalWrite(pinActuadorLuz, LOW);
  }else{
    Serial.println("Es de dia!! Luces apagadas");
    digitalWrite(pinActuadorLuz, HIGH);
  }
  Serial.println("");
  delay(5000);  
}
