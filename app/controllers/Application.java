package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;

import models.InvoiceItem;
import fr.watchnext.utils.controllers.ReverseAuthenticateHelperAdapter;
import fr.watchnext.utils.controllers.ReverseCrudHelperAdapter;
import play.*;
import play.libs.Json;
import play.mvc.*;

public class Application extends Controller {

    public Result index() {
    	return redirect(
    			ReverseCrudHelperAdapter.displayAll("Invoices", 20)
            );
    }
    
    public Result logout() {
    	return redirect(
    			ReverseAuthenticateHelperAdapter.logout("StandardLogin")
            );
    }
    
    public Result ajaxDoesItemAlreadyExist(String name, Long idOfInvoice) {
    	Logger.info("ajaxDoesItemAlreadyExist : "+name+"  "+idOfInvoice);
    	ObjectNode result = Json.newObject().put("result", InvoiceItem.doesAlreadyExist(name, idOfInvoice));
    	Logger.error("ajaxDoesItemAlreadyExist -> result : "+result);
    	return ok(result);
    }

}
