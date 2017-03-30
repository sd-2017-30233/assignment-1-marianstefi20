package presentation;

import bussiness.LoginController;
import org.json.JSONObject;

import javax.servlet.MultipartConfigElement;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static spark.Spark.*;

public class GetPages {

    private String renderContent(String htmlFile) {
        try {
            URL url = getClass().getResource(htmlFile); // string cu path
            Path path = Paths.get(url.toURI());
            return new String(Files.readAllBytes(path), Charset.defaultCharset());
        } catch (IOException | URISyntaxException e) {
            System.out.println("I could not find that file!");
        }
        return null;
    }

    public void getLogin() {
        staticFiles.location("/www"); // Static files
        before("/area",(request, response) -> {
            Object ob = request.session().attribute("loggedIn");
            System.out.println(ob.toString());
            if(request.session().attribute("loggedIn")) {
                System.out.println("Am gasit sesiunea!");
            } else {
                System.out.println("Nu am gasit sesiunea!");
            }
        });
        get("/login", (request, response) -> renderContent("/www/login.html"));
        get("/area", (request, response) -> renderContent("/www/members.html"));
        post("/login", (request, response) -> {
            String body = request.body();
            JSONObject obj = new JSONObject(body);
            String username = obj.getString("username");
            String password = obj.getString("password");
            // check the connection via the bussiness layer and send back if ok
            if(LoginController.checkLogin(username, password)) {
                // e logat...minunat...setam sesiune
                request.session().attribute("loggedIn", 1);
                request.session().attribute("username", username);
                request.session().attribute("password", password);
                return Message.OK;
            } else {
                return Message.ERROR;
            }
        });
    }

    public void getLoginSuccessful() {

    }


}
