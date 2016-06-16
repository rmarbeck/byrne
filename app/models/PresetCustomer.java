package models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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
 * Definition of a Preset Customers
 */
@Entity 
public class PresetCustomer extends Model implements CrudReady<PresetCustomer, PresetCustomer> {
	private static PresetCustomer singleton = null;
	
	@Id
	public Long id;
	
	@Column(name="short_name")
	public String shortName;
	
	@Column(length = 10000, name="name_and_address")
	public String nameAndAddress;
	
	@Column(length = 10000)
	public String remarks;
	
	public static PresetCustomer of() {
    	if (singleton == null)
    		singleton = new PresetCustomer();
    	return singleton;
    }
	
	// -- Special Accessors
	public String getShortName() {
		return shortName;
	}
	
	public String getNameAndAddress() {
		return nameAndAddress;
	}
    
    // -- Queries
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Model.Finder<String,PresetCustomer> find = new Model.Finder(PresetCustomer.class);
    
    public static List<PresetCustomer> findAll() {
        return notNullList(find.all());
    }
    
    public static List<PresetCustomer> findAllByDisplayNameAsc() {
        return find.orderBy("short_name ASC").findList();
    }
    
    public static List<PresetCustomer> findAllByDisplayNameDesc() {
        return find.orderBy("short_name DESC").findList();
    }
    
    public static PresetCustomer findByName(String name) {
        return find.where().ieq("shortName", name).findUnique();
    }
        
    private static String defaultOrdering() {
    	return "short_name ASC";
    }

    public static PagedList<PresetCustomer> page(int page, int pageSize, String sortBy, String order, String filter) {
    	return pageDisjunction(find, page, pageSize, sortBy, order, filter, Arrays.asList("short_name", "name_and_address"), defaultOrdering());
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
	public Finder<String, PresetCustomer> getFinder() {
		return find;
	}

	@Override
	public PagedList<PresetCustomer> getPage(int page, int pageSize, String sortBy, String order, String filter) {
		return page(page, pageSize, sortBy, order, filter);
	}
    
	public static Selector<PresetCustomer> getSelectorByDisplayNameAsc() {
		return getSelector(PresetCustomer::findAllByDisplayNameAsc);
	}
	
	public static Selector<PresetCustomer> getSelectorByDisplayNameDesc() {
		return getSelector(PresetCustomer::findAllByDisplayNameDesc);
	}
	
	public static Selector<PresetCustomer> getDefaultSelector() {
		return getSelector(PresetCustomer::findAll);
	}
	
	public static Selector<PresetCustomer> getSelector(Supplier<List<PresetCustomer>> presets) {
		return Selector.of(presets, p -> p.id, p -> p.getShortName());
	}
	
	public static Selector<PresetCustomer> getDataSelector() {
		return Selector.of(PresetCustomer::findAllByDisplayNameAsc, p -> p.getNameAndAddress(), p -> p.getShortName());
	}
}

