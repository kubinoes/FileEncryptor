package utilities;

import java.util.regex.Pattern;

public class PasswordUtil {
    // password and authentication to be stored in thread memory
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

    // verify if input password matches to the keystore password
    public static boolean verifyPassword(char[] password) {
        if (validatePassword(password)) {
            return KeyStoreManager.loadKeyStore(password);
        }
        return false;
    }

    // validate and sanitize password input with regular expression
    public static boolean validatePassword(char[] password) {
        String passwordString = new String(password);
        // regex to limit to lowercase and uppercase alphabetical characters, digits and certain characters
        // make sure that each of the above mentioned occurs at least once in the password
        // limit password from min 8 to max 20 characters
        String regex = "^[a-zA-Z\\d"
                + "@#$%+/\\\\.,?!:'^~\\-_\\[\\](){}]*"
                + "((?=.*\\d)"
                + "(?=.*[a-z])"
                + "(?=.*[A-Z])"
                + "(?=.*[@#$%+/\\\\.,?!:'^~\\-_\\[\\](){}]))"
                + ".{8,20}$";
        return Pattern.matches(regex, passwordString);
    }
}
