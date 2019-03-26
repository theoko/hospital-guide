package models.user;

public class Admin extends User {

    public Admin(int userID, String username, String password, int userType) {
        super(userID, username, password, userType);
    }
}
