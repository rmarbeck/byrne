package models;

import static fr.watchnext.utils.models.ModelHelper.notNullList;
import static fr.watchnext.utils.models.ModelHelper.pageDisjunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.data.validation.Constraints.Required;
import play.data.validation.ValidationError;

import com.avaje.ebean.Model;
import com.avaje.ebean.PagedList;

import fr.watchnext.utils.controllers.CrudReady;

/**
 * Definition of a User
 */
@Entity
@Table(name="appuser")
public class User extends Model implements CrudReady<User, User> {
	private static User singleton = null;
	
	@Id
	public Long id;
	
	@Column(unique=true)
	@Required
	public String login;
	
	public String password;
	
	public String name;
	
	@Column(name="privilege_level")
	public Long privilegeLevel = 0L;
	
	public User() {
    }
	
	public static User of() {
    	if (singleton == null)
    		singleton = new User();
    	return singleton;
    }
	
	// -- Special Accessors
	public boolean isPasswordValid(String enteredPassword) {
		return password.equals(enteredPassword);
	}
	
    // -- Queries
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Model.Finder<String,User> find = new Model.Finder(User.class);
    
    public static List<User> findAll() {
        return notNullList(find.all());
    }
        
    public static Optional<User> findByLogin(String login) {
    	User user = find.where().ieq("login", login).findUnique();
        if (user != null)
        	return Optional.of(user);
        return Optional.empty();
	}
    
    private static String defaultOrdering() {
    	return "name DESC";
    }

    public static PagedList<User> page(int page, int pageSize, String sortBy, String order, String filter) {
    	return pageDisjunction(find, page, pageSize, sortBy, order, filter, Arrays.asList("login", "name"), defaultOrdering());
    }
    
	@Override
	public void save() {
		prepareBeforeSaveOrUpdate();
		super.save();
	}


	@Override
	public void update() {
		prepareBeforeSaveOrUpdate();
		super.update();
	}
	
	private void prepareBeforeSaveOrUpdate() {
		
	}
	
    public List<ValidationError> validate() {
    	List<ValidationError> errors = new ArrayList<ValidationError>();

        return errors.isEmpty() ? null : errors;
    }

	@Override
	public Finder<String, User> getFinder() {
		return find;
	}

	@Override
	public PagedList<User> getPage(int page, int pageSize, String sortBy, String order, String filter) {
		return page(page, pageSize, sortBy, order, filter);
	}
}


