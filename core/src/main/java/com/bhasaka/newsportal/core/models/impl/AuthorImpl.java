package com.bhasaka.newsportal.core.models.impl;


import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;

import com.bhasaka.newsportal.core.models.Author;

@Model(adaptables = SlingHttpServletRequest.class, adapters = Author.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AuthorImpl implements Author{
    
    @SlingObject // Injects the current SlingHttpServletRequest
    private SlingHttpServletRequest request;

    @Inject
    String fname;
    

    @Override
    public String getFirstName(){
        if (fname == null && request != null) {
            fname = request.getParameter("fname"); // Extracts the "fname" parameter from the request
        }
        return fname;
    }
}
