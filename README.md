# NX Client 1.12.2

Legacy port of NX Client for Minecraft Java 1.12.2 (Forge), with a ClickGUI,
HUD and addon API.

The main version of this project targets Minecraft 1.21 (Fabric) and lives here:
https://github.com/NX-developer/NX-Client-1.21

This repository is the older-version port for people who still play 1.12.2.
The two versions share the same design, the same module names and the same addon
concept, but they are built on different toolchains, so their code is separate.

## Modules

Combat: KillAura, Aimbot, TriggerBot, AutoClicker, Criticals, AntiKnockback
Movement: Fly, Speed, NoFall, Jesus, Step, HighJump, AutoSprint, BunnyHop, Glide, Spider, AutoWalk, AutoSneak, SafeWalk, NoSlow, Timer
Render: Fullbright, Zoom, HUD, Coordinates, FpsCounter, XRay, EntityESP, Tracers, ItemESP, ChestESP, BedESP, Nametags, Freecam
Player: AutoRespawn, AutoEat, AutoTool, Nuker


## Controls

- Right Shift: open the ClickGUI
- Left click a module: toggle it
- Right click a module: expand its settings and keybind
- Type in the search bar to filter every module
- Escape: close the ClickGUI

## Addons

NX Client detects addons automatically through Forge's annotation scanner.
Drop an addon jar into the mods folder and its modules are registered on startup,
with settings, keybinds and config saving handled for you. Loaded addons appear
under the Addons tab in the ClickGUI.

See docs/ADDONS.md for the full addon development guide.

## Build

Push to GitHub and the Actions workflow builds the mod automatically.
Pushing a `vX.Y.Z` tag publishes a GitHub Release with the mod jar attached.

Requires Java 8, Minecraft 1.12.2 and Forge 14.23.5.

## Credits

All code in this project is written from scratch for NX Client.
No code has been copied from other clients or projects.
