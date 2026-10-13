# Module 17 - JPA & Hibernate

Order persistence with Spring Data JPA. An `Order` owns its `OrderLine`s, and writes run inside transactions.

## Concepts
- **Entities:** `@Entity`, `@Id`, `@GeneratedValue`. Each entity maps to a table.
- **Relationships:** `@OneToMany` with `cascade = ALL` and `orphanRemoval = true` keeps lines in step with their order.
- **Repository:** `JpaRepository` provides CRUD and derived queries.
- **Transactions:** `@Transactional` on the service. All-or-nothing writes.
- **Fetch types:** `LAZY` by default for collections. Loading them outside a transaction fails with `LazyInitializationException`.

## Interview questions
1. **What is the N+1 problem?** One query loads parents, then one query per parent loads children. Fix with `JOIN FETCH` or `@EntityGraph`.
2. **`save` vs `persist`?** `save` in Spring Data merges if the entity has an id. `persist` in JPA makes a new managed entity.
3. **First-level vs second-level cache?** The first is per persistence context (session). The second is shared across sessions and must be enabled.
4. **Why `equals`/`hashCode` on entities?** Use the business key or id carefully. A generated id is null before persist.
5. **`orphanRemoval` vs `CascadeType.REMOVE`?** Orphan removal also deletes children that are removed from the collection.
6. **Why is `@Transactional` on the service, not the repository?** Business operations can span several repository calls, and they must commit together.

## Run
```bash
mvn test
```
