package models;

import play.data.validation.Constraints;
import fr.watchnext.utils.models.SimpleLogin;
import fr.watchnext.utils.usual.SecurityHelper;

public class FullLogin extends SimpleLogin {
	@Constraints.Required
    public String username;
	
	@Constraints.Required
    public String password;
	
	public String origin;
	
	public boolean isAuthorized() {
		if (isItAValidLogin())
			return true;
		return false;
	}
	
	public String getToken() {
		return getUsername();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}
	
	private boolean isItAValidLogin() {
		if (doesUserExistInDB() && isPasswordCorrectForExistingUser()) {
			updatePasswordForExistingUserIfNeeded();
			return true;
		}
		return false;
	}
	
	private boolean doesUserExistInDB() {
		return User.findByLogin(username).isPresent();
	}
	
	private boolean isPasswordCorrectForExistingUser() {
		User user = User.findByLogin(username).get();
		return isItCorrectPassword(this.password, user.password);
	}
	
	private boolean isItCorrectPassword(String toCheck, String correctValue) {
		if (toCheck.equals(correctValue) || encryptPassword(toCheck).equals(correctValue))
			return true;
		return false;
	}
	
	private void updatePasswordForExistingUserIfNeeded() {
		User user = User.findByLogin(username).get();
		user.password = encryptPassword(this.password);
		user.update();
	}
	
	private String encryptPassword(String toEncrypt) {
		return SecurityHelper.toMD5(toEncrypt);
	}
}
