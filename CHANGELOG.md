# Changelog

## v2.1.1
- fixed annoyingly simple bug breaking `Optional<String>` returns for `@ReturnsResponse`

## v2.1.0
- add support for `Optional<String>` return type for `@ReturnsResponse`
- deprecate returning blank `String` value for `@ReturnsResponse`

## v2.0.0
- modules for Java 9 sake
- can't depend on internal Javacord logger now so using log4j directly
- some code changes and cleanups

## v1.0.2
- all the javadocs, all of it >:3

## v1.0.1
- fixed threading mode not using default value correctly