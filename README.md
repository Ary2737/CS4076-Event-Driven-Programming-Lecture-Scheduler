# CS4076 Event-Driven-Programming: Lecture Scheduler
This repository contains my project for the CS4076 (Event-Driven Programming) module. It is a TCP Client-Server Lecture Scheduler application built using Java and JavaFX. 

This project is developed using the **MVC (Model-View-Controller)** design architechture. This application is split into 2 main applications...

* **24436143_Server**: This is my console server application where a TCP connection between the client and server applications is created and the client's requests are handled.

* **24436143_Client:**: This is a graphical user interface (GUI) application built with JavaFX and SceneBuilder. It connects to the server application and provides a GUI to the user to preform actions such as...
* Adding a lecture
* Removing a lecture
* Displaying scheduled lectures
* Stopping the TCP connection ('Other' button)

This project is divided into 2 phases...
* **Phase 1**: This phase of the project consists of the initial development of the server and client applications. Here the core networking and scheduling logic is implemented via the use of models and controllers. The GUI/console interfaces are also developed for the user.
* **Phase 2**: This phase of the project is an extension to Phase 1. Here the application will need to support multithreading/multiprocessing to handle a higher load of clients at any one time. 


  ## Technologies/Skills used
  * **Java**: This forms the core logic for the models/controllers of both applications. OOP in particular was used for the models of both     applications
  * **JavaFX + SceneBuilder**: This creates the GUI for the client application via the use of .fxml files.
  * **TCP Network Programming**: This is done via the use of Java's Socket/ServerSocket classes.
  * **I/O Streams**: This is done to read input + write output to the client/server. This was done via Java's InputStreamReader,
  *  PrintWriter and BufferedReader classes.
  *  **Exception Handling**: This was doen via the extensive use of try-catch blocks in both applications. I handle exceptions such as IOException and I create my own custom exception for unexpected actions that may occur in the TCP connection.
## How to run the project 

### Prerequisites 
* Java Development Kit (JDK) 21+
* JavaFX SDK: 21.0.6
* An IDE. E.g. IntelliJ or VSCode

### Steps to start application
1. **Start the Server:** Navigate to the '24436143_Server' appllication and run the 'createServerConnection' class in the ServerController package.
2. **Start the Client:** Once the server is running, navigate to the '24436143_Client' application and run the 'Launcher' class.

*Note: This project is currently still in development. The first phase of this project is done and now I will have to develop the second phase.*

