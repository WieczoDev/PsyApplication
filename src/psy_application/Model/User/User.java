package psy_application.Model.User;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class User {
    static final AtomicInteger count = new AtomicInteger(0);
    int user_ID;
    String user_login;
    String user_password;

    public int getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(int user_ID) {
        this.user_ID = user_ID;
    }

    public String getUser_login() {
        return user_login;
    }

    public void setUser_login(String user_login) {
        this.user_login = user_login;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    boolean isFilled;
}
