package models;

import static fr.watchnext.utils.models.ModelHelper.notNullList;
import static fr.watchnext.utils.models.ModelHelper.pageDisjunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import play.Logger;
import play.data.validation.Constraints.*;
import play.data.validation.ValidationError;

import com.avaje.ebean.Model;
import com.avaje.ebean.PagedList;

import fr.watchnext.utils.controllers.CrudReady;

/**
 * Definition of a Item
 */
@Entity 
public class Invoice extends Model implements CrudReady<Invoice, Invoice> {
	private static Invoice singleton = null;
	
	@Id
	public Long id;
	
	@Column(unique=true, name="unique_serial_key")
	@Required
	public String uniqueSerialKey;
	
	@Column(name="creation_date")
	public Date creationDate;
	
	@Column(name="last_modification_date")
	public Date lastModificationDate;

	@Column(name="invoice_date")
	public Date invoiceDate;
	
	@Column(name="customer_type_of_invoice")
	public String customerTypeOfInvoice;
	
	public String brands;
	
	public String movements;
	
	public String serials;
	
	public String cases;
	
	public String straps;
	
	public String dials;
	
	@Column(length = 10000)
	@Required
	public String customer;
	
	@OneToMany(mappedBy="invoice", cascade = CascadeType.ALL)
	public List<InvoiceItem> items;
	
	public boolean checkItems = true;
	
	@Column(name="parts_cost")
	public Float partsCost;
	
	@Column(name="shipping_cost")
	public Float shippingCost = 0f;
	
	@Column(length = 10000)
	public String remarks;
	
	public Invoice() {
    	this.invoiceDate = new Date();
    }
	
	public static Invoice of() {
    	if (singleton == null)
    		singleton = new Invoice();
    	return singleton;
    }
	

	
	// -- Special Accessors
	public String getUniqueSerialKey() {
		return uniqueSerialKey;
	}
	
	public Date getInvoiceDate() {
		return invoiceDate;
	}
	
	public Float getBeforeTaxesPrice() {
		Float price = 0f;
		for(InvoiceItem item : items)
			if(item.cost != null)
				price+=item.cost;
		price+=shippingCost;
		return price;
	}
	
	public Float getVat() {
		return getPrice() - getBeforeTaxesPrice();
	}
	
	public Float getCalculatedPartsCost() {
		if(partsCost == null || partsCost < 0)
			return (getBeforeTaxesPrice() - getShippingCost()) * 0.4f;
		return partsCost;
	}
	
	public Float getManWorkingCost() {
		return getBeforeTaxesPrice() - getCalculatedPartsCost() - getShippingCost();
	}
	
	public Float getShippingCost() {
		return shippingCost;
	}
		
	public Float getPrice() {
		Float price = 0f;
		for(InvoiceItem item : items)
			if(item.cost != null)
				if(item.noVat) {
					price+=item.cost;
				} else {
					price+=item.cost*1.2f;
				}
		price+=shippingCost*1.2f;
		return price;
	}
	
    // -- Queries
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Model.Finder<String,Invoice> find = new Model.Finder(Invoice.class);
    
    public static List<Invoice> findAll() {
        return notNullList(find.all());
    }
    
    public static Optional<Invoice> findLastByKey() {
    	List<Invoice> invoices = find.orderBy("unique_serial_key ASC").findList();
        if (invoices != null && invoices.size() != 0)
        	return Optional.of(invoices.get(0));
        return Optional.empty();
	}
    
    private static String defaultOrdering() {
    	return "invoice_date ASC";
    }

    public static PagedList<Invoice> page(int page, int pageSize, String sortBy, String order, String filter) {
    	return pageDisjunction(find, page, pageSize, sortBy, order, filter, Arrays.asList(/*"unique_serial_key", "customer"*/), defaultOrdering());
    }
    
	@Override
	public void save() {
		Logger.error("Saving..."+this.uniqueSerialKey);
		Logger.error("Saving..."+this.id);
		creationDate = new Date();
		lastModificationDate = new Date();
		super.save();
	}


	@Override
	public void update() {
		try {
			Logger.debug("Updating "+this.getClass().getName());
		} catch (Throwable t) {
			t.printStackTrace();
		}
		lastModificationDate = new Date();
		super.update();
	}
	
    public List<ValidationError> validate() {
    	List<ValidationError> errors = new ArrayList<ValidationError>();
    	int index = 0;
    	if (this.checkItems)
    		for(InvoiceItem currrentItem : this.items) {
    			if (InvoiceItem.doesAlreadyExist(currrentItem.name)) {
    				errors.add(new ValidationError("checkItems", currrentItem.name + " existe deja."));
    				errors.add(new ValidationError("items["+index+"].name", currrentItem.name + " existe deja."));
    			}
    			index++;
    	}
        return errors.isEmpty() ? null : errors;
    }

	@Override
	public Finder<String, Invoice> getFinder() {
		return find;
	}

	@Override
	public PagedList<Invoice> getPage(int page, int pageSize, String sortBy, String order, String filter) {
		return page(page, pageSize, sortBy, order, filter);
	}
	
	public String toString() {
		return uniqueSerialKey;
	}
}


