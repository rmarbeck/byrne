package controllers;

import fr.watchnext.utils.controllers.ReverseControllerHelper;
import fr.watchnext.utils.controllers.ReverseCrudHelperAdapter;
import play.*;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {

    public Result index() {
    	return redirect(
    			ReverseCrudHelperAdapter.displayAll("Invoices", 20)
            );
    }
    
    public Result createInvoice() {
    	
        return ok(index.render("Your new application is ready."));
    }

}
