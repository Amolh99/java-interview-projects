# Module 13 - Spring Boot & Spring Framework

Order pricing service showing core Spring concepts: dependency injection, profiles, typed configuration, bean lifecycle, and a slice test.

## Concepts
- **IoC / DI:** constructor injection. Spring builds the graph and wires the beans.
- **Profiles:** `@Profile("prod")` selects a bean per environment.
- **Typed config:** `@ConfigurationProperties` binds `pricing.*` to a record and validates it.
- **Auto-configuration:** starters add beans based on the classpath.
- **Bean scopes:** singleton by default. Prototype creates a new bean per request.

## Interview questions
1. **What is the difference between `@Component`, `@Service`, `@Repository`?** They are all stereotypes. `@Repository` also translates persistence exceptions.
2. **Field vs constructor injection?** Constructor injection makes dependencies final and testable without Spring.
3. **How does Spring Boot auto-configuration decide?** Conditional annotations such as `@ConditionalOnClass` and `@ConditionalOnMissingBean`.
4. **What is `@SpringBootTest` vs `@WebMvcTest`?** The first loads the whole context. The second loads only the web layer.
5. **Singleton scope thread safety?** Singletons are shared, so they must be stateless or thread-safe.
6. **What is a circular dependency?** A depends on B and B depends on A. Constructor injection fails fast, which exposes it.

## Run
```bash
mvn test
mvn spring-boot:run
```
