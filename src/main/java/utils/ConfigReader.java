package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties;

    static {
        properties = new Properties();
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find config.properties");
            }
            properties.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Error loading configuration properties", ex);
        }
    }

    public static String getDefaultBrowser() {
        return properties.getProperty("default.browser", "chrome");
    }


    public static int getImplicitWait() {
        return Integer.parseInt(properties.getProperty("implicit.wait.seconds", "10"));
    }

    public static int getExplicitWait() {
        return Integer.parseInt(properties.getProperty("explicit.wait.seconds", "10"));
    }

    public static String getScreenshotDirectory() {
        return properties.getProperty("screenshot.directory", "./screenshots/");
    }
    public static Properties getProperties(){
        return properties;
    }
}
