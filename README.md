<img src="doc/icon.png" align="right" width="180px"/>

# LibGuiFoxified

[![Hosted By: Cloudsmith](https://img.shields.io/badge/OSS%20hosting%20by-cloudsmith-blue?logo=cloudsmith&style=flat-square)](https://cloudsmith.com)

LibGui unofficial (Neo)Forge port.

Minecraft GUIs without spending forever painstakingly aligning things to the background image.
Instead, LibGui takes a logical description of your GUI, and draws it on-the-fly like any modern
GUI system. Controls can be hung on an itemslot grid or offset from it. Panel styles, colors,
and opacity can be customized, and everything can be extended.

## Dev Setup

This is how to get LibGuiFoxified into your development environment:

1. Add the CloudSmith repository:
```groovy
repositories {
    maven {
        name = "CloudSmith"
        url = "https://dl.cloudsmith.io/public/thinkingstudio/libguifoxified/maven/"
    }
}
```
> Note: This is not the same repositories as the one in publishing! 
> You have to add the repository to a top-level repositories block.

2. Add the dependency, replacing <version> with your desired LibGuiFoxified version:
```groovy
dependencies {
	modImplementation include("org.thinkingstudio:LibGuiFoxified:<version>")
}
```
The include makes Loom bundle LibGuiFoxified within your mod jar.

## Docs
See the [LibGui wiki](https://github.com/CottonMC/LibGui/wiki)

And for code examples:
* [Client-Only Guis](https://github.com/CottonMC/LibGui/wiki/Client-Sided-Guis) 
* [Inventory Guis](https://github.com/CottonMC/LibGui/wiki/Getting-Started-with-GUIs)
