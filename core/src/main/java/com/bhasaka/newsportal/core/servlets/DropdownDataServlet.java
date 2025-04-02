package com.bhasaka.newsportal.core.servlets;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet to provide dropdown data dynamically for AEM dialogs.
 */
@Component(
    service = Servlet.class,
    property = {
        Constants.SERVICE_DESCRIPTION + "=Dropdown Data Servlet",
        "sling.servlet.methods=GET",
        "sling.servlet.paths=/bin/dropdownData"
    }
)
public class DropdownDataServlet extends SlingSafeMethodsServlet {
    
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        
        // Create the JSON response object
        JsonObject jsonResponse = new JsonObject(); // Root JSON object
        JsonArray itemsArray = new JsonArray(); // Array for dropdown options

        // Create dropdown options
        JsonObject option1 = new JsonObject();
        option1.addProperty("value", "option1");
        option1.addProperty("text", "Option 1");

        JsonObject option2 = new JsonObject();
        option2.addProperty("value", "option2");
        option2.addProperty("text", "Option 2");

        // Add options to the array
        itemsArray.add(option1);
        itemsArray.add(option2);

        // Wrap in "items" object
        jsonResponse.add("items", itemsArray);

        // Set response type
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Write response
        ServletOutputStream out = response.getOutputStream();
        out.print(jsonResponse.toString());
        out.flush();
    }
}
