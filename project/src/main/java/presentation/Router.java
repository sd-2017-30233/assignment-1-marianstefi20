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

    private static void logout(Request request) {
        request.session().removeAttribute("loggedIn");
        request.session().removeAttribute("employeeID");
    }

    private static void login(Request request, String username, String password) {
        request.session().attribute("loggedIn", true);
        request.session().attribute("employeeID", UserController.getID(username, password));
    }

    public static void getPages() {
        get("/login", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "/www/login.vm");
        }, new VelocityTemplateEngine());

        get("/logout", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            Router.logout(request);
            response.redirect("/login");
            return null;
        }, new VelocityTemplateEngine());

        get("/dash", (request, response) -> {
            Router.checkLoginStatus(request, response);
            Map<String, Object> model = new HashMap<>();
            int id = request.session().attribute("employeeID");
            int admin = request.session().attribute("admin");
            model.put("getAllClients", ClientController.getAllClientsAsTable());
            model.put("getAllEmployees", UserController.getAllEmployeesAsTable());
            model.put("admin", admin);
            model.put("employeeName", UserController.getName(id));
            model.put("employeeUsername", UserController.getUsername(id));
            model.put("employeePassword", UserController.getPassword(id));
            return new ModelAndView(model, "/www/members.vm");
        }, new VelocityTemplateEngine());

        get("/transactions/*/*", (request, response) -> {
            Router.checkLoginStatus(request, response);
            String type = request.splat()[0];
            int id = Integer.parseInt(request.splat()[1]);
            Map<String, Object> model = new HashMap<>();
            model.put("clientName", ClientController.getName(id));
            model.put("transactions", ClientController.getTransactionsAsTable(id));
            return new ModelAndView(model, "/www/transactions.vm");
        }, new VelocityTemplateEngine());

        get("/edit/*", (request, response) -> {
            Router.checkLoginStatus(request, response);
            int id = Integer.parseInt(request.splat()[0]);
            Map<String, Object> model = new HashMap<>();
            model.put("clientID", id);
            model.put("clientName", ClientController.getName(id));
            model.put("clientCNP", ClientController.getCNP(id));
            model.put("clientAdress", ClientController.getAdress(id));
            return new ModelAndView(model, "/www/edit.vm");
        }, new VelocityTemplateEngine());

    }

    private static void checkAdmin(String username, String password, Request request) {
        int role = UserController.getRole(username,password);
        if(role == 1) {
            request.session().attribute("admin", 1);
        } else request.session().attribute("admin", 0);
    }

    public static void postPages() {
        post("/login", (request, response) -> {
            String body = request.body();
            JSONObject obj = new JSONObject(body);
            String username = obj.getString("username");
            String password = obj.getString("password");
            // check the connection via the bussiness layer and send back if ok
            if(LoginController.checkLoginCredentials(username, password)) {
                Router.checkAdmin(username, password, request);
                Router.login(request, username, password);
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
            if(ClientController.edit(id,name,cnp,adress)) {
                return Message.OK;
            } else return Message.ERROR;
        });


    }

}
