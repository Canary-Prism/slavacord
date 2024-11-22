# Changelog

## v2.6.0
- fixed bug where duplicate command names registered across different servers would cause incorrect ones to be called
- added autocomplete suggestion search and filtering functionality
- loosened restrictions on autocompleter signatures so that primitive and boxed types are allowed as well as bounded wildcards
- fixed bug where `@Trans` wasn't properly repeatable because the container type `@Translations` was module private
- cleaned up code a bit thanks to intellij inspection (please stop yelling about unused public stuff and not using diamond though)

## v2.5.0
- added support for Option Bounds
- fixed Async throwing erroneous exception when applied directly to a command method in Java versions prior to 21
- added just... so many logging calls.. turn on `TRACE` level logs and enjoy lmfto
- opened internal Data classes for Reflection (have fun :3 (or not, there's really not that much there))

## v2.4.1
- attempts to make autocompleter method accessible in reflection
- fixed bug where registering with `overwrites: false` just... doesn't work that well and often breaks

## v2.4.0
- added support for ATTACHMENT option type
- added support for autocompletes

## v2.3.0
- added nsfw flag to commands and command group (not applicable to nested commands and command groups)
- warn setting `setEnabledInDMs` in nested commands and command groups
- updated RespondLater ReturnResponses to act like ImmediateResponders
- added the ability to set localizations for commannds and commnand groups and options and option choices
- added `getCustomNameTranslations()` method to `CustomChoiceName`

## v2.2.0
- added `@RequiresPermissions` annotation to interface with discord's default required permissions
- added support for the `DECIMAL` or `NUMBER` option type (whoops gues i just missed it all this time)

## v2.1.2
- changed dependencies slightly... i don't even know what was happening

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