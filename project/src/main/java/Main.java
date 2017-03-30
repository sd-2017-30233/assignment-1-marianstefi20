import presentation.Filters;
import presentation.GetPages;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        // Spark config

        // Set up before-filters (called before each get/post)

        // Set up routes
        GetPages getPages = new GetPages();
        getPages.getLogin();

        //Set up after-filters (called after each get/post)
        after("*", Filters.addGzipHeader);
    }
}