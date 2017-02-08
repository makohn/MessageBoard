package de.htwsaar.wirth.remote.model;

import de.htwsaar.wirth.remote.model.interfaces.Message;

import javax.persistence.*;
import java.io.Serializable;
import java.rmi.server.UID;
import java.util.Date;

/**
 * Implements Message-Interface
 * Created by stefanschloesser1 on 03.02.17.
 * Edited by oliverseibert on 08.02.17
 */
@Entity
@Table(name="messages")
public class MessageImpl implements Serializable, Message {

    private static final long serialVersionUID = -5415774293797687291L;
    @Column
    @Id
    private UID id;
    @Column
    private String msg;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;
    @Column
    private String author;
    @Column
    private String section;
    @Column
    private String group;
    @Column
    private boolean isPublic;

    @Column
    private boolean isPublished;

    /**
     * Constructor
     * @param msg
     * @param author
     * @param section
     * @param group
     * @param isPublic
     */
    public MessageImpl(String msg, String author, String section, String group, boolean isPublic) {
        this.id = new UID();
        this.msg = msg;
        this.createdAt = new Date();
        this.author = author;
        this.section = section;
        this.group = group;
        this.isPublic = isPublic;
    }

    public UID getID() {
        return id;
    }

    public String getGroup() { return group; }

    public boolean isPublic() { return isPublic; }

    public boolean isPublished() { return isPublished; }

    public String getMessage(){ return msg; }

    public String getAuthor() {
        return author;
    }

    public String getSection() {
        return author;
    }

    public Date getCreatedAt() { return createdAt; }

    public Date getModifiedAt() { return modifiedAt; }
    
    public void setSection(String section) {
        this.section = section;
    }

    public void setPublished(boolean published) { isPublished = published; }

    public void changeMessage(String msg){
        this.msg = msg;
        modifiedAt = new Date();
    }

    @Override
    public String toString() {
        return "MessageImpl{" +
                "id=" + id +
                ", msg='" + msg + '\'' +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                ", author='" + author + '\'' +
                ", section='" + section + '\'' +
                ", group='" + group + '\'' +
                ", isPublic=" + isPublic +
                ", isPublished=" + isPublished +
                '}';
    }
}