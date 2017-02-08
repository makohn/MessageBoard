package de.htwsaar.wirth.remote.model;

import de.htwsaar.wirth.remote.model.interfaces.Message;

import java.io.Serializable;
import java.util.UUID;
import java.time.LocalDateTime;

public class MessageImpl implements Serializable,Message {

    private static final long serialVersionUID = -5415774293797687291L;

    private UUID id;
    private String msg;
    private LocalDateTime time;
    private String author;
    private String section;

    public MessageImpl (String msg,String author,String section) {
        this.id = UUID.randomUUID();
        this.msg = msg;
        this.author = author;
        this.section = section;
        this.time = LocalDateTime.now();

    }

    public String getMessage(){
        return msg;
    }

    public void changeMessage(String msg){
        this.msg = msg;
        time = LocalDateTime.now();
    }

    public LocalDateTime getTime() {
        return time;
    }

    public UUID getID() {
        return id;
    }

    public String getAuthor() {
        return author;
    }
    
    public String getSection() {
        return author;
    }
    
    public void setSection(String section) {
        this.section = section;
    }


    public boolean equals(Object o){
        if(o instanceof Message){
            Message m = (Message) o;
            return this.getID().equals(m.getID());
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("ID : ").append(author).append("\n");
        sb.append("Abteilung :").append(section).append("\n");
        sb.append("Time: ").append(time).append("\n");
        sb.append(msg);
        return sb.toString();
    }
}