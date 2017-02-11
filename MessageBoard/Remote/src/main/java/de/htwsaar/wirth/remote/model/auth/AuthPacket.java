package de.htwsaar.wirth.remote.model.auth;

import java.io.Serializable;
import java.util.UUID;

/**
 * The AuthPacket-Class is used by the client to authenticate on the server to execute remote functions.
 */
public class AuthPacket implements Serializable {

	private static final long serialVersionUID = 4872936952972156634L;
	
	private String username;

	private UUID token;
	
	public AuthPacket(String username) {
		this.username = username;
		this.token = UUID.randomUUID();
	}
	
	public String getUsername() {
		return username;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
