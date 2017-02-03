package de.htwsaar.remote.remoteModel.impls;

import java.io.Serializable;
import java.rmi.server.UID;
import java.time.LocalDateTime;

public class MessageImpl implements Serializable {

    private static final long serialVersionUID = -5415774293797687291L;

    private UID id;
    private String msg;
    private LocalDateTime time;
    private String author;
    private String section;

    public MessageImpl (String msg, LocalDateTime time,String author,String section){
        this.id = new UID();
        this.msg = msg;
        this.author = author;
        this.section = section;
        this.time = time;

    }

    public String getMessage(){
        return msg;
    }

    public void changeMessage(String msg){
        this.msg = msg;
        time = LocalDateTime.now();
    }

    public LocalDateTime getTime()
    {
        return time;
    }

    public UID getID()
    {
        return id;
    }

    public String getAuthor(){
        return author;
    }
    public String getSection(){
        return author;
    }
    public void setSection(String section){
        this.section = section;
    }

    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer();

        sb.append("ID : ").append(author).append("\n");
        sb.append("Abteilung :").append(section).append("\n");
        sb.append("Time: ").append(time).append("\n");
        sb.append(msg);
        return sb.toString();
    }
}