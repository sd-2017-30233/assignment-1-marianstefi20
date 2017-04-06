package bussiness;

import datasource.UserGateway;

/**
 * Created by maria on 3/30/2017.
 */
public class LoginController {
    private UserGateway userGateway = new UserGateway();

    private boolean userExists(String username, String password) {
        return userGateway.ifuserexists(username, password);
    }

    public boolean checkLoginCredentials(String username, String password) {
        // call the hash passwords
        //...
        //check if the user exists
        if(userExists(username, password)) {
            return true;
        }
        return false;
    }

}
