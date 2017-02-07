package eu.clarussecure.dataviewer.model;

import java.util.List;

public class SecurityPolicy {

    private long policyId;

    private String policyName;
    private String dataUsage;

    private SecurityPolicyEndpoint endpoint;

    private List<SecurityPolicyAttribute> attributes;

    private SecurityPolicyProtection protection;

    public long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(long policyId) {
        this.policyId = policyId;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public String getDataUsage() {
        return dataUsage;
    }

    public void setDataUsage(String dataUsage) {
        this.dataUsage = dataUsage;
    }

    public SecurityPolicyEndpoint getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(SecurityPolicyEndpoint endpoint) {
        this.endpoint = endpoint;
    }

    public List<SecurityPolicyAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<SecurityPolicyAttribute> attributes) {
        this.attributes = attributes;
    }

    public SecurityPolicyProtection getProtection() {
        return protection;
    }

    public void setProtection(SecurityPolicyProtection protection) {
        this.protection = protection;
    }

}
