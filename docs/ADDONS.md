# NX Client Addon Development (1.12.2)

NX Client discovers addons automatically through Forge's annotation scanner.
Any mod whose class is annotated with `@NXAddonEntry` and implements `NXAddon`
is detected on startup, its modules are registered into the client, and it
appears under the Addons tab in the ClickGUI.

## 1. Create a normal Forge 1.12.2 mod

Set up a standard Forge mod for Minecraft 1.12.2 with Java 8.

## 2. Depend on NX Client

Download the release jar from
https://github.com/NX-developer/NX-Client-1.12.2/releases and place it in `libs/`:

```gradle
dependencies {
    compile files("libs/nx-client-1.12.2-1.0.0.jar")
}
```

## 3. Write the addon entry class

```java
package com.example;

import com.nx.client.addon.NXAddon;
import com.nx.client.addon.NXAddonEntry;

@NXAddonEntry(name = "My Addon", author = "YourName", version = "1.0.0")
public class MyAddon implements NXAddon {

    @Override
    public void onInitialize() {
        NXAddon.Registry.registerModule(new ExampleModule());
    }
}
```

## 4. Write a module

```java
package com.example;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import com.nx.client.settings.BooleanSetting;
import com.nx.client.settings.NumberSetting;

public class ExampleModule extends Module {

    private final NumberSetting strength = new NumberSetting("Strength", 1.0, 0.1, 5.0, 0.1);
    private final BooleanSetting notify = new BooleanSetting("Notify", true);

    public ExampleModule() {
        super("ExampleModule", "Does something useful", Category.PLAYER);
        addSetting(strength);
        addSetting(notify);
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onTick() {
    }
}
```

## 5. Build and install

Build your addon and drop the jar into the `mods` folder next to NX Client.
On startup the module appears in its category, its settings work in the ClickGUI,
its toggle state is saved to the config, and the addon is listed under Addons.

## API reference

- `Module(String name, String description, Category category)` - base module
- `Module(String name, String description, Category category, int defaultKey)` - with an LWJGL default keybind
- `onEnable()`, `onDisable()`, `onTick()` - lifecycle hooks
- `addSetting(Setting<?>)` - register a setting, shown in the ClickGUI
- `BooleanSetting(String name, boolean defaultValue)`
- `NumberSetting(String name, double defaultValue, double min, double max, double step)`
- `Category.COMBAT`, `MOVEMENT`, `RENDER`, `PLAYER`
- `NXClient.moduleManager` - registry of every loaded module

An addon that throws during initialization is marked as failed in the Addons tab
and does not stop the client or other addons from loading.

## Keybinds

Every module gets a keybind slot automatically, including addon modules.
Right click a module in the ClickGUI, click the Keybind row and press any key.
Keybinds are stored in the game options and survive restarts.

To ship a default keybind, pass an LWJGL key code:

```java
super("ExampleModule", "Does something useful", Category.PLAYER, Keyboard.KEY_G);
```

## Localization

NX Client resolves module descriptions, setting names and category labels through
Minecraft's language system, so the client follows the game language.

Addons are responsible for their own translations. If an addon ships no language
file, its text falls back to the English string passed in the constructor, even
when the game is set to Turkish. Nothing breaks, but the addon stays in whatever
language its author wrote it in.

To translate your own addon, add these keys to your addon's `.lang` files:

```
nxclient.module.examplemodule.description=Faydali bir sey yapar
nxclient.setting.strength=Guc
```

If an addon you use has no translation for your language, either ask its author
to add one or add the keys yourself through a resource pack.
