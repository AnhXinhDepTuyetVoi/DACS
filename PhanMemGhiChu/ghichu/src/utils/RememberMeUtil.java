package utils;

import java.io.*;
import java.util.Properties;

public class RememberMeUtil {
    private static final String FILE = "rememberme.properties";

    public static void save(String username, String password) {
        try (OutputStream output = new FileOutputStream(FILE)) {
            Properties props = new Properties();
            props.setProperty("username", username);
            props.setProperty("password", password);
            props.store(output, null);
        } catch (IOException ex) { ex.printStackTrace(); }
    }

    public static String[] load() {
        try (InputStream input = new FileInputStream(FILE)) {
            Properties props = new Properties();
            props.load(input);
            return new String[]{props.getProperty("username"), props.getProperty("password")};
        } catch (IOException ex) {
            return new String[]{"", ""};
        }
    }

    public static void clear() {
        new File(FILE).delete();
    }
}

