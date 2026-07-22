package com.nx.client.module;

import com.nx.client.module.modules.combat.Aimbot;
import com.nx.client.module.modules.combat.AntiKnockback;
import com.nx.client.module.modules.combat.AutoClicker;
import com.nx.client.module.modules.combat.Criticals;
import com.nx.client.module.modules.combat.KillAura;
import com.nx.client.module.modules.combat.TriggerBot;
import com.nx.client.module.modules.movement.AutoSneak;
import com.nx.client.module.modules.movement.AutoSprint;
import com.nx.client.module.modules.movement.AutoWalk;
import com.nx.client.module.modules.movement.BunnyHop;
import com.nx.client.module.modules.movement.Fly;
import com.nx.client.module.modules.movement.Glide;
import com.nx.client.module.modules.movement.HighJump;
import com.nx.client.module.modules.movement.Jesus;
import com.nx.client.module.modules.movement.NoFall;
import com.nx.client.module.modules.movement.NoSlow;
import com.nx.client.module.modules.movement.SafeWalk;
import com.nx.client.module.modules.movement.Speed;
import com.nx.client.module.modules.movement.Spider;
import com.nx.client.module.modules.movement.Step;
import com.nx.client.module.modules.player.AutoEat;
import com.nx.client.module.modules.player.AutoRespawn;
import com.nx.client.module.modules.player.AutoTool;
import com.nx.client.module.modules.player.Nuker;
import com.nx.client.module.modules.render.Coordinates;
import com.nx.client.module.modules.render.FpsCounter;
import com.nx.client.module.modules.render.Fullbright;
import com.nx.client.module.modules.render.HudModule;
import com.nx.client.module.modules.render.Zoom;

public final class Modules {

    private Modules() {
    }

    public static void registerAll(ModuleManager manager) {
        manager.add(new KillAura());
        manager.add(new Aimbot());
        manager.add(new TriggerBot());
        manager.add(new AutoClicker());
        manager.add(new Criticals());
        manager.add(new AntiKnockback());

        manager.add(new Fly());
        manager.add(new Speed());
        manager.add(new NoFall());
        manager.add(new Jesus());
        manager.add(new Step());
        manager.add(new HighJump());
        manager.add(new AutoSprint());
        manager.add(new BunnyHop());
        manager.add(new Glide());
        manager.add(new Spider());
        manager.add(new AutoWalk());
        manager.add(new AutoSneak());
        manager.add(new SafeWalk());
        manager.add(new NoSlow());

        manager.add(new Fullbright());
        manager.add(new Zoom());
        manager.add(new HudModule());
        manager.add(new Coordinates());
        manager.add(new FpsCounter());

        manager.add(new AutoRespawn());
        manager.add(new AutoEat());
        manager.add(new AutoTool());
        manager.add(new Nuker());
    }
}
