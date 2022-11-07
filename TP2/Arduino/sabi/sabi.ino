// Descomentar la siguiente línea para trabajar en el IDE de Arduino
// #include <DS3231.h>
#include <SoftwareSerial.h>

// ------------------------------------------------
// Etiquetas
// ------------------------------------------------
#define LOG // Comentar esta linea para desactivar logs

// ------------------------------------------------
// Constantes
// ------------------------------------------------
#define MAX_STATES 6
#define MAX_EVENTS 8
#define MAX_VALUE_POTENTIOMETER_AUTO 341
#define MAX_VALUE_POTENTIOMETER_OFF 648
#define CONTINUE_VALUE -1
#define COLOR_MAX_AMOUNT 255
#define COLOR_MIN_AMOUNT 0
#define COLOR_GREEN_AMOUNT_FOR_ORANGE 69
// Para probar con Serial.read, cambiar el rango horario entre dos valores < 10
#define DATE_TIME_HOUR_MIN 9
#define DATE_TIME_HOUR_MAX 21
#define BLUETOOTH_WATER_LEVEL_COMMAND 1

// ------------------------------------------------
// TEMPORIZADORES
// ------------------------------------------------
#define TMP_EVENTS_MILLIS 50

// ------------------------------------------------
// Pines sensores (A = analógico | D = Digital)
// ------------------------------------------------
#define PIN_D_QUARTER_CABLE 2
#define PIN_D_HALF_CABLE 4
#define PIN_D_THREE_QUARTER_CABLE 7
#define PIN_D_FULL_CABLE 8
#define PIN_D_RX 5
#define PIN_D_TX 6
#define PIN_A_POTENTIOMETER A0
//////////// #define PIN_SCL_CLOCK
//////////// #define PIN_SDA_CLOCK

// ------------------------------------------------
// Pines actuadores (P = PWM | D = Digital)
// ------------------------------------------------
#define PIN_P_RGB_LED_RED 9
#define PIN_P_RGB_LED_BLUE 10
#define PIN_P_RGB_LED_GREEN 11
#define PIN_D_RELAY 12

// ------------------------------------------------
// Estados posibles del embebido
// ------------------------------------------------
enum states_e {
    STATE_INITIAL,
    STATE_OFF,
    STATE_AUTO_WITHIN_RANGE,
    STATE_AUTO_OUT_OF_RANGE,
    STATE_ON,
    STATE_ERROR
};

String states_s [] = {
    "STATE_INITIAL",
    "STATE_OFF", 
    "STATE_AUTO_WITHIN_RANGE",
    "STATE_AUTO_OUT_OF_RANGE", 
    "STATE_ON",
    "STATE_ERROR"
};

// ------------------------------------------------
// Eventos posibles del embebido
// ------------------------------------------------
enum events_e {
    EVENT_CONTINUE,
    EVENT_OFF_SELECTED,
    EVENT_AUTO_SELECTED_WITHIN_RANGE,
    EVENT_AUTO_SELECTED_OUT_OF_RANGE,
    EVENT_ON_SELECTED,
    EVENT_WATER_LEVEL_CHANGED,
    EVENT_TIME_LIMIT_REACHED,
    EVENT_BT_COMMAND_RECEIVED,
    EVENT_UNKNOWN
};

String events_s [] = {
    "EVENT_CONTINUE",
    "EVENT_OFF_SELECTED",
    "EVENT_AUTO_SELECTED_WITHIN_RANGE",
    "EVENT_AUTO_SELECTED_OUT_OF_RANGE",
    "EVENT_ON_SELECTED",
    "EVENT_WATER_LEVEL_CHANGED",
    "EVENT_TIME_LIMIT_REACHED",
    "EVENT_BT_COMMAND_RECEIVED",
    "EVENT_UNKNOWN"
};

// ------------------------------------------------
// Estados del potenciómetro
// ------------------------------------------------
enum potentiometer_state_e
{
    POTENTIOMETER_STATE_INIT,
    POTENTIOMETER_STATE_AUTO,
    POTENTIOMETER_STATE_OFF,
    POTENTIOMETER_STATE_ON
};

// ------------------------------------------------
// Estados del tanque
// ------------------------------------------------
enum tank_state_e
{
    TANK_INIT,
    TANK_EMPTY,
    TANK_QUARTER,
    TANK_HALF,
    TANK_THREE_QUARTER,
    TANK_FULL
};

// ------------------------------------------------
// Estructura de sensores
// ------------------------------------------------
typedef struct
{
    int pin;
    bool is_submerged;
} cable_t;

typedef struct
{
    int pin;
    potentiometer_state_e current_state;
    potentiometer_state_e previous_state;
} potentiometer_t;


// ------------------------------------------------
// Estructura de actuadores
// ------------------------------------------------
typedef struct
{
    int pin_red;
    int pin_green;
    int pin_blue;
    int color;
} rgb_led_t;

typedef struct
{
    int pin;
    bool is_on;
} relay_t;

// ------------------------------------------------
// Variables globales
// ------------------------------------------------
states_e current_state;
events_e new_event;
tank_state_e current_tank_state;
tank_state_e previous_tank_state;

cable_t quarter_cable;
cable_t half_cable;
cable_t three_quarter_cable;
cable_t full_cable;
potentiometer_t potentiometer;
rgb_led_t rgb_led;
relay_t relay;
boolean previous_time_range_check;
boolean current_time_range_check;
/*DateTime current_date;*/int current_hour;

unsigned long previous_time;
unsigned long current_time;

String bt_command;

SoftwareSerial BTSerial(PIN_D_RX, PIN_D_TX);

// ------------------------------------------------
// Logs
// ------------------------------------------------
void log(const String state, const String event)
{
#ifdef LOG
    Serial.println("------------------------------------------------");
    Serial.println(state);
    Serial.println(event);
    Serial.println("------------------------------------------------");
#endif
}

void log(const char *msg)
{
#ifdef LOG
    Serial.println(msg);
#endif
}

void log(int val)
{
#ifdef LOG
    Serial.println(val);
#endif
}

// ------------------------------------------------
// Logica de sensores y reloj
// ------------------------------------------------

// Reviso si cambió de posición el potenciómetro
bool potentiometer_changed() {
    get_current_potentiometer_state();

    if (potentiometer.current_state != potentiometer.previous_state) {
        potentiometer.previous_state = potentiometer.current_state;
        switch (potentiometer.current_state) {
            case POTENTIOMETER_STATE_AUTO:
                update_current_time_range_check();
                if(current_time_range_check) {
                    new_event = EVENT_AUTO_SELECTED_WITHIN_RANGE;
                } else {
                    new_event = EVENT_AUTO_SELECTED_OUT_OF_RANGE;
                }
                break;
            case POTENTIOMETER_STATE_OFF:
                new_event = EVENT_OFF_SELECTED;
                break;
            case POTENTIOMETER_STATE_ON:
                new_event = EVENT_ON_SELECTED;
                break;
        }
        return true;
    }
    return false;
}

// Calculo el estado del potenciómetro según su posición
void get_current_potentiometer_state() {
    int current_potentiometer_value = analogRead(potentiometer.pin);
    if (current_potentiometer_value < MAX_VALUE_POTENTIOMETER_AUTO) {
        potentiometer.current_state = POTENTIOMETER_STATE_AUTO;
    }
    else if (current_potentiometer_value > MAX_VALUE_POTENTIOMETER_OFF) {
        potentiometer.current_state = POTENTIOMETER_STATE_ON;
    }
    else {
        potentiometer.current_state = POTENTIOMETER_STATE_OFF;
    }
}

// Reviso si hay que cambiar el estado del relay por un cambio de rango horario según el nivel de agua
bool time_limit_reached() {
    if (changed_time_range()) {
        new_event = EVENT_TIME_LIMIT_REACHED;
        return true;
    }
    return false;
}

// Indica si se pasó de un rango horario a otro
bool changed_time_range() {
    update_current_time_range_check();
    if (previous_time_range_check != current_time_range_check) {
        previous_time_range_check = current_time_range_check;
        return true;
    }
    return false;
}

// Según el horario, actualiza el booleano que indica si se está dentro el rango horario donde se puede encender la bomba.
void update_current_time_range_check() {
    if(Serial.available()){
        /*current_date = read_rtc();*/current_hour = Serial.read() - '0';
    }
    if (/*current_date.hour*/current_hour >= DATE_TIME_HOUR_MIN && /*current_date.hour*/current_hour <= DATE_TIME_HOUR_MAX) {
        current_time_range_check = true;
    }
    else {
        current_time_range_check = false;
    }
}

// Reviso si cambió el nivel de agua en el tanque
bool water_level_changed() {
    get_current_tank_state();

    if (previous_tank_state != current_tank_state) {
        previous_tank_state = current_tank_state;
        new_event = EVENT_WATER_LEVEL_CHANGED;
        return true;
    }
    return false;
}

// Calculo el estado del tanque según sus cables
void get_current_tank_state() {
    quarter_cable.is_submerged = digitalRead(quarter_cable.pin);
    half_cable.is_submerged = digitalRead(half_cable.pin);
    three_quarter_cable.is_submerged = digitalRead(three_quarter_cable.pin);
    full_cable.is_submerged = digitalRead(full_cable.pin);

    if (full_cable.is_submerged) {
        current_tank_state = TANK_FULL;
    }
    else if (three_quarter_cable.is_submerged) {
        current_tank_state = TANK_THREE_QUARTER;
    }
    else if (half_cable.is_submerged) {
        current_tank_state = TANK_HALF;
    }
    else if (quarter_cable.is_submerged) {
        current_tank_state = TANK_QUARTER;
    }
    else {
        current_tank_state = TANK_EMPTY;
    }
}

// Reviso si llegó un comando por Bluetooth y le saco los caracteres de separación
bool bt_command_received() {
  if (BTSerial.available()) {
      bt_command = BTSerial.readString();
      bt_command.trim(); 
      new_event = EVENT_BT_COMMAND_RECEIVED;
      return true;
  }
  return false;
}

// ------------------------------------------------
// Manejo de eventos
// ------------------------------------------------
// Inicialización de variables para primer pasada por la máquina de estados

void initialize() {
    potentiometer.previous_state = POTENTIOMETER_STATE_INIT;
    previous_tank_state = TANK_INIT;
    current_state = STATE_OFF;
    previous_time_range_check = false;
    current_time_range_check = false;
}

// Modo "Auto" seleccionado en el potenciómetro dentro del rango horario aceptable para el funcionamiento de la bomba
void handle_event_auto_selected_within_range() {
    current_state = STATE_AUTO_WITHIN_RANGE;
    update_relay_based_on_tank_state();
}

// Modo "Auto" seleccionado en el potenciómetro fuera del rango horario aceptable para el funcionamiento de la bomba
void handle_event_auto_selected_out_of_range() {
    current_state = STATE_AUTO_OUT_OF_RANGE;
    update_relay(false);
}

// Modo "Off" seleccionado en el potenciómetro
void handle_event_off_selected() {
    update_relay(false);
    current_state = STATE_OFF;
}

// Modo "On" seleccionado en el potenciómetro
void handle_event_on_selected() {
    if (!full_cable.is_submerged) {
        update_relay(true);
    }
    current_state = STATE_ON;
}

// Cambio de rango horario estando en el rango aceptable -> pasa a rango horario no aceptable
void handle_time_limit_reached_within_range() {
    current_state = STATE_AUTO_OUT_OF_RANGE;
    update_relay(false);
}

// Cambio de rango horario estando en el rango no aceptable -> pasa a rango horario aceptable
void handle_time_limit_reached_out_of_range() {
    current_state = STATE_AUTO_WITHIN_RANGE;
    update_relay_based_on_tank_state();
}

// Cambio de nivel de agua detectado en el tanque en el estado STATE_AUTO_WITHIN_RANGE
void handle_water_level_changed_auto_within_range() {
    update_rgb_led();
    send_water_level_through_bluetooth();
    update_relay_based_on_tank_state();
}

// Cambio de nivel de agua detectado en el tanque en el estado STATE_AUTO_OUT_OF_RANGE
void handle_water_level_changed_auto_out_of_range() {
    update_rgb_led();
    send_water_level_through_bluetooth();
}

// Cambio de nivel de agua detectado en el tanque en el estado STATE_OFF
void handle_water_level_changed_off() {
    update_rgb_led();
    send_water_level_through_bluetooth();
}

// Cambio de nivel de agua detectado en el tanque en el estado STATE_ON
void handle_water_level_changed_on() {
    update_rgb_led();
    send_water_level_through_bluetooth();
    if(current_tank_state == TANK_FULL) {
        update_relay(false);
    }
}

// Actúo sobre el embebido en base al comando recibido por Bluetooth
void handle_bt_command_received() {
    if (bt_command == String(BLUETOOTH_WATER_LEVEL_COMMAND)) {
        send_water_level_through_bluetooth();
    }
}

void none() {
}

void error() {
    current_state = STATE_ERROR;
    log("ERROR");
    log(current_state);
}

// ------------------------------------------------
// Logica de actuadores
// ------------------------------------------------

// Enciende o apaga la bomba de agua según el valor recibido
void update_relay(bool turn_on) {
    if (turn_on) {
        digitalWrite(relay.pin, HIGH);
        relay.is_on = true;
    }
    else {
        digitalWrite(relay.pin, LOW);
        relay.is_on = false;
    }
}

// Enciende o apaga la bomba de acuerdo al nivel de agua
void update_relay_based_on_tank_state() {
    switch(current_tank_state) {
        case TANK_EMPTY:
            update_relay(true);
            break;
        case TANK_FULL:
            update_relay(false);
            break;
    }
}

// Cambia color del led RGB
void update_rgb_led() {
    switch(current_tank_state) {
        // Color Rojo
        case TANK_EMPTY:
            analogWrite(rgb_led.pin_red, COLOR_MAX_AMOUNT);
            analogWrite(rgb_led.pin_green, COLOR_MIN_AMOUNT);
            analogWrite(rgb_led.pin_blue, COLOR_MIN_AMOUNT);
            break;
        // Color Naranja
        case TANK_QUARTER:
            analogWrite(rgb_led.pin_red, COLOR_MAX_AMOUNT);
            analogWrite(rgb_led.pin_green, COLOR_GREEN_AMOUNT_FOR_ORANGE);
            analogWrite(rgb_led.pin_blue, COLOR_MIN_AMOUNT);
            break;
        // Color Amarillo
        case TANK_HALF:
            analogWrite(rgb_led.pin_red, COLOR_MAX_AMOUNT);
            analogWrite(rgb_led.pin_green, COLOR_MAX_AMOUNT);
            analogWrite(rgb_led.pin_blue, COLOR_MIN_AMOUNT);
            break;
        // Color Verde
        case TANK_THREE_QUARTER:
            analogWrite(rgb_led.pin_red, COLOR_MIN_AMOUNT);
            analogWrite(rgb_led.pin_green, COLOR_MAX_AMOUNT);
            analogWrite(rgb_led.pin_blue, COLOR_MIN_AMOUNT);
            break;
        // Color Azul
        case TANK_FULL:
            analogWrite(rgb_led.pin_red, COLOR_MIN_AMOUNT);
            analogWrite(rgb_led.pin_green, COLOR_MIN_AMOUNT);
            analogWrite(rgb_led.pin_blue, COLOR_MAX_AMOUNT);
            break;
    }
}

// Envía el nivel de agua a través del Bluetooth
void send_water_level_through_bluetooth() {
    BTSerial.println(current_tank_state);
}

// ------------------------------------------------
// Captura de eventos
// ------------------------------------------------
void get_new_event()
{
    current_time = millis();
    unsigned long time_diff = current_time - previous_time;
    if (time_diff >= TMP_EVENTS_MILLIS) {
        previous_time = current_time;

        if (potentiometer_changed() || time_limit_reached() || water_level_changed() || bt_command_received()) {
            return;
        }
    }
    new_event = EVENT_CONTINUE;
}

// ------------------------------------------------
// Inicialización
// ------------------------------------------------
void start()
{
    Serial.begin(9600);

    BTSerial.begin(9600);
    pinMode(LED_BUILTIN, OUTPUT);

    // Asigno los pines a los sensores correspondientes
    potentiometer.pin = PIN_A_POTENTIOMETER;
    pinMode(potentiometer.pin, INPUT);

    quarter_cable.pin = PIN_D_QUARTER_CABLE;
    half_cable.pin = PIN_D_HALF_CABLE;
    three_quarter_cable.pin = PIN_D_THREE_QUARTER_CABLE;
    full_cable.pin = PIN_D_FULL_CABLE;

    pinMode(quarter_cable.pin, INPUT);
    pinMode(half_cable.pin, INPUT);
    pinMode(three_quarter_cable.pin, INPUT);
    pinMode(full_cable.pin, INPUT);

    // Asigno los pines al actuador led indicador de carga
    rgb_led.pin_red = PIN_P_RGB_LED_RED;
    rgb_led.pin_green = PIN_P_RGB_LED_GREEN;
    rgb_led.pin_blue = PIN_P_RGB_LED_BLUE;
    pinMode(rgb_led.pin_red, OUTPUT);
    pinMode(rgb_led.pin_green, OUTPUT);
    pinMode(rgb_led.pin_blue, OUTPUT);

    // Asigno los pines al actuador relay y lo inicializo
    relay.pin = PIN_D_RELAY;
    pinMode(relay.pin, OUTPUT);
    relay.is_on = false;

    // Inicializo el estado del embebido
    current_state = STATE_INITIAL;

    // Inicializo el nivel de agua del tanque
    previous_tank_state = TANK_EMPTY;

    // Inicializo el temporizador
    previous_time = millis();
}

// ------------------------------------------------
// Matriz de eventos
// ------------------------------------------------
typedef void (*transition)();

transition state_table[MAX_STATES][MAX_EVENTS] =
{
      {initialize       , none                      , none                                      , none                                      , none                      , none                                          , none                                    , none                        } , // STATE_INITIAL
      {none             , handle_event_off_selected , handle_event_auto_selected_within_range   , handle_event_auto_selected_out_of_range   , handle_event_on_selected  , handle_water_level_changed_off                , none                                    , handle_bt_command_received  } , // STATE_OFF
      {none             , handle_event_off_selected , handle_event_auto_selected_within_range   , handle_event_auto_selected_out_of_range   , handle_event_on_selected  , handle_water_level_changed_auto_within_range  , handle_time_limit_reached_within_range  , handle_bt_command_received  } , // STATE_AUTO_WITHIN_RANGE
      {none             , handle_event_off_selected , handle_event_auto_selected_within_range   , handle_event_auto_selected_out_of_range   , handle_event_on_selected  , handle_water_level_changed_auto_out_of_range  , handle_time_limit_reached_out_of_range  , handle_bt_command_received  } , // STATE_AUTO_OUT_OF_RANGE
      {none             , handle_event_off_selected , handle_event_auto_selected_within_range   , handle_event_auto_selected_out_of_range   , handle_event_on_selected  , handle_water_level_changed_on                 , none                                    , handle_bt_command_received  } , // STATE_ON
      {error            , error                     , error                                     , error                                     , error                     , error                                         , error                                   , error                       } , // STATE_ERROR

     //EVENT_CONTINUE   , EVENT_OFF_SELECTED        , EVENT_AUTO_SELECTED_WITHIN_RANGE          , EVENT_AUTO_SELECTED_OUT_OF_RANGE          , EVENT_ON_SELECTED         , EVENT_WATER_LEVEL_CHANGED                     , EVENT_TIME_LIMIT_REACHED                , EVENT_BT_COMMAND_RECEIVED
};

// ------------------------------------------------
// Implementación maquina de estados
// ------------------------------------------------
void fsm()
{
    get_new_event();

    if( (new_event >= 0) && (new_event < MAX_EVENTS) && (current_state >= 0) && (current_state < MAX_STATES) )
  {
    if( new_event != EVENT_CONTINUE )
    {
        log(states_s[current_state], events_s[new_event]);
    }

    state_table[current_state][new_event]();
  }
  else
  {
    log(states_s[STATE_ERROR], events_s[EVENT_UNKNOWN]);
  }

  // Consumo el evento...
  new_event   = EVENT_CONTINUE;
}

// ------------------------------------------------
// Arduino setup
// ------------------------------------------------
void setup()
{
    start();
}

// ------------------------------------------------
// Arduino loop
// ------------------------------------------------
void loop()
{
    fsm();
}
