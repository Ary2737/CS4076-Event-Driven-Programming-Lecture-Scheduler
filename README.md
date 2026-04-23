# CS4076 Event-Driven-Programming: Lecture Scheduler

This repository contains my project for the CS4076 (Event-Driven Programming) module. It is a TCP Client-Server Lecture Scheduler application built using Java and JavaFX.

This project is developed using the **MVC (Model-View-Controller)** design architechture. This application is split into 2 main applications...

- **24436143_Server**: This is my console server application where a TCP connection between the client and server applications is created and the client's requests are handled.

- **24436143_Client:**: This is a graphical user interface (GUI) application built with JavaFX and SceneBuilder. It connects to the server application and provides a GUI to the user to preform actions such as...
- Adding a lecture
- Removing a lecture
- Displaying scheduled lectures on a weekly timetable grid
- Requesting an 'Early Lectures' shift (Phase 2) — all scheduled lectures are compacted toward the earliest available morning slots
- Stopping the TCP connection ('Other' button)

This project is divided into 2 phases...

- **Phase 1**: This phase of the project consists of the initial development of the server and client applications. Here the core networking and scheduling logic is implemented via the use of models and controllers. The GUI/console interfaces are also developed for the user.

- **Phase 2**: This phase of the project is an extension to Phase 1. The server now supports multiple clients concurrently — a new Thread is spawned per client connection, and access to the shared schedule is controlled with `synchronized` methods so concurrent ADD/REMOVE requests can't corrupt the data. A new **'Early Lectures'** feature was added: when requested, the server uses a **Fork-Join** divide-and-conquer algorithm (one `RecursiveAction` subtask per weekday) to shift lectures toward the earliest free morning slots in parallel. The Fork-Join invocation is wrapped in a `javafx.concurrent.Task` so it is decoupled from the request-handling pipeline, leaving other clients free to run ADD/REMOVE/DISPLAY while the shift executes. The client's DISPLAY screen renders the schedule on a proper weekly grid (styled after the UL timetable) and fetches the data on a background `Task` so the JavaFX Application Thread stays responsive.

  ## Technologies/Skills used
  - **Java**: This forms the core logic for the models/controllers of both applications. OOP in particular was used for the models of both applications
  - **JavaFX + SceneBuilder**: This creates the GUI for the client application via the use of .fxml files.
  - **TCP Network Programming**: This is done via the use of Java's Socket/ServerSocket classes.
  - **I/O Streams**: This is done to read input + write output to the client/server. This was done via Java's InputStreamReader,
  - PrintWriter and BufferedReader classes.
  - **Exception Handling**: This was doen via the extensive use of try-catch blocks in both applications. I handle exceptions such as IOException and I create my own custom exception for unexpected actions that may occur in the TCP connection.
  - **Multi-threading (Phase 2)**: The server spawns a new `Thread` per client connection so many clients can be served at once. Shared state (the lecture `HashMap`) is protected with `synchronized` methods to avoid race conditions.
  - **Fork-Join / Divide and Conquer (Phase 2)**: The 'Early Lectures' feature uses `java.util.concurrent.ForkJoinPool` and a `RecursiveAction` that forks one subtask per weekday, so Monday–Friday are compacted in parallel.
  - **javafx.concurrent (Phase 2)**: `javafx.concurrent.Task` is used on the server to wrap the 'Early Lectures' work so it runs on its own worker thread, and on the client to fetch display/early-lectures data off the JavaFX Application Thread.

## How to run the project

### Prerequisites

- Java Development Kit (JDK) 21+
- JavaFX SDK: 21.0.6 (the server also depends on `javafx-base` and `javafx-graphics` at runtime because the 'Early Lectures' feature uses `javafx.concurrent.Task`)
- An IDE. E.g. IntelliJ or VSCode

### Steps to start application

1. **Start the Server:** Navigate to the '24436143_Server' appllication and run the 'createServerConnection' class in the ServerController package.
2. **Start the Client:** Once the server is running, navigate to the '24436143_Client' application and run the 'Launcher' class.
3. **(Optional) Test multi-client support:** Start a second instance of the Client — the server will accept both connections on separate threads, and changes from one client are visible to the other after a DISPLAY refresh.
