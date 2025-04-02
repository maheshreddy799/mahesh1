package com.bhasaka.newsportal.core.servlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/countrie",
                "sling.servlet.methods=GET"
        }
)
public class DynamicDropDownServlet extends SlingSafeMethodsServlet {
    
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ResourceResolver resourceResolver = request.getResourceResolver();
        
        try {
            Resource jsonResource = resourceResolver.getResource("/content/dam/newsportal/json-data/countries.json");
            
            if (jsonResource == null) {
                response.getWriter().write("[]"); // Return an empty array if not found
                return;
            }
            
            Resource renditionResource = jsonResource.getChild("jcr:content/renditions/original");
            InputStream jsonStream = renditionResource != null ? renditionResource.adaptTo(InputStream.class) : null;
            
            if (jsonStream == null) {
                response.getWriter().write("[]");
                return;
            }

            // Read JSON file as string
            String jsonData;
            try (Scanner scanner = new Scanner(jsonStream, StandardCharsets.UTF_8.name())) {
                jsonData = scanner.useDelimiter("\\A").next();
            }
            
            // Parse JSON
            JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();
            JsonArray itemsArray = jsonObject.getAsJsonArray("items");

            // Prepare response
            JsonArray dropdownOptions = new JsonArray();
            for (int i = 0; i < itemsArray.size(); i++) {
                JsonObject item = itemsArray.get(i).getAsJsonObject();
                JsonObject option = new JsonObject();
                option.addProperty("value", item.get("value").getAsString());
                option.addProperty("text", item.get("text").getAsString());
                dropdownOptions.add(option);
            }

            response.getWriter().write(dropdownOptions.toString());
        } catch (Exception e) {
            response.getWriter().write("[]"); // Return empty array in case of an error
        }
    }
}
