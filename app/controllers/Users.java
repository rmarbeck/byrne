package controllers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import models.User;
import fr.watchnext.utils.controllers.*;
import play.Logger;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import utils.InvoiceKey;

@Security.Authenticated(MyAuthenticator.class)
public class Users extends Controller {
	public static Crud<User, User> crud = Crud.of(
			User.of(),
			views.html.cruds.user.ref(),
			views.html.cruds.user_form.ref(),
			views.html.cruds.users.ref());
	
}

