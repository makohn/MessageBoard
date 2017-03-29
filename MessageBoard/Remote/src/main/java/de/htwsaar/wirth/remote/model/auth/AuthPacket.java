package de.htwsaar.wirth.remote.model.auth;

import java.io.Serializable;
import java.util.UUID;

/**
 * Class {@code AuthPacket} is given to the client if the user is already authenticated by the loginPackage.
 */
public class AuthPacket implements Serializable {

	private static final long serialVersionUID = 4872936952972156634L;
	
	private String username;
	private String groupName;
	private boolean isGroupLeader;
	private boolean isConnectedToRoot;

	private UUID token;

	public AuthPacket(String username, boolean isGroupLeader, String groupName,boolean connectedToRoot) {
		this.username = username;
		this.isGroupLeader = isGroupLeader;
		this.groupName = groupName;
		this.token = UUID.randomUUID();
		this.isConnectedToRoot= connectedToRoot;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getGroupName() {
		return groupName;
	}

	public boolean isGroupLeader() {
		return isGroupLeader;
	}

	public boolean isConnectedToRoot() {
		return isConnectedToRoot;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
		result = prime * result + (isGroupLeader ? 1231 : 1237);
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuthPacket other = (AuthPacket) obj;
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		if (isGroupLeader != other.isGroupLeader)
			return false;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

}
