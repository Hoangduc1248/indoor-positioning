///** Duy: This is a very naive receiver. Please improve it!
///**
int analogPin = 3; // potentiometer wiper (middle terminal) connected to analog pin 3 
int val = 0; // variable to store the value read
int bit_duration = 5;
int threshold = 512;
int i = 0;
int ar[10];
// All possible 10 bit patterns - just for sure 
int data1[] = {1,0,1,0,1,0,1,0,1,0}; // bit 1 
int data2[] = {0,1,0,1,0,1,0,1,0,1}; // bit 1 
int data3[] = {1,1,0,1,1,0,1,1,0,1}; // bit 0 
int data4[] = {1,0,1,1,0,1,1,0,1,1}; // bit 0 
int data5[] = {0,1,1,0,1,1,0,1,1,0}; // bit 0 
boolean tmp;

void setup() {
    Serial.begin(9600); // setup serial
}
void loop() {
    while(1){
        val = analogRead(analogPin);  // read the input signal
        Serial.println(val);
        if (val>threshold) ar[i] = 1; 
        else ar[i] = 0;
        i++;
        if (i>9) break; delay(bit_duration);
    }
    
//    i = 0; 
//    delay(bit_duration); // decoding bit 1 
//    tmp = true;
//    for (i = 0;i<=9;i++){
//        if (ar[i] != data1[i]){ tmp = false;
//        break;
//      }
//    }
//    
//    if (tmp) Serial.println(1); 
//    else{
//        tmp = true;
//        for (i = 0;i<=9;i++){
//        if (ar[i] != data2[i]){ tmp = false;
//        break;
//      } 
//    }
//    if (tmp) Serial.println(1); 
//    }
//    
//    // decoding bit 0s
//    tmp = true;
//    for (i = 0;i<=9;i++){ 
//      if (ar[i] != data3[i]){
//        tmp = false;
//        break; 
//        }
//    }
//    if (tmp) Serial.println(0); 
//    else{
//          tmp = true;
//          for (i = 0;i<=9;i++){
//          if (ar[i] != data4[i]){ 
//            tmp = false;
//            break;
//          }
//    }
//    if (tmp) Serial.println(0);
//    else{
//      tmp = true;
//      for (i = 0;i<=9;i++){
//        if (ar[i] != data5[i]){ tmp = false;
//        break;
//      } 
//    }
//    if (tmp) Serial.println(0); 
//    }
//   }
}
