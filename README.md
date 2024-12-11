# SlavaCord

i did not, in fact, come up with this name  
all blame goes to the person who came up with it (*you know who you are*)

### this library requires [Javacord](https://github.com/Javacord/Javacord)

anyways this library (or is it framework,,,?) helps make slash commands in Javacord (and actually all the other Discord api wrappers in general as far as i can tell) much less painful.

it uses Reflection and Annotations and such so that you write methods for a command and that becomes both the command's declaration and implementation


## Adding as dependency and whatever

**Minimum Java version: 17**

### Maven:
```xml
<dependency>
  <groupId>io.github.canary-prism</groupId>
  <artifactId>slavacord</artifactId>
  <version>3.0.2</version>
</dependency>
```
### Gradle:
figure it out i'm lazy

### sbt
why?

### ant
see: [sbt](#sbt)

## How use

### Command Handler
to start, you need to make a `CommandHandler` 
```java
DiscordApi api = getApiSomehow();
var handler = new CommandHandler(api);
```

### Basic Commands
you must make a class that implements `Commands` and has `@CreateServer` or `@CreateGlobal` annotation on it,  this then applies to all commands declared within the class  
a command in the `Commands` class is a method annotated with `@Command`
```java
@CreateGlobal // commands made here will be available everywhere
class MyCommands implements Commands {
    @Command(name = "test", description = "test command")
    static void test() {

    }
}
```
this declares a command `/test` with description "test command" that takes no parameters and currently doesn't do anything (invoking this from discord will simply timeout after 3 seconds and discord will report the command has failed)

then you submit the class to the `CommandHandler` to parse its methods into commands, register them with discord, and start listening for events
```java
handler.register(MyCommands.class, true);
```
it's worth noting these command methods may be static *or* virtual (instance methods), however if they are virtual you must pass in an **instance** of your `Commands` class instead of just the `Class` object of it
```java
@CreateServer(1234567890) // commands created here will only be available for the server with the id
class InstanceMethods implements Commands {
    @Command(name = "mewo", description = "mewoo :3")
    void mewo() {}
}
handler.register(new InstanceMethods(), true);
```

you might have noticed the `true` that's also passed to the `register()` method. that's a boolean for whether or not to overwrite existing registered commands the discord bot. when mass registering commands to discord's api you cannot append commands. if you pass false for overwrite the library will attempt to merge the declared commands with existing commands obtained from the api and then submit all of those in order to preserve existing commands.

in order to.. actually interact with the api from the command, you can just use the basic Javacord method of obtaining the `SlashCommandInteraction` object given by the event objects  
you may add `SlashCommandInteraction` as a parameter in your method as long as the parameter is annotated with `@Interaction` *and* it is the FIRST parameter in your method
```java
// in a commands class..
@Command(name = "ping", description = "pong !")
void ping(@Interaction SlashCommandInteraction interaction) {
    interaction.createImmediateResponder()
        .setContent("Pong !")
        .setFlags(MessageFlag.EPHEMERAL);
        .respond()
        .join();
}
```
now obviously this is all well and good but it is also very verbose (it's Java obviously it is)

### @ReturnsResponse
to mitigate the verbosity for something as simple as a `/ping` command or any other command that simply returns a String message as its response, there is the `@ReturnsResponse` annotation  
to use this annotation your method must also have a return type of `String` or `Optional<String>`
```java
// in a commands class..
@ReturnsResponse(ephemeral = true) // set the response message to be ephemeral (visible only to the command's invoker and temporary)
@Command(name = "ping", description = "pong !")
String ping() {
    return "Pong !";
}

@ReturnsResponse(silent = true) // set the response message to be silent (not trigger any notifications or new message sounds)
@Command(name = "beep", description = "boop")
Optional<String> ping() {
    return Optional.of("boop");
}
```
you may still ask for the `SlashCommandInteraction` object to be passed to the method, and use both methods for interacting with the user. 

it might be desirable to respond with simple messages for most cases but a complex response in a specific scenario, for this purpose this library provides several methods of expressing that you'd like to return without sending a response.

- if your `@ReturnsResponse` command method has an `Optional<String>` return type you can simply return `Optional.empty()` and the CommandHandler will make no attempt to send a response. note that a `null` return value will throw an IllegalArgumentException as Optionals are by convention never allowed to be `null`.

- if your `@ReturnsResponse` command method has a `String` return type, you *can* return `null` to indicate no response, but this is less clear than returning an empty Optional and so is not recommended. in future versions this might generate a warning to try to guard against accidental `null` returns.

use one of the above methods to return after responding with the `SlashCommandInteraction` yourself and the handler will simply not send a response  

### Command Arguments

obviously the main benefit of declaring commands like this is that the command declaration syntax roughly matches the syntax for function declarations

thus, arguments are declared as the method's parameters. however they must be annotated with `@Option` to provide more information about the parameter that which discord requires
```java
// in a commands class..
@ReturnsResponse(ephemeral = true)
@Command(name = "echo", description = "echoes whatever you say back to you")
String echo(@Option(name = "text") String str) {
    return str;
}
```
note: like commands, the description field for command arguments are normally *required* by discord. however, this library makes it optional for convenience

here is a list of types that are supported by discord and this library:
- `java.lang.String`
- `long`
- `java.lang.Long`
- `double`
- `java.lang.Double`
- `boolean`
- `java.lang.Boolean`
- `org.javacord.api.entity.user.User`
- `org.javacord.api.entity.channel.ServerChannel` or subtypes (see [Channel Type Bounds](#channel-type-bounds))
- `org.javacord.api.entity.Role`
- `org.javacord.api.entity.Mentionable`
- `org.javacord.api.entity.Attachment`
- any Enum

Enums are automatically converted to longs with "option choices" on their ordinals for discord, then converted back for you

speaking of which

### Option Choices

the `@Option` parameter has the properties `longChoices` and `stringChoices` which let you turn these options into option *choices*, where the user can only select from a provided set of values, these are represented with annotations `@OptionChoiceLong` and `@OptionChoiceString` and are pretty much 1 to 1 with discord's api
```java
// in command method parameter list..
@Option(name = "day_of_week", longChoices = {
    @OptionChoiceLong(name = "Sunday", value = 0),
    @OptionChoiceLong(name = "Monday", value = 1),
    @OptionChoiceLong(name = "Tuesday", value = 2),
    @OptionChoiceLong(name = "Wednesday", value = 3),
    @OptionChoiceLong(name = "Thursday", value = 4),
    @OptionChoiceLong(name = "Friday", value = 5),
    @OptionChoiceLong(name = "Saturday", value = 6),
}) long day_of_week
```
(and the same concept applies for Strings just with different names)

however the usage of these are not recommended because,,, just look at them they are ugly as sh-  
thus it is almost always a better choice to just use enums as previously mentioned

like so
```java
// anywhere
enum DaysOfWeek {
    sunday, monday, tuesday, wednesday, thursday, friday, saturday
}
```
```java
// in command method parameter list..
@Option(name = "day_of_week") DaysOfWeek day_of_week
```

this uses the enums' literal names (defined by their identifiers in sourcecode) as the String keys for the option choices, however, if your enum implements `CustomChoiceName` then it will be the String returned by calling `getCustomName()`

it is important to note that option choice names are not allowed to be more than 25 characters long. enums that don't have custom choice names will get their names trimmed to fit under the limit (however as of the current version the library does NOT make a check that the trimmed names are still mutually unique), but enums that *do* implement `CustomChoiceName`, as well as manual long or String option choices don't get this treatment

### Option Bounds

if you want your option to have some validation but not go so far as to use Option Choices you can use Option Bounds to limit the allowed values

#### Basic Bounds

you can use `@DoubleBounds` `@LongBounds` and `@StringLengthBounds` to limit `double`, `long`, and `String` options respectively
```java
// in command method parameter list...
@DoubleBounds(min = -10, max = 10) @Option(name = "d") double d,
@LongBounds(min = 0) @Option(name = "l") long l, // you can choose to only specify one of the bounds
@StringLengthBounds(min = 1, max = 10) @Option(name = "str") String str // you can limit the String lengths too
```
these values are all inclusive (i think)

### Channel Type Bounds

you can also limit what kinds of channels are allowed with `@ChannelTypeBounds`
```java
// in command method parameter list...
@ChannelTypeBounds({ ChannelType.SERVER_TEXT_CHANNEL, ChannelType.SERVER_VOICE_CHANNEL })
@Option(name = "channel") ServerChannel channel
```
in this example users may only select a ServerTextChannel or ServerVoiceChannel for this slash command option

because subtyping is fun you are also allowed to specify a **subtype** of `ServerChannel` in order to limit allowed channels that way
```java
// in command method parameter list...
@Option(name = "text_channel") ServerTextChannel text_channel,
@Option(name = "voice_channel") ServerVoiceChannel vc,
```

you can even use both methods at the same time, specify multiple specific channel types with `@ChannelTypeBounds` and declare the parameter's type as a subtype that they all share to minimise casting. however obviously all of the specified channel types must be able to be assigned to your parameter type

### Optional Command Arguments

a discord command may have optional arguments which are arguments the user does not need to fill in, they are required by discord to all be grouped together on the end of the argument list

in this library, to make a command argument optional, you wrap its type in a `Optional<T>` wrapper
```java
// in command method parameter list..
@Option(name = "text") Optional<String> opt_string
```
this is done to enforce proper treatment of arguments that aren't filled in, and besides they're Optionals, they're very fun to chain

### Subcommands

a discord command may have subcommands or subcommand groups that then chain to more subcommands, in the api a subcommand is treated as a weird sort of argument. this is weird and confusing so here's how subcommands work in this library

you declare a group of commands by making a nested class inside of the main `Commands` class. this nested class must then be annotated with `@CommandGroup`
```java
@CreateGlobal
class MyCommands implements Commands {
    @CommandGroup(name = "group")
    class Group {
        @ReturnsResponse
        @Command(name = "inner", description = "inner")
        String inner() {
            return "mewo";
        }
    }
}
```
this creates a command `/group inner`

you can nest up to two command groups 

it's important to note that this is just a helpful abstraction on top of how discord's api still does subcommands, as such it is important to know the restrictions caused by discord's api:

1. commands and command groups may not share names

this is relatively straightforward

2. commands and command groups may not share a command group

in other words
```java
@CreateGlobal
class MyCommands implements Commands {
    @CommandGroup(name = "group")  // inside a command group
    class Group {

        @ReturnsResponse
        @Command(name = "inner", description = "inner") // command
        String inner() {
            return "mewo";
        }

        @CommandGroup(name = "group2") // command group
        class Group2 {

        }
    }
}
```
**this is not allowed**

(for nerds: this library is inner-type-aware. if you originally registered with an *instance* of `Commands` then it will continue to new instances for your nested classes, they can be instance nested or static nested)

### Permissions

Discord commands have a property called "Default Required Permissions" and it means that any user must have all of the permissions the command requires granted in order to be able to use it in a server

in this library that's represented by annotating with `@RequiresPermissions()`, it takes an array of `PermissionType`s where any user must have all of them granted in order to execute the command

```java
// in a commands class...
@RequiresPermissions({ PermissionType.MANAGE_SERVER })
@Command(name = "mewo", description = "mewo")
void mewo() {}
```

NOTE: if you specify an empty array here, Discord's default required permissions will take effect, which is requiring `PermissionType.ADMINISTRATOR`

keep in mind this is just a *default*, aka, anyone with the permission Manage Server may be able to change who can execute a particular command and where at any time in that particular server

### @Async

just kidding, it's not actual async in the sense of async/await (obviously)

this annotation can be applied onto the `Commands` class, commands, or command groups and simply means that all command methods nested in them (or just the single command itself) will be invoked using its own thread and won't block the event listener

what exactly it uses depends on the set `ThreadingMode`

- `platform`: uses an ExecutorService with a cached thread pool
- `daemon`: similar to `platform` but uses daemon threads that won't keep the JVM alive
- `virtual`: uses virtual threads (only available for Java 21 or higher)
- `prefervirtual`: tries to use `virtual` but fallbacks to `daemon` if they aren't supported on the JVM this is run in

by default commands do not specify a `ThreadingMode` and instead use the `CommandHandler`'s default which is `prefervirtual`

it is obviously recommended to use this annotation for commands that require long running computations in order to reach a result.  
discord counts a command as "failed" if the bot has failed to submit a response within 3 seconds, so the `@Async` annotation also implicitly makes the handler *NOT* use an ImmediateResponder by default

it instead uses a RespondLater, which tells discord that the command *has* gone through it just needs a bit before a proper response can be sent (in discord this is represented by the "BotName is thinking..." text)

### Locales

#### for Commands CommandGroups and Options
you can add a localised name and/or description for a command or command group or option using the `@Trans` annotation (the Trans stands for Translation)
```java
// in a commands class...

@Trans(locale = DiscordLocale.CHINESE_TAIWAN, description = "貓")
@Trans(locale = DiscordLocale.JAPANESE, description = "ねこ")
@CommandGroup(name = "kitty", description = "cat")
class Kitty {
    @Trans(locale = DiscordLocale.CHINESE_TAIWAN, name = "喵", description = "馬卡龍")
    @Trans(locale = DiscordLocale.JAPANESE, name = "にゃー", description = "マカロン")
    @Command(name = "meow", description = "macaron")
    void meow(
        @Trans(locale = DiscordLocale.CHINESE_TAIWAN, name = "使用者")
        @Trans(locale = DiscordLocale.JAPANESE, name = "ユーザー")
        @Option(name = "user") User user
    ) {}
}
```

for each annotation you are allowed to only provide the name or description or both. if the user's locale isn't in the provided localised names or options the default names and descriptions specified by the `@Command` or `@CommandGroup` or `@Option` annotation will be displayed

#### for OptionChoices

you can use `@OptionChoiceString` and `@OptionChoiceLong`'s `translations` property and set it to an array of `@OptionChoiceTrans` like so
```java
// in command method parameter list..
@Option(name = "number", longChoices = {
    @OptionChoiceLong(name = "zero", value = 0, translations = {
        @OptionChoiceTrans(locale = DiscordLocale.CHINESE_TAIWAN, value = "零")
        @OptionChoiceTrans(locale = DiscordLocale.JAPANESE, value = "ゼロ")
    }),
    @OptionChoiceLong(name = "one", value = 1, translations = {
        @OptionChoiceTrans(locale = DiscordLocale.CHINESE_TAIWAN, value = "ㄧ")
        @OptionChoiceTrans(locale = DiscordLocale.JAPANESE, value = "いち")
    })
}) long number
```

or if you're using an enum (recommended), you can implement `CustomChoiceName`'s `getCustomNameTranslations()` method
```java
// anywhere
enum Numbers implements CustomChoiceName {
    ZERO, ONE;

    @Override
    public String getCustomName() {
        return switch (this) {
            case ZERO -> "zero";
            case ONE -> "one";
        };
    }

    @Override
    public Map<DiscordLocale, String> getCustomNameTranslations() {
        return switch (this) {
            case ZERO -> Map.of(
                DiscordLocale.CHINESE_TAIWAN, "零",
                DiscordLocale.JAPANESE, "ゼロ"
            );
            case ONE -> Map.of(
                DiscordLocale.CHINESE_TAIWAN, "一",
                DiscordLocale.JAPANESE, "いち"
            );
        };
    }
}
```
```java
// in command method parameter list..
@Option(name = "number") Numbers number
```

### Autocompletes

this is probably the least intuitive part of the library,,,

in Discord a `String` or `long` SlashCommandOption may be marked `@Autocompletable` which means that when the user starts typing in the option discord will send an event to your bot telling it what their current input is and you can respond by suggesting SlashCommandOptionChoices that the user may choose from

**NOTE: unlike [Static Option Choices](#option-choices) these choices are NOT FORCED. the user is still free to enter any arbitrary value they like**

it is due to this discrepancy that this library avoids the term "Option Choice" when talking about autocompletes and prefers "Suggestions"

you mark a slash command option autocompletable by annotating it with `@Autocompletes`. you must specify the *class* and *name ofmethod* that will supply the autocomplete suggestions
```java
// in command method parameter list..
@Autocompletes(autocompleterClass = Mewo.class, autocompleter = "getSuggestions") 
@Option(name = "query") String query
```
the referenced method must then be an Autocompleter method, which must be a static method annotated with `@Autocompleter` that its parameters are only ever of the type `AutocompleteInteraction` or `T` where each type appears at most once and has a return type assignable to `List<? extends AutocompleteSuggestion<? extends T>>`, where `T` is the type of the option. the passed `T` is the current value the user has entered into the option
```java
// anywhere
class Mewo {
    @Autocompleter
    static List<AutocompleteSuggestion<String>> getSuggestions(String partial_input) {
        return List.of(
            AutocompleteSuggestion.of("name", "value")
        );
    }
}
```

if your autocompleter method is in the same class as your command method, the syntax is slightly more convenient  
you are allowed to only specify the autocompleter's method name, and your autocompleter method is allowed to be non-static as long as you passed in an instance to the command handler when registering your Commands class
```java
// in a commands class..

@Command(name = "mewo", description = "mewo")
void mewo(@Autocompletes(autocompleter = "getSuggestions") @Option(name = "text") String text) {
}

@Autocompleter
List<AutocompleteSuggestion<String>> getSuggestions(AutocompleteInteraction interaction, String partial_input) {
    // please do not attempt to respond with suggestions with the interaction yourself
    return List.of(
        AutocompleteSuggestion.of("name", "value")
    );
}
```

Autocompleter methods are allowed to be annotated with `@Async`

currently in this example the Autocompleter method is responsible for taking the passed partial input into consideration when returning suggestions.

### Searching in AutocompleteSuggestions

if you annotate an autocompleter with `@SearchSuggestions` the command handler will take your returned suggestions and match them with the user's input in a manner as specified by the properties in the annotation. this essentially offloads the job of actually providing suggestions tailored to the user's input to the command handler

### Parsing Exception

this library throws ParsingException for issues relating to the conversion of your `Commands` class and its command methods and command group classes. 

their stacktraces include information on what exactly it was parsing and what it was expecting 

hopefully this makes it easier for you to see what's wrong with your class and fix the error
