# Changelog

## v6.0.0
- upgraded discord-bridge version to 4.1.0
- migrated logging from log4j2 to slf4j

## v5.1.2
- upgraded to Discord Bridge version 3.0.1

## v5.1.1
- fixed bug where enum choices still didn't work

## v5.1.0
- added support for double option choices
- fixed bug where enum choices were not represented properly

## v5.0.1
- made the discord-bridge dependency accessibly transitive

## v5.0.0
- migrated library again to use Discord Bridge version 3.0.0

## v4.0.0
- migrated entire library to use Discord Bridge

## v3.0.3
- fixed bug where all null and blank String returns are still ignored

## v3.0.2
- fixed bug where nested classes weren't parsed properly and would attempt to reparse the enclosing class instead

## v3.0.1
- fixed bug where `@DoubleBounds` and `@LongBounds` that only specified min or max would get erroneous out of range exceptions

## v3.0.0
- renamed all enum constants to be SCREAMING_SNAKE_CASE
- removed `@Target`s of `@OptionChoiceLong` and `@OptionChoiceString`. you are no longer allowed to annotate them on anything (it never actually did anything anyway)
- now throws an exception when `enabledInDMs` is set to `false` for nested Commands or CommandGroups
- disallowed returning a blank String from a `@ReturnsResponse` method, it now throws an exception instead of just cancelling the response
- changed it so only **empty** `@CommandGroup` `description`s will be automatically filled in instead of *blank* strings
- `CommandHandler::setDefaultThreadingMode` now throws an exception if you try to set it to `VIRTUAL` on a JVM without Virtual Threads. it also throws an exception if you try to set it to `NONE`
- `ThreadingMode.PREFER_VIRTUAL` now fallbacks to `DAEMON` instead of `PLATFORM` to better mimick the semantics of Virtual Threads (which never keep JVM alive)
- changed Autocompleter method search logic so now the only requirements for the **parameters** the method takes is that they are only ever of the type `AutocompleteInteraction` or `T` where `T` is the type of the option, and each type appears at most once. in other words the parameter types `(AutocompleteInteraction, T)`, `(T, AutocompleteInteraction)`, `(T)`, `(AutocompleteInteraction)`, and `()` are all valid
- `@LongBounds` and `@DoubleBounds` are now checked at parsing time for values that exceed discord's limits

## v2.6.2
- changed getLogger call to pass explicit CommandHandler.class (i guess the inferred one breaks sometimes)
- fixed bug where option bounds of undefined defaults would try to apply their default values when submitting commands

## v2.6.1
- fixed bug where if `@SearchSuggestions` ignoreWhitespace or ignorePunctuation was set to true, user input containing whitespace or punctuation wouldn't match anything

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