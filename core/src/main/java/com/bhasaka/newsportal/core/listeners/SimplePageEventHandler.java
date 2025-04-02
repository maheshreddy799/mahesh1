// package com.bhasaka.newsportal.core.listeners;

// import com.day.cq.search.Query;
// import com.day.cq.search.QueryBuilder;
// import com.day.cq.search.result.SearchResult;
// import com.day.cq.search.PredicateGroup;
// import org.apache.sling.api.resource.*;
// import org.osgi.service.component.annotations.*;
// import org.osgi.service.event.Event;
// import org.osgi.service.event.EventConstants;
// import org.osgi.service.event.EventHandler;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import javax.jcr.Session;
// import java.util.HashMap;
// import java.util.Iterator;
// import java.util.Map;

// @Component(
//         service = EventHandler.class,
//         immediate = true,
//         property = {
//                 EventConstants.EVENT_TOPIC + "=org/apache/sling/api/resource/Resource/ADDED",
//                 EventConstants.EVENT_TOPIC + "=org/apache/sling/api/resource/Resource/CHANGED",
//                 EventConstants.EVENT_TOPIC + "=org/apache/sling/api/resource/Resource/REMOVED",
//                 EventConstants.EVENT_FILTER + "(&(path=/content/newsportal/us/en/author/*))"
//         }
// )
// public class SimplePageEventHandler implements EventHandler {

//     private static final Logger LOG = LoggerFactory.getLogger(SimplePageEventHandler.class);
//     private static final String AUTHOR_PATH = "/content/newsportal/us/en/author";

//     @Reference
//     private ResourceResolverFactory resourceResolverFactory;

//     @Reference
//     private QueryBuilder queryBuilder;

//     @Override
//     public void handleEvent(Event event) {
//         String path = (String) event.getProperty("path");

//         if (path == null || path.isEmpty()) {
//             LOG.warn("⚠️ No path found in event, skipping.");
//             return;
//         }

//         LOG.info("\n\n🚀 Event Triggered for Path: {} | Type: {}\n", path, event.getTopic());

//         try (ResourceResolver resolver = getServiceUserResolver()) {
//             if (resolver != null) {
//                 Resource resource = resolver.getResource(path);

//                 if (resource != null) {
//                     if (path.matches(AUTHOR_PATH + "/[^/]+$")) { // Page created
//                         LOG.info("\n📝 New Page Created: {}\n", path);
//                         fetchAllPages(resolver);
//                     } else if (path.contains("jcr:content")) { // Component added/modified
//                         LOG.info("\n🧩 Component Modified: {}\n", path);
//                         fetchAllComponents(resolver);
//                     }
//                 }
//             }
//         } catch (Exception e) {
//             LOG.error("❌ Error handling event", e);
//         }
//     }

//     private ResourceResolver getServiceUserResolver() {
//         Map<String, Object> params = new HashMap<>();
//         params.put(ResourceResolverFactory.SUBSERVICE, "eventServiceUser");
//         try {
//             return resourceResolverFactory.getServiceResourceResolver(params);
//         } catch (LoginException e) {
//             LOG.error("⚠️ Unable to get service user resolver", e);
//         }
//         return null;
//     }

//     private void fetchAllPages(ResourceResolver resolver) {
//         LOG.info("\n📂 Fetching All Pages Under: {}\n", AUTHOR_PATH);

//         Resource authorResource = resolver.getResource(AUTHOR_PATH);
//         if (authorResource == null) {
//             LOG.warn("❌ No pages found under {}", AUTHOR_PATH);
//             return;
//         }

//         for (Resource page : authorResource.getChildren()) {
//             LOG.info("\n📄 Page: {}\n----------------------", page.getPath());

//             for (Map.Entry<String, Object> entry : page.getValueMap().entrySet()) {
//                 LOG.info("{} = {}", entry.getKey(), entry.getValue());
//             }
//         }
//     }

//     private void fetchAllComponents(ResourceResolver resolver) {
//         LOG.info("\n🧩 Fetching All Components for Pages Under: {}\n", AUTHOR_PATH);

//         Map<String, String> queryMap = new HashMap<>();
//         queryMap.put("path", AUTHOR_PATH);
//         queryMap.put("type", "nt:unstructured");
//         queryMap.put("p.limit", "-1");

//         try {
//             Session session = resolver.adaptTo(Session.class);
//             Query query = queryBuilder.createQuery(PredicateGroup.create(queryMap), session);
//             SearchResult result = query.getResult();

//             Iterator<Resource> resources = result.getResources();
//             while (resources.hasNext()) {
//                 Resource component = resources.next();
//                 LOG.info("\n🧱 Component: {}\n----------------------", component.getPath());

//                 for (Map.Entry<String, Object> entry : component.getValueMap().entrySet()) {
//                     LOG.info("{} = {}", entry.getKey(), entry.getValue());
//                 }
//             }
//         } catch (Exception e) {
//             LOG.error("❌ Query execution failed", e);
//         }
//     }
// }

// package com.bhasaka.newsportal.core.listeners;

// import com.day.cq.search.Query;
// import com.day.cq.search.QueryBuilder;
// import com.day.cq.search.result.SearchResult;
// import com.day.cq.search.PredicateGroup;
// import com.day.cq.wcm.api.Page;
// import com.day.cq.wcm.api.PageManager;
// import org.apache.sling.api.SlingConstants;
// import org.apache.sling.api.resource.*;
// import org.osgi.service.component.annotations.Component;
// import org.osgi.service.component.annotations.Reference;
// import org.osgi.service.event.Event;
// import org.osgi.service.event.EventConstants;
// import org.osgi.service.event.EventHandler;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

// import javax.jcr.Session;
// import java.util.Iterator;
// import java.util.Map;
// import java.util.HashMap;

// @Component(service = EventHandler.class, 
//            immediate = true, 
//            property = {
//                EventConstants.EVENT_TOPIC + "=org/apache/sling/api/resource/Resource/ADDED",
//                EventConstants.EVENT_TOPIC + "=org/apache/sling/api/resource/Resource/CHANGED",
//                EventConstants.EVENT_TOPIC + "=org/apache/sling/api/resource/Resource/REMOVED"
//            })
// public class SimplePageEventHandler implements EventHandler {

//     private static final Logger LOG = LoggerFactory.getLogger(SimplePageEventHandler.class);
//     private static final String AUTHOR_PATH = "/content/newsportal/us/en/author";

//     @Reference
//     private ResourceResolverFactory resourceResolverFactory;

//     @Reference
//     private QueryBuilder queryBuilder;

//     @Override
//     public void handleEvent(Event event) {
//         String resourcePath = (String) event.getProperty(SlingConstants.PROPERTY_PATH);
//         String eventType = event.getTopic();

//         LOG.info("🔔 Event Triggered for Path: {} | Type: {}", resourcePath, eventType);

//         try (ResourceResolver resolver = getServiceUserResolver()) {
//             if (resolver != null) {
//                 Resource resource = resolver.getResource(resourcePath);
//                 if (resource != null) {
//                     PageManager pageManager = resolver.adaptTo(PageManager.class);
//                     Page page = pageManager.getContainingPage(resource);

//                     if (page != null) {
//                         LOG.info("\n📄 Processing Page: {}", page.getPath());
//                         logPageProperties(page);
//                         processChildComponents(page.getContentResource());

//                         // Fetch all pages under author
//                         fetchAllPages(resolver);

//                         // Fetch all components under author
//                         fetchAllComponents(resolver);
//                     }
//                 } else {
//                     LOG.warn("⚠️ Resource not found: {}", resourcePath);
//                 }
//             }
//         } catch (Exception e) {
//             LOG.error("❌ Error processing event", e);
//         }
//     }

//     private ResourceResolver getServiceUserResolver() {
//         Map<String, Object> params = new HashMap<>();
//         params.put(ResourceResolverFactory.SUBSERVICE, "eventServiceUser");
//         try {
//             return resourceResolverFactory.getServiceResourceResolver(params);
//         } catch (LoginException e) {
//             LOG.error("⚠️ Unable to get service user resolver", e);
//         }
//         return null;
//     }

//     private void logPageProperties(Page page) {
//         ValueMap properties = page.getProperties();
//         String title = properties.get("jcr:title", "N/A");

//         LOG.info("  - jcr:title: {}", title);
//         LOG.info("  - sling:resourceType: {}", properties.get("sling:resourceType", "N/A"));
//         LOG.info("  - cq:lastModifiedBy: {}", properties.get("cq:lastModifiedBy", "N/A"));

//         for (Map.Entry<String, Object> entry : properties.entrySet()) {
//             LOG.info("  - {}: {}", entry.getKey(), entry.getValue());
//         }
//     }

//     private void processChildComponents(Resource resource) {
//         if (resource == null) return;

//         for (Resource child : resource.getChildren()) {
//             ValueMap properties = child.getValueMap();
//             LOG.info("🧩 Component: {}", child.getPath());
//             LOG.info("  - sling:resourceType: {}", properties.get("sling:resourceType", "N/A"));
//             LOG.info("  - text: {}", properties.get("text", "N/A"));
//             LOG.info("  - lastModifiedBy: {}", properties.get("cq:lastModifiedBy", "N/A"));

//             processChildComponents(child); // Recursively process nested components
//         }
//     }

//     private void fetchAllPages(ResourceResolver resolver) {
//         LOG.info("\n📂 Fetching All Pages Under: {}\n", AUTHOR_PATH);

//         Map<String, String> queryMap = new HashMap<>();
//         queryMap.put("path", AUTHOR_PATH);
//         queryMap.put("type", "cq:Page");
//         queryMap.put("p.limit", "-1"); // No limit

//         try {
//             Session session = resolver.adaptTo(Session.class);
//             Query query = queryBuilder.createQuery(PredicateGroup.create(queryMap), session);
//             SearchResult result = query.getResult();

//             Iterator<Resource> resources = result.getResources();
//             while (resources.hasNext()) {
//                 Resource pageResource = resources.next();
//                 LOG.info("\n📄 Found Page: {}\n----------------------", pageResource.getPath());

//                 Page page = pageResource.adaptTo(Page.class);
//                 if (page != null) {
//                     logPageProperties(page);
//                 }
//             }
//         } catch (Exception e) {
//             LOG.error("❌ Query execution failed", e);
//         }
//     }

//     private void fetchAllComponents(ResourceResolver resolver) {
//         LOG.info("\n🧩 Fetching All Components for Pages Under: {}\n", AUTHOR_PATH);

//         Map<String, String> queryMap = new HashMap<>();
//         queryMap.put("path", AUTHOR_PATH);
//         queryMap.put("type", "nt:unstructured"); // Components are nt:unstructured
//         queryMap.put("p.limit", "-1"); // No limit

//         try {
//             Session session = resolver.adaptTo(Session.class);
//             Query query = queryBuilder.createQuery(PredicateGroup.create(queryMap), session);
//             SearchResult result = query.getResult();

//             Iterator<Resource> resources = result.getResources();
//             while (resources.hasNext()) {
//                 Resource component = resources.next();
//                 LOG.info("\n🧱 Found Component: {}\n----------------------", component.getPath());

//                 ValueMap properties = component.getValueMap();
//                 LOG.info("  - sling:resourceType: {}", properties.get("sling:resourceType", "N/A"));
//                 LOG.info("  - text: {}", properties.get("text", "N/A"));
//                 LOG.info("  - lastModifiedBy: {}", properties.get("cq:lastModifiedBy", "N/A"));
//             }
//         } catch (Exception e) {
//             LOG.error("❌ Query execution failed", e);
//         }
//     }
// }

package com.bhasaka.newsportal.core.listeners;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingConstants;
import org.apache.sling.api.resource.*;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Component(service = EventHandler.class, 
           immediate = true, 
           property = {
               EventConstants.EVENT_TOPIC + "=org/apache/sling/api/resource/Resource/ADDED",
               EventConstants.EVENT_TOPIC + "=org/apache/sling/api/resource/Resource/CHANGED",
               EventConstants.EVENT_TOPIC + "=org/apache/sling/api/resource/Resource/REMOVED"
           })
public class SimplePageEventHandler implements EventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(SimplePageEventHandler.class);

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Override
    public void handleEvent(Event event) {
        String resourcePath = (String) event.getProperty(SlingConstants.PROPERTY_PATH);
        String eventType = event.getTopic();

        LOG.info("🔔 Event Triggered: {} | Type: {}", resourcePath, eventType);

        try (ResourceResolver resolver = getServiceUserResolver()) {
            if (resolver != null) {
                Resource resource = resolver.getResource(resourcePath);
                if (resource != null) {
                    PageManager pageManager = resolver.adaptTo(PageManager.class);
                    Page page = pageManager.getContainingPage(resource);

                    if (page != null) {
                        if (eventType.contains("ADDED")) {
                            LOG.info("📄 New Page Created: {}", page.getPath());
                        } else if (eventType.contains("CHANGED")) {
                            LOG.info("✏️ Page Modified: {}", page.getPath());
                        } else if (eventType.contains("REMOVED")) {
                            LOG.info("🗑️ Page Deleted: {}", page.getPath());
                        }

                        // Log only the newly added/modified component
                        logResourceProperties(resource);
                    }
                } else {
                    LOG.warn("⚠️ Resource not found: {}", resourcePath);
                }
            }
        } catch (Exception e) {
            LOG.error("❌ Error processing event", e);
        }
    }

    private ResourceResolver getServiceUserResolver() {
        Map<String, Object> params = Map.of(ResourceResolverFactory.SUBSERVICE, "eventServiceUser");
        try {
            return resourceResolverFactory.getServiceResourceResolver(params);
        } catch (LoginException e) {
            LOG.error("⚠️ Unable to get service user resolver", e);
        }
        return null;
    }

    private void logResourceProperties(Resource resource) {
        if (resource == null) return;

        ValueMap properties = resource.getValueMap();
        LOG.info("📝 Component Updated: {}", resource.getPath());
        LOG.info("  - sling:resourceType: {}", properties.get("sling:resourceType", "N/A"));
        LOG.info("  - title: {}", properties.get("jcr:title", "N/A"));
        LOG.info("  - lastModifiedBy: {}", properties.get("cq:lastModifiedBy", "N/A"));
    }
}

