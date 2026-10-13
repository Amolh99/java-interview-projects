# Module 25 - Spring Security

Securing the order API. HTTP Basic for the demo, role-based rules on URLs, and method security with `@PreAuthorize` so that customers see only their own orders.

## Concepts
- **Authentication vs authorisation:** who you are vs what you may do.
- **SecurityFilterChain:** a bean that defines URL rules. Lambda DSL is the current style.
- **Password storage:** `BCryptPasswordEncoder`. Never store plain text.
- **Method security:** `@EnableMethodSecurity` plus `@PreAuthorize("#customerId == authentication.name")`.
- **CSRF:** disable only for stateless token APIs. Keep it on for browser sessions.

## Interview questions
1. **Authentication vs authorisation?** Authentication proves identity. Authorisation checks permissions.
2. **How does a JWT differ from a session?** A JWT is self-contained and stateless. A session is stored server-side and can be revoked immediately.
3. **Why BCrypt?** It is slow and salted, which resists brute force.
4. **What is CSRF?** A malicious site makes the browser send an authenticated request. Tokens or SameSite cookies stop it.
5. **Where should authorisation checks live?** At the URL layer for coarse rules, and at the method layer for object-level rules.

## Run
```bash
mvn test
```
