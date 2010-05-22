package net.stax.roo.addon;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;
import org.springframework.roo.metadata.MetadataService;
import org.springframework.roo.process.manager.FileManager;
import org.springframework.roo.process.manager.MutableFile;
import org.springframework.roo.project.Path;
import org.springframework.roo.project.PathResolver;
import org.springframework.roo.project.ProjectMetadata;
import org.springframework.roo.support.util.Assert;
import org.springframework.roo.support.util.FileCopyUtils;

/**
 * Implementation of commands that are available via the Roo shell.
 *
 * @author Ben Alex
 * @since 1.1.0M1
 */
@Component
@Service
public class OperationsImpl implements Operations{

	private static Logger logger = Logger.getLogger(Operations.class.getName());

	@Reference private FileManager fileManager;
	@Reference private MetadataService metadataService;

    private ComponentContext context;

    protected void activate(ComponentContext context) {
	    this.context = context;
    }

	public boolean isProjectAvailable() {
		return getPathResolver() != null;
	}

	/**
	 * @param propertyName to obtain (required)
	 * @return a message that will ultimately be displayed on the shell
	 */
	public String getProperty(PropertyName propertyName) {
		Assert.notNull(propertyName, "Property name required");
		String internalName = "user.name";
		if (PropertyName.HOME_DIRECTORY.equals(propertyName)) {
			internalName = "user.home";
		}
		return propertyName.getPropertyName() + " : " + System.getProperty(internalName);
	}

	/**
	 * @return true if the user's project has a /[name].txt file
	 */
	public boolean isTextFileAvailable(String name) {
		Assert.hasText(name, "Text file name to check for is required");
		PathResolver pr = getPathResolver();
		if (pr == null) {
			return false;
		}
		File file = new File(pr.getIdentifier(Path.ROOT, name + ".txt"));
		return file.exists();
	}

	public void writeTextFile(String name) {
		Assert.hasText(name, "Text file name to write is required");
		PathResolver pr = getPathResolver();
		Assert.notNull(pr, "Path resolver not found");
		String path = pr.getIdentifier(Path.ROOT, name + ".txt");
		File file = new File(path);
		MutableFile mutableFile;
		if (file.exists()) {
			mutableFile = fileManager.updateFile(path);
		} else {
			mutableFile = fileManager.createFile(path);
		}
		byte[] input = new String("Write text file method called at " + new Date().toString()).getBytes();
		try {
			FileCopyUtils.copy(input, mutableFile.getOutputStream());
		} catch (IOException ioe) {
			throw new IllegalStateException(ioe);
		}
		logger.log(Level.WARNING, "Wrote: " + new String(input));
	}

	/**
	 * @return the path resolver or null if there is no user project
	 */
	private PathResolver getPathResolver() {
		ProjectMetadata projectMetadata = (ProjectMetadata) metadataService.get(ProjectMetadata.getProjectIdentifier());
		if (projectMetadata == null) {
			return null;
		}
		return projectMetadata.getPathResolver();
	}
}