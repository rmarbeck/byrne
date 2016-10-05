package controllers;

import java.util.Optional;

import models.User;
import fr.watchnext.utils.controllers.ActionAuthenticator;

public class MyAuthenticator extends ActionAuthenticator {
	private static MyAuthenticator singleton = null;

	public static MyAuthenticator of() {
		if (singleton == null)
			singleton = new MyAuthenticator();
		return singleton;
	}
	
    public String retrieveUserNameByToken(String token) {
    	Optional<User> user = User.findByLogin(token);
    	if (user.isPresent())
    		return user.get().name;
    	return null;
    }

}