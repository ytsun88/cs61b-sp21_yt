package gitlet;

// TODO: any imports you need here

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Utils.sha1;

/**
 * Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 * @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /**
     * The message of this Commit.
     */
    private String message;

    /**
     * SHA1 ID of the parent Commit.
     */
    private List<String> parents;

    /**
     * The date of this Commit.
     */
    private Date date;

    /**
     * A map that tracks all the blob files of this Commit.
     * Key is file name and value is SHA1 ID.
     */
    private Map<String, String> blobs;

    /**
     * The SHA1 ID of this Commit.
     */
    private String id;

    /* TODO: fill in the rest of this class. */
    public Commit() {
        this.date = new Date(0);
        this.message = "initial commit";
        this.parents = new LinkedList<>();
        this.blobs = new HashMap<>();
        this.id = generateID();
    }

    public Commit(String message, List<String> parents, Map<String, String> blobs) {
        this.date = new Date();
        this.message = message;
        this.parents = parents;
        this.blobs = blobs;
        this.id = generateID();
    }

    public String getID() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getParents() {
        return parents;
    }

    public Date getDate() {
        return date;
    }

    public Map<String, String> getBlobs() {
        return blobs;
    }

    public String getTimestamp() {
        // Thu Jan 1 00:00:00 1970 +0000
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.ENGLISH);
        return dateFormat.format(date);
    }

    public String generateID() {
        return sha1(message, parents.toString(), getTimestamp(), blobs.toString());
    }

    public String getFirstParentID() {
        if (parents == null) {
            return null;
        }
        return parents.get(0);
    }
}
