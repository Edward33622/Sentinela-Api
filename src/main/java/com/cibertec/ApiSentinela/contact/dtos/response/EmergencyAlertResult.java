package com.cibertec.ApiSentinela.contact.dtos.response;

import java.util.ArrayList;
import java.util.List;

public class EmergencyAlertResult {
    private int totalRecipients;
    private int attempted;
    private int sent;
    private int failed;
    private List<RecipientResult> recipients = new ArrayList<>();

    public int getTotalRecipients() { return totalRecipients; }
    public void setTotalRecipients(int totalRecipients) { this.totalRecipients = totalRecipients; }

    public int getAttempted() { return attempted; }
    public void setAttempted(int attempted) { this.attempted = attempted; }

    public int getSent() { return sent; }
    public void setSent(int sent) { this.sent = sent; }

    public int getFailed() { return failed; }
    public void setFailed(int failed) { this.failed = failed; }

    public List<RecipientResult> getRecipients() { return recipients; }
    public void setRecipients(List<RecipientResult> recipients) { this.recipients = recipients; }

    public void addRecipient(RecipientResult r) { this.recipients.add(r); }
}
