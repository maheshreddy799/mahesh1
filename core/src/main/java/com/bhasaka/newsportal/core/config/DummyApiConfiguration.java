package com.bhasaka.newsportal.core.config;


import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;


@Designate(ocd = DummyApiConfiguration.Config.class)
@Component(service = DummyApiConfiguration.class, immediate = true)
public class DummyApiConfiguration {

    @ObjectClassDefinition(name = "Dummy API Configuration")
    public @interface Config {
        @AttributeDefinition(name = "Dummy API URL", description = "Enter the API endpoint URL")
        String apiUrl() default "https://jsonexamples.com/products";
    }

    private String apiUrl;

    // @Reference
    // private ResourceResolverFactory resolverFactory;

    @Activate
    @Modified
    protected void activate(Config config) {
        this.apiUrl = config.apiUrl();
    }

    public String getApiUrl() {
        return apiUrl;
    }

    // // Method to retrieve Service Resource Resolver
    // private ResourceResolver getServiceResourceResolver() throws LoginException {
    //     Map<String, Object> authInfo = Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, "newsportalService");
    //     return resolverFactory.getServiceResourceResolver(authInfo);
    // }
}