#include <Stepper.h>
#include <Arduino.h>

#define LIGHT_SENSOR_PIN A0

#define motSpeed 10               //Speed in RPM
#define stepsPerRev 2048   // change this to fit the number of steps per revolution
#define REVS 1              //Anz Umdrehungen, um Markise hochzufahren
#define THRESHOLD 50

#define MPIN1 5
#define MPIN2 4
#define MPIN3 0
#define MPIN4 2

const int steps = stepsPerRev * REVS;


Stepper myStepper(stepsPerRev, MPIN1, MPIN3, MPIN2, MPIN4);

//Programmzustand
typedef enum{MANU,AUTO} Modus;
Modus modus = AUTO;

//Schalter fuer Zustandsaenderung, welche automatisch gesetzt werden
unsigned short schalteManu = 0;
unsigned short istOben = 1;
unsigned short istUnten = 0;

//Ausgaenge
int revDirection = 0;

//Eingaenge
int PWM = 0;
int PWM_PC = 0;

//Sonsiges
int i = 0;

void setup() {
  // set the speed at 60 rpm:
  myStepper.setSpeed(motSpeed);
  // initialize the serial port:
  Serial.begin(115200);
}       

void motor_abschalten(){
  digitalWrite(MPIN1, LOW);
  digitalWrite(MPIN2, LOW);
  digitalWrite(MPIN3, LOW);
  digitalWrite(MPIN4, LOW);
}

//Drives the motor revolution Revolutions 
//Feeds the Watchdog
//Markise hoch
void fahre_hoch(){
  for(i = 0; i < steps; i++){
    ESP.wdtFeed();
    myStepper.step(-1);
    //Serial.println(i);
  }
  motor_abschalten();
  istOben = 1;
  istUnten = 0;
}

//Drives the motor revolution Revolutions
//Feeds the Watchdog
//Markise runter
void fahre_runter(){
  for(i = 0; i < steps; i++){
    ESP.wdtFeed();
    myStepper.step(1);
    //Serial.println(i);
  }
  motor_abschalten();
  istOben = 0;
  istUnten = 1;
}



void loop() {

  //Eingang auslesen
  //Licht gering => R hoch => U hoch => PWM hoch
  PWM = analogRead(LIGHT_SENSOR_PIN);
  PWM_PC = PWM*100/1023; 

  if(modus == AUTO){
    if(PWM_PC <= THRESHOLD && istOben == 1){
      fahre_runter();
    }else if(PWM_PC > THRESHOLD && istUnten == 1){
      fahre_hoch();
    }else{
      motor_abschalten();
    }
  }
  

}
