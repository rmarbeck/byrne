import java.util.List;
import java.util.Map;

import models.User;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.libs.Yaml;

import com.avaje.ebean.Ebean;
import com.typesafe.config.ConfigFactory;

public class Global extends GlobalSettings {
	
	static class InitialData {
		@SuppressWarnings("unchecked")
		public static void insert(Application app) {
			if (app.isDev() || forceReload()) {
				if(Ebean.find(User.class).findRowCount() == 0) {
					Map<String,List<Object>> all = (Map<String,List<Object>>)Yaml.load("reference-data-default.yml");

					Ebean.save(all.get("users"));
					
					Ebean.save(all.get("presetcustomers"));
				}
			}
		}

	}

    public void onStart(Application app) {
		if (!app.isTest()) {
			Logger.info("Application has started");
			InitialData.insert(app);
		} else {
			Logger.info("Application has started in test mode.");
		}
    }

    public void onStop(Application app) {
        Logger.info("Application shutdown...");
    }

    private static boolean forceReload() {
    	try {
    		return ConfigFactory.load().getBoolean("force.initial.data.reload");
    	} catch (Throwable t) {
    		return false;
    	}
    }
}