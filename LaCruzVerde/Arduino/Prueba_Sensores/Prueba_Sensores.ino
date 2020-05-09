void setup() {
  pinMode(33, INPUT);
  Serial.begin(9600);
}

void loop() {
  int medidaHumedad = analogRead(33);
  Serial.println(medidaHumedad);
  Serial.println(((100*medidaHumedad)/4096));
}
