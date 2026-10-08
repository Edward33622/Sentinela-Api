package com.cibertec.ApiSentinela.contact.dtos.response;

public class RecipientResult {
    private String fullName;
    private String originalPhone;
    private String normalizedPhone;
    private boolean sent;
    private String sid;
    private String error;

    public RecipientResult() {}

    public RecipientResult(String fullName, String originalPhone, String normalizedPhone) {
        this.fullName = fullName;
        this.originalPhone = originalPhone;
        this.normalizedPhone = normalizedPhone;
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getOriginalPhone() { return originalPhone; }
    public void setOriginalPhone(String originalPhone) { this.originalPhone = originalPhone; }

    public String getNormalizedPhone() { return normalizedPhone; }
    public void setNormalizedPhone(String normalizedPhone) { this.normalizedPhone = normalizedPhone; }

    public boolean isSent() { return sent; }
    public void setSent(boolean sent) { this.sent = sent; }

    public String getSid() { return sid; }
    public void setSid(String sid) { this.sid = sid; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}
