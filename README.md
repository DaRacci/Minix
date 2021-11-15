<img src="https://cdn.discordapp.com/attachments/431545763928211457/853353180271214662/mfthread.png">
<h1 align="center">Racci Lib<h1>
<p>
  <a href="https://jitpack.io/#DaRacci/RacciCore"><img src="https://jitpack.io/v/DaRacci/RacciCore.svg?style=flat-square" alt="language"/></a>
  <img src="https://img.shields.io/github/license/DaRacci/RacciCore?color=blue&style=flat-square"  alt="license"/>
<a href="https://discord.gg/9D986MAfZk"><img src="https://img.shields.io/discord/812625173315584030?label=discord&style=flat-square"  alt="language"/></a>
</p>
 
### Plugin Information
  - Supports 1.16.5+

# Information for development

## Javadoc
* The 0.1.7 Javadoc can be found [here](https://javadoc.jitpack.io/com/github/DaRacci/RacciCore/0.1.7/javadoc)
* You can also get the docs from [here](https://racciCore.sylphmc.com) with the GitHub format. 
  
## Plugin Information
  
RacciCore is a standalone plugin, so it can provide all of its features, this means you will have to install it
on the server with your plugin, to depend on RacciCore add it as a dependency in your plugin.yml:

```yaml
depend:
  - RacciCore
```
  
## Get from JitPack:

Gradle:

```kotlin
repositories {
        maven("https://jitpack.io")
}
```

```kotlin
dependencies {
        compileOnly("com.github.DaRacci:RacciCore:tag")
}
```

Replace `tag` with a release tag, eg `0.2.0`.
  
Maven:

```xml
<repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
</repository>
```

```xml
<dependency>
        <groupId>com.github.DaRacci</groupId>
        <artifactId>RacciCore</artifactId>
        <version>tag</version>
        <scope>provided</scope>
</dependency>
```
  
Replace `tag` with a release tag, eg `0.2.0`.

## 🤝 Contributing

Contributions, issues and feature requests are welcome!<br />Feel free to check [issues page](https://github.com/DaRacci/RacciCore/issues).

## 📝 License

Copyright © 2021 [Racci](https://github.com/DaRacci)
  
This project is [licensed](https://github.com/DaRacci/RacciCore/blob/master/LICENSE.md).