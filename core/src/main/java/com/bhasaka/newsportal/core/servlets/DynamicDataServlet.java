package com.bhasaka.newsportal.core.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.bhasaka.newsportal.core.services.DummyApiService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

@Component(service = { Servlet.class }, property = {
        "sling.servlet.paths=/bin/dynamicDataServlet",
        "sling.servlet.methods=GET,POST"
})
public class DynamicDataServlet extends SlingAllMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(DynamicDataServlet.class);

    @Reference
    private DummyApiService dummyApiService;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        handleRequest(request, response);
    }

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        handleRequest(request, response);
    }

    private void handleRequest(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Check if the DummyApiService is available
        if (dummyApiService == null) {
            LOG.error("DummyApiService is not available!");
            response.getWriter().write("{\"error\": \"Service is unavailable\"}");
            return;
        }

        String searchInput = request.getParameter("searchInput");
        List<String> filteredData;

        try {
            int id = Integer.parseInt(searchInput.trim());
            LOG.info("Fetching data by ID: {}", id);
            filteredData = dummyApiService.getFilteredDataById(id);
        } catch (NumberFormatException e) {
            LOG.info("Fetching data by Title: {}", searchInput);
            filteredData = dummyApiService.getFilteredDataByTitle(searchInput.trim());
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        response.getWriter().write(gson.toJson(filteredData));
    }
}
