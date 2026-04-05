# Finance backend (assignment)

Spring Boot 3 + MySQL REST API for **users**, financial **records** (income/expense), and **dashboard** summaries. **HTTP Basic** authentication with **role-based access**. **Swagger UI** documents APIs and supports **Authorize** (email + password).

## Prerequisites

- Java 17
- Maven 3.9+
- MySQL 8+ with a database user that can create/use `finance_db`

## Configuration

Edit `src/main/resources/application.properties`:

- `spring.datasource.url`, `username`, `password` — point at your MySQL instance.

Default DB name: `finance_db`.

## Run

```bash
mvn spring-boot:run
```

- **Swagger UI:** http://localhost:8080/swagger-ui.html  
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs  
- Root `/` redirects to Swagger UI (no auth). After opening Swagger, use **Authorize** with a user’s **email** and **password**.

## Authentication and roles

Full per-endpoint matrix: **[docs/API_ACCESS_BY_ROLE.md](docs/API_ACCESS_BY_ROLE.md)**.

| Role | Dashboard (`/dashboard/**`) | Records read (`GET /records/**`) | Records write (`POST/PUT/DELETE /records`) | Users (`/users/**`) |
|------|----------------------------|----------------------------------|--------------------------------------------|---------------------|
| **VIEWER** | Yes | No (403) | No (403) | No (403) |
| **ANALYST** | Yes | Yes | No (403) | No (403) |
| **ADMIN** | Yes | Yes | Yes | Yes |

- **401** — missing or wrong Basic credentials (JSON body from `JsonHttpSecurityHandlers`).
- **403** — authenticated but role not allowed for that URL/method.
- **Inactive users** (`status` ≠ `active`, case-insensitive) cannot log in (`DisabledException`).

Passwords stored in the database must be **BCrypt** hashes (see `UserService` when creating/updating users via API).

## Database tables

| Table     | Purpose |
|----------|---------|
| `users`  | name, email, password (BCrypt), `role` (`VIEWER` / `ANALYST` / `ADMIN`), `status` (e.g. `active`). |
| `records`| `amount`, `type` (`INCOME` / `EXPENSE`), `category`, `date`, optional `note`. |

Canonical DDL: `src/main/resources/db/mysql_schema.sql`.

Dummy data: `src/main/resources/db/mysql_seed.sql` — seeded users use password **`password`** (see comment in that file for the BCrypt hash).

**Flow:** start MySQL → create DB if needed → run app with `ddl-auto=update` → run `mysql_seed.sql` after tables exist.

If you had an older version of this project:

- Drop leftover table `transaction` if it still exists (see `mysql_schema.sql`).
- If Hibernate logs errors about `records_ibfk_1` / `user_id`, run `src/main/resources/db/mysql_migrate_drop_legacy_records_fk.sql`.

## API overview

| Area | Base path | Notes |
|------|-----------|--------|
| Users | `GET/POST /users`, `GET/PUT/DELETE /users/{id}` | **ADMIN only.** Password is write-only in JSON responses. |
| Records | `GET/POST /records`, `GET/PUT/DELETE /records/{id}` | Read: **ANALYST** or **ADMIN**. Write: **ADMIN** only. Filters on `GET /records`: `type`, `category`, `from`, `to`. |
| Record trends | `GET /records/summary?type=monthly` or `weekly` | Same read roles as other record GETs. |
| Dashboard | `GET /dashboard/summary`, `/dashboard/category`, `/dashboard/recent` | **VIEWER**, **ANALYST**, **ADMIN**. |

## Code layout (access control)

- `security/ApiRolePolicy.java` — documents which role names mean what.
- `config/SecurityConfiguration.java` — single `SecurityFilterChain`: URL + HTTP method rules.
- `security/JsonHttpSecurityHandlers.java` — JSON **401** / **403** for APIs.
- `service/AccountUserDetailsService.java` — loads user by email, attaches Spring authorities, blocks non-active accounts.

## Assumptions

- **Validation:** Jakarta Validation on bodies; **400** with JSON `message` where applicable.
- **Not found:** **404** via `GlobalExceptionHandler`.
- One financial model: **`records`** only.

## Build

```bash
mvn clean verify
```

Tests use in-memory **H2** (`src/test/resources/application.properties`) and do not require MySQL.
