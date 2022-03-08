![IMG](https://cdn.discordapp.com/attachments/431545763928211457/853353180271214662/mfthread.png)
<div align="center">

# Minix

[![GitHub Workflow Status](https://img.shields.io/github/workflow/status/DaRacci/Minix/Java%20CI?color=purple&style=for-the-badge)](https://github.com/DaRacci/Minix/actions/workflows/java-ci.yml)
[![Maven metadata URL](https://img.shields.io/maven-metadata/v?color=purple&metadataUrl=https%3A%2F%2Frepo.racci.dev%2Freleases%2Fdev%2Fracci%2FMinix%2Fmaven-metadata.xml&style=for-the-badge)]()

[![CodeFactor Grade](https://img.shields.io/codefactor/grade/github/DaRacci/Minix?color=purple&style=for-the-badge)]()
[![Maintainability](https://img.shields.io/codeclimate/maintainability/DaRacci/Minix?color=purple&style=for-the-badge)]()
[![Lines](https://img.shields.io/tokei/lines/github/DaRacci/Minix?color=purple&style=for-the-badge)]()
[![Code Climate issues](https://img.shields.io/codeclimate/issues/DaRacci/Minix?color=purple&style=for-the-badge)]()

[![Servers](https://img.shields.io/bstats/servers/13706?color=purple&style=for-the-badge)](https://bstats.org/plugin/bukkit/Minix/13706)
[![Players](https://img.shields.io/bstats/players/13706?color=purple&style=for-the-badge)](https://bstats.org/plugin/bukkit/Minix/13706)

Minix is library written for purpur based plugins, providing full integration with [KotlinX Coroutines](https://github.com/Kotlin/kotlinx.coroutines) for easy Async integration with the Bukkit API.

</div>

## Plugin Information

- Supports 1.18.1+

## Docs

* The docs can be found [here](https://minix.racci.dev)

## Plugin Information

Minix is a standalone plugin, so it can provide all of its features, this means you will have to install it on the server with your plugin, to depend on Minix add it as a dependency in your plugin.yml:

#### Note: Minix currently will only run on [my purpur fork](https://github.com/DaRacci/Tentacles) due to pending prs and waiting on adventure 4.10.0 Release. This is a simple drop in jar and all changes are viewable in the patches on the github page.

```yaml
depend:
  - Minix
```

## Getting started:

```kotlin
repositories {
        maven("https://repo.racci.dev/releases/")
}
```

```kotlin
dependencies {
    compileOnly("dev.racci:Minix:tag")
}
```

Replace `tag` with a release tag, eg `1.0.0`.

## Features

- Coroutines integration
- Async suspended functions
- A lot of use of kotlin lazy which is worse on memory usage but allows things to be only loaded when needed for better performance.

- [CoroutineScheduler](Minix-API/src/main/kotlin/dev/racci/minix/api/scheduler/CoroutineScheduler.kt)
- [CoroutineTask](Minix-API/src/main/kotlin/dev/racci/minix/api/scheduler/CoroutineTask.kt)
- [CoroutineRunnable](Minix-API/src/main/kotlin/dev/racci/minix/api/scheduler/CoroutineRunnable.kt)

- [PluginService](Minix-Core/src/main/kotlin/dev/racci/minix/core/services/PluginServiceImpl.kt)
  - Manages all minix plugins and allows plugins to be safe to use new versions of minix without code changes.

- [Extensions](Minix-API/src/main/kotlin/dev/racci/minix/api/extension/Extension.kt):
  - Registers as a Koin injection automatically with the ability to bind to another class.
  - Provides a way of depending on other extension modules to start first and make sure they have successfully started.
  - Acts like it's own mini plugin with a enable and disable function, this allows you to more efficiently split your code up into smaller modules and helps with debugging.
  - Allows lots of the Ex Utils to be used without more interfaces, object calls, or the need to use the `this` keyword.

- Events:
  - [PlayerShiftLeftClickEvent](Minix-API/src/main/kotlin/dev/racci/minix/api/events/ComboEvent.kt#L66)
  - [PlayerShiftRightClickEvent](Minix-API/src/main/kotlin/dev/racci/minix/api/events/ComboEvent.kt#L76)
  - [PlayerShiftOffhandEvent](Minix-API/src/main/kotlin/dev/racci/minix/api/events/ComboEvent.kt#L86)
  - [PlayerShiftDoubleOffhandEvent](Minix-API/src/main/kotlin/dev/racci/minix/api/events/ComboEvent.kt#L111)
  - [PlayerDoubleOffhandEvent](Minix-API/src/main/kotlin/dev/racci/minix/api/events/ComboEvent.kt#L135)
  - [PlayerEnterLiquidEvent](Minix-API/src/main/kotlin/dev/racci/minix/api/events/PlayerLiquidEvent.kt#L20)
  - [PlayerExitLiquidEvent](Minix-API/src/main/kotlin/dev/racci/minix/api/events/PlayerLightEvent.kt#26)
  - [PlayerMoveXYZEvent](Minix-API/src/main/kotlin/dev/racci/minix/api/events/PlayerMoveXYZEvent.kt#18)
  - [PlayerMoveFullXYZEvent](Minix-API/src/main/kotlin/dev/racci/minix/api/events/PlayerMoveXYZEvent.kt#41)

- [Event Flows](Minix-API/src/main/kotlin/dev/racci/minix/api/flow/EventFlow.kt):
  - Allows you to suspend and wait for an event such as player input.
  - Allows you to put easy timeouts.
  - A much more intuitive and easier to read way of getting inputs and such.

- Destructors:
  - [Common](Minix-API/src/main/kotlin/dev/racci/minix/api/destructors/CommonDestructor.kt)
  - [Event](Minix-API/src/main/kotlin/dev/racci/minix/api/destructors/EventDestructor.kt)
  - [Pattern](Minix-API/src/main/kotlin/dev/racci/minix/api/destructors/PatternDestructor.kt)

- Fluent Item Builders:
  - [Item](Minix-API/src/main/kotlin/dev/racci/minix/api/builders/BaseItemBuilder.kt)
  - [Banner](Minix-API/src/main/kotlin/dev/racci/minix/api/builders/BannerBuilder.kt)
  - [Book](Minix-API/src/main/kotlin/dev/racci/minix/api/builders/BookBuilder.kt)
  - [Firework](Minix-API/src/main/kotlin/dev/racci/minix/api/builders/FireworkBuilder.kt)
  - [Head](Minix-API/src/main/kotlin/dev/racci/minix/api/builders/HeadBuilder.kt)
  - [Item](Minix-API/src/main/kotlin/dev/racci/minix/api/builders/ItemBuilder.kt)
  - [Map](Minix-API/src/main/kotlin/dev/racci/minix/api/builders/MapBuilder.kt)
  - [Builder DSL](Minix-API/src/main/kotlin/dev/racci/minix/api/builders/ItemBuilderDSL.kt)

- Serializers:
  - [Attribute Modifier](Minix-API/src/main/kotlin/dev/racci/minix/api/serializables/AttributeModifierSerializer.kt)
  - [Duration](Minix-API/src/main/kotlin/dev/racci/minix/api/serializables/DurationSerializer.kt)
  - [Enchant](Minix-API/src/main/kotlin/dev/racci/minix/api/serializables/EnchantSerializer.kt)
  - [Location](Minix-API/src/main/kotlin/dev/racci/minix/api/serializables/LocationSerializer.kt)
  - [MiniMessage](Minix-API/src/main/kotlin/dev/racci/minix/api/serializables/MiniMessageSerializer.kt)
  - [NamespacedKey](Minix-API/src/main/kotlin/dev/racci/minix/api/serializables/NamespacedKeySerializer.kt)
  - [Pattern](Minix-API/src/main/kotlin/dev/racci/minix/api/serializables/PatternSerializer.kt)
  - [PotionEffect](Minix-API/src/main/kotlin/dev/racci/minix/api/serializables/PotionEffectSerializer.kt)
  - [Range](Minix-API/src/main/kotlin/dev/racci/minix/api/serializables/RangeSerializers.kt)
  - [Item Stack](Minix-API/src/main/kotlin/dev/racci/minix/api/serializables/SerializableItemStack.kt)
  - [UUID](Minix-API/src/main/kotlin/dev/racci/minix/api/serializables/UUIDSerializer.kt)
  - [Vector](Minix-API/src/main/kotlin/dev/racci/minix/api/serializables/VectorSerializer.kt)
  - [World](Minix-API/src/main/kotlin/dev/racci/minix/api/serializables/WorldSerializer.kt)
  - Recipes:
    - [Blasting](Minix-API/src/main/kotlin/dev/racci/minix/api/serializables/recipes/BlastingRecipeIngredients.kt)
    - [Campfire](Minix-API/src/main/kotlin/dev/racci/minix/api/serializables/recipes/CampfireRecipeIngredients.kt)
    - [Furnace](Minix-API/src/main/kotlin/dev/racci/minix/api/serializables/recipes/FurnaceRecipeIngredients.kt)
    - [Shaped](Minix-API/src/main/kotlin/dev/racci/minix/api/serializables/recipes/ShapedRecipeIngredients.kt)
    - [Shapeless](Minix-API/src/main/kotlin/dev/racci/minix/api/serializables/recipes/ShapelessRecipeIngredients.kt)
    - [Smithing](Minix-API/src/main/kotlin/dev/racci/minix/api/serializables/recipes/SmithingRecipeIngredients.kt)
    - [Smoking](Minix-API/src/main/kotlin/dev/racci/minix/api/serializables/recipes/SmokingRecipeIngredients.kt)
    - [Stonecutting](Minix-API/src/main/kotlin/dev/racci/minix/api/serializables/recipes/StoneCuttingRecipeIngredients.kt)

- Utils:
  - Collections:
    - [Collection Utils](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/collections/CollectionUtils.kt)
    - [ExpirationList](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/collections/ExpirationList.kt)
    - [ExpirationMap](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/collections/ExpirationMap.kt)
    - [MultiMap](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/collections/MultiMap.kt)
    - [ObservableCollections](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/collections/ObservableCollections.kt)
    - [OnlinePlayerCollection](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/collections/OnlinePlayerCollections.kt)

  - Jetbrains exposed:
    - [Block](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/exposed/Block.kt)
    - [BlockPos](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/exposed/BlockPos.kt)
    - [Cached](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/exposed/Cached.kt)
    - [Chunk](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/exposed/Chunk.kt)
    - [ChunkPos](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/exposed/ChunkPos.kt)
    - [Item](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/exposed/Item.kt)
    - [Location](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/exposed/Location.kt)
    - [LocationPos](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/exposed/LocationPos.kt)
    - [OfflinePlayer](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/exposed/OfflinePlayer.kt)
    - [World](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/exposed/World.kt)

  - Kotlin:
    - [Infix Utils](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/kotlin/Infix.kt)
    - [Kotlin Utils](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/kotlin/KotlinUtils.kt)
    - [Lazy Utils](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/kotlin/LazyUtils.kt)

  - Minecraft:
    - [Block](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/minecraft/BlockUtils.kt)
    - [Inventory](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/minecraft/InventoryUtils.kt)
    - [Location](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/minecraft/LocationUtils.kt)
    - [MaterialTagExtensions](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/minecraft/MaterialTagsExtension.kt)
    - [NBT](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/minecraft/NBT.kt)
    - [Player](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/minecraft/PlayerUtils.kt)
    - [Plugin](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/minecraft/PluginUtils.kt)
    - [Projectile](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/minecraft/ProjectileUtils.kt)
    - [Range](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/minecraft/RangeUtils.kt)
    - [Tame](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/minecraft/TameUtils.kt)
    - [World](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/minecraft/WorldUtils.kt)
  
  - Misc:
    - [Colour](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/primitive/ColourUtils.kt)
    - [Enum](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/primitive/EnumUtils.kt)
    - [Legacy](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/primitive/LegacyUtils.kt)
    - [Math](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/primitive/MathUtils.kt)
    - [Number](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/primitive/NumberUtils.kt)
    - [String](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/primitive/StringUtils.kt)
    - [Text](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/primitive/TextUtils.kt)
    - [Koin](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/Koin.kt)
    - [Reflection](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/ReflectionUtils.kt)
    - [TakeMaxTimePerTick](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/TakeMaxTimePerTickUtils.kt)
    - [Time](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/TimeUtils.kt)
    - [Unsafe](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/UnsafeUtil.kt)
    - [UpdateChecker](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/UpdateChecker.kt)


## Plans

## 🤝 Contributing

Contributions, issues and feature requests are welcome!<br />
Feel free to check [issues page](https://github.com/DaRacci/Minix/issues).

## 📝 License

Copyright © 2021-2022 [Racci](https://github.com/DaRacci) <br />
This project is [licensed](https://github.com/DaRacci/Minix/blob/master/LICENSE.md).