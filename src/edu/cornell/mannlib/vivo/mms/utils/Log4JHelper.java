/* $This file is distributed under the terms of the license in /doc/license.txt$ */

package edu.cornell.mannlib.vivo.mms.utils;

import java.net.URL;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;

/**
 * Methods that will help when setting up the Logging.
 */
public class Log4JHelper {
	private static final String DEFAULT_LOG_FILE_PATTERN = "%d{yyyy-MM-dd HH:mm:ss,SSS} %-5p [%c{1}] %m%n";

	/**
	 * Useful for unit testing. Discard whatever logging parameters were there
	 * and log to the console instead.
	 */
	public static void resetToConsole() {
		LogManager.resetConfiguration();

		PatternLayout layout = new PatternLayout(DEFAULT_LOG_FILE_PATTERN);
		ConsoleAppender appender = new ConsoleAppender(layout);
		Logger.getRootLogger().addAppender(appender);
		Logger.getRootLogger().setLevel(Level.WARN);
	}

	/**
	 * Useful for unit testing. Set the logging level for all classes.
	 */
	public static void setLoggingLevel(Level level) {
		Logger.getRootLogger().setLevel(level);
	}

	/**
	 * Useful for unit testing. Set the logging level for a class or package.
	 */
	public static void setLoggingLevel(String category, Level level) {
		Logger.getLogger(category).setLevel(level);
	}

	/**
	 * Find a properties file in the classpath, and add it to the Log4J
	 * configuration.
	 * 
	 * Fault tolerance: If the file can't be found, write a message to Standard
	 * Error, but don't throw an exception.
	 */
	public static void addConfigfile(String resourcePath) {
		URL configUrl = ClassLoader.getSystemResource(resourcePath);
		if (configUrl == null) {
			System.err.println("ERROR Log4JHelper: Can't find '" + resourcePath
					+ "' in the classpath.");
		} else {
			PropertyConfigurator.configure(configUrl);
			System.out.println("Log4JHelper: Added Log4j configuration from '"
					+ resourcePath + "'");
		}
	}

}
