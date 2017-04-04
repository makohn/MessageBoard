package de.htwsaar.wirth.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.htwsaar.wirth.remote.exceptions.AuthenticationException;
import de.htwsaar.wirth.remote.exceptions.NotLoggedInException;
import de.htwsaar.wirth.remote.model.auth.AuthPacket;
import de.htwsaar.wirth.remote.model.auth.LoginPacket;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.remote.model.interfaces.User;
import de.htwsaar.wirth.remote.util.HashUtil;
import de.htwsaar.wirth.server.service.Services;

/**
 * {@code SessionManager} manages each client-server session running between the
 * corresponding server instance and its clients. Its main purpose is to match a
 * client instance's username to an authentification token, that is used to 
 * authenticate the user within the running session.
 */
public class SessionManager {
	
	private static final Logger logger = LogManager.getLogger(SessionManager.class);
	
    /**
     * the session map, which stores an authentification token for each username
     */
    private Map<String, AuthPacket> sessions = new ConcurrentHashMap<String, AuthPacket>();
    
    private String groupName;
    private boolean isRootServer;
    
    public SessionManager(String groupName, boolean isRootServer) {
    	this.groupName = groupName;
    	this.isRootServer = isRootServer;
    	logger.info("Initialising the SessionManager for group: " + groupName);
    }

    /**
     * {@code authenticate} logs a user in and generates a new token for the given username.
     * If the username or password is wrong, an AuthenticationException is thrown.
     * @return the {@code AuthPacket}, which is used to authenticate the user to remote-
     * 		   call methods on the server.
     * @throws AuthenticationException, if the username or password is wrong
     */
    public AuthPacket authenticate(LoginPacket login) {
        String givenPassword = login.getPassword();
    	// hash the given cleartext password
        givenPassword = HashUtil.hashSha512(givenPassword);
        
        // load the user from the database
    	String givenUsername = login.getUsername();
    	User user = Services.getInstance().getUserService().getUser(givenUsername);
    	if ( user != null) {
	    	// check, if the hashValues are equal
	    	if (givenPassword.equals(user.getPassword())) {
	    		// successful login
	            AuthPacket auth = new AuthPacket(givenUsername, user.isGroupLeader(), groupName, isRootServer);
                sessions.put(givenUsername, auth);
                logger.info("User " + givenUsername + " logged in successfully");
                return auth;
	        }
	    }
    	throw new AuthenticationException("Wrong Username or Password.");
    }

    /**
     * {@code verifyAuthPacket} checks, whether the user is authenticated by the given token.
     * @param auth - the {@code AuthPacket} that the user sent within its request
     * @throws NotLoggedInException, if the there is no such user token
     * @throws AuthenticationException, if the given token does not match the token 
     * 		   in the {@code sessions} Map
     */
    public void verifyAuthPacket(AuthPacket auth) {
    	AuthPacket serverAuthPacket = sessions.get(auth.getUsername());
        if (serverAuthPacket == null) {
            throw new NotLoggedInException("The user "+ auth.getUsername() + " has no token.");
        }
        if (!serverAuthPacket.equals(auth)) {
            throw new AuthenticationException("ServerToken and UserToken do not match.");
        }
    }
    
    /**
     * {@code isGroupLeader} returns whether the given {@AuthPacket} authenticates a super user. 
     * @param auth - the {@code AuthPacket} that the user sent within its request
     * @return true, if the user is a {@code GroupLeader} (super user) | false otherwise
     */
    public boolean isGroupLeader(AuthPacket auth) {
        User user = Services.getInstance().getUserService().getUser(auth.getUsername());
        return user.isGroupLeader();
    }

    /**
     * {@code isAuthor} returns whether a given {@code Message}'s author matches with
     * the user that sends the request.
     * @param auth - the {@code AuthPacket} that the user sent within its request
     * @param msg, the {@code Message} to check against
     * @return true, if the user is the author of the given {@code Message} | false otherwise
     */
    public boolean isAuthor(AuthPacket auth, Message msg) {
        return auth.getUsername().equals(msg.getAuthor()) && groupName.equals(msg.getGroup());
    }
    
    /**
     * {@code logout} removes a user from the {@code sessions} map.
     * This method is usually called, whenever a user is about to log out.
     * @param username - the user that sent the {@code logout} request.
     */
    public void logout(String username) {
    	logger.info("User " + username + " logged out.");
        sessions.remove(username);
    }

	public String getGroupName() {
		return groupName;
	}

}