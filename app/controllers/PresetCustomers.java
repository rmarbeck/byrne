package controllers;

import models.PresetCustomer;
import fr.watchnext.utils.controllers.*;
import play.mvc.Controller;
import play.mvc.Security;

public class PresetCustomers extends Controller {
	public static Crud<PresetCustomer, PresetCustomer> crud = Crud.of(
			PresetCustomer.of(),
			views.html.cruds.presetcustomer.ref(),
			views.html.cruds.presetcustomer_form.ref(),
			views.html.cruds.presetcustomers.ref());
}

