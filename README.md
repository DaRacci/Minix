![IMG](https://cdn.discordapp.com/attachments/431545763928211457/853353180271214662/mfthread.png)
<div align="center">

# Minix

![CI](https://github.com/DaRacci/Minix/actions/workflows/gradle-ci.yml/badge.svg)
[![Package](https://badgen.net/maven/v/metadata-url/repo.racci.dev/releases/dev/racci/Minix/maven-metadata.xml)](https://repo.racci.dev/#/releases/dev/racci/Minix)

Minix is library written for paper based plugins, providing full integration with [KotlinX Coroutines](https://github.com/Kotlin/kotlinx.coroutines) for easy Async integration with the Bukkit API.

[![Releases](https://img.shields.io/github/v/release/DaRacci/Minix?color=informational)]()
[![Servers](https://img.shields.io/bstats/servers/13706?color=informational)](https://bstats.org/plugin/bukkit/Minix/13706)
[![Players](https://img.shields.io/bstats/players/13706?color=informational)](https://bstats.org/plugin/bukkit/Minix/13706)
[![Docs](https://img.shields.io/badge/docs-gitbook-informational)]()
[![Build](https://img.shields.io/github/workflow/status/DaRacci/Minix/Java%20CI/develop?color=informational)]()
</div>

## Plugin Information

- Supports 1.18.1+

## Docs

* The docs can be found [here](https://minix.racci.dev)

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

## 🤝 Contributing

Contributions, issues and feature requests are welcome!<br />Feel free to check [issues page](https://github.com/DaRacci/Minix/issues).

## 📝 License

Copyright © 2021-2022 [Racci](https://github.com/DaRacci)

This project is [licensed](https://github.com/DaRacci/Minix/blob/master/LICENSE.md).