# Led-NodeMCU
An IOT + Android project to operate electronic appliences like LEDs remotely through the internet using NodeMCU ESP-12E microcontroller and Android app.

### How it works :
The NodeMCU microcontroller acts as a http server connected to the internet through WiFi. The Andoid app acts as a http client. When the user performs an operation the app sends an http request to the server with the updated values and the microcontroller server performs tasks accordingly. if any http request fails then the app shows disconnected screen

### Libraries used :
* OkHttp (Android)
* ESP8266WiFi (Arduino)

### Video Demonstration : <a href="https://youtu.be/qOJX1L13l5U"/><img src="https://www.vectorlogo.zone/logos/youtube/youtube-icon.svg" alt="Youtube" height="16" width="16"/> https://youtu.be/qOJX1L13l5U</a>
