# TaskManagerRest

A REST API for managing a to-do list, built with Spring Boot 4, Spring Data JPA and an in-memory H2 database.

I started this project by following Devtiro's Task App course, then fixed a bug I found in the original, added transaction handling to the service layer and wrote unit tests. This repository only contains the backend. The UI is a prebuilt image provided with the course; see [Credits](#credits).

## Stack

Java 25, Spring Boot 4.1, Spring Web MVC, Spring Data JPA (Hibernate), Bean Validation, H2, JUnit 5, Mockito, Maven.

## API

All endpoints are under `/api/v1/tasks`.

| Method | Path | Body | Response |
| --- | --- | --- | --- |
| `POST` | `/api/v1/tasks` | `title`, `description`, `dueDate`, `priority` | `201` with the created task |
| `GET` | `/api/v1/tasks` | | `200` with all tasks, oldest first |
| `PUT` | `/api/v1/tasks/{id}` | `title`, `description`, `dueDate`, `priority`, `status` | `200` with the updated task |
| `DELETE` | `/api/v1/tasks/{id}` | | `204` |

`priority` is one of `HIGH`, `MEDIUM` or `LOW`, and `status` is `OPEN` or `COMPLETE`. New tasks always start as `OPEN`. Errors come back as `{"error": "..."}` with a `400` status.

Example:

```bash
curl -X POST http://localhost:8080/api/v1/tasks \
  -H "Content-Type: application/json" \
  -d '{"title": "Read about JPA", "dueDate": "2026-12-01", "priority": "MEDIUM"}'
```

## How it's organised

The code is split into the usual three layers, each with its own types:

- `controller`: HTTP endpoints and a global exception handler. Request and response bodies are DTOs (`domain/dto`), validated with Bean Validation.
- `mapper`: converts between DTOs and the types the service layer uses.
- `service`: business rules. It takes plain request records (`CreateTaskRequest`, `UpdateTaskRequest`) and knows nothing about HTTP.
- `repository` and `domain/entity`: the `Task` JPA entity and its Spring Data repository.

The JPA entity never leaves the backend. The API only ever returns `TaskDto`.

## Changes from the original course project

- **Overdue tasks could not be completed.** The update endpoint rejected any due date in the past. Because the frontend sends the whole task on every update, a task became impossible to complete or edit once its due date had passed. I removed that check from the update DTO and moved it to the service layer, where it only applies when the due date actually changes.
- **Transactions.** Creating, listing and updating tasks now run inside transactions (`@Transactional`). Before, the read and the write in an update happened in two separate transactions.
- **Tests.** I added unit tests for the service layer, using Mockito instead of a database.

## Known issues

- The frontend sends due dates as UTC timestamps instead of plain dates. In timezones ahead of UTC (Spain, for example), a date picked in the UI can be stored as the day before.
- Updating a task that doesn't exist returns `400` rather than the more conventional `404`.
- Data lives in memory and is lost when the app stops. There is no authentication.

## Running it

You need JDK 25. Docker is only needed for the UI.

```bash
./mvnw spring-boot:run
```

The API starts on port 8080. To use the UI, run this in a second terminal:

```bash
docker compose up -d
```

This pulls Devtiro's prebuilt UI image and serves it at http://localhost:3000, forwarding API calls to the backend on port 8080. You can also skip the UI and call the API directly with curl or Postman.

To run the tests:

```bash
./mvnw test
```

## Credits

The original project design and course are by Devtiro, and the course code is MIT licensed. The UI used by `docker-compose.yml` is Devtiro's image (`ghcr.io/devtiro/devtiro-build-task-app`) and is not part of this repository.
