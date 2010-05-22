package net.stax.roo.addon;

import java.util.logging.Logger;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;
import org.springframework.roo.shell.CliAvailabilityIndicator;
import org.springframework.roo.shell.CliCommand;
import org.springframework.roo.shell.CliOption;
import org.springframework.roo.shell.CommandMarker;
import org.springframework.roo.shell.converters.StaticFieldConverter;
import org.springframework.roo.support.util.Assert;

/**
 * Sample of a command class. The command class is registered by the Roo shell following an
 * automatic classpath scan. You can provide simple user presentation-related logic in this
 * class. You can return any objects from each method, or use the logger directly if you'd
 * like to emit messages of different severity (and therefore different colours on 
 * non-Windows systems).
 * 
 * @since 1.1.0-M1
 */
@Component
@Service
public class Commands implements CommandMarker {
	
	private static Logger logger = Logger.getLogger(Commands.class.getName());

	@Reference private Operations operations;
	@Reference private StaticFieldConverter staticFieldConverter;

	protected void activate(ComponentContext context) {
	    staticFieldConverter.add(PropertyName.class);
    }

	protected void deactivate(ComponentContext context) {
		staticFieldConverter.remove(PropertyName.class);
	}
	
	@CliAvailabilityIndicator("stax setup")
    public boolean isSetupAvailable() {
        return true;
    }
    
    @CliCommand(value="stax setup", help="Sets up this project for Stax deployments")
    public String setup(@CliOption(key="appId", mandatory=false, help="The Stax application id") PropertyName appId) {
        return "TODO: setup stax: " + appId;
    }
	
	@CliAvailabilityIndicator("stax run")
	public boolean isRunAvailable() {
		return true;  // it's safe to always see the properties we expose
	}
	
	@CliCommand(value="stax run", help="Runs the local Stax server")
	public String run(@CliOption(key="port", mandatory=false, specifiedDefaultValue="8080", unspecifiedDefaultValue="8080", help="The port to listen on") int port) {
		return "TODO: launch server on port: " + port;
	}
}