package models;

import java.util.Date;

public class Notification {

    private int id;
    private String message;
    private Date timestamp;
    private String status;

    public int getId(){ return id; }
    public void setId(int id){ this.id = id; }

    public String getMessage(){ return message; }
    public void setMessage(String message){ this.message = message; }

    public Date getTimestamp(){ return timestamp; }
    public void setTimestamp(Date timestamp){ this.timestamp = timestamp; }

    public String getStatus(){ return status; }
    public void setStatus(String status){ this.status = status; }
}
