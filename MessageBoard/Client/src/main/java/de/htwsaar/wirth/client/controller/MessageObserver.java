package de.htwsaar.wirth.client.controller;

import de.htwsaar.wirth.remote.model.interfaces.Message;

public interface MessageObserver {
	public void notifyNew(Message msg);
	public void notifyEdit(Message msg);
	public void notifyDelete(Message msg);
}
