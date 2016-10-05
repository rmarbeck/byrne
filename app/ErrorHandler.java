import javax.inject.Inject;
import javax.inject.Provider;

import play.Configuration;
import play.Environment;
import play.Logger;
import play.api.OptionalSourceMapper;
import play.api.UsefulException;
import play.api.routing.Router;
import play.http.DefaultHttpErrorHandler;
import play.libs.F.Promise;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import play.mvc.Results;

public class ErrorHandler extends DefaultHttpErrorHandler {
	
    @Inject
    public ErrorHandler(Configuration configuration, Environment environment,
                        OptionalSourceMapper sourceMapper, Provider<Router> routes) {
        super(configuration, environment, sourceMapper, routes);
    }
    
    @Override
    protected Promise<Result> onProdServerError(RequestHeader request, UsefulException exception) {
        return Promise.<Result>pure(
                Results.internalServerError("A server error occurred: " + exception.getMessage())
        );
    }

    @Override
    protected Promise<Result> onForbidden(RequestHeader request, String message) {
    	Logger.error("Forbidden found !!!!!!!!!!");
        return Promise.<Result>pure(
        		Results.redirect("/login")
        );
    }

	@Override
	protected Promise<Result> onDevServerError(RequestHeader arg0,
			UsefulException arg1) {
		Logger.error("Server Error found !!!!!!!!!!");
		return super.onDevServerError(arg0, arg1);
	}

	@Override
	public Promise<Result> onClientError(RequestHeader arg0, int arg1,
			String arg2) {
		return super.onClientError(arg0, arg1, arg2);
	}
}