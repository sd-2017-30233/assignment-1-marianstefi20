package presentation;

import bussiness.ClientController;
import bussiness.LoginController;
import bussiness.UserController;
import com.sun.deploy.util.SessionState;
import org.eclipse.jetty.server.Authentication;
import org.json.JSONObject;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static spark.Spark.*;

public class Router {
    // Not used - implements basic finder with getTagValues
    private static final Pattern TAG_REGEX = Pattern.compile("<<(.+?)>>");
    private LoginController loginController = new LoginController();
    private static UserController userController;

    private static List<String> getTagValues(final String str) {
        final List<String> tagValues = new ArrayList<String>();
        final Matcher matcher = TAG_REGEX.matcher(str);
        while (matcher.find()) {
            tagValues.add(matcher.group(1));
        }
        return tagValues;
    }

    // Rendering functionality
    private String renderContent(String htmlFile) {
        try {
            URL url = getClass().getResource(htmlFile); // string cu path
            Path path = Paths.get(url.toURI());
            byte[] content = Files.readAllBytes(path); // get all the bytes
            Object options  = getTagValues(new String(content)).toArray();
            System.out.println(Arrays.toString(getTagValues(new String(content)).toArray()));
            return new String(Files.readAllBytes(path), Charset.defaultCharset());
        } catch (IOException | URISyntaxException e) {
            System.out.println("I could not find that file!");
        }
        return null;
    }

    public static void setStatic() {
        staticFiles.location("/www"); // Static files
    }

    private static void checkLoginStatus(Request request, Response response) {
        if(request.session().attribute("loggedIn") == null) {
            request.session().attribute("loginRedirect", request.pathInfo());
            response.redirect("/login");
        }
    }

    private void logout(Request request) {
        request.session().removeAttribute("loggedIn");
        request.session().removeAttribute("employeeID");
    }

    private void login(Request request, String username, String password) {
        request.session().attribute("loggedIn", true);
        request.session().attribute("employeeID", userController.getId());
    }

    public void getPages() {
        get("/login", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "/www/login.vm");
        }, new VelocityTemplateEngine());

        get("/logout", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            this.logout(request);
            response.redirect("/login");
            return null;
        }, new VelocityTemplateEngine());

        get("/dash", (request, response) -> {
            Router.checkLoginStatus(request, response);
            Map<String, Object> model = new HashMap<>();
            int id = request.session().attribute("employeeID");
            int admin = request.session().attribute("admin");
            model.put("getAllClients", ClientController.getAllClientsAsTable());
            model.put("getAllEmployees", userController.getAllEmployeesAsTable());
            model.put("admin", admin);
            model.put("employeeName", userController.getName());
            model.put("employeeUsername", userController.getUsername());
            model.put("employeePassword", userController.getPassword());
            return new ModelAndView(model, "/www/members.vm");
        }, new VelocityTemplateEngine());

        get("/transactions/*/*", (request, response) -> {
            Router.checkLoginStatus(request, response);
            String type = request.splat()[0];
            int id = Integer.parseInt(request.splat()[1]);
            Map<String, Object> model = new HashMap<>();
            ClientController client = new ClientController(id);
            model.put("clientName", client.getName());
            model.put("transactions", client.getTransactionsAsTable());
            return new ModelAndView(model, "/www/transactions.vm");
        }, new VelocityTemplateEngine());

        get("/edit/*", (request, response) -> {
            Router.checkLoginStatus(request, response);
            int id = Integer.parseInt(request.splat()[0]);
            ClientController client = new ClientController(id);
            Map<String, Object> model = new HashMap<>();
            model.put("clientID", id);
            model.put("clientName", client.getName());
            model.put("clientCNP", client.getCnp());
            model.put("clientAdress", client.getAdress());
            return new ModelAndView(model, "/www/edit.vm");
        }, new VelocityTemplateEngine());

    }

    private void checkAdmin(Request request) {
        int role = userController.getRole();
        if(role == 1) {
            request.session().attribute("admin", 1);
        } else request.session().attribute("admin", 0);
    }

    public void postPages() {
        post("/login", (request, response) -> {
            String body = request.body();
            JSONObject obj = new JSONObject(body);
            String username = obj.getString("username");
            String password = obj.getString("password");
            // check the connection via the bussiness layer and send back if ok
            if(loginController.checkLoginCredentials(username, password)) {
                userController = new UserController(username, password);
                this.checkAdmin(request);
                this.login(request, username, password);
                return Message.OK;
            } else {
                return Message.ERROR;
            }
        });

        post("/edit/*", (request,response) -> {
            int id = Integer.parseInt(request.splat()[0]);
            String body = request.body();
            JSONObject obj = new JSONObject(body);
            String name = obj.getString("name");
            Long cnp = Long.parseLong(obj.getString("cnp"));
            String adress = obj.getString("adress");
            ClientController client = new ClientController(id);
            client.setName(name);
            client.setCnp(cnp);
            client.setAdress(adress);
            return (client.update()) ? Message.OK : Message.ERROR;
        });
    }

}
