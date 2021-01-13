package tictactoe.model;

public final class Password {
    private static final long key = 6745902892214988737L;
    private final long encryptedPassword;

    public Password(String password) {
        this.encryptedPassword = encryptDecrypt(password);
    }

    private long encryptDecrypt(String password) {
        long numericPassword = 0;
        char character;
        for(int i = 0; i < password.length(); i++) {
            character = password.charAt(i);
            numericPassword += character;
        }
        return numericPassword ^ key;
    }

    public boolean letMeIn(String password) {
        return encryptDecrypt(password) == encryptedPassword;
    }
}
