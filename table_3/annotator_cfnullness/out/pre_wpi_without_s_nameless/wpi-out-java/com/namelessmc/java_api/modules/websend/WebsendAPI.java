package com.namelessmc.java_api.modules.websend;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.namelessmc.java_api.NamelessException;
import com.namelessmc.java_api.RequestHandler;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class WebsendAPI {

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RequestHandler requests;

    @org.checkerframework.dataflow.qual.SideEffectFree
    public WebsendAPI(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RequestHandler requests) {
        this.requests = Objects.requireNonNull(requests, "Request handler is null");
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<WebsendCommand> getCommands(int serverId) throws NamelessException {
        JsonObject response = this.requests.get("websend/commands", "server_id", serverId);
        JsonArray commandsJson = response.getAsJsonArray("commands");
        List<WebsendCommand> commands = new ArrayList<>(commandsJson.size());
        for (JsonElement e : commandsJson) {
            JsonObject commandJson = e.getAsJsonObject();
            int commandId = commandJson.get("id").getAsInt();
            String commandLine = commandJson.get("command").getAsString();
            commands.add(new WebsendCommand(commandId, commandLine));
        }
        return Collections.unmodifiableList(commands);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void sendConsoleLog(int serverId, Collection<String> lines) throws NamelessException {
        JsonObject body = new JsonObject();
        body.addProperty("server_id", serverId);
        JsonArray content = new JsonArray();
        for (String line : lines) {
            content.add(line);
        }
        body.add("content", content);
        this.requests.post("websend/console", body);
    }
}
