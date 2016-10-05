package controllers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import models.Invoice;
import models.PresetCustomer;
import fr.watchnext.utils.controllers.*;
import play.Logger;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import utils.InvoiceKey;

@Security.Authenticated(MyAuthenticator.class)
public class Invoices extends Controller {
	public static Crud<Invoice, Invoice> crud = Crud.of(
			Invoice.of(),
			views.html.cruds.invoice.ref(),
			views.html.cruds.invoice_form.ref(),
			views.html.cruds.invoices.ref());
	
	public Result createEmptyInvoice() {
        return createInvoice(-1L);
    }
	
	public Result createInvoice(Long id) {
        return crud.create(newInvoice(id.intValue()));
    }
	
	public Result duplicateInvoice(Long id) {
        return crud.create(duplicate(id));
    }
	
	private static Form<Invoice> newInvoice(int id) {
		models.Invoice invoice = new Invoice();
		switch(id) {
			case 0:
				invoice.customer = PresetCustomer.findByName("collector square").nameAndAddress;
				invoice.customerTypeOfInvoice = "SAV";
				invoice.checkItems = true;
				break;
			case 1:
				invoice.customer = PresetCustomer.findByName("collector square").nameAndAddress;
				invoice.customerTypeOfInvoice = "AVV";
				invoice.checkItems = true;
				break;
			case 2:
				invoice.customer = PresetCustomer.findByName("hometime").nameAndAddress;
				invoice.checkItems = false;
				break;
			case 3:
				invoice.customer = PresetCustomer.findByName("miller").nameAndAddress;
				invoice.checkItems = false;
				break;
			default:
				invoice.checkItems = true;
		}
		InvoiceKey.retrieveNextInvoiceKey().ifPresent(key -> invoice.uniqueSerialKey = key.toString());
		
		return Form.form(models.Invoice.class).fill(invoice);
	}
	
	private static Form<Invoice> duplicate(Long id) {
		models.Invoice invoice = new Invoice();
		InvoiceKey.retrieveNextInvoiceKey().ifPresent(key -> invoice.uniqueSerialKey = key.toString());
		
		Optional<models.Invoice> invoiceToDuplicate = Invoice.findById(id);
		invoiceToDuplicate.ifPresent(invoiceToDup -> {
			invoice.customer = invoiceToDup.customer;
			invoice.customerTypeOfInvoice = invoiceToDup.customerTypeOfInvoice;
			invoice.remarks = invoiceToDup.remarks;
		});
		return Form.form(models.Invoice.class).fill(invoice);	
	}
}

