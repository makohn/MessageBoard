package de.htwsaar.wirth.client.util;

import java.io.*;

/**
 * Created by stefanschloesser1 on 23.03.17.
 */
public class LoginCacheService {
    private static final String FILE = "MessageBoard/Client/loginCache.txt";
    private String username;
    private String hostName;
    private String port;
    private String GroupName;

    public LoginCacheService(){
           initData();
    }


    private void initData() {
        try{
        File file = new File(FILE);
        if(!file.exists() && !file.isDirectory())
        {
            file.createNewFile();
        }
        if (file != null) {
            BufferedReader br = new BufferedReader(new FileReader(file));
                this.username = br.readLine();
                this.hostName = br.readLine();
                this.port = br.readLine();
                this.GroupName = br.readLine();
            } } catch (FileNotFoundException x) {x.printStackTrace();} catch (IOException e) {e.printStackTrace();}}


    public void saveData(String username,String hostname,String port,String groupName) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(FILE, false), "utf-8"))) {
            writer.write(username + "\n" + hostname + "\n" + port + "\n" + groupName);
        } catch (IOException e1) {e1.printStackTrace();}
    }

    public String getUsername() {
        return username;
    }

    public String getHostName() {
        return hostName;
    }

    public String getPort() {
        return port;
    }

    public String getGroupName() {
        return GroupName;
    }
}
