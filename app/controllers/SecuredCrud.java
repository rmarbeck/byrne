package controllers;

import play.mvc.Security;
import fr.watchnext.utils.controllers.CrudHelper;

@Security.Authenticated(MyAuthenticator.class)
public class SecuredCrud extends CrudHelper {
	
}

