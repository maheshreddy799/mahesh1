package com.bhasaka.newsportal.core.models;

import com.adobe.cq.wcm.core.components.models.Text;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
    adaptables = Resource.class,
    adapters = { CustomTextModel.class, Text.class }, // Extending Core Model
    resourceType = "newsportal/components/customtext",
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class CustomTextModel implements Text {

    @Self
    private Resource resource;

    @ValueMapValue
    private String customProperty;

    @ValueMapValue
    private String id;  // <-- Add this field

    public String getCustomProperty() {
        return customProperty;
    }

    public String getId() {  // <-- Add this getter
        return id;
    }

    @Override
    public String getText() {
        // Adapting the resource to Core Text Component
        Text coreText = resource.adaptTo(Text.class);
        return coreText != null ? coreText.getText() : "No Core Text Available";
    }
}
