package de.htwsaar.wirth.client.util;

import java.util.prefs.Preferences;

/**
 * Class {@code PreferenceService} stores user-specific data. The persistence process
 * is completely handled by the operating system.
 */
public class PreferenceService {

	private static final String KEY_USERNAME = "username";
	private static final String KEY_HOSTNAME = "hostname";
	private static final String KEY_PORT = "port";
	private static final String KEY_GROUP_NAME = "groupName";
	private static final String KEY_IS_PORT_SELECTED = "portSelected";
	private static Preferences prefs;
	private static PreferenceService instance;

	public static synchronized PreferenceService getInstance() {
		if (instance == null) {
			instance = new PreferenceService();
		}
		return instance;
	}

	protected PreferenceService() {
		prefs = Preferences.userNodeForPackage(this.getClass());
	}

	public void setPreference(String username, String hostname, boolean isCallbackPortSelected, String port,
			String groupName) {
		prefs.put(KEY_USERNAME, username);
		prefs.put(KEY_HOSTNAME, hostname);
		prefs.putBoolean(KEY_IS_PORT_SELECTED, isCallbackPortSelected);
		prefs.put(KEY_PORT, port);
		prefs.put(KEY_GROUP_NAME, groupName);
	}

	public String getUsername() {
		return prefs.get(KEY_USERNAME, "");
	}

	public String getHostName() {
		return prefs.get(KEY_HOSTNAME, "");
	}
	
	public boolean isCallbackPortSelected() {
		return prefs.getBoolean(KEY_IS_PORT_SELECTED, false);
	}

	public String getPort() {
		return prefs.get(KEY_PORT, "");
	}

	public String getGroupeName() {
		return prefs.get(KEY_GROUP_NAME, "");
	}

}
