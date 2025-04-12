package com.namelessmc.java_api.modules.websend;

import org.jetbrains.annotations.NotNull;

public class WebsendCommand {

    private final int id;

    private final String commandLine;

    public WebsendCommand(final int id, final String commandLine) {
        this.id = id;
        this.commandLine = commandLine;
    }

    public int getId() {
        return id;
    }

    public String getCommandLine() {
        return this.commandLine;
    }
}
