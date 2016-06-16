package models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.Logger;
import play.data.validation.ValidationError;

import com.avaje.ebean.Model;
import com.avaje.ebean.PagedList;

import fr.watchnext.utils.views.Selector;
import fr.watchnext.utils.controllers.CrudReady;
import static fr.watchnext.utils.models.ModelHelper.toOptional;
import static fr.watchnext.utils.models.ModelHelper.pageDisjunction;
import static fr.watchnext.utils.models.ModelHelper.notNullList;

/**
 * Definition of a Invoice Item
 */
@Entity 
public class InvoiceItem extends Model implements CrudReady<InvoiceItem, InvoiceItem> {
	private static InvoiceItem singleton = null;
	
	@Id
	public Long id;

	public String name;
	
	public Float cost;
	
	public boolean noVat = false;

	@ManyToOne
	public Invoice invoice;
	
	public InvoiceItem() {
    	super();
    }
	
	public InvoiceItem(String name, Float cost, boolean noVat) {
    	this.name = name;
    	this.cost = cost;
    	this.noVat = noVat;
    }
	
	public static InvoiceItem of() {
    	if (singleton == null)
    		singleton = new InvoiceItem();
    	return singleton;
    }
	
	// -- Special Accessors

    
    // -- Queries
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Model.Finder<String,InvoiceItem> find = new Model.Finder(InvoiceItem.class);
    
    public static List<InvoiceItem> findAll() {
        return notNullList(find.all());
    }
    
    public static boolean doesAlreadyExist(String name, Long idOfInvoice) {
    	if (idOfInvoice == null) {
    		List<InvoiceItem> result = find.where().ieq("name", name).findList();
    		return result != null && result.size() !=0;
    	} else {
    		List<InvoiceItem> result = find.where().conjunction().ieq("name", name).ne("invoice.id", idOfInvoice).findList();
    		return result != null && result.size() !=0;
    	}
    }
    
    private static String defaultOrdering() {
    	return "name ASC";
    }

    public static PagedList<InvoiceItem> page(int page, int pageSize, String sortBy, String order, String filter) {
    	return pageDisjunction(find, page, pageSize, sortBy, order, filter, Arrays.asList("name"), defaultOrdering());
    }
    
	@Override
	public void save() {
		super.save();
	}


	@Override
	public void update() {
		try {
			Logger.debug("Updating "+this.getClass().getName());
		} catch (Throwable t) {
			t.printStackTrace();
		}
		super.update();
	}
	
    public List<ValidationError> validate() {
    	List<ValidationError> errors = new ArrayList<ValidationError>();
        return errors.isEmpty() ? null : errors;
    }

	@Override
	public Finder<String, InvoiceItem> getFinder() {
		return find;
	}

	@Override
	public PagedList<InvoiceItem> getPage(int page, int pageSize, String sortBy, String order, String filter) {
		return page(page, pageSize, sortBy, order, filter);
	}
}

