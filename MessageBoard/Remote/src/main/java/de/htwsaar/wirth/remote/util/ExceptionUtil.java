package de.htwsaar.wirth.remote.util;

import javafx.scene.paint.Color;

import java.io.Serializable;

public enum ExceptionUtil implements Serializable {

	USER_ALREADY_EXISTS("Messagebord","Der verwendete Benutzername existiert bereits."),
	NO_PERMISSION("Messagebord","Sie sind nicht berechtigt diese Aktion auszuführen."),
	UNKNOWN_USER("Messagebord","Der User existiert nicht auf dem Server."),
	MESSAGE_NOT_EXISTS("Messagebord", "Die Nachricht existiert nicht auf dem Server."),
	UNKNOWN_ERROR_MESSAGEBOARD("Messagebord","Bitte versuchen Sie es erneut."),
	PORT_FORMAT("Login","Bitte geben Sie für den Port eine Zahl ein z.B. 40010."),
	WRONG_USER_OR_PSW("Login","Der Username oder das Passwort ist falsch."),
	UNKNOWN_GROUPNAME("Login","Der eingegebene Gruppenname existiert nicht."),
	UNKNOWN_HOST("Login","Der Hostname wurde nicht gefunden. Der Hostname kann eine IP-Adresse oder ein Domainname  sein, wird das Feld leer gelassen wird automatisch der Localhost angesprochen."),
	CONNECTION_ERROR("Login","Es konnte keine Verbindung zum Server hergestellt werden"),
	PORT_IN_USE("Login","Der eingegebene Port wird bereits verwendet. Stellen Sie sicher, dass der eingebene Port nicht schon von einem anderen Programm benutzt wird."),
	UNKNOWN_ERROR_LOGIN("Login","Bitte versuchen Sie es erneut.");

	private final String DEFAULT = "UPS! Es ist ein Fehler aufgetreten";
	private String location;
	private String msg;

	ExceptionUtil(String location, String msg) {
		this.location = location;
		this.msg = msg;
	}

	@Override
	public String toString() {
		return msg;
	}
	public String getDefaultText(){return DEFAULT;}
	public String getLocation() {
		return location;
	}
}
