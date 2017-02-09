package de.htwsaar.wirth.server;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import de.htwsaar.wirth.remote.exceptions.AuthenticationException;
import de.htwsaar.wirth.remote.exceptions.NoPermissionException;
import de.htwsaar.wirth.remote.exceptions.NotLoggedInException;

public class SessionManager {

    /**
     * the session map, which stores a authentification token for each username
     */
    private static Map<String, UUID> sessions = new ConcurrentHashMap<String, UUID>();

    public static UUID authenticate(String username, String password) throws RemoteException {
        // TODO: check, if this password equals the password in the database
        if(true) {
            UUID token = UUID.randomUUID();
            sessions.put(username, token);
            return token;
        } else {
            throw new AuthenticationException("Wrong Username or Password.");
        }
    }

    public static void isAuthenticatedByToken(String username, UUID token) throws RemoteException {
        UUID serverToken = sessions.get(username);
        if (serverToken == null) {
            throw new NotLoggedInException("The user "+ username+ " has no token.");
        }
        if (!serverToken.equals(token)) {
            throw new AuthenticationException("ServerToken and UserToken do not match.");
        }
    }

    public static void isGroupLeader(String username) throws RemoteException {
        // TODO: check if the user is a group leader
        if (true) {
            throw new NoPermissionException("The user is not a group-leader");
        }
    }

    public static void logout(String username) {
        sessions.remove(username);
    }

}