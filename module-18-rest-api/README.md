# Module 18 - REST API Design

Orders API with proper HTTP semantics: resource URLs, status codes, pagination, validation, and RFC 7807 errors.

## Endpoints
| Method | Path | Result |
|---|---|---|
| POST | `/api/v1/orders` | 201 Created + `Location` header |
| GET | `/api/v1/orders/{id}` | 200 or 404 |
| GET | `/api/v1/orders?page=0&size=20` | 200, paginated list |
| DELETE | `/api/v1/orders/{id}` | 204, or 409 if already shipped |

## Concepts
- **Resource naming:** nouns, plural, no verbs in paths.
- **Status codes:** 201 for create, 204 for delete, 400 for validation, 404 for missing, 409 for state conflicts.
- **Idempotency:** GET, PUT, and DELETE are idempotent. POST is not.
- **Errors:** `ProblemDetail` (RFC 7807) gives a consistent error body.

## Interview questions
1. **PUT vs PATCH?** PUT replaces the whole resource. PATCH changes part of it.
2. **Why return 201 with `Location`?** The client learns where the new resource lives.
3. **Offset vs cursor pagination?** Offset is simple but slows and skips items when data changes. Cursor is stable for large feeds.
4. **How do you version an API?** URL (`/v1`), header, or media type. URL versioning is the most visible.
5. **Why not return entities directly?** It leaks internal fields and couples clients to the schema. Use DTOs.

## Run
```bash
mvn test
mvn spring-boot:run
```
