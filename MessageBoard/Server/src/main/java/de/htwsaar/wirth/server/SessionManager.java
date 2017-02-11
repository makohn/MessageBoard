package de.htwsaar.wirth.server;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.htwsaar.wirth.remote.exceptions.AuthenticationException;
import de.htwsaar.wirth.remote.exceptions.NoPermissionException;
import de.htwsaar.wirth.remote.exceptions.NotLoggedInException;
import de.htwsaar.wirth.remote.model.auth.AuthPacket;
import de.htwsaar.wirth.remote.model.auth.LoginPacket;
import de.htwsaar.wirth.remote.model.interfaces.Message;

public class SessionManager {

    /**
     * the session map, which stores a authentification token for each username
     */
    private static Map<String, AuthPacket> sessions = new ConcurrentHashMap<String, AuthPacket>();

    /**
     * logs a user in and generates a new token for the given username
     * if the username or password is wrong, a AuthenticationException gets thrown
     * @param username
     * @param password
     * @return the AuthPacket, which can be used by the client to access the remote methods
     * @throws RemoteException
     * @throws AuthenticationException, when the username or password is wrong
     */
    public static AuthPacket authenticate(LoginPacket login) throws RemoteException {
        // TODO: check, if this password equals the password in the database
        //if(true) {
            String username = login.getUsername();
            AuthPacket auth = new AuthPacket(username);
            sessions.put(username, auth);
            return auth;
        //} else {
        //    throw new AuthenticationException("Wrong Username or Password.");
        //}
    }

    /**
     * check, if the user is authenticated by the given token.
     * 
     * @param username
     * @param auth
     * @throws RemoteException
     * @throws NotLoggedInException, if the there is no such user token
     * @throws AuthenticationException, if the given token does not match the token in the sessionsMap a AuthenticationException gets thrown
     */
    public static void isAuthenticatedByToken(AuthPacket auth) throws RemoteException {
    	AuthPacket serverAuthPacket = sessions.get(auth.getUsername());
        if (serverAuthPacket == null) {
            throw new NotLoggedInException("The user "+ auth.getUsername() + " has no token.");
        }
        if (!serverAuthPacket.equals(auth)) {
            throw new AuthenticationException("ServerToken and UserToken do not match.");
        }
    }
    
    /**
     * if the user is a GroupLeader nothing happens
     * if the user is not a GroupLeader a NoPermissionException gets thrown
     * it should be catched on the client-side
     * @param username
     * @throws RemoteException
     * @throws NoPermissionException
     */
    public static void isGroupLeader(AuthPacket auth) throws RemoteException {
        // TODO: check if the user is a group leader
        if (true) {
            throw new NoPermissionException("The user is not a group-leader");
        }
    }

    public static void isAuthor(AuthPacket auth, Message msg) throws RemoteException {
        // TODO: check if the user is a group leader
        if (true) {
            throw new NoPermissionException("The user is not the author");
        }
    }
    
    /**
     * removes the username from the sessionMap
     * call this to log the user out
     * @param username
     */
    public static void logout(String username) {
        sessions.remove(username);
    }

}