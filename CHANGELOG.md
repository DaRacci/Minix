# Changelog

All notable changes to this project will be documented in this file.

## [unreleased]

### Miscellaneous Tasks

- Update dependabot config ([0d6d451](https://github.com/DaRacci/Minix/commit/0d6d45155d8a2aee7898a68a290519cd42d9b319))

### Other

- Add paper repo back for paperdev plugin
  ([00bbe27](https://github.com/DaRacci/Minix/commit/00bbe270b319ca57d7697fa1808a8d74794c7f9d))
- Update minix-conventions version
  ([6f0a79b](https://github.com/DaRacci/Minix/commit/6f0a79b77427327727cb10b2abb3d1ffcba45f68))
- Ignore .idea/ because it keeps wanting to add itself to git
  ([a258a2c](https://github.com/DaRacci/Minix/commit/a258a2cfbf79c4e7ecf31535cbd67f1e17260925))
- [ci-skip]
  Add support for binding the plugins koin class to a different class
  ([b41d73a](https://github.com/DaRacci/Minix/commit/b41d73a01299169181ec77a918ed60539e34d135))
- [ci-skip]
  Added Any.invokeIfOverrides for cleaner invoking of units Added TameUtils.kt
  ([832bdb1](https://github.com/DaRacci/Minix/commit/832bdb152aeca3e83ed85be0535b2020c0ad8b89))
- [ci-skip]
  Added some new Ex functions for Players and entities Change more cache to caffeine cache
  ([3f5d21c](https://github.com/DaRacci/Minix/commit/3f5d21c3f5989c4e742d9830b7804b0e87ef9b43))
- [ci-skip]
  Better output of errors for Extensions, removed Exceptions.kt due to being kinda useless
  ([83caea1](https://github.com/DaRacci/Minix/commit/83caea1070d79b58cfc09bc1ecaf0c473779885a))
- [ci-skip]
  Add some utility functions
  ([d062e69](https://github.com/DaRacci/Minix/commit/d062e69802a71aa6d5ebfab4670c96c705a42ae9))
- [ci-skip]
  Move aliases and remove unused file
  ([5322b1f](https://github.com/DaRacci/Minix/commit/5322b1fc4ca4b55dd8a0b2945ad1327c63613315))
- [ci-skip]
  Update to Minix version 0.4.43 - Moved to gradle catalog for dependencies, updated README.md a bit, added caffeine to replace guava caches as well as hikariCP
  ([66cb627](https://github.com/DaRacci/Minix/commit/66cb627851b2a214f73058f42a2f7f2369018add))
- [ci-skip] Refactoring Moved removing callbacks from an event listener to the handleUnload function Use for loop to trigger remove on all collections
  ([d61f0c1](https://github.com/DaRacci/Minix/commit/d61f0c1a50f4c177f0f37f903b16986d121ea893))
- [ci-skip] Refactoring Refactored the isDay utils and added isOverworld etc
  ([29de559](https://github.com/DaRacci/Minix/commit/29de5597837be5d7b025558ec07d34fa052686fb))
- [ci-skip] Minor Actually it seems i do indeed use nms lol, need nms for the internal mojang profile stuffs, also added reobfJar dependsOn for assemble since nms will break without it
  ([d410811](https://github.com/DaRacci/Minix/commit/d4108118720a09cf628cc0e64965ce174058356a))
- [ci-skip] Minor Remove NMS from build.gradle.kts as it isn't used here Fixed fucking idea from formatting with the wrong settings so now i wont have to recommit every file lol Hopefully successfully removed all references to the core package in the api to get ready for
  splitting them.
  ([8ffb885](https://github.com/DaRacci/Minix/commit/8ffb885440186eae1271f2cb511a1e640018f69d))
- [ci-skip] Cleanup leftover files
  ([d1464ce](https://github.com/DaRacci/Minix/commit/d1464cead08e080c80efcde3eeba89d082d3f46d))
- Getting closer to release Updated to minix-conventions version 0.3.35 Download all required libraries from the plugin.yml libraries thingy, Shaded adventure kotlin and minimessage into plugin for so i can use updated minimessage cool stuffs while not breaking the normal
  adventure API Did a ton more formatting everywhere to abide to the Ktlint rules Updated extensions so you can now specify which class to bind them to for example an internal impl class binding to the api interface Updated the CoroutineScheduler to now take Kotlin Durations
  instead of milliseconds Added Task names so you can keep track of tasks through the scheduler, note however this will cause some overhead when finding by name compared to by id, if a name isn't given to a task it will be named by the ID Added getting an IntArray of a plugins
  active tasks, getting a task by name and canceling a task by name Updated ObservableCollections.kt and now return the key and values of the map which the action was called from. Added KClass<*>.doesOverride(method), T?.invokeIfNotNull(block), T?.invokeIfNull(block),
  Boolean?.ifTrue(block), Boolean?.ifFalse(block) to KotlinUtils.kt Removed ACFPaper from being included with Minix, this also removes the well. completely useless reload command. Removed support for reloading plugins, extensions etc. Nothing good comes from it. Changed the
  ListenerService.kt listeners back to normal events and not eventflows since it does indeed suspend and wait Fixed the PlayerServiceImpl.kt cancelling all chat messages. The TimeService no longer runs if there are no players online Did some cleanup and updates inside of Minix.kt

NOW for the big changes

Added PluginData.kt and PluginServiceImpl.kt to manage all the logic for loading plugins, extensions and all that cool stuff from within minix, meaning that backwards compatibility will be made sure to be kept so that all Minix based plugins can update the Minix plugin without
worry of breaking their own. Methods like handleEnable are only called if overridden now. Extensions are now Topologically sorted by dependencies thanks to the magic of Github co-pilot which i got today. The kotlin logging lib has been removed and i have written my own simple
logging system which uses the plugins java logging level. Loggers now have colours to easily define if it is a error, debug, info message etc all logging is still lazy like the kotlin logging lib so there is almost 0 overhead.
([d005fb5](https://github.com/DaRacci/Minix/commit/d005fb525b47491af4fd0cd9f25847b43f6131d0))

- [ci-skip] Fixes Forgot to add the forceAsync param for the inline event listener things
  ([0f77855](https://github.com/DaRacci/Minix/commit/0f77855546e1975893e340403ebf170c9431ebfd))
- [ci-skip] Fixes Fixed the imports in PlayerLiquidEvent.kt Changes dependencies KClass to an out because am dumb lol
  ([a93a86b](https://github.com/DaRacci/Minix/commit/a93a86b910400323fd4b383dca77f7ad359b0955))
- Woopsy fix the skip message being [SKIP] and not [ci-skip]. also seems i forgot to commit the removal of old raccicore classes
  ([25eca15](https://github.com/DaRacci/Minix/commit/25eca1589c7f6d7f274118c0a9ba8c3572ae1779))
- [ci-skip] Coming in with another set of updates

API:
Transitioned to the official kotlin rule spec via ktlint instead of the custom rules from detekt. Returned back to single module while in dev phase, will be transitioned to separated once i have figured koin out and everything. Added new aliases folder, all aliases will be moved
to here soon. Removed some redundant annotations and added RunAsync.kt which will enforce that event methods are called async. Merged MCCoroutine into project and have began making changes and improvements to better connect it to the core of Minix. Added some destructors for
common bukkit stuff. Refactored and improved the events. Added Extension.kt and all other things in that directory as a replacement to Lifecycle.kt, Extensions will be tightly integrated with koin in order to eliminate writing instance variables for each manager, service etc as
well as binding internal Impl classes to interfaces for a much cleaner API. Extensions will also include a plugin-like dependencies list which will ensure your extensions are loaded in a correct order. Did some refractors in a lot of the Extensions, major changes within
ExEvent.kt Added MinixLogger.kt so that i can base the logger level off the plugins debugging level. Split MinixPlugin.kt into SusPlugin.kt for the documents and methods which are only overridden. Added an extension, command and listener dsl builder for MinixPlugin.kt Added a lot
of new serializable things n stuffs. Renamed Managers to Services and are now obtainable via koin. Added MultiMap.kt in order to replace googles multimap and incorporate more kotlin friendly interactions. Made some new additions to ObservableCollections.kt to make them more
useful (Since im actually using them now lol). Added utils for saving minecraft objects such as a Location within the jetbrains exposed framework.

Core:
Moved listeners and stuff into ListenerService.kt, they are also flows now which i haven't tested and honestly don't think will work but i will be testing as it allows some very nice and clean filtering and instance checking. Added Sentry logging to gather logs created by minix
or my own plugins for easier bug tracking and fixing.

Other stuff:
Updated to Minix-Conventions version 30 Started redoing README.md with plans of implementing some actual documentation alongside this.
([d72129b](https://github.com/DaRacci/Minix/commit/d72129b20d4ce565d8861047f1e8e8d6c41ee78f))

- [ci-skip] Beginning of DEV for 1.0.0 Release

## Major changes

* Rebranding from RacciCore to Minix.
* Now using Minix-Conventions for setting up and keeping gradle clean between all projects.
* Modules are now going to be split up into specific sections such as API for general use, Kotlin for kotlin based extensions usable outside of minecraft development, and Core which will contain the backend brains of minix.

## Changes

* Added Annotations for DSL, Preview features and Experimental features.
* Removed Jitpack.yml due to moving away from jitpack and to my own hosted repo.
* No longer support versions under 1.18.1 though this doesn't mean they wont work.
* Updated to latest kotlin and kotlinx versions via Minix-Conventions.
* Updated to gradle 7.3.3
* Generate the plugin.yml through a gradle plugin now so we don't have the janky shit like before.
* Updated Item builders with new docs, methods and a cleaner overall codebase.
  ([f6f3efb](https://github.com/DaRacci/Minix/commit/f6f3efb70c81a870d7f90b647af9447ffdc66781))

- Updated minimessage stuffs, added adventure extra kotlin
  ([9417f19](https://github.com/DaRacci/Minix/commit/9417f19c5ac794ccf7dcc213cb1190e9dcf92733))
- Merge pull request #13 from DaRacci/dependabot/gradle/net.kyori-adventure-text-minimessage-4.10.0-SNAPSHOT

Bump adventure-text-minimessage from 4.2.0-SNAPSHOT to 4.10.0-SNAPSHOT ([e6ac410](https://github.com/DaRacci/Minix/commit/e6ac410f4364207532352b6143f1265485145db7))

- Merge pull request #12 from DaRacci/dependabot/gradle/org.geysermc.floodgate-api-2.1.1-SNAPSHOT

Bump api from 2.1.0-SNAPSHOT to 2.1.1-SNAPSHOT ([2fa7385](https://github.com/DaRacci/Minix/commit/2fa73852382a79a0fa9de5d51e52ebd7aba60cb8))

- Bump adventure-text-minimessage from 4.2.0-SNAPSHOT to 4.10.0-SNAPSHOT

Bumps adventure-text-minimessage from 4.2.0-SNAPSHOT to 4.10.0-SNAPSHOT.

---
updated-dependencies:

- dependency-name: net.kyori:adventure-text-minimessage dependency-type: direct:production update-type: version-update:semver-minor ... ([d85d362](https://github.com/DaRacci/Minix/commit/d85d362d727b8b035725508ee29e9fdf8007a9ee))
- Bump api from 2.1.0-SNAPSHOT to 2.1.1-SNAPSHOT

Bumps [api](https://github.com/GeyserMC/Floodgate) from 2.1.0-SNAPSHOT to 2.1.1-SNAPSHOT.

- [Release notes](https://github.com/GeyserMC/Floodgate/releases)
- [Commits](https://github.com/GeyserMC/Floodgate/commits)

---
updated-dependencies:

- dependency-name: org.geysermc.floodgate:api dependency-type: direct:production update-type: version-update:semver-patch ... ([9f7803e](https://github.com/DaRacci/Minix/commit/9f7803e1d0d52f256a1ca51f6b15c556a6610871))
- Yeah fuck those docs
  ([4b80bf4](https://github.com/DaRacci/Minix/commit/4b80bf4d216e98c469e97ee6f43c4c2379dc60c4))
- Jekyll docs test
  ([142f163](https://github.com/DaRacci/Minix/commit/142f16331cfebacf99bdb9c9b72d234ad914b9f7))
- Merge remote-tracking branch 'origin/master'
  ([1641ca8](https://github.com/DaRacci/Minix/commit/1641ca8b658478238888aac2c7da8613826c00a3))
- Fucking updatey bullshitery
  ([7aa8c9f](https://github.com/DaRacci/Minix/commit/7aa8c9f36a3b2dc62642f76e4c6995c2b9e9c341))
- Create CNAME ([e0f9585](https://github.com/DaRacci/Minix/commit/e0f9585781f380b7fd055ef9577f2339401f2afd))
- Set theme jekyll-theme-cayman ([7885f53](https://github.com/DaRacci/Minix/commit/7885f53a869d8b2162c726c84eef54d93c66111d))
- Set theme jekyll-theme-midnight ([883e49c](https://github.com/DaRacci/Minix/commit/883e49c16413b9ca53deb116569f3caa66be92f2))
- Im abit dumb...
  ([2d79858](https://github.com/DaRacci/Minix/commit/2d798580c8ddc0ecb9d766e9eba8e9595e8068dc))
- Stuffs
  ([748f317](https://github.com/DaRacci/Minix/commit/748f31711d3c6b36d58b5e865f97e372a3368497))
- Update ExMiniMessage.kt
  ([5849e3c](https://github.com/DaRacci/Minix/commit/5849e3cf0c62f4f8e7912b9fcf0cd48238a84de9))
- Update Docs
  ([1557d01](https://github.com/DaRacci/Minix/commit/1557d01fd34db941fc0fdcafda26a70a90fffb39))
- Update Docs
  ([66e9192](https://github.com/DaRacci/Minix/commit/66e919246bfdf2f1e25de42c98d33d47d958140c))
- Update Docs
  ([59c0fb8](https://github.com/DaRacci/Minix/commit/59c0fb874ec2e3a77ab897946295356e0acd7daa))
- Iyuhkragsd
  ([484b0ca](https://github.com/DaRacci/Minix/commit/484b0ca7bc55f1edb59fa2388693685ab6f92932))
- Merge remote-tracking branch 'origin/master'

# Conflicts:

# build.gradle.kts

([211864b](https://github.com/DaRacci/Minix/commit/211864b729fcedc307601c569500cc1cba29bc4d))

- Iyuhkragsd
  ([f18d930](https://github.com/DaRacci/Minix/commit/f18d930328ba771b35bf38580b98d69baa65f086))
- Iyuhkragsd
  ([c286e43](https://github.com/DaRacci/Minix/commit/c286e436a41a174e0fbe49cbd6eedf535bc36755))
- Okay okay, maybe????
  ([4118dda](https://github.com/DaRacci/Minix/commit/4118ddac15186e997d00d10b5a128f32a364b66a))
- Okay okay, maybe?
  ([5c8a7d1](https://github.com/DaRacci/Minix/commit/5c8a7d1b55bdb190db92866e912c967c115532ae))
- Merge remote-tracking branch 'origin/master'
  ([d065135](https://github.com/DaRacci/Minix/commit/d065135632eea21f85e7ff68a9d3f54d6a651cf2))
- Aefd
  ([372e8b7](https://github.com/DaRacci/Minix/commit/372e8b7ed3f2f54576dbfee235e6f4d6861dd29e))
- Aefd
  ([8dff72a](https://github.com/DaRacci/Minix/commit/8dff72a60e9ec3a1b78ed66c315e53d4a9c4d8d3))
- Sdhyufigbsejglawerg
  ([82cf1c9](https://github.com/DaRacci/Minix/commit/82cf1c91fc14f3f066aabfe34c279b7a45f6bda0))
- Merge remote-tracking branch 'origin/master'
  ([3c24f25](https://github.com/DaRacci/Minix/commit/3c24f25e570be0e22654b439c231bb316c63716a))
- Aaaaaaaaaaaaaaaaaaaaaaaaaaaa
  ([b8862c0](https://github.com/DaRacci/Minix/commit/b8862c07dfeee7de6303e74b0211c11ce6eafda8))
- Aaaaaaaaaaaaaaaaaaaaaaaaaaaa
  ([774d1a1](https://github.com/DaRacci/Minix/commit/774d1a1d6ecd877c6e027d7ee3feeb7291ab6ebb))
- Please... fucking please
  ([9d3156f](https://github.com/DaRacci/Minix/commit/9d3156fd51265c9a920261b764031e47566f6001))
- Merge remote-tracking branch 'origin/master'
  ([e98726c](https://github.com/DaRacci/Minix/commit/e98726c55b98a6c20f084945738c5506e0e7b7a0))
- Please... fucking please
  ([bd96df6](https://github.com/DaRacci/Minix/commit/bd96df6b3fc990071e3674b23b0cff92b82d6e73))
- Please... fucking please
  ([1e13810](https://github.com/DaRacci/Minix/commit/1e1381085d4bc35f28cadf2a2f492399e90d5d32))
- Merge remote-tracking branch 'origin/master'

# Conflicts:

# .github/workflows/java-ci.yml

([c0867a2](https://github.com/DaRacci/Minix/commit/c0867a2e7d4261d4371a1235946a5221034a6388))

- Please... fucking please
  ([aee3cab](https://github.com/DaRacci/Minix/commit/aee3cabfdf335ea43c9e4c7ce57e450d4c2ee01b))
- Please... fucking please
  ([0b1c1f1](https://github.com/DaRacci/Minix/commit/0b1c1f1ddb9f3c3ceaeca616cf0f4c57e38ce407))
- Maybe...
  ([6584944](https://github.com/DaRacci/Minix/commit/6584944df94eada3ca9f05e28d99b22d5b3d332c))
- Maybe with a group change?
  ([fc03324](https://github.com/DaRacci/Minix/commit/fc033246d0123d52a29b789f7625f12644a6c5a2))
- Im so confused
  ([310d6c1](https://github.com/DaRacci/Minix/commit/310d6c1dec1fdfe46109a5ef9f6877374cd31593))
- Okay no actually this time
  ([73b0e44](https://github.com/DaRacci/Minix/commit/73b0e44987e1f2b0fea50cca63f635bcb647c3e3))
- Okay no this time
  ([b32577b](https://github.com/DaRacci/Minix/commit/b32577b7907aecd86eb3acc912710e78ced2cee2))
- Yes yes please i think i did it
  ([8661420](https://github.com/DaRacci/Minix/commit/8661420d1cc73d1352a369bc1b1bd2990117ba84))
- Try fix jitpack this time again
  ([124f240](https://github.com/DaRacci/Minix/commit/124f2405dd3e05e72a5f3b1d38777f4dda60930f))
- Try fix jitpack this time?
  ([fc7209f](https://github.com/DaRacci/Minix/commit/fc7209f781d226838f0cdc3a27b95c88b0e94aee))
- [SKIP] try fix jitpack
  ([85f9d42](https://github.com/DaRacci/Minix/commit/85f9d424aa7859018bef933b598b4e67cdf4dd2f))
- Merge pull request #11 from DaRacci/dependabot/gradle/org.geysermc.floodgate-api-2.1.0-SNAPSHOT

Bump api from 2.0-SNAPSHOT to 2.1.0-SNAPSHOT ([944d77f](https://github.com/DaRacci/Minix/commit/944d77fea6daac4d6a0b3be724d98cd284240553))

- Bump api from 2.0-SNAPSHOT to 2.1.0-SNAPSHOT

Bumps [api](https://github.com/GeyserMC/Floodgate) from 2.0-SNAPSHOT to 2.1.0-SNAPSHOT.

- [Release notes](https://github.com/GeyserMC/Floodgate/releases)
- [Commits](https://github.com/GeyserMC/Floodgate/commits)

---
updated-dependencies:

- dependency-name: org.geysermc.floodgate:api dependency-type: direct:production ... ([301cc2a](https://github.com/DaRacci/Minix/commit/301cc2a88ae13490cc390571b1e33ee47357333b))
- [SKIP] lets try this again
  ([86b6e7d](https://github.com/DaRacci/Minix/commit/86b6e7d8a6cb2a3a924b19686ff24ed925915f62))
- [SKIP] try fix jitpack
  ([f97bef4](https://github.com/DaRacci/Minix/commit/f97bef4695ddc076b50a3f34939c89467bc0ebbd))
- [Release] Update 0.3.0-SNAPSHOT build.gradle.kts:
- Fix Transitive API
- Add floodgate as compileOnly
- Exclude adventure and minimessage from purpur API
- remove Idea plugin API:
  Builders:
- Moved to API Package
- Removed EntityBuilder.kt, may or may not be looked into at a later date Events:
- Moved to API Package
- Removed typealias of KotlinEvent
- Added PlayerUnloadEvent.kt Flows:
- Moved to API Package Lifecycles:
- Moved to API Package Plugins:
- Moved to API Package Scheduler:
- Moved to API Package
- Renamed ITask to CoroutineTask
- Remove ICoroutineRunnable.kt all just in the abstract class now
- Renamed ICoroutineScheduler to CoroutineScheduler
- Did some formatting in CoroutineScheduler.kt Serializer:
- Added MiniMessageSerializer.kt and UUIDSerializer.kt for KotlinXSerialization Utils:
- Moved to API Package
- Organized the Utils
- Did some formatting
- Added some javadocs
- Added FloodgateUtils.kt Extensions:
- Moved to API Package
- Added ExData.kt and ExItemStack.kt Removed Hooks for now, will remake later. Removed IFactory.kt and IManager.kt Removed Provider.kt Core:
  Data:
- Changed constructor data to UUID from player
- Removed init and close method
- Added inAccess to check if its being used from Sylph Listeners:
- Moved the full block move checker from PlayerMoveListener.kt to PlayerMoveXYZListener.kt Managers:
- Added HookManager with floodgate hook
- Removed Provider.kt use companion objects now PluginManager.kt:
- Moved Plugin enable logic here, made into a anon class that acts on its own.
  ([de9dbfe](https://github.com/DaRacci/Minix/commit/de9dbfed15006ad94d18c0192b150de6a888dbbe))
- Merge pull request #10 from DaRacci/dependabot/gradle/org.jetbrains.dokka-1.6.0

Bump org.jetbrains.dokka from 1.5.31 to 1.6.0 ([b100538](https://github.com/DaRacci/Minix/commit/b100538bbd3fe2a9c51f19af3990ba00e747fa70))

- Bump org.jetbrains.dokka from 1.5.31 to 1.6.0

Bumps [org.jetbrains.dokka](https://github.com/Kotlin/dokka) from 1.5.31 to 1.6.0.

- [Release notes](https://github.com/Kotlin/dokka/releases)
- [Commits](https://github.com/Kotlin/dokka/compare/v1.5.31...v1.6.0)

---
updated-dependencies:

- dependency-name: org.jetbrains.dokka dependency-type: direct:production update-type: version-update:semver-minor ... ([5cf5b35](https://github.com/DaRacci/Minix/commit/5cf5b35c3ab6b0c8dd767374452b3a57f14034c2))
- [Release] Update 0.2.7
  ([b1e4125](https://github.com/DaRacci/Minix/commit/b1e4125ba76ebf944b76431e68be30ee0572a1f0))
- [SKIP] Deprecated in favour of lifecycles
  ([d3e35b6](https://github.com/DaRacci/Minix/commit/d3e35b6a1f81f3d9696a37ef7d0e795100bd4caf))
- [SKIP] Move WIP stuff to dev branch
  ([417100f](https://github.com/DaRacci/Minix/commit/417100f8ff114c085a89431fe6e671c45d24b2d4))
- [SKIP] Update Kotlin
  ([6d99412](https://github.com/DaRacci/Minix/commit/6d99412b62a28211f19e5fa9f0d1e14765ae0cd7))
- Merge pull request #9 from DaRacci/dependabot/gradle/jvm-1.6.0

Bump jvm from 1.6.0-RC2 to 1.6.0 ([791da0f](https://github.com/DaRacci/Minix/commit/791da0ff744b7adb2d36cd842ab576ffcf843eb3))

- Bump jvm from 1.6.0-RC2 to 1.6.0

Bumps jvm from 1.6.0-RC2 to 1.6.0.

---
updated-dependencies:

- dependency-name: jvm dependency-type: direct:production update-type: version-update:semver-patch ... ([1eb992e](https://github.com/DaRacci/Minix/commit/1eb992e45c513b21c5163068e726643791fe890a))
- Merge pull request #7 from DaRacci/dependabot/gradle/plugin.serialization-1.6.0

Bump plugin.serialization from 1.5.31 to 1.6.0 ([bb0b246](https://github.com/DaRacci/Minix/commit/bb0b2461d047dc3fa77757002510da3b55d96219))

- Bump plugin.serialization from 1.5.31 to 1.6.0

Bumps plugin.serialization from 1.5.31 to 1.6.0.

---
updated-dependencies:

- dependency-name: plugin.serialization dependency-type: direct:production update-type: version-update:semver-minor ... ([6314e35](https://github.com/DaRacci/Minix/commit/6314e350332bd13bc1030c9fbceea0d9ac92ca62))
- [Release] Bump version to 0.2.6
  ([57a4509](https://github.com/DaRacci/Minix/commit/57a4509cbe9dc858270157778efb2f982ffb422e))
- [SKIP] WIP Lang and Command framework
  ([62862bf](https://github.com/DaRacci/Minix/commit/62862bfafcc66495af9df6b78c705d617300da7a))
- [SKIP] Unprivated
  ([1003c12](https://github.com/DaRacci/Minix/commit/1003c129084e41718b04c318688bf76b591cede6))
- [SKIP] Updated to fix errors after manager updates
  ([891900c](https://github.com/DaRacci/Minix/commit/891900c09d27e55e306a1618bbfd2dbbdd2ca1a2))
- [SKIP] Updated to fix error after manager updates
  ([cc6f7b5](https://github.com/DaRacci/Minix/commit/cc6f7b57d59e8a3e9beba1c5d6321d22e631984a))
- [SKIP] Sealed AbstractComboEvent.kt
  ([aabb865](https://github.com/DaRacci/Minix/commit/aabb865f0b7ceb6ce317f40d206558772bd010ff))
- [SKIP] Move Managers to core package
- Managers are now utilizing lifecycles
- Provider.kt is to be used instead of the direct object of the manager as they are now classes.
  ([df1b2e2](https://github.com/DaRacci/Minix/commit/df1b2e2166dc6eb671c429f960ce180df88a6b63))
- [SKIP] Add reload command
  ([7a3ee7b](https://github.com/DaRacci/Minix/commit/7a3ee7bb95734ffc3b2935db70acd3338a8464a4))
- [SKIP] Add Lifecycles.
- Lifecycles are like their own mini little plugins once enabled, they will receive load, enable, reload and disable events and can do their own thing from there.
  ([ea36849](https://github.com/DaRacci/Minix/commit/ea368495c560226813a95c5f7ceab4dda11ac96f))
- [SKIP] Add Managers
  ([44d3a9c](https://github.com/DaRacci/Minix/commit/44d3a9cc70d01f9636dff1130c501c72febb4c8b))
- [SKIP] Javadocs
  ([cdc9891](https://github.com/DaRacci/Minix/commit/cdc989199ba18fe2b49a32e1c4320e28435e7edc))
- [SKIP] Formatting
  ([e97b3eb](https://github.com/DaRacci/Minix/commit/e97b3eb1e374047699fe75ab3f17dd2dea7f7138))
- [SKIP] Fix nullability
  ([486cbde](https://github.com/DaRacci/Minix/commit/486cbde2bb43d1cfae3db57adac1c906fababa68))
- [SKIP] Fix
  ([fde71d8](https://github.com/DaRacci/Minix/commit/fde71d887e907e74a54bff745e9695eca709bc6d))
- [SKIP] Move
  ([04d97eb](https://github.com/DaRacci/Minix/commit/04d97eb5a041e086af2538ef5569584f6bdab1ac))
- [SKIP] Fix spelling
  ([8c64020](https://github.com/DaRacci/Minix/commit/8c640208aef478ebe134358896c4f953f773b8e0))
- [SKIP] Moved to ExPersistentDataContainer
  ([c8e46a3](https://github.com/DaRacci/Minix/commit/c8e46a3aebac42b2ab5ab920d88c71bd7635c510))
- [SKIP] Fix plugin to be RacciPlugin
  ([52ac6dd](https://github.com/DaRacci/Minix/commit/52ac6ddf1c3f62180a050738805089a79c840893))
- [SKIP] Add catchAndReturn
  ([b80d72e](https://github.com/DaRacci/Minix/commit/b80d72ec1f69e12f523d0de731b26511e729827f))
- [SKIP] Add BungeeCordUtils.kt, PlayerUtils.kt and PluginUtils.kt
  ([a5b8a27](https://github.com/DaRacci/Minix/commit/a5b8a2770e819cc15b24dacdeff15527067885c0))
- [SKIP] fix main class path
  ([59ec349](https://github.com/DaRacci/Minix/commit/59ec349bdb6b27103b0a512ff84c51280fc17cb5))
- [SKIP] Build changes and bump version to 0.2.5
  ([6547cff](https://github.com/DaRacci/Minix/commit/6547cff125d372177a032cd3886b054f065ba31e))
- [SKIP] Grammar
  ([5c271ad](https://github.com/DaRacci/Minix/commit/5c271ad4c8769565ce50f9d96949d6892e319e5a))
- [SKIP] Missed file from push
  ([204f344](https://github.com/DaRacci/Minix/commit/204f344e89bad9e20bef50114963b2427c261f35))
- [SKIP] Remove
  ([5cdaeb9](https://github.com/DaRacci/Minix/commit/5cdaeb94dd5f7bc28d4a1b2a92dad9d2a3c5421b))
- [SKIP] Add more Extensions
  ([8aa17fd](https://github.com/DaRacci/Minix/commit/8aa17fd832ec10a49287c545030d5d2ce0747ece))
- [SKIP] Move extensions to separate folder
  ([827e767](https://github.com/DaRacci/Minix/commit/827e767764e3af6308d0f08799887431c572b061))
- [SKIP] Small changes and fixes
  ([fe73b64](https://github.com/DaRacci/Minix/commit/fe73b6454c8c2cbc8cb20d9d91f9c17c1ea45c47))
- Move core plugin to Separate folder
  ([c5c34c1](https://github.com/DaRacci/Minix/commit/c5c34c189d9f9badd7eafdf4d9bacb763531fcdc))
- [SKIP] Remove, tbh i don't see a use for these
  ([fc1d252](https://github.com/DaRacci/Minix/commit/fc1d252918d2833bffa7c9ba865faca48f5747b0))
- Merge remote-tracking branch 'origin/master' into master
  ([75a600c](https://github.com/DaRacci/Minix/commit/75a600cc79d2ff048ce22a673adfab9f5035f484))
- [SKIP] Update Javadoc and formatting for CollectionUtils.kt
  ([19f5407](https://github.com/DaRacci/Minix/commit/19f54072e63bad4092485812e6213d9942333ecf))
- Merge pull request #5 from DaRacci/dependabot/gradle/com.mojang-authlib-2.3.31

Bump authlib from 1.5.21 to 2.3.31 ([b72bd2d](https://github.com/DaRacci/Minix/commit/b72bd2d800a2d47ae2a11b21f3f75ec5a9a8febe))

- [SKIP] Fix ComboListeners
  ([c4a384a](https://github.com/DaRacci/Minix/commit/c4a384a245209304221ca1966c9bd3a403d0a322))
- Bump authlib from 1.5.21 to 2.3.31

Bumps authlib from 1.5.21 to 2.3.31.

---
updated-dependencies:

- dependency-name: com.mojang:authlib dependency-type: direct:production update-type: version-update:semver-major ... ([af1d9dd](https://github.com/DaRacci/Minix/commit/af1d9ddec36efaf97eb28d4e97efd2026214435e))
- [Release] Update 0.2.4
- Fix dependencies
- Update to gradle 7.3
- Reorder build.gradle.kts slightly (still needs cleanup)
  ([4f0a086](https://github.com/DaRacci/Minix/commit/4f0a086f5db26d33348179c3e0fb9593336ce4f6))
- [SKIP] Unmark Coroutine Scheduler ask Experimental
  ([3f38a0c](https://github.com/DaRacci/Minix/commit/3f38a0c9ed5266c53a746cbdb8810134708eb70b))
- [SKIP] Version 0.2.3 Rewrite the ComboEvents.kt again,
- Remove individual Classes for entity events and block events
- Use class Reflection to make the constructor instead of when stacks
- Shorten code a lot
- Create AbstractComboEvent.kt for general functions inside all ComboEvents.kt
  ([d07c825](https://github.com/DaRacci/Minix/commit/d07c825cba1e48ead882f9457f6ed4c8402be7c1))
- [SKIP] Update Utils
- Add reflection class constructor util
- Fix Deprecated method
  ([eb54201](https://github.com/DaRacci/Minix/commit/eb5420193b1dd44dcc2bd001b1b4853173850bbe))
- Merge remote-tracking branch 'origin/master' into master
  ([48e4590](https://github.com/DaRacci/Minix/commit/48e45902bc2d6d25a1fb2825afc508fb90731e49))
- [SKIP] Build changes
- Move off version catalog for now as its being really buggy
- Disable Auto downloading of Kotlin and stuff as Bukkit is having the shits with me, and I need to figure it out later
  ([f0c1592](https://github.com/DaRacci/Minix/commit/f0c159270602dbeb1dd80fbd1c2326758ceac608))
- [SKIP] Build changes
- Move off version catalog for now as its being really buggy
- Disable Auto downloading of Kotlin and stuff as Bukkit is having the shits with me, and I need to figure it out later
  ([6bcf702](https://github.com/DaRacci/Minix/commit/6bcf70259791d4a7b06062e1f7aa1aeefaeb3bc1))
- [SKIP] Add EnumUtils.kt
  ([90ef567](https://github.com/DaRacci/Minix/commit/90ef5670d3584166639dc63fdc934dd004cef230))
- Merge remote-tracking branch 'origin/master' into master
  ([573b396](https://github.com/DaRacci/Minix/commit/573b396b41bee75ab03055f0d0d2553724a9d728))
- [SKIP] god im so sick of this shit just please fix
  ([9365759](https://github.com/DaRacci/Minix/commit/936575973fc205b4ef1910f25a107d0a83521c4a))
- [SKIP] god im so sick of this shit just please fix
  ([3310563](https://github.com/DaRacci/Minix/commit/3310563d9f523bf37ba3064d9a985fa06c0dcd34))
- Merge remote-tracking branch 'origin/master' into master

# Conflicts:

# settings.gradle.kts

([852d7ed](https://github.com/DaRacci/Minix/commit/852d7eddf3da53027b05bd6fee3897d4ec185102))

- [SKIP] Fix jitpack maybe yes yes?
  ([3cf4249](https://github.com/DaRacci/Minix/commit/3cf4249c76f012d69d8de325387dc0f4adf69ce9))
- [Release] Fix jitpack maybe yes yes?
  ([5c7d31c](https://github.com/DaRacci/Minix/commit/5c7d31ccb38219af680fcd249d3fd1bb5658806f))
- [Release] Updates vars n stuff and fix jitpack hopefully
  ([6595f29](https://github.com/DaRacci/Minix/commit/6595f2976173471befb74546891d56f680c416c0))
- [SKIP] Add missing non shift events
  ([76ccf50](https://github.com/DaRacci/Minix/commit/76ccf50cf9e7fcdd8efe4a3e1e57c3cc48f43b8b))
- [SKIP] Rewrite for new listeners and add InteractAtEntity and PlayerAttackEntity listeners
  ([5a0927c](https://github.com/DaRacci/Minix/commit/5a0927c6c73583a6c8cb32a837db360e09d0cbb0))
- [SKIP] Add javadocs
  ([b71381f](https://github.com/DaRacci/Minix/commit/b71381faba105bb8364f8b78e8773b83f2d0ffc0))
- [SKIP] Update javadocs to represent that it is fired Async
  ([1d3676a](https://github.com/DaRacci/Minix/commit/1d3676a3a245824f9c45528b72deb50580af56eb))
- [SKIP] Add Javadocs and change Supertypes to KWorldEvent
  ([43e482f](https://github.com/DaRacci/Minix/commit/43e482f26d7a0294a85424a36a9941791f6c7a8e))
- [SKIP] Add Javadocs and renaming
- Renamed KotlinEvent to KEvent for consistency.
- Added KWorldEvent
  ([cbb0e5a](https://github.com/DaRacci/Minix/commit/cbb0e5a2672aa96c3e3b849587ade454a5f9cbda))
- [SKIP] Add Javadocs and renaming
- Renamed KotlinEvent to KEvent for consistency.
- Added KWorldEvent
  ([ddfc8cd](https://github.com/DaRacci/Minix/commit/ddfc8cdc227db1e0fc352803221ace94f0f5739b))
- [SKIP] Add Javadocs and renaming
- Renamed KotlinEvent to KEvent for consistency.
- Added KWorldEvent
  ([6717e4a](https://github.com/DaRacci/Minix/commit/6717e4a7f97a340969aa312e82196543b26a9a60))
- [SKIP] Update Javadoc
  ([53959e1](https://github.com/DaRacci/Minix/commit/53959e11753c06fb494140eaeb0a36c36016845b))
- [SKIP] New combo events and interfaces
- Combo events are being split into sections, so ComboEvents.kt will still fire as normal with everything, ComboEntityEvents.kt will fire instead of ComboEvents.kt if an entity is involved, and ComboBlockEvents.kt will fire instead of ComboEvents.kt if a block is involed
  ([1c8a25f](https://github.com/DaRacci/Minix/commit/1c8a25ffa48cb469f724e42e850376280788b170))
- [SKIP] Update javadoc and improve liquidType param
  ([1f08592](https://github.com/DaRacci/Minix/commit/1f085920253b7e2e511cbce05b690273a5f65ebc))
- [Release] 0.2.1 Update
- Disable debug being enabled by default
- Disable downloading kotlin etc for now
- Fix offhand events being fired sync instead of async
  ([4288596](https://github.com/DaRacci/Minix/commit/4288596903d3ebaa4d72280ccaf43c73cdca1282))
- [SKIP] Fix debugging timer
  ([cbb4b43](https://github.com/DaRacci/Minix/commit/cbb4b43f734398f516dfe8f54d810d8649a788b8))
- [SKIP] Fix Builders
  ([3f2bc57](https://github.com/DaRacci/Minix/commit/3f2bc5738753909b56b3ebb7199f47874f6673f5))
- [SKIP] Github url fixes
  ([b3b23f1](https://github.com/DaRacci/Minix/commit/b3b23f16b776f0e92572f5cd2660f7554e1c941a))
- [SKIP] Libs change Move libs.versions.toml to separate project for use in all projects instead of having to copy past everywhere when one thing changed
  ([8cf0a2c](https://github.com/DaRacci/Minix/commit/8cf0a2c285cb1ed9e7575c5878b8eaf9272e5d4f))
- [SKIP] Docs update
  ([743cc60](https://github.com/DaRacci/Minix/commit/743cc60600140cbdbe6f03eeb1317c9513660adc))
- [Release]
  ([5d60b33](https://github.com/DaRacci/Minix/commit/5d60b33791e49274aa1ec0f8bab544f2d495c745))
- Merge remote-tracking branch 'origin/master' into master
  ([0ea10cd](https://github.com/DaRacci/Minix/commit/0ea10cdb57a6efcb8f8489372283ddfee85fc22a))
- Update 0.2.0 RacciPlugin.kt - Removed Deprecated for 0.2.0 and the registerRunnables fun, as this is no longer used with the new CoroutineRunnables

CoroutineScheduler.kt - Remove old KotlinRunnable.kt and RunnableManager.kt Add new very sexy CoroutineScheduler.kt, hopefully replacing the BukkitScheduler once its finished and verified to be stable. When Running a task from a CoroutineRunnable.kt or CoroutineTask.kt it's ran
in a suspended Unit and is provided with both the plugin which owns the task/runnable and the CoroutineScope which it is contained within.

RacciCore.kt - add easier use internal methods and Fix some errors from changes elsewhere

build.gradle.kts - Add Kotlin stdLib, reflection and KotlinX Coroutines Core, Coroutines JVM, Datetime and Serialization to the API scope, so they are registered as included however these are still downloaded and run at runtime by spigot

gradle.properties - Bump version to 0.2.0 and enable gradle caching
([8a58874](https://github.com/DaRacci/Minix/commit/8a588741f23bd76a9c7955113ae838fd23f295c4))

- Rename and update import location
  ([1f90588](https://github.com/DaRacci/Minix/commit/1f905888aa7a277c0b2b176b95740c8ca8e35761))
- Update versions inside lib file
  ([50203bb](https://github.com/DaRacci/Minix/commit/50203bbf78213cd3b5c84b1e0420e8619b939e65))
- Fix now() usage
  ([2939c21](https://github.com/DaRacci/Minix/commit/2939c21e7e0203c33d3bbe4dbacfb5b88b81f1b7))
- Update import location
  ([b31b0a3](https://github.com/DaRacci/Minix/commit/b31b0a31d8c83cb1694eca9807f884a7bd6d43bb))
- Add more to BlockUtils.kt
  ([adeba21](https://github.com/DaRacci/Minix/commit/adeba21254c0e3ce093d02878fae6e7db09fd340))
- Update import location
  ([3056be9](https://github.com/DaRacci/Minix/commit/3056be9f058faca86f013000f9084676a39120c6))
- Fix pm being imported from the removed location
  ([1e4d36b](https://github.com/DaRacci/Minix/commit/1e4d36bd701e0da41e5352298bfdc50f471e2f98))
- Add IManager.kt interface
  ([ab47e4c](https://github.com/DaRacci/Minix/commit/ab47e4ccfdfff24e5e48eaf5f68abf1a9478810f))
- Update internal PlayerManager.kt
  ([c291759](https://github.com/DaRacci/Minix/commit/c29175926b6f99f58be31626e847551ab946221c))
- Removed
  ([0266c56](https://github.com/DaRacci/Minix/commit/0266c560336aa43b13b488eebaccb8aea45f4259))
- General Usage style improvements Removed dupe var fun methods and made most var
  ([143b154](https://github.com/DaRacci/Minix/commit/143b154d9645474d2e0821257d8a341aac6be8b1))
- Update now() usage
  ([d1f109c](https://github.com/DaRacci/Minix/commit/d1f109c682891ab441f2790d7c5f2fc28ffccc26))
- Remove useless methods
  ([b744772](https://github.com/DaRacci/Minix/commit/b7447723db055fa503272813e4e41c8cbc3e9276))
- Removed for 0.2.0
  ([27db8b9](https://github.com/DaRacci/Minix/commit/27db8b9709e4a43583e989c87e4434327a393940))
- Add ProjectileUtils.kt
  ([6ef99e0](https://github.com/DaRacci/Minix/commit/6ef99e0a132de50dc1dd913b99c1a9b1bfcc26ab))
- Add InventoryUtils.kt
  ([c394597](https://github.com/DaRacci/Minix/commit/c394597cf261e6285cbfb0b07550b83621892ad7))
- Add ClassUtils.kt
  ([bff7bcb](https://github.com/DaRacci/Minix/commit/bff7bcb714b3484b190289b300ca5faa6740e5fc))
- Remove useless method
  ([fcf25f6](https://github.com/DaRacci/Minix/commit/fcf25f6dc8be847cc94bd90671cad343abede314))
- Formatting
  ([4143c37](https://github.com/DaRacci/Minix/commit/4143c3779aa63e50c6792226c287da06e416e186))
- Remove useless component thingy
  ([778afa8](https://github.com/DaRacci/Minix/commit/778afa8be089c741467a9c1a3d51208173781673))
- Formatting
  ([737d5cb](https://github.com/DaRacci/Minix/commit/737d5cb38a88e25ffc7abd631cdd813647e78cde))
- Fix tab spacing
  ([6fb877a](https://github.com/DaRacci/Minix/commit/6fb877a71c5c1fc64089a2aea5578197d6c6e0c5))
- Rename file, move out of object cleanup
  ([1a9eb69](https://github.com/DaRacci/Minix/commit/1a9eb6953b7d0a010d0a9af6f1e282243066827a))
- Moved to ExBukkit.kt
  ([29d07d8](https://github.com/DaRacci/Minix/commit/29d07d8fb7daef99c3b0791e8b2997928fb3bd51))
- Moved out of folder
  ([376bc04](https://github.com/DaRacci/Minix/commit/376bc04fc74741e1ce8e47148d171661712edadf))
- Removed nowMilli() and nowNano()
  now() can now to used to turn into either, has also become an instant type
  ([4136948](https://github.com/DaRacci/Minix/commit/41369480d9e88d93dced6f7938f8dc98c9f6d1b6))
- Remove ExceptionUtils.kt, shouldnt be in the core lib
  ([f7e7ee5](https://github.com/DaRacci/Minix/commit/f7e7ee50122d9c60a85b161c1c5efff31ab4417a))
- Remove files utils, may or may not come back
  ([d83108e](https://github.com/DaRacci/Minix/commit/d83108e4f94a3851321c232eae386c942324a0ec))
- Update build
  ([9b08b3f](https://github.com/DaRacci/Minix/commit/9b08b3fa548e052e8f8b0bf902b7ad4b7e6faf6b))
- [skip-ci] fix README.md ([7d3b5db](https://github.com/DaRacci/Minix/commit/7d3b5dbe78889d88f7ad0cb0206c9018a59db72d))
- Added thrown arg for logs to print the stacktrace
  ([daee413](https://github.com/DaRacci/Minix/commit/daee413edebe60136594028ca35f0cce4509e94c))

## [0.1.7] - 2021-11-06

### Bug Fixes

- Fix logs for each plugin
  ([113cfd9](https://github.com/DaRacci/Minix/commit/113cfd9b03e6e546d7e85eb063fb08cb669c310b))

### Other

- Actually fix actions?
  ([8b4017b](https://github.com/DaRacci/Minix/commit/8b4017bf56b8ca1dfcd34fb1bb9fc52197eb647b))
- Fix?
  ([7103257](https://github.com/DaRacci/Minix/commit/7103257324a33f1fdf21ce86e8a332bc7711d05c))
- Merge remote-tracking branch 'origin/master' into master
  ([4358b42](https://github.com/DaRacci/Minix/commit/4358b426c09e50f00f2e3501770b6459f0a5495f))
- Add automatic versioning
  ([dd99d2f](https://github.com/DaRacci/Minix/commit/dd99d2f722706606e137427d7b44e7f9ffb2e0c7))
- Fix javadoc link ([cdcf7f8](https://github.com/DaRacci/Minix/commit/cdcf7f87c23b666daa7f82ee89790142d2f83514))
- Merge remote-tracking branch 'origin/master' into master

# Conflicts:

# .github/workflows/build.yml

([ec1ce5d](https://github.com/DaRacci/Minix/commit/ec1ce5d0b03418949dac32507104a725a03660ab))

- [ci-skip] fix build.yml
  ([acf7771](https://github.com/DaRacci/Minix/commit/acf7771d5dc2fc1d6ccaf6db69b61568fc4e5356))
- [ci-skip] fix build.yml
  ([c73ec22](https://github.com/DaRacci/Minix/commit/c73ec229d090b115694ff26ec5ec4ae8cfd2dfcd))
- Merge remote-tracking branch 'origin/master' into master
  ([32b47c1](https://github.com/DaRacci/Minix/commit/32b47c1098431c2a16633b942d4b836cb00b491e))
- [ci-skip] Add ci-skip to build.yml
  ([9572d53](https://github.com/DaRacci/Minix/commit/9572d53840bd0de0b0733798afa3c2cea1c2c859))
- Update README.md ([851ec7f](https://github.com/DaRacci/Minix/commit/851ec7f17b62e03e0661ea1b578f8e25e638d718))
- Fix publish again...
  ([2ebadb0](https://github.com/DaRacci/Minix/commit/2ebadb0692673cdd23a2777cd7c3926c3774af1a))
- Merge branch 'dependabot/gradle/jvm-1.6.0-RC2'
  ([d0275a1](https://github.com/DaRacci/Minix/commit/d0275a156052dd6dbf2a45d3a4146b78efe96e56))
- Merge branch 'dependabot/gradle/com.github.johnrengelman.shadow-7.1.0'
  ([3db8693](https://github.com/DaRacci/Minix/commit/3db8693c6e6df8315ffd28b0b8d3274d968ba23c))
- Bump jvm from 1.6.0-RC to 1.6.0-RC2

Bumps jvm from 1.6.0-RC to 1.6.0-RC2.

---
updated-dependencies:

- dependency-name: jvm dependency-type: direct:production update-type: version-update:semver-patch ... ([e45ed47](https://github.com/DaRacci/Minix/commit/e45ed476f3062bd9a6e3f0d62dc4cb1405c2799d))
- Fix publish
  ([bad8090](https://github.com/DaRacci/Minix/commit/bad80904cc9795986f18a0d4870f11e3dab1fe67))
- Merge remote-tracking branch 'origin/master' into master

# Conflicts:

# .github/workflows/build.yml

([9312d12](https://github.com/DaRacci/Minix/commit/9312d1200be35b36912acd9f1252ab72b93d5e5f))

- Fix workflow files
  ([8c964b1](https://github.com/DaRacci/Minix/commit/8c964b17cffbb526c1b522d2c1739d2ee7cebfab))
- Fix workflow files
  ([4129b66](https://github.com/DaRacci/Minix/commit/4129b6642fe1942879e2a61450eb87ce01b2a384))
- Update build file and maybe add auto publishing
  ([2b889c7](https://github.com/DaRacci/Minix/commit/2b889c788c82b57a52d1493abee4b54418ead07e))
- Add publish.yml defiantly was not taken from someone else's repo
  ([a4bb5b1](https://github.com/DaRacci/Minix/commit/a4bb5b1f9b0eafe3026020badf41d9c1fea09b99))
- Fix Fix purpur using my local version and github not liking it Remove windows build form actions
  ([5c8d52d](https://github.com/DaRacci/Minix/commit/5c8d52deb9ca41eaeb09752c053c65a4427fbf4c))
- Bump com.github.johnrengelman.shadow from 7.0.0 to 7.1.0

Bumps com.github.johnrengelman.shadow from 7.0.0 to 7.1.0.

---
updated-dependencies:

- dependency-name: com.github.johnrengelman.shadow dependency-type: direct:production update-type: version-update:semver-minor ... ([219e126](https://github.com/DaRacci/Minix/commit/219e126613f0d71a09c1462f5b364d77f3e665a3))
- Local build.gradle.kts Moved out of Parent for everything because well that no work well with github actions, Removed all apache commons as they are unused
  ([a06f266](https://github.com/DaRacci/Minix/commit/a06f266d745e29d62742964662233dd501c2505c))
- Create dependabot.yml ([69d0dbf](https://github.com/DaRacci/Minix/commit/69d0dbf3b22df6513cc0f122f512fb5d94ba1664))
- Merge remote-tracking branch 'origin/master' into master
  ([4fb6fa6](https://github.com/DaRacci/Minix/commit/4fb6fa61451dc8239ef07a5246d30b8f00e5bdef))
- Still trying to get github actions to work...
  ([3812b78](https://github.com/DaRacci/Minix/commit/3812b786c37636ee39af892f3b01c5699ef5ae2d))
- Merge remote-tracking branch 'origin/master' into master
  ([d8bb711](https://github.com/DaRacci/Minix/commit/d8bb7110d8099fc75934bb086b4e4e3b48a7b5f9))
- Still trying to get github actions to work...
  ([eca2b71](https://github.com/DaRacci/Minix/commit/eca2b713740cd26d124afdc0274c8581b660a480))
- Update build.yml ([888dd4d](https://github.com/DaRacci/Minix/commit/888dd4d1e2fad57058169fad6333c89850004a7b))
- Update and rename raccilib.yml to build.yml ([a6ac6a0](https://github.com/DaRacci/Minix/commit/a6ac6a04fc01a2add1ccf81bb7e71c7f4fd67f13))
- Create validate-gradle-wrapper.yml ([e44694f](https://github.com/DaRacci/Minix/commit/e44694f366fb321a3ca51febcbc6d1ac9b3c76eb))
- Add files via upload ([70893ba](https://github.com/DaRacci/Minix/commit/70893bad3bc28b9b774cd04def472d261f573425))
- Add files via upload ([54b13cf](https://github.com/DaRacci/Minix/commit/54b13cf682a36e4ca6ac548f3cf97fab0b17a92e))
- Create raccilib.yml ([0b46b44](https://github.com/DaRacci/Minix/commit/0b46b44c1b9a3e0ab3034f1fdf77fe75c8be4626))
- Update docs
  ([d6c9d89](https://github.com/DaRacci/Minix/commit/d6c9d89adbbddeb603f23681fbcfd78169933b6d))
- Update docs
  ([ee07ce1](https://github.com/DaRacci/Minix/commit/ee07ce16e5adf7cf62b7ce39a32b3f2a5060ca90))
- Merge time utils
  ([216ecba](https://github.com/DaRacci/Minix/commit/216ecba883e92c82cda9045aaafa7d9eb96fc788))
- Moved kyori methods
  ([d37dca3](https://github.com/DaRacci/Minix/commit/d37dca33c6823725820e22325b71dfab68d54a4f))
- Remove useless aliases
  ([f900432](https://github.com/DaRacci/Minix/commit/f900432f16ecce01d49456cc6afd470ca23640f8))
- Add ExAdventureKyori.kt for adventure extensions
  ([456be03](https://github.com/DaRacci/Minix/commit/456be035ec62fc3ab8c03bc321d027359805389e))
- Deprecated for and moving
  ([5b6a6f1](https://github.com/DaRacci/Minix/commit/5b6a6f1c613bdc070fd99e7a2a5dedfd894d999b))
- More WIP stuff
  ([000a9f4](https://github.com/DaRacci/Minix/commit/000a9f49cebac29c206717d189a3665d140148cf))
- Updated comboListeners to also cancel their calling events when cancelled
  ([af2d9a0](https://github.com/DaRacci/Minix/commit/af2d9a064b5c31b8efcd8c2335a0d812a5888435))
- Add IFactory.kt more to come soon
  ([2df3b62](https://github.com/DaRacci/Minix/commit/2df3b620b8f771da904cfa816ba08b1ddb28bacd))
- Add more var methods
  ([b4d785d](https://github.com/DaRacci/Minix/commit/b4d785db8f663144a3b0a757b2d4924ba9397440))
- Fix broken docs
  ([731087b](https://github.com/DaRacci/Minix/commit/731087b48af2110d1e17f2e241b33893ab116eca))
- Dosc brocky
  ([0b7f8ae](https://github.com/DaRacci/Minix/commit/0b7f8ae9d76d00d8f61d3fa59d60c683d7d9fbe2))
- Merge remote-tracking branch 'origin/master' into master

# Conflicts:

# docs/-racci-core/package-list

([bfa1fe1](https://github.com/DaRacci/Minix/commit/bfa1fe1858c085c226634ca22b364137ef5a98db))

- Update Docs
  ([3ad5dc2](https://github.com/DaRacci/Minix/commit/3ad5dc26111ceab7c3c9451edab5f57ae090f4b6))
- Update Docs
  ([31edafb](https://github.com/DaRacci/Minix/commit/31edafb81baa5f0ebfd9848d44a76265c6afaa8b))
- Update 1.6 New Handler for assisting plugins using RacciCore.kt at runtime, handleEnable() now fires before commands, listeners and runnables are registered. Other misc improvements
  ([691e764](https://github.com/DaRacci/Minix/commit/691e764a22260f70cb3191ede473b235f9113876))
- Deprecated old loggers system Per plugin based much more modular system in use now
  ([3cca011](https://github.com/DaRacci/Minix/commit/3cca01147e45c466bb9a63c90a04854b4c45fc44))
- Util changes and improvements.
  ([b91863c](https://github.com/DaRacci/Minix/commit/b91863c5dbd3c9d1629513a4e611f40288507749))
- WIP Coroutine based tasks Currently very Experimental don't use for production versions.
  ([3b1e38f](https://github.com/DaRacci/Minix/commit/3b1e38f2ba003f638b007adad83e8b221fc09093))
- Deprecated Skedule in favour of MCCoroutine More to come soon
  ([91b4383](https://github.com/DaRacci/Minix/commit/91b43832938cf5b1b078e137711dba2e4fba5297))
- Event fixes? i dont remmember what this was for
  ([35d07c1](https://github.com/DaRacci/Minix/commit/35d07c11f30509f6f9c12874fc67903e659bb271))
- InventoryFramework Ex Provides more kotlin friendly ways of use
  ([80deed0](https://github.com/DaRacci/Minix/commit/80deed066172ead034c8ed418b9f97ffa1daa4e5))
- WIP Hook service
  ([3eb0e69](https://github.com/DaRacci/Minix/commit/3eb0e698b0df1957d29e93dfba0ea82642a93ac1))
- Fix Listeners
  ([551a184](https://github.com/DaRacci/Minix/commit/551a1843ed6de169436f16089cee6debcfee0e9f))
- PlayerManager.kt improvements Move PlayerJoinLeaveListener.kt inside PlayerManager.kt class and create init() and close() methods for cleaner use
  ([10c20b3](https://github.com/DaRacci/Minix/commit/10c20b3b02c883be9cca00bf94b7e3e74c4dfc82))
- Comment wip classes until done
  ([eb3f986](https://github.com/DaRacci/Minix/commit/eb3f986dcaf18d25cbd134940fc4c41b00b4afed))
- Added some better kotlin dsl like support for builders
  ([724bf4b](https://github.com/DaRacci/Minix/commit/724bf4bc5f8d7a6d2a2fc86b891be2037c898377))
- Removed Useless / empty classes
  ([a6f8217](https://github.com/DaRacci/Minix/commit/a6f8217204539b0982788fc317f556cde57a89af))
- Improved Builders Moved Builders folder and added proper kotlin dsl like building
  ([e89f928](https://github.com/DaRacci/Minix/commit/e89f928485bc0efb9d0b3ff39a3642d8682af2d8))
- Push
  ([8876ed6](https://github.com/DaRacci/Minix/commit/8876ed60d1f680ade24a7170d857f951414da2f7))
- Push
  ([1d694e6](https://github.com/DaRacci/Minix/commit/1d694e6d9b1dbe129b6d7dcfd08f05b7f83da7b3))
- Updated 0.1.4 Changed coroutine handling, made onEnable etc async, did some general improvements, moved build.gradle.kts logic into proper file, smart dependencies added so they are always up-to-date and correct.
  ([b8c83ca](https://github.com/DaRacci/Minix/commit/b8c83ca605f83538653be34af31fa522ffa57ba9))
- Update 0.1.1
  ([709ed2b](https://github.com/DaRacci/Minix/commit/709ed2b9b9daea93d1e551ca0b8ef4eb415541da))
- Update 0.1.1
  ([8e26e62](https://github.com/DaRacci/Minix/commit/8e26e62787b679fd703d5306dcf70d0276a3de0f))
- Update 0.1.0 Added a bucket load of new utils and random other shizz too
  ([ae978b9](https://github.com/DaRacci/Minix/commit/ae978b9ab139e224d9debeceb37c9ebba699340e))
- Updated plugins to load before in plugin.yml
  ([cc9e963](https://github.com/DaRacci/Minix/commit/cc9e96300f7f3f5169c30e1de21647a1bfa794c0))
- Moved Build info to Parent of all projects
  ([a47cd33](https://github.com/DaRacci/Minix/commit/a47cd33e88db056e536f02eecf2d551cec11a4dc))
- Quick push
  ([59f3059](https://github.com/DaRacci/Minix/commit/59f3059dd7519b7877940e34ca353e42aa98a150))
- Update 0.0.5 Added Kyori Bukkit as a dependency for Audiences. Updated logger for easier access. Added new ways to register listeners, command and runnables for RacciPlugin.kt Updated RacciCore.kt to match New RacciPlugin.kt logic Added UpdateChecker.kt, this will check for
  updates if a spigot id is given when creating the RacciPlugin.kt Updated replace in TextUtils.kt with a much improved method, also added textOf as a replacement for LegacyUtils Added the start of MathUtils.kt Added KotlinListener.kt for easier addition of features later Added
  WorldEvents.kt, has NightEvent and DayEvent, will include more in the future. Made PlayerData.kt and PlayerManager.kt internal so to not interfere with other plugin classes.
  ([2cb38a5](https://github.com/DaRacci/Minix/commit/2cb38a53df4a4f189699120d135402cc04c9856b))
- Added WorldEvents.kt Added DayEvent and NightEvent
  ([77e7298](https://github.com/DaRacci/Minix/commit/77e72982d0e218d23ebc2d5da846dfe6314badf3))
- Bug fixes Fixed RacciCore plugin not running Updated main class method to RacciCore from RacciLib Updated plugin.yml
  ([c6358db](https://github.com/DaRacci/Minix/commit/c6358db4b91265d588e676dba21f9d8d0146608a))
- Update 0.0.3 Move to gradle build structure, rename to RacciCore from RacciLib
  ([79d12f7](https://github.com/DaRacci/Minix/commit/79d12f7974e3164a5bf491c2b82975c7b83852df))
- Update 0.0.2 Updated docs, Removed all suppress unused and file jvm declarations
  ([133b264](https://github.com/DaRacci/Minix/commit/133b2644e7a81594be808ba205f9f0ddf6de5a8c))
- Added racciCore and playerManager vars out of class in RacciLib.kt for easy access. Added PlayerJoinLeaveListener.kt for last action time stuff Added PlayerComboListeners.kt for events of PlayerComboEvent.kt Added PlayerShiftLeftClickEvent, PlayerShiftRightClickEvent,
  PlayerShiftOffhandEvent, PlayerShiftDoubleOffhandEvent, PlayerLeftClickEvent, PlayerRightClickEvent, PlayerOffhandEvent, and PlayerDoubleOffhandEvent. Added PlayerData.kt and PlayerManager.kt to keep track of player last offhand times etc
  ([ac82452](https://github.com/DaRacci/Minix/commit/ac82452e2df84431b9f3be809fc501a981158c47))
- Push test
  ([c51a85a](https://github.com/DaRacci/Minix/commit/c51a85acdc49f3dbb393fbfae0811c2f9fe8a6e1))
- Push test
  ([a4bd250](https://github.com/DaRacci/Minix/commit/a4bd250cd8d1b27fa21c2cc98a72c2127a55428e))
- Push
  ([629e314](https://github.com/DaRacci/Minix/commit/629e314d40300a964569855f4846cc034a59bad5))
- Added colour translations for lists, made kotlin event able to trigger async, added lists for legacy parsing, make player movement events trigger async and added location and world to usable parameters for isDay and isNight
  ([875230f](https://github.com/DaRacci/Minix/commit/875230f11cb733148f91121bb28168e1ac492de8))
- Tried out using commandApi, was kinda shit compared to ACF still so removed dependency.
  ([071c143](https://github.com/DaRacci/Minix/commit/071c1439dc9443c838e690c502b43f6665a6e8fe))
- Fixed exception error with checking if a block is liquid
  ([98d4ab7](https://github.com/DaRacci/Minix/commit/98d4ab7e379252ef0fae1678877fea03c0ef74cc))
- Merge remote-tracking branch 'origin/master' into master
  ([89467e3](https://github.com/DaRacci/Minix/commit/89467e39e476e9509f785312d2b08d8bfef17768))
- Updated some stuff to reduce file size, under 1mb again now instead of 8mb, fixed some errors on startup with kotlin
  ([83c6759](https://github.com/DaRacci/Minix/commit/83c6759b40c2a3769b4061382419eb80cf54fe04))
- Update README.md ([36ff454](https://github.com/DaRacci/Minix/commit/36ff45411447c105217f13bc24e802a5b32f50a3))
- Update README.md ([d4a9738](https://github.com/DaRacci/Minix/commit/d4a9738b03fcc2022c382b3ed41393db518ab509))
- Update README.md ([1da9d96](https://github.com/DaRacci/Minix/commit/1da9d96378eb9346711571f40cf92e23b725608c))
- Create CNAME ([5bfea96](https://github.com/DaRacci/Minix/commit/5bfea9648f2bf919b1411bb6181d2c64b000f7c0))
- Merge remote-tracking branch 'origin/master'

([a0fe3bb](https://github.com/DaRacci/Minix/commit/a0fe3bbd2ece0ee66988784e69567096b7e98e2c))

- Missed files

woops missed these files in the push ([839b4ee](https://github.com/DaRacci/Minix/commit/839b4ee06a1c45aa1d7bce1e027411e1c3fa78fe))

- Add Docs and misc

Made some fixes to null returns and added jvmNames to some files missing it. Also added docs and updated pom.xml ([e0f16eb](https://github.com/DaRacci/Minix/commit/e0f16eb1aece59cbd3e5c14c964d3fd85fd81369))

- Rename LICENSE to LICENSE.md ([ed93012](https://github.com/DaRacci/Minix/commit/ed930127b48b0adc15b262b8260b3ad259ec86af))
- Create LICENSE ([dce5b6f](https://github.com/DaRacci/Minix/commit/dce5b6f7087c098f062e4438b69e4f4cd5ee9649))
- Create README.md ([1530ba8](https://github.com/DaRacci/Minix/commit/1530ba8c0d20b3158013d72cbfa09db348e17135))
- Added abstract KotlinEvent.kt to fix Bukkit throwing hissy fits over static handler getters for custom events, Renamed utils/text to utils/strings, Added experimental YAMLConfig.kt
  ([4b6db5d](https://github.com/DaRacci/Minix/commit/4b6db5d58366151a555f25793344d478a12deae1))
- Remove HeadBuilder.kt in favour of SkullBuilder, update coroutines to 1.5.2
  ([3773bea](https://github.com/DaRacci/Minix/commit/3773beaf561e63efc02f6899943cc2f45043e470))
- Updated stuff?? i forgot what i did, i do know i added legacy parser and uh colour util???
  ([81dc315](https://github.com/DaRacci/Minix/commit/81dc315c6e488cb3345df142d5a94bcb786d14fa))
- Initial commit
  ([ecb75da](https://github.com/DaRacci/Minix/commit/ecb75da0c1967fd3b4c633e7a40ba2644dc51023))

### Testing

- Test2 ([74ca94c](https://github.com/DaRacci/Minix/commit/74ca94c867b19ea644886131722d4ad66ea92592))
- Test ([64fc468](https://github.com/DaRacci/Minix/commit/64fc4684442939e1ca8074bb2b440d41d96c3615))

<!-- generated by git-cliff -->
