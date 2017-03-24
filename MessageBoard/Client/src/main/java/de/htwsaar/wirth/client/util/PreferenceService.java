package de.htwsaar.wirth.client.util;

import java.util.prefs.Preferences;

/**
 * Created by stefanschloesser1 on 24.03.17.
 */
public class PreferenceService {

    private static final String KEY_USERNAME = "username";
    private static final String KEY_HOSTNAME = "hostname";
    private static final String KEY_PORT = "port";
    private static final String KEY_GROUP_NAME = "groupName";
    private static Preferences prefs;
    private static PreferenceService instance;

    public static synchronized PreferenceService getInstance() {
        if (instance == null) {
                instance = new PreferenceService();
        }
        return instance;
    }
    protected PreferenceService(){
        prefs = Preferences.userNodeForPackage( this.getClass() );
    }


    public void setPreference(String username,String hostname,String port,String groupName)
    {
        prefs.put(KEY_USERNAME,username);
        prefs.put(KEY_HOSTNAME,hostname);
        prefs.put(KEY_PORT,port);
        prefs.put(KEY_GROUP_NAME,groupName);
    }

   public String getUsername(){
        return prefs.get(KEY_USERNAME,"");
   }
    public String getHostName(){
        return prefs.get(KEY_HOSTNAME,"");
    }
    public String getPort(){
        return prefs.get(KEY_PORT,"");
    }
    public String getGroupeName(){
        return prefs.get(KEY_GROUP_NAME,"");
    }

}
