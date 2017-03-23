package de.htwsaar.wirth.remote.model;

import java.io.Serializable;

import javafx.scene.paint.Color;

public enum Status implements Serializable {
	ONLINE("Online", Color.LIGHTGREEN), 
	OFFLINE("Offline",Color.RED), 
	AWAY("Abwesend", Color.YELLOW), 
	BUSY("Beschäftigt", Color.ORANGE);
	
	private String desc;
	private Color color;
	
	Status(String desc, Color color) {
		this.desc = desc;
		this.color = color;
	}
	
	@Override
	public String toString() {
		return desc;
	}
	
	public Color getColor() {
		return color;
	}
}
