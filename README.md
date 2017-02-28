# IOU Core

[![Travis CI](https://travis-ci.org/ioweyou/iou-core.svg)](https://travis-ci.org/ioweyou/iou-core)
[![Coverage Status](https://coveralls.io/repos/github/ioweyou/iou-core/badge.svg?branch=master)](https://coveralls.io/github/ioweyou/iou-core?branch=master)
[![License MIT](https://img.shields.io/:license-mit-blue.svg)](http://badges.mit-license.org)

<a href="https://promisesaplus.com/">
    <img src="https://promisesaplus.com/assets/logo-small.png" alt="Promises/A+ logo"
         title="Promises/A+ 1.0 compliant" align="right" />
</a>
Java [Promises/A+](https://github.com/promises-aplus/promises-spec) compliant promise library. It designed to be extended by other libraries, like [IOU Java](https://github.com/ioweyou/iou-java) and [IOU Android](https://github.com/ioweyou/iou-android).

#### Table Of Contents

[Implementations](#implementations)

[Dependency management](#dependency-management)
  * [Maven](#maven)
  * [Gradle](#gradle)

[Examples](#examples)
  * [Call with single then](#call-with-single-then)
  * [Call with single then and Java 8 lambda](#call-with-single-then-and-java-8-lambda)
  * [Chained or piped promise](#chained-or-piped-promise)
  * [Parallel promise](#parallel-promise)
  * [Rejecting a promise](#rejecting-a-promise)
  * [Failing a promise](#failing-a-promise)

## Implementations
* [IOU Java](https://github.com/ioweyou/iou-java)
* [IOU Android](https://github.com/ioweyou/iou-android)

## Dependency management
### Maven
-----
```xml
<dependency>
    <groupId>nl.brusque.iou</groupId>
    <artifactId>iou-core</artifactId>
    <version>1.0.0-beta-01</version>
</dependency>
```

### Gradle
-----
```
compile 'nl.brusque.iou:iou-core:1.0.0-beta-01'
```

## Examples
-----
### Given the example implementation
```java
class TestTypedIOU<TInput> extends AbstractIOU<TInput> {
  ...
}

class TestTypedPromise<TInput> extends AbstractPromise<TInput> {
  ...
}

TestTypedIOU<Integer> iou = new TestTypedIOU<>();

```
### Call with single then
```java
iou.getPromise()
    .then(new IThenCallable<Integer, Void>() {
        @Override
        public Void apply(Integer integer) throws Exception {
            System.out.println(integer);

            return null;
        }
    });

iou.resolve(42); // prints 42
```
### Call with single then and Java 8 lambda
```java
iou.getPromise()
    .then((Integer integer) -> {
        System.out.println(integer);

        return null;
    });

iou.resolve(42); // prints 42
```
### Chained or piped promise
```java
iou.getPromise()
    .then(new IThenCallable<Integer, Integer>() {
        @Override
        public Integer apply(Integer input) throws Exception {
            return input * 10;
        }
    })
    .then(new IThenCallable<Integer, String>() {
        @Override
        public String apply(Integer input) throws Exception {
            return String.format("The result: %d", input);
        }
    })
    .then(new IThenCallable<String, Void>() {
        @Override
        public Void apply(String input) throws Exception {
            System.out.println(input);

            return null;
        }
    });

iou.resolve(42); // prints "The result: 420"
```
### Sequential promise
```java
TestTypedPromise<Integer> promise = iou.getPromise();

promise
    .then(new IThenCallable<Integer, Void>() {
        @Override
        public Void apply(Integer input) throws Exception {
            System.out.println(input);

            return null;
        }
    });

promise
    .then(new IThenCallable<Integer, String>() {
        @Override
        public Void apply(Integer input) throws Exception {
            String result = String.format("%d * 10 = %d", input, input * 10);
            System.out.println(result);

            return result;
        }
    });

iou.resolve(42); // prints "42" and "42 * 10 = 420" in exactly this order
```
### Rejecting a promise
```java
iou.getPromise()
    .then(new IThenCallable<Integer, Integer>() {
        @Override
        public Integer apply(Integer integer) throws Exception {
            return integer * 42;
        }
    }, new IThenCallable<Object, Void>() {
        @Override
        public Void apply(Object reason) throws Exception {
            System.out.println(String.format("%s I can't do that.", reason));

            return null;
        }
    });

iou.reject("I'm sorry, Dave."); // prints "I'm sorry, Dave. I can't do that."
```
### Failing a promise
```java
iou.getPromise()
    .then(new IThenCallable<Integer, Integer>() {
        @Override
        public Integer apply(Integer input) throws Exception {
            throw new Exception("I just don't care.");
        }
    })
    .then(new IThenCallable<Integer, Void>() {
        @Override
        public Void apply(Integer input) throws Exception {
            System.out.println("What would you say it is you do here?");

            return null;
        }
    }, new IThenCallable<Object, Void>() {
        @Override
        public Void apply(Object reason) throws Exception {
            System.out.println(
              String.format("It's not that I'm lazy, it's that %s",
                ((Exception)reason).getMessage()));

            return null;
        }
    });

iou.resolve(42); // prints "It's not that I'm lazy, it's that I just don't care."
```
