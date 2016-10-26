package eu.clarussecure.dataviewer.model;

public class SecurityPolicyProtectionAttributeType {
    
    private String type;
    private String protection;
    
    private SecurityPolicyProtectionParameter parameter;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProtection() {
        return protection;
    }

    public void setProtection(String protection) {
        this.protection = protection;
    }

    public SecurityPolicyProtectionParameter getParameter() {
        return parameter;
    }

    public void setParameter(SecurityPolicyProtectionParameter parameter) {
        this.parameter = parameter;
    }
            
}
