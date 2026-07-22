package com.nx.client.addon;

import com.nx.client.NXClient;
import com.nx.client.module.Module;

public interface NXAddon {

    void onInitialize();

    class Registry {

        private Registry() {
        }

        public static void registerModule(Module module) {
            NXClient.moduleManager.add(module);
        }
    }
}
