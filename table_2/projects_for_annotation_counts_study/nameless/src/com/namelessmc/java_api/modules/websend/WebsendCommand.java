package com.namelessmc.java_api.modules.websend;
public class WebsendCommand {
	private final @Positive int id;
	private final  String commandLine;
	public WebsendCommand(final @Positive int id,
						  final  String commandLine) {
		this.id = id;
		this.commandLine = commandLine;
	}
	public @Positive int getId() {
		return id;
	}
	public  String getCommandLine() {
		return this.commandLine;
	}
}
