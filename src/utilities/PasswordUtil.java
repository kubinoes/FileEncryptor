package utilities;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordUtil {
    // password and authentication to be stored in memory
    private static final ThreadLocal<char[]> password = new ThreadLocal<>();

    public static char[] getPassword() {
        return password.get();
    }
    public static void setPassword(char[] passwordInput) {
        password.set(passwordInput);
    }
    public static void removePassword() {
        password.remove();
    }

    public static boolean verifyPassword(char[] password) {
        if (validatePassword(password)) {
            return KeyStoreManager.loadKeyStore(password);
        }
        return false;
    }

    public static boolean validatePassword(char[] password) {
        String passwordString = new String(password);
        String regex = "^((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%?!_'^]).{8,20})$";
        Pattern regexPattern = Pattern.compile(regex);
        Matcher matcher = regexPattern.matcher(passwordString);
        return matcher.matches();
    }
}
