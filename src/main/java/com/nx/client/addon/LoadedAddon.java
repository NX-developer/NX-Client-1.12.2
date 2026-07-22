package com.nx.client.addon;

public class LoadedAddon {

    private final String name;
    private final String author;
    private final String version;
    private final int moduleCount;
    private final boolean failed;

    public LoadedAddon(String name, String author, String version, int moduleCount, boolean failed) {
        this.name = name;
        this.author = author;
        this.version = version;
        this.moduleCount = moduleCount;
        this.failed = failed;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getVersion() {
        return version;
    }

    public int getModuleCount() {
        return moduleCount;
    }

    public boolean isFailed() {
        return failed;
    }
}
