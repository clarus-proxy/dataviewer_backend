package eu.clarussecure.dataviewer.model;

import java.util.List;

public class SecurityPolicyProtection {
    
    private String module;
    
    private List<SecurityPolicyProtectionAttributeType> attributeTypes;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public List<SecurityPolicyProtectionAttributeType> getAttributeTypes() {
        return attributeTypes;
    }

    public void setAttributeTypes(List<SecurityPolicyProtectionAttributeType> attributeTypes) {
        this.attributeTypes = attributeTypes;
    }
    
    
}
