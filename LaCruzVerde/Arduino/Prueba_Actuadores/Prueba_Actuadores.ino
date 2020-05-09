const int frecuencia = 5000;
const int canalSonido = 0;
const int resolucion = 8;

void setup() {
  Serial.begin(9600);
  pinMode(14, OUTPUT);
  pinMode(12, OUTPUT);
  pinMode(27, OUTPUT);
  pinMode(25, OUTPUT);
  pinMode(35, INPUT);
  pinMode(33, INPUT);

  ledcSetup(canalSonido, frecuencia, resolucion);
  ledcAttachPin(14, canalSonido);
}

void loop() {
  //float medidaLuz = (100*analogRead(pinSensorLuz))/4095; //Lectura en porcentaje
  float medidaHumedad = (100-((100*analogRead(35))/4095)); //Lectura en porcentaje
  //float medidaTemperatura = analogRead(pinSensorTemperatura)*0,1220703125; //Lectura en grados
  float medidaLiquidos = (100*analogRead(33))/4095; //Lectura en porcentaje
  Serial.print("La humedad es del: ");
  Serial.println(medidaHumedad);
  delay(2000);
  //Serial.print("La temperatura es de: ");
  //Serial.print(medidaTemperatura);
  //Serial.println("ª");
  //delay(2000);
  if(medidaLiquidos < 10){
    Serial.println("Tanque casi vacio");
  }else{
    Serial.println("Tanque lleno");
  }
  delay(2000);
//  if(medidaLuz < 20){
//    Serial.println("Es de noche!! Luces encendidas!!");
//  }else{
//    Serial.println("Es de dia!! Luces apagadas");
//  } 
  Serial.println("CALOR ENCENDIDO");
  digitalWrite(25, LOW);
  delay(2000);
  Serial.println("CALOR APAGADO");
  digitalWrite(25, HIGH);
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
}
