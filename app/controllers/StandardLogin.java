package controllers;

import fr.watchnext.utils.controllers.*;
import models.FullLogin;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Security;

public class StandardLogin extends Controller {
	public static Login<FullLogin, MyAuthenticator> login = Login.of(
			MyAuthenticator.of(),
			views.html.login.ref(),
			Form.form(FullLogin.class),
			"/login",
			"/display/Invoices/All");
}

