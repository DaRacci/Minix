<div align="center">

# Minix

[![GitHub branch checks state](https://img.shields.io/github/checks-status/DaRacci/minix/master?color=purple&style=for-the-badge)](https://github.com/DaRacci/minix/actions)
[![Maven metadata URL](https://img.shields.io/maven-metadata/v?color=purple&metadataUrl=https%3A%2F%2Frepo.racci.dev%2Freleases%2Fdev%2Fracci%2FMinix%2Fmaven-metadata.xml&style=for-the-badge)]()

[![Codacy grade](https://img.shields.io/codacy/grade/fdad9d4b693c4c6f8fd0d6b5ee6e533a?color=purple&style=for-the-badge)](https://app.codacy.com/gh/DaRacci/Minix/dashboard)
[![Codacy coverage](https://img.shields.io/codacy/coverage/fdad9d4b693c4c6f8fd0d6b5ee6e533a?color=purple&style=for-the-badge)](https://app.codacy.com/gh/DaRacci/Minix/dashboard)
![Lines](https://img.shields.io/tokei/lines/github/DaRacci/Minix?color=purple&style=for-the-badge)
[![Code Climate issues](https://img.shields.io/codeclimate/issues/DaRacci/Minix?color=purple&style=for-the-badge)](https://codeclimate.com/github/DaRacci/Minix/)

[![Modrinth Game Versions](https://img.shields.io/modrinth/game-versions/minix?color=purple&logoColor=purple&style=for-the-badge)]()
[![Servers](https://img.shields.io/bstats/servers/13706?color=purple&style=for-the-badge)](https://bstats.org/plugin/bukkit/Minix/13706)
[![Players](https://img.shields.io/bstats/players/13706?color=purple&style=for-the-badge)](https://bstats.org/plugin/bukkit/Minix/13706)

Minix is the most advanced and performant framework to build plugins on!</br>
With full in depth support for kotlin coroutines and many useful utilities and quality of life features for developers.</br>
Minix makes developing performant, non-blocking code easy and concise.</br>

# Minix is currently in a state of heavy development for the upcoming 5.0.0 release, all features are subject to change and may not be fully documented.
# Coming from version 4.x.x to 5.0.0 will require a lot of code changes, please be aware of this before starting development before the release of version 5.0.0.

</div>

## Docs
  *The docs can be found [here](https://minix.racci.dev)

## Plugin Information

Minix is a standalone plugin, so it can provide all of its features, this means you will have to install it on the server with your plugin, to depend on Minix add it as a dependency in your plugin.yml:

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

  - [PluginService](minix-plugin/core-common/src/main/kotlin/dev/racci/minix/core/services/PluginServiceImpl.kt)
    - Manages all minix plugins and allows plugins to be safe to use new versions of minix without code changes.

  - [Extensions](minix-plugin/api-common/src/main/kotlin/dev/racci/minix/api/extension/Extension.kt):
    - Registers as a Koin injection automatically with the ability to bind to another class.
    - Provides a way of depending on other extension modules to start first and make sure they have successfully started.
    - Acts like it's own mini plugin with a enable and disable function, this allows you to more efficiently split your code up into smaller modules and helps with debugging.
    - Allows lots of the Ex Utils to be used without more interfaces, object calls, or the need to use the `this` keyword.

  - Events:
    - [PlayerOffhandEvent](Minix-API/src/main/kotlin/dev/racci/minix/api/events/keybind/PlayerOffhandEvent.kt)
    - [PlayerPrimaryEvent](Minix-API/src/main/kotlin/dev/racci/minix/api/events/keybind/PlayerPrimaryEvent.kt)
    - [PlayerSecondaryEvent](Minix-API/src/main/kotlin/dev/racci/minix/api/events/keybind/PlayerSecondaryEvent.kt)
    - [PlayerDoubleOffhandEvent](Minix-API/src/main/kotlin/dev/racci/minix/api/events/keybind/PlayerDoubleOffhandEvent.kt)
    - [PlayerDoublePrimaryEvent](Minix-API/src/main/kotlin/dev/racci/minix/api/events/keybind/PlayerDoublePrimaryEvent.kt)
    - [PlayerDoubleSecondaryEvent](Minix-API/src/main/kotlin/dev/racci/minix/api/events/keybind/PlayerDoubleSecondaryEvent.kt)
    - [PlayerSneakOffhandEvent](Minix-API/src/main/kotlin/dev/racci/minix/api/events/keybind/PlayerSneakOffhandEvent.kt)
    - [PlayerSneakPrimaryEvent](Minix-API/src/main/kotlin/dev/racci/minix/api/events/keybind/PlayerSneakPrimaryEvent.kt)
    - [PlayerSneakSecondaryEvent](Minix-API/src/main/kotlin/dev/racci/minix/api/events/keybind/PlayerSneakSecondaryEvent.kt)
    - [PlayerSneakDoubleOffhandEvent](Minix-API/src/main/kotlin/dev/racci/minix/api/events/keybind/PlayerSneakDoubleOffhandEvent.kt)
    - [PlayerSneakDoublePrimaryEvent](Minix-API/src/main/kotlin/dev/racci/minix/api/events/keybind/PlayerSneakDoublePrimaryEvent.kt)
    - [PlayerSneakDoubleSecondaryEvent](Minix-API/src/main/kotlin/dev/racci/minix/api/events/keybind/PlayerSneakDoubleSecondaryEvent.kt)

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
    - [NamespacedKey](Minix-API/src/main/kotlin/dev/racci/minix/api/serializables/KeySerializer.kt)
    - [Pattern](Minix-API/src/main/kotlin/dev/racci/minix/api/serializables/PatternSerializer.kt)
    - [PotionEffect](Minix-API/src/main/kotlin/dev/racci/minix/api/serializables/PotionEffectSerializer.kt)
    - [Range](Minix-API/src/main/kotlin/dev/racci/minix/api/serializables/RangeSerializers.kt)
    - [Item Stack](minix-modules/module-data/src/main/kotlin/dev/racci/minix/data/serializers/kotlin/minecraft/SerializableItemStack.kt)
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
      - [Block](minix-modules/module-data/src/main/kotlin/dev/racci/minix/api/data/serializers/exposed/Block.kt)
      - [BlockPos](minix-modules/module-data/src/main/kotlin/dev/racci/minix/api/data/serializers/exposed/BlockPos.kt)
      - [Cached](minix-modules/module-data/src/main/kotlin/dev/racci/minix/api/data/serializers/exposed/Cached.kt)
      - [Chunk](minix-modules/module-data/src/main/kotlin/dev/racci/minix/api/data/serializers/exposed/Chunk.kt)
      - [ChunkPos](minix-modules/module-data/src/main/kotlin/dev/racci/minix/api/data/serializers/exposed/ChunkPos.kt)
      - [Item](minix-modules/module-data/src/main/kotlin/dev/racci/minix/api/data/serializers/exposed/Item.kt)
      - [Location](minix-modules/module-data/src/main/kotlin/dev/racci/minix/api/data/serializers/exposed/Location.kt)
      - [LocationPos](minix-modules/module-data/src/main/kotlin/dev/racci/minix/api/data/serializers/exposed/LocationPos.kt)
      - [OfflinePlayer](minix-modules/module-data/src/main/kotlin/dev/racci/minix/api/data/serializers/exposed/OfflinePlayer.kt)
      - [World](minix-modules/module-data/src/main/kotlin/dev/racci/minix/api/data/serializers/exposed/World.kt)
  
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
  
    - Misc:
      - [Enum](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/primitive/EnumUtils.kt)
      - [Math](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/primitive/MathUtils.kt)
      - [Number](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/primitive/NumberUtils.kt)
      - [String](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/primitive/StringUtils.kt)
      - [Koin](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/Koin.kt)
      - [Reflection](minix-modules/module-common/src/main/kotlin/dev/racci/minix/api/utils/ReflectionUtils.kt)
      - [TakeMaxTimePerTick](minix-modules/module-common/src/main/kotlin/dev/racci/minix/api/utils/TakeMaxTimePerTickUtils.kt)
      - [Time](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/TimeUtils.kt)
      - [Unsafe](Minix-API/src/main/kotlin/dev/racci/minix/api/utils/UnsafeUtil.kt)

## Plugin Updater

Using the minix-updater is very easy however it does need to be configured.
Using the format below you can easily configure the updater.
Note that when using more than one provider only the first one will be used unless it fails multiple times.
In this case it will traverse down the list and if none of them work, disable that updater.

When an update is found and is downloaded, a backup will be created of the current state and placed in plugins/Minix/MinixUpdater

Format
```
plugin-updaters=[
    {
        channels=[release]  # What release channels to update to options: [release, alpha, beta, snapshot]
        ignored=[]          # Any folders which should be ignored when backing up for an update
        name=Minix          # The name of the plugin this must be the same as what is in the `/plugins` output
        update-mode=UPDATE  # How to update the plugin options: [UPDATE, CHECK, DISABLED]
        providers=[
            {
                AlwaysUpdateProvider {
                    downloadUrl=api.example.com/download/
                    fileName=file.jar
                    releaseType=RELEASE
                }
            },
            {
                BukkitUpdateProvider {
                    projectID=12345
                    apiKey=12345 # Optional
                }
            },
            {
                GithubUpdateProvider {
                    projectOwner=DaRacci
                    projectRepo=Minix
                    userAgent=AGENT # Optional
                    jarSearchRegex=.*\.jar$ # Optional
                    md5SearchRegex=.*\.md5$ # Optional
                }
            },
            {
                JenkinsUpdateProvider {
                    host=jenkins.example.com
                    job=Minix
                    token=12345 # Optional
                    artifactSearchRegex=.*\.jar$ # Optional
                }
            },
            {
                SpigotUpdateProvider {
                    projectID=12345
                    fileName=file.jar # Optional
                }
            }
        ]
    }
]
```

## 🤝 Contributing

Contributions, issues and feature requests are welcome!<br/>
Feel free to check [issues page](https://github.com/DaRacci/Minix/issues).

## 📝 License

Copyright © 2021-2023 [Racci](https://github.com/DaRacci)<br/>
This project is [licensed](https://github.com/DaRacci/Minix/blob/master/LICENSE.md).

[![FOSSA Status](https://app.fossa.com/api/projects/custom%2B34449%2Fgithub.com%2FDaRacci%2FMinix.svg?type=large)](https://app.fossa.com/projects/custom%2B34449%2Fgithub.com%2FDaRacci%2FMinix?ref=badge_large)

## Special Thanks To
[<img src="https://user-images.githubusercontent.com/21148213/121807008-8ffc6700-cc52-11eb-96a7-2f6f260f8fda.png" alt="" width="150">](https://www.jetbrains.com)

[JetBrains](https://www.jetbrains.com/), creators of the IntelliJ IDEA, supports Minix with one of their [Open Source Licenses](https://www.jetbrains.com/opensource/). IntelliJ IDEA is the recommended IDE for working with Minix or any Java / Kotlin project.
