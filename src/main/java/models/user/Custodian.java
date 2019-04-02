package models.user;

import helpers.Constants;

public class Custodian extends User{
    public Custodian (int userID, String username, String password, Constants.Auth userType) {
        super(userID, username, password, userType);
    }
}