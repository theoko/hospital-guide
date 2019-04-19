package helpers;

import controllers.auth.AuthController;
import models.user.User;

public class UserHelpers {

    public static User getCurrentUser() {

        User currentUser = AuthController.currentUser;

        return currentUser;

    }

}
