package bussiness;

import datasource.UserGateway;

/**
 * Created by maria on 3/30/2017.
 */
public class LoginController {

    private static boolean userExists(String username, String password) {
        return UserGateway.ifuserexists(username, password);
    }

    public static boolean checkLogin(String username, String password) {
        // make hashed passwords
        //...
        //check if the user exists
        if(userExists(username, password)) {
            return true;
        }
        return false;
    }

}
