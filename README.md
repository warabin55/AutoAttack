# AutoAttack (unofficial fork)

An unofficial fork of [vin350/AutoAttack](https://github.com/vin350/AutoAttack), updated for newer Minecraft versions.
The original mod is published by Vin35 — this fork is not affiliated with, or endorsed by, the original author.

Auto Attack makes it so that holding the attack button autoswings the sword, similar to the combat snapshots.

## Download

Jars built from this fork are available on the [Releases page](https://github.com/warabin55/AutoAttack/releases).
Each Minecraft version lives on its own branch (e.g. `26.1.X`, `1.19.X`).

For the original mod (up to the versions the original author supports), see the [upstream repository](https://github.com/vin350/AutoAttack).

## Features

- Hold the attack button to swing automatically (respects the attack cooldown)
- Attack entities through non-solid blocks such as grass (like the CleanCut mod)
- Prevent hitting blocks while holding a sword (or any tool), toggleable with a keybind
- Auto-release fully charged bows, crossbows and tridents
- AFK auto-attack mode, toggleable with a keybind
- Configurable in-game via [MidnightLib](https://modrinth.com/mod/midnightlib) (bundled)

## Requirements

- Minecraft Java Edition 26.1.x
- [Fabric Loader](https://fabricmc.net/use/) 0.19.3+
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 25 (bundled with the official launcher)

## Building

```
./gradlew build
```

Requires JDK 25. The jar is produced in `build/libs/`.

## License

MIT — see [LICENSE](LICENSE). All credit for the original mod goes to [Vin35](https://github.com/vin350).
