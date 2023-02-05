# Protobuf Message Templates

```proto2
subject: {
  id: ${agentId}
}
predicate: PREDICATE.KNOWS
object: {
  name: ${fullname}
}
```

```java
Expression expr = Protobuf.format(
  "subject: {                 " +
  "  id: %d                   " +
  "}                          " +
  "predicate: PREDICATE.KNOWS " +
  "object: {                  " +
  "  name: %s                 " +
  "}                          ",
  006, "James Bond");
```

Becomes:

```java
Expression expr =
  Expression.newBuilder()
    .setSubject(Thing.newBuilder().setId(006))
    .setPredicate(Predicate.KNOWS)
    .setObject(Thing.newBuilder().setName("James Bond"))
  .build();
```

