package de.htwsaar.wirth.client.controller;

import java.util.List;

import de.htwsaar.wirth.remote.model.interfaces.Message;

public interface MessageProvider {
	public List<Message> register(MessageObserver observer);
	public void sendMessage(String msg);
}
