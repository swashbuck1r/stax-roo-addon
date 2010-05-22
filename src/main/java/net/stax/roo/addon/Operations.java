package net.stax.roo.addon;

/**
 * Interface of commands that are available via the Roo shell.
 *
 * @author Ben Alex
 * @since 1.1.0-M1
 */
public interface Operations {

	boolean isProjectAvailable();

	/**
	 * @param propertyName to obtain (required)
	 * @return a message that will ultimately be displayed on the shell
	 */
	String getProperty(PropertyName propertyName);

	/**
	 * @return true if the user's project has a /[name].txt file
	 */
	boolean isTextFileAvailable(String name);

	void writeTextFile(String name);
}