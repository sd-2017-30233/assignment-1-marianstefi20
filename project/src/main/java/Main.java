import presentation.Filters;
import presentation.Router;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        // Spark config

        // Set up before-filters (called before each get/post)

        // Set up routes
        Router.setStatic(); // sets the static folder
        Router.getPages(); // gets all the pages
        Router.postPages(); // handles post request from the pages

        //Set up after-filters (called after each get/post)
        after("*", Filters.addGzipHeader);
    }
}