const int LED = 8; //led connects to digital pin 13
const int bit_duration = 5; //milisecond
int location = 0; // location code: 0 or 1

void setup()
{
  Serial.begin(115200); // Too high several hundred
  pinMode(LED, OUTPUT); //set the digital pin as output
}

void loop()
{   
      if (location == 1)
      {
        // code pattern: 10
        digitalWrite(LED,1); //turn on the led
        delay(bit_duration);   
        digitalWrite(LED,0); //turn on the led
        delay(bit_duration); 
      }
      else
      {
        // code pattern 110
        digitalWrite(LED,1);
        delay(bit_duration);
        digitalWrite(LED,1);
        delay(bit_duration);
        digitalWrite(LED,0);
        delay(bit_duration);
      }
}
