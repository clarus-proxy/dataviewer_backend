package eu.clarussecure.dataviewer.model;

public class DatasetDescription {

    private long policyId;
    private String policyName;
    private String protocol;
    private String technique;

    private String urlProtectedData;
    private String urlUnprotectedData;
    private String urlAttributes;
    private String urlPolicy;

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String name) {
        this.policyName = name;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getTechnique() {
        return technique;
    }

    public void setTechnique(String technique) {
        this.technique = technique;
    }

    public String getUrlProtectedData() {
        return urlProtectedData;
    }

    public void setUrlProtectedData(String urlProtectedData) {
        this.urlProtectedData = urlProtectedData;
    }

    public String getUrlUnprotectedData() {
        return urlUnprotectedData;
    }

    public void setUrlUnprotectedData(String urlUnprotectedData) {
        this.urlUnprotectedData = urlUnprotectedData;
    }

    public String getUrlAttributes() {
        return urlAttributes;
    }

    public void setUrlAttributes(String urlAttributes) {
        this.urlAttributes = urlAttributes;
    }

    public String getUrlPolicy() {
        return urlPolicy;
    }

    public void setUrlPolicy(String urlPolicy) {
        this.urlPolicy = urlPolicy;
    }

    public long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(long policyId) {
        this.policyId = policyId;
    }

}
