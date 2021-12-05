---
title: CoreCommand
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.core.commands](../index.html)/[CoreCommand](index.html)



# CoreCommand



[jvm]\
class [CoreCommand](index.html) : BaseCommand



## Functions


| Name | Summary |
|---|---|
| [canExecute](index.html#-1546555712%2FFunctions%2F863300109) | [jvm]<br>~~open~~ ~~fun~~ [~~canExecute~~](index.html#-1546555712%2FFunctions%2F863300109)~~(~~~~issuer~~~~:~~ CommandIssuer~~,~~ ~~cmd~~~~:~~ RegisteredCommand&lt;*&gt;~~)~~~~:~~ [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [doHelp](index.html#1818665068%2FFunctions%2F863300109) | [jvm]<br>open fun [doHelp](index.html#1818665068%2FFunctions%2F863300109)(issuer: CommandIssuer, vararg args: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))<br>open fun [doHelp](index.html#-721414265%2FFunctions%2F863300109)(issuer: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), vararg args: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) |
| [execute](index.html#-664778240%2FFunctions%2F863300109) | [jvm]<br>open fun [execute](index.html#-664778240%2FFunctions%2F863300109)(issuer: CommandIssuer, command: CommandRouter.CommandRouteResult) |
| [getCommandHelp](index.html#1850315847%2FFunctions%2F863300109) | [jvm]<br>~~open~~ ~~fun~~ [~~getCommandHelp~~](index.html#1850315847%2FFunctions%2F863300109)~~(~~~~)~~~~:~~ CommandHelp |
| [getCommandsForCompletion](index.html#841381031%2FFunctions%2F863300109) | [jvm]<br>open fun [getCommandsForCompletion](index.html#841381031%2FFunctions%2F863300109)(issuer: CommandIssuer, args: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;): [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [getContextFlags](index.html#-2062522235%2FFunctions%2F863300109) | [jvm]<br>open fun [getContextFlags](index.html#-2062522235%2FFunctions%2F863300109)(cls: [Class](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)&lt;*&gt;): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getCurrentCommandIssuer](index.html#534940092%2FFunctions%2F863300109) | [jvm]<br>open fun [getCurrentCommandIssuer](index.html#534940092%2FFunctions%2F863300109)(): CommandIssuer |
| [getCurrentCommandManager](index.html#-1690669800%2FFunctions%2F863300109) | [jvm]<br>open fun [getCurrentCommandManager](index.html#-1690669800%2FFunctions%2F863300109)(): CommandManager&lt;[Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), CommandIssuer, [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), MessageFormatter&lt;[Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt;, *, ConditionContext&lt;CommandIssuer&gt;&gt; |
| [getDefaultRegisteredCommand](index.html#299886111%2FFunctions%2F863300109) | [jvm]<br>open fun [getDefaultRegisteredCommand](index.html#299886111%2FFunctions%2F863300109)(): RegisteredCommand&lt;*&gt; |
| [getExceptionHandler](index.html#-483610196%2FFunctions%2F863300109) | [jvm]<br>open fun [getExceptionHandler](index.html#-483610196%2FFunctions%2F863300109)(): ExceptionHandler |
| [getExecCommandLabel](index.html#633284845%2FFunctions%2F863300109) | [jvm]<br>open fun [getExecCommandLabel](index.html#633284845%2FFunctions%2F863300109)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getExecSubcommand](index.html#-746685749%2FFunctions%2F863300109) | [jvm]<br>open fun [getExecSubcommand](index.html#-746685749%2FFunctions%2F863300109)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getLastCommandOperationContext](index.html#623673942%2FFunctions%2F863300109) | [jvm]<br>open fun [getLastCommandOperationContext](index.html#623673942%2FFunctions%2F863300109)(): CommandOperationContext&lt;CommandIssuer&gt; |
| [getName](index.html#-1482051556%2FFunctions%2F863300109) | [jvm]<br>open fun [getName](index.html#-1482051556%2FFunctions%2F863300109)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getOrigArgs](index.html#-228787639%2FFunctions%2F863300109) | [jvm]<br>open fun [getOrigArgs](index.html#-228787639%2FFunctions%2F863300109)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [getRegisteredCommands](index.html#1500522205%2FFunctions%2F863300109) | [jvm]<br>open fun [getRegisteredCommands](index.html#1500522205%2FFunctions%2F863300109)(): [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;RegisteredCommand&lt;*&gt;&gt; |
| [getRequiredPermissions](index.html#-158859730%2FFunctions%2F863300109) | [jvm]<br>open fun [getRequiredPermissions](index.html#-158859730%2FFunctions%2F863300109)(): [MutableSet](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [getSubCommands](index.html#-192538613%2FFunctions%2F863300109) | [jvm]<br>open fun [getSubCommands](index.html#-192538613%2FFunctions%2F863300109)(): SetMultimap&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), RegisteredCommand&lt;*&gt;&gt; |
| [hasPermission](index.html#-1802946348%2FFunctions%2F863300109) | [jvm]<br>open fun [hasPermission](index.html#-1802946348%2FFunctions%2F863300109)(issuer: CommandIssuer): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>open fun [hasPermission](index.html#-811990507%2FFunctions%2F863300109)(issuer: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [help](index.html#-1434220351%2FFunctions%2F863300109) | [jvm]<br>open fun [help](index.html#-1434220351%2FFunctions%2F863300109)(issuer: CommandIssuer, args: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;)<br>open fun [help](index.html#-339016686%2FFunctions%2F863300109)(issuer: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), args: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;) |
| [onRegister](index.html#502280861%2FFunctions%2F863300109) | [jvm]<br>open fun [onRegister](index.html#502280861%2FFunctions%2F863300109)(manager: CommandManager&lt;[Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), CommandIssuer, [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), MessageFormatter&lt;[Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt;, *, ConditionContext&lt;CommandIssuer&gt;&gt;) |
| [onReload](on-reload.html) | [jvm]<br>fun [onReload](on-reload.html)() |
| [requiresPermission](index.html#-1669582076%2FFunctions%2F863300109) | [jvm]<br>open fun [requiresPermission](index.html#-1669582076%2FFunctions%2F863300109)(permission: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [setContextFlags](index.html#-410093794%2FFunctions%2F863300109) | [jvm]<br>open fun [setContextFlags](index.html#-410093794%2FFunctions%2F863300109)(cls: [Class](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)&lt;*&gt;, flags: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [setExceptionHandler](index.html#1463342139%2FFunctions%2F863300109) | [jvm]<br>open fun [setExceptionHandler](index.html#1463342139%2FFunctions%2F863300109)(exceptionHandler: ExceptionHandler): BaseCommand |
| [showCommandHelp](index.html#-1005856530%2FFunctions%2F863300109) | [jvm]<br>~~open~~ ~~fun~~ [~~showCommandHelp~~](index.html#-1005856530%2FFunctions%2F863300109)~~(~~~~)~~ |
| [showSyntax](index.html#-1709132891%2FFunctions%2F863300109) | [jvm]<br>open fun [showSyntax](index.html#-1709132891%2FFunctions%2F863300109)(issuer: CommandIssuer, cmd: RegisteredCommand&lt;*&gt;) |
| [tabComplete](index.html#430725139%2FFunctions%2F863300109) | [jvm]<br>open fun [tabComplete](index.html#430725139%2FFunctions%2F863300109)(issuer: CommandIssuer, commandLabel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), args: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;): [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;<br>open fun [tabComplete](index.html#-303548334%2FFunctions%2F863300109)(issuer: CommandIssuer, rootCommand: RootCommand, args: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, isAsync: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;<br>open fun [tabComplete](index.html#-1672957167%2FFunctions%2F863300109)(issuer: CommandIssuer, commandLabel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), args: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, isAsync: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |


## Properties


| Name | Summary |
|---|---|
| [commandName](index.html#-305930169%2FProperties%2F863300109) | [jvm]<br>@Nullable<br>val [commandName](index.html#-305930169%2FProperties%2F863300109): @Nullable[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [conditions](index.html#850216901%2FProperties%2F863300109) | [jvm]<br>@Nullable<br>val [conditions](index.html#850216901%2FProperties%2F863300109): @Nullable[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [contextFlags](index.html#-1133011867%2FProperties%2F863300109) | [jvm]<br>val [contextFlags](index.html#-1133011867%2FProperties%2F863300109): [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)&lt;[Class](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)&lt;*&gt;, [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [description](index.html#-1203277951%2FProperties%2F863300109) | [jvm]<br>@Nullable<br>val [description](index.html#-1203277951%2FProperties%2F863300109): @Nullable[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [hasHelpCommand](index.html#-762197075%2FProperties%2F863300109) | [jvm]<br>val [hasHelpCommand](index.html#-762197075%2FProperties%2F863300109): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [manager](index.html#-2072978192%2FProperties%2F863300109) | [jvm]<br>val [manager](index.html#-2072978192%2FProperties%2F863300109): CommandManager&lt;*, *, *, *, *, *&gt; |
| [parentCommand](index.html#1612967900%2FProperties%2F863300109) | [jvm]<br>val [parentCommand](index.html#1612967900%2FProperties%2F863300109): BaseCommand |
| [permission](index.html#-249491314%2FProperties%2F863300109) | [jvm]<br>@Nullable<br>val [permission](index.html#-249491314%2FProperties%2F863300109): @Nullable[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [registeredCommands](index.html#1125472147%2FProperties%2F863300109) | [jvm]<br>val [registeredCommands](index.html#1125472147%2FProperties%2F863300109): [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), RootCommand&gt; |
| [subCommands](index.html#443197461%2FProperties%2F863300109) | [jvm]<br>val [subCommands](index.html#443197461%2FProperties%2F863300109): SetMultimap&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), RegisteredCommand&lt;*&gt;&gt; |
| [subScopes](index.html#705222110%2FProperties%2F863300109) | [jvm]<br>val [subScopes](index.html#705222110%2FProperties%2F863300109): [MutableSet](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-set/index.html)&lt;BaseCommand&gt; |

