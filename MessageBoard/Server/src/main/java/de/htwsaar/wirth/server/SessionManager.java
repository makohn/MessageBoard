package de.htwsaar.wirth.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.htwsaar.wirth.remote.exceptions.AuthenticationException;
import de.htwsaar.wirth.remote.exceptions.NotLoggedInException;
import de.htwsaar.wirth.remote.model.auth.AuthPacket;
import de.htwsaar.wirth.remote.model.auth.LoginPacket;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.remote.model.interfaces.User;
import de.htwsaar.wirth.remote.util.HashUtil;
import de.htwsaar.wirth.server.service.Services;

public class SessionManager {

    /**
     * the session map, which stores a authentification token for each username
     */
    private static Map<String, AuthPacket> sessions = new ConcurrentHashMap<String, AuthPacket>();

    /**
     * logs a user in and generates a new token for the given username
     * if the username or password is wrong, a AuthenticationException gets thrown

     * @return the AuthPacket, which can be used by the client to access the remote methods
     * @throws AuthenticationException, when the username or password is wrong
     */
    public static AuthPacket authenticate(LoginPacket login) {
        String givenPassword = login.getPassword();
    	// hash the given cleartext password
        givenPassword = HashUtil.hashSha512(givenPassword);
        
        // load the user from the database
    	String givenUsername = login.getUsername();
    	User user = Services.getInstance().getUserService().getUser(givenUsername);
    	// check, if the hashValues are equal
    	if (givenPassword.equals(user.getPassword())) {
    		// successful login
            AuthPacket auth = new AuthPacket(givenUsername);
            sessions.put(givenUsername, auth);
            return auth;
        } else {
            throw new AuthenticationException("Wrong Username or Password.");
        }
    }

    /**
     * check, if the user is authenticated by the given token.
     * 
     * @param auth
     * @throws NotLoggedInException, if the there is no such user token
     * @throws AuthenticationException, if the given token does not match the token in the sessionsMap a AuthenticationException gets thrown
     */
    public static void verifyAuthPacket(AuthPacket auth) {
    	AuthPacket serverAuthPacket = sessions.get(auth.getUsername());
        if (serverAuthPacket == null) {
            throw new NotLoggedInException("The user "+ auth.getUsername() + " has no token.");
        }
        if (!serverAuthPacket.equals(auth)) {
            throw new AuthenticationException("ServerToken and UserToken do not match.");
        }
    }
    
    /**
     * returns whether the given authPacket belongs to a user, who is a group leader
     * @param auth, the authPacket of the user
     * @return true, if the user is a groupLeader | false otherwise
     */
    public static boolean isGroupLeader(AuthPacket auth) {
        User user = Services.getInstance().getUserService().getUser(auth.getUsername());
        return user.isGroupLeader();
    }

    /**
     * returns whether the given msg is from the given authPackets user
     * @param auth, the authPacket of the user
     * @param msg, the message to check against
     * @return true, if the user is the author of the given msg | false otherwise
     */
    public static boolean isAuthor(AuthPacket auth, Message msg) {
        return auth.getUsername().equals(msg.getAuthor());
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