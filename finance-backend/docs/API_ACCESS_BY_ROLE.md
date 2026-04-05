# API access by role

This document matches **`SecurityConfiguration`** in the codebase. If behavior and this file disagree, trust the Java config.

**Authentication:** HTTP Basic (Swagger **Authorize**: username = **email**, password = account password).  
**Without valid credentials:** **401** (not logged in).  
**Logged in but role not allowed:** **403**.

---

## Roles

| Role | Typical use |
|------|-------------|
| **VIEWER** | Read dashboard summaries only |
| **ANALYST** | Dashboard + read financial records (no create/update/delete records, no user admin) |
| **ADMIN** | Full access: users + records CRUD + dashboard |

**Inactive** users (`status` ≠ `active`, case-insensitive) cannot authenticate.

---

## Public (no login)

| Method | Path | Notes |
|--------|------|--------|
| * | `/` | Redirects to Swagger UI |
| * | `/swagger-ui`, `/swagger-ui.html`, `/swagger-ui/**` | Swagger UI assets |
| * | `/v3/api-docs`, `/v3/api-docs/**` | OpenAPI JSON |

---

## Business APIs — who can call what

Legend: **Yes** = allowed · **No** = **403** (if authenticated) · **—** = not applicable  

Assume the user is **authenticated** when reading **Yes** / **No**.

### Dashboard (`/dashboard`)

| Method | Path | VIEWER | ANALYST | ADMIN |
|--------|------|:------:|:-------:|:-----:|
| GET | `/dashboard/summary` | Yes | Yes | Yes |
| GET | `/dashboard/category` | Yes | Yes | Yes |
| GET | `/dashboard/recent` | Yes | Yes | Yes |

### Financial records (`/records`)

| Method | Path | VIEWER | ANALYST | ADMIN |
|--------|------|:------:|:-------:|:-----:|
| GET | `/records` (list / filters) | No | Yes | Yes |
| GET | `/records/summary?type=monthly` or `weekly` | No | Yes | Yes |
| GET | `/records/{id}` | No | Yes | Yes |
| POST | `/records` | No | No | Yes |
| PUT | `/records/{id}` | No | No | Yes |
| DELETE | `/records/{id}` | No | No | Yes |

### Users (`/users`)

| Method | Path | VIEWER | ANALYST | ADMIN |
|--------|------|:------:|:-------:|:-----:|
| GET | `/users` | No | No | Yes |
| GET | `/users/{id}` | No | No | Yes |
| POST | `/users` | No | No | Yes |
| PUT | `/users/{id}` | No | No | Yes |
| DELETE | `/users/{id}` | No | No | Yes |

---

## Seeded demo accounts (`mysql_seed.sql`)

| Role | Email | Password (seed) |
|------|-------|-------------------|
| ADMIN | `admin@finance.local` | `password` |
| ANALYST | `analyst@finance.local` | `password` |
| VIEWER | `viewer@finance.local` | `password` |

---

## Quick summary

| Role | Dashboard | Records (read) | Records (write) | Users |
|------|-----------|------------------|-----------------|-------|
| VIEWER | Yes | No | No | No |
| ANALYST | Yes | Yes | No | No |
| ADMIN | Yes | Yes | Yes | Yes |

---

## Source of truth

```text
src/main/java/com/junaid/finance_backend/config/SecurityConfiguration.java
```

Role name constants:

```text
src/main/java/com/junaid/finance_backend/security/ApiRolePolicy.java
```
