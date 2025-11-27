package models;

import java.util.Date;

public class Payment {

    private int id;
    private Date date;
    private int method;
    private String transactionStatus;
    private String refundStatus;

    public int getId(){ return id; }
    public void setId(int id){ this.id = id; }

    public Date getDate(){ return date; }
    public void setDate(Date date){ this.date = date; }

    public int getMethod(){ return method; }
    public void setMethod(int method){ this.method = method; }

    public String getTransactionStatus(){ return transactionStatus; }
    public void setTransactionStatus(String transactionStatus){ this.transactionStatus = transactionStatus; }

    public String getRefundStatus(){ return refundStatus; }
    public void setRefundStatus(String refundStatus){ this.refundStatus = refundStatus; }
}
