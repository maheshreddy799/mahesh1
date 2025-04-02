package com.bhasaka.newsportal.core.services.impl;

import com.google.gson.*;
import com.bhasaka.newsportal.core.config.DummyApiConfiguration;
import com.bhasaka.newsportal.core.services.DummyApiService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component(service = DummyApiService.class, immediate = true)
public class DummyApiServiceImpl implements DummyApiService {

    private static final Logger LOG = LoggerFactory.getLogger(DummyApiServiceImpl.class);

    @Reference
    private DummyApiConfiguration dummyApiConfiguration;

    @Override
    public List<String> getFilteredDataById(int id) {
        List<String> filteredData = new ArrayList<>();

        try {
            JsonObject jsonResponse = fetchApiData();
            JsonArray productsArray = jsonResponse.getAsJsonArray("products");
            int totalProducts = productsArray.size();

            if (id <= 0) {
                filteredData.add("Error: ID must be greater than 0.");
            } else if (id > totalProducts) {
                filteredData.add("Error: There are only " + totalProducts + " records available. You entered " + id + ", which exceeds the limit.");
            } else {
                for (int i = 0; i < id; i++) {
                    JsonObject product = productsArray.get(i).getAsJsonObject();
                    filteredData.add(product.toString());
                }
            }
        } catch (Exception e) {
            LOG.error("Error fetching data by ID: {}", id, e);
            filteredData.add("Error fetching data from API: " + e.getMessage());
        }

        return filteredData;
    }

    @Override
    public List<String> getFilteredDataByTitle(String title) {
        List<String> filteredData = new ArrayList<>();

        try {
            JsonObject jsonResponse = fetchApiData();
            JsonArray productsArray = jsonResponse.getAsJsonArray("products");

            for (JsonElement element : productsArray) {
                JsonObject product = element.getAsJsonObject();
                if (product.has("title") && product.get("title").getAsString().equalsIgnoreCase(title)) {
                    filteredData.add(product.toString());
                }
            }

            if (filteredData.isEmpty()) {
                filteredData.add("Error: No product found with title '" + title + "'.");
            }
        } catch (Exception e) {
            LOG.error("Error fetching data by Title: {}", title, e);
            filteredData.add("Error fetching data from API: " + e.getMessage());
        }

        return filteredData;
    }

    private JsonObject fetchApiData() throws IOException {
        String apiUrl = dummyApiConfiguration.getApiUrl();

        if (apiUrl == null || apiUrl.isEmpty()) {
            throw new IllegalStateException("API URL is not set in OSGi configuration");
        }

        LOG.info("Fetching data from API: {}", apiUrl);

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        JsonElement jsonElement = JsonParser.parseString(response.toString());
        if (jsonElement.isJsonObject()) {
            return jsonElement.getAsJsonObject();
        } else {
            throw new IllegalStateException("API response is not in expected JSON object format.");
        }
    }
}
