// This #include statement was automatically added by the Particle IDE.
#include <SparkJson.h>

#define TEMP_PIN A0
#define SERVO_PIN D0
#define BTN1_PIN D2
#define BTN2_PIN D1

double temp;
double desiredTemp;
double lastTemp;
int onOrOff;

int pos;
Servo myservo;

const unsigned long PUBLISH_PERIOD_MS = 5000;
const unsigned long FIRST_PUBLISH_MS = 0;
unsigned long lastPublish = FIRST_PUBLISH_MS - PUBLISH_PERIOD_MS;

void setup() {
    myservo.attach(SERVO_PIN);
    pinMode(BTN1_PIN, INPUT_PULLUP);
    pinMode(BTN2_PIN, INPUT_PULLUP);
    
    Particle.variable("temp", temp);
    Particle.variable("desiredTemp", desiredTemp);
    Particle.variable("lastTemp", lastTemp);
    Particle.variable("onOrOff", onOrOff);
    Particle.variable("pos", pos);
    Particle.subscribe("hook-response/config", getDataHandler, MY_DEVICES);
    
    getTemp();
    lastTemp = temp;
}

void loop() {
    getTemp();
    servoMode(onOrOff);
    
    if (millis() - lastPublish >= PUBLISH_PERIOD_MS) {
        lastPublish = millis();
        publishData();
        
        if (heat()) {
            if (desiredTemp - temp > 2)
                servoMode(1);
            else
                servoMode(0);
        } else {
            if (temp - desiredTemp > 2)
                servoMode(1);
            else
                servoMode(0);
        }
    }
}

void servoMode(int onOff) {
    if (onOff == 0)
        pos = 13;
    if (onOff == 1)
        pos = 53;
    myservo.write(pos);
}

void getTemp() {
    temp = analogRead(TEMP_PIN)*5/4095.0;
    temp = temp - 0.5;
    temp = temp / 0.01;
}

bool heat() {
    if(pos == 53 && lastTemp < temp)
        return true;
    
    lastTemp = temp;
    return false;
}

void publishData() {
    String data = "{\"actualTemp\":" + String(temp) + ",\"desiredTemp\":" + String(desiredTemp) + "}";
    Particle.publish("timedata", data, PRIVATE);
    Particle.publish("config", "", PRIVATE);
    String data2 = "{\"currentTemp\":" + String(temp) + "}";
    Particle.publish("currentTemp", data2, PRIVATE);
}

void getDataHandler(const char *topic, const char *data) {
    StaticJsonBuffer<256> jsonBuffer;
	char *mutableCopy = strdup(data);
	JsonObject& root = jsonBuffer.parseObject(mutableCopy);
	free(mutableCopy);

    desiredTemp = atoi(root["desiredTemp"]);
    onOrOff = atoi(root["onOrOff"]);
}