package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;

import models.InvoiceItem;
import fr.watchnext.utils.controllers.ReverseControllerHelper;
import fr.watchnext.utils.controllers.ReverseCrudHelperAdapter;
import play.*;
import play.libs.Json;
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
    
    public Result ajaxDoesItemAlreadyExist(String name, Long idOfInvoice) {
    	Logger.error("!!!!!!!!!!!!!!!!! "+name+"  "+idOfInvoice);
    	ObjectNode result = Json.newObject().put("result", InvoiceItem.doesAlreadyExist(name, idOfInvoice));
    	Logger.error("->>>>>>>>>>>> "+result);
    	return ok(result);
    }

}
