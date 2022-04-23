package viewComponents;

import utilities.KeyStoreManager;

public class User {
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

    public static boolean verifyPassword(char[] passwordToVerify) {
        if (passwordToVerify.length != 0) {
            return KeyStoreManager.loadKeyStore(passwordToVerify);
        }
        return false;
    }
}
