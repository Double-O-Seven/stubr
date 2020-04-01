[![Actions Status](https://github.com/Double-O-Seven/stubr/workflows/Java%20CI/badge.svg)](https://github.com/Double-O-Seven/stubr/actions)
[![Release Version](https://img.shields.io/maven-central/v/ch.leadrian.stubr/stubr-core.svg?label=release)](https://search.maven.org/search?q=g:ch.leadrian.stubr)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/ba08c6eccb7045c8a5fa6c9c90d3f5bf)](https://www.codacy.com/manual/Double-O-Seven/stubr?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Double-O-Seven/stubr&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/ba08c6eccb7045c8a5fa6c9c90d3f5bf)](https://www.codacy.com/manual/Double-O-Seven/stubr?utm_source=github.com&utm_medium=referral&utm_content=Double-O-Seven/stubr&utm_campaign=Badge_Coverage)

# stubr

Stubr is a small, extensible library for creating stubs for unit tests.

## Basics

The basic concept is very simple: You build a stubber, using different stubbing strategies for different classes and types and then you request a specific instance for your desired type.

For example:

```java
Stubber stubber = Stubber.builder()
        .stubWith(StubbingStrategies.constantValue("Foo"))
        .stubWith(StubbingStrategies.suppliedValue(int.class, (int sequenceNumber) -> sequenceNumber))
        .build();
String stringValue = stubber.stub(String.class); // Foo
int intValue1 = stubber.stub(int.class); // 0
int intValue2 = stubber.stub(int.class); // 1
```

## Concepts

### Stubbing strategies

A `Stubber` is composed using different `StubbingStrategy`s.
A `StubbingStrategy` may provide stub values for only a specific type or it may provide a generic solution to provide stubs for suitable types.

It consist of two basic methods, that depend on each other
 *  `boolean accepts(StubbingContext context, Type type)`
 *  `Object stub(StubbingContext context, Type type)`

`accept` determines whether a `StubbingStrategy` is applicable for the given type and context.
The `StubbingContext` consists of the calling `Stubber` as well as the `StubbingSite` which describes where the stub value was requested.

`stub` implements the provision of the stub value, which might be a fixed value, might depend on the type or context or might even be a random value.

Any implementation of `stub` may assume that the given context and type are accepted, so double-checking the input is not required.

A custom implementation may be used as follows:

```java
StubbingStrategy myAmazingStrategy = new MyAmazingStrategy();
Stubber stubber = Stubber.builder()
        .stubWith(myAmazingStrategy)
        .build();
```

Multiple strategies might accept the same types and therefore conflict each other.
The behaviour in this case is that the `Stubber` will prioritize the `StubbingStrategy` that was added the last during the build of the stubber `Stubber`.
The rule is therefore that the most generic strategies should be added first, while the most specific strategies should be added last.

For example:

```java
Stubber stubber = Stubber.builder()
        .stubWith(veryGenericStrategy) // Accepts int, float and Strings for example
        .stubWith(verySpecificStrategy) // Accepts only int
        .build();
```

In the above example, the `veryGenericStrategy` accepts multiple types, while the `verySpecificStrategy` only accepts `int`s.
The built `Stubber` will therefore use `verySpecificStrategy` to provide stub values for `int`s and use `veryGenericStrategy` for other types.

Various `StubbingStrategy` implementations can be found in `ch.leadrian.stubr.core.strategy.StubbingStrategies`.

### Stubbing sites

`StubbingStrategy`s can use the calling `Stubber` themselves to instantiate stubs.
An simple example for that is a stubbing strategy that instantiates a stub using a suitable constructor.
This constructor however, might itself need parameter values which can be provided by the `Stubber`.

A `StubbingSite` describes where exactly and under what circumstances a `StubbingStrategy` requests additional stub values from the calling `Stubber`.
This information can then be used to provide different values of the same type for different `StubbingSite`s.
One might for example configure the `Stubber` to provide a `null` value when a method parameter is annotated with `@Nullable` and non-null value otherwise.

Various `StubbingSite` implementations can be found in `ch.leadrian.stubr.core.site.StubbingSites`.

### Matchers

A `StubbingStrategy` has one additional default method:
```java
StubbingStrategy when(Matcher<? super Type> typeMatcher)
```

This method returns a new `StubbingStrategy` that uses the receiver (`this`) to stub values, but in addition only accepts a given context and type if the given `Matcher` matches both the context and type.

It is therefore possible conditionally apply `StubbingStrategy`s, for example depending on the `StubbingSite` or the type or both.

A `Matcher<T>` is a generic functional interface that requires the following method to be implemented:
```java
boolean matches(StubbingContext context, T value)
```

Since `Matcher`s are generic, a matcher may be combined using logical _and_, _or_ and _not_ operations, and in addition matcher might delegate to other matches in order to match for example directly the `StubbingSite`.

Various `Matcher` implementations can be found in `ch.leadrian.stubr.core.matcher.Matchers`.

### Selector

A `Selector<T>` is a generic functional interface that selects zero or one values from a given list of values:
```java
Optional<T> select(StubbingContext context, List<? extends T> values)
```

In core implementations of `StubbingStrategy`s `Selector`s are used to select an enum constant, a constructor or a factory.

Various `Selector` implementations can be found in `ch.leadrian.stubr.core.selector.Selectors`.

## Download

For Maven:
```xml
<dependency>
  <groupId>ch.leadrian.stubr</groupId>
  <artifactId>stubr-core</artifactId>
  <version>1.3.0</version>
</dependency>
```

For Gradle (Groovy DSL):
```groovy
implementation 'ch.leadrian.stubr:stubr-core:1.3.0'
```

For Gradle (Kotlin DSL):
```groovy
implementation("ch.leadrian.stubr:stubr-core:1.3.0")
```

## 3rd party support

### JUnit 5

Stubr also includes an extension for JUnit Jupiter that allows you to get stub values through parameter injection.
The only requirement is to extend your test with the extension `ch.leadrian.stubr.junit.Stubr` and to annotated the desired method parameter with `ch.leadrian.stubr.junit.annotation.Stub`.

In addition, the tests might be configured using `ch.leadrian.stubr.junit.Include`, `ch.leadrian.stubr.junit.StubWith` and `ch.leadrian.stubr.junit.StubberBaseline`.

For example:

```java
@ExtendWith(Stubr.class)
@StubWith(MyAmazingStubberProvider.class)
class MyAmazingTest {

    @Test
    void testSomething(@Stub String value) {
        // test something
    }

}
```

The JUnit 5 extension can be downloaded here:

For Maven:
```xml
<dependency>
  <groupId>ch.leadrian.stubr</groupId>
  <artifactId>stubr-junit</artifactId>
  <version>1.3.0</version>
</dependency>
```

For Gradle (Groovy DSL):
```groovy
implementation 'ch.leadrian.stubr:stubr-junit:1.3.0'
```

For Gradle (Kotlin DSL):
```groovy
implementation("ch.leadrian.stubr:stubr-junit:1.3.0")
```

### Mockito

Stubr also includes a Mockito module that provides `StubbingStrategy` implementations that provide stubs using Mockito mocks.
All non-void method call on such a mock will return a stub value provided by the `Stubber` that was used to mock the stub.

Concrete instances can be accessed through `ch.leadrian.stubr.mockito.MockitoStubbingStrategies`.

A usage example:
```java
Stubber stubber = Stubber.builder()
    .stubWith(StubbingStrategies.constantValue("stubbed"))
    .stubWith(MockitoStubbingStrategies.mock())
    .stubWith(MockitoStubbingStrategies.mock(MyMockedObject.class, mock -> Mockito.when(mock.doSomething()).thenReturn("done")))
    .build();
Foo someOtherMock = stubber.stub(Foo.class); // Stubbed using the generic mock stubbing strategy
MyMockedObject obj = stubber.stub(MyMockedObject.class); // Stubbed using the specific mock stubbing strategy
String result = obj.doSomething(); // result = "done"
```

The Mockito extension can be downloaded here:

For Maven:
```xml
<dependency>
  <groupId>ch.leadrian.stubr</groupId>
  <artifactId>stubr-mockito</artifactId>
  <version>1.3.0</version>
</dependency>
```

For Gradle (Groovy DSL):
```groovy
implementation 'ch.leadrian.stubr:stubr-mockito:1.3.0'
```

For Gradle (Kotlin DSL):
```groovy
implementation("ch.leadrian.stubr:stubr-mockito:1.3.0")
```

### Kotlin

Stubr also includes a Kotlin extension to simplify method calls and to stub Kotlin object classes.

```kotlin
object MyObject {

    fun doSomething() {
    }

}

var stubber = Stubber.builder()
    .stubWith(KotlinStubbingStrategies.objectInstance())
    .build()
val stub: MyObject = stubber.stub()
```

The Kotlin extension can be downloaded here:

For Maven:
```xml
<dependency>
  <groupId>ch.leadrian.stubr</groupId>
  <artifactId>stubr-kotlin</artifactId>
  <version>1.3.0</version>
</dependency>
```

For Gradle (Groovy DSL):
```groovy
implementation 'ch.leadrian.stubr:stubr-kotlin:1.3.0'
```

For Gradle (Kotlin DSL):
```groovy
implementation("ch.leadrian.stubr:stubr-kotlin:1.3.0")
```
