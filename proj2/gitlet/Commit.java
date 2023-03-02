package gitlet;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import java.util.TimeZone;
import static gitlet.Utils.*;


/**
 * Represents a gitlet commit object.
 * Saves a snapshot of tracked files in the current commit and staging area,
 * so they can be restored at a later time, creating a new commit.
 * The commit is said to be tracking the saved files.
 * @author Ziqi He
 */
public class Commit implements Serializable {
    /**
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;

    /** The author of this Commit. */
    private String author = "Ziqi He";

    /** The timestamp of this Commit. */
    private String timestamp;

    /** The parent commit of this Commit. */
    private String hashOfParent;

    /** The snapshot of staging area. */
    private Index index;

    /** Constructor */
    public Commit(String hashOfParent, String timestamp, String message) {
        this.hashOfParent = hashOfParent;
        this.timestamp = timestamp;
        this.message = message;
        this.index = Index.fromFile();
    }

    /**
     * Serializes this commit.
     * Store this commit into directory "Objects".
     * It has to follow the naming rule in this folder.
     * Use first two hash as folder name, and the rest of hash as file name.
     * */
    public void writeCommit() {
       String hashOfCommit = sha1(this);
       String firstTwoHash = hashOfCommit.substring(0, 2);
       String restHash = hashOfCommit.substring(2, UID_LENGTH);

        // Using the first two hash of blob to create a directory
        // if it doesn't exist
        File objectDirectory = join(Repository.OBJECTS, firstTwoHash);
        if (!objectDirectory.exists()) {
            objectDirectory.mkdir();
        }

        // Using the rest of hash as file name
        File commitContent = join(objectDirectory, restHash);
        // Write the content into blob
        writeObject(commitContent, this);
    }

    /**
     * Gets formatted date.
     * @return String of formatted date
     */
    public static String getFormattedDate() {
        // Create a Date object representing the date and time you want to format
        Date date = new Date();

        // Create a SimpleDateFormat object with the desired format
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z");

        // Set the time zone to -0800
        formatter.setTimeZone(TimeZone.getTimeZone("GMT-8"));

        // Format the date to a string
        String formattedDate = formatter.format(date);

        return formattedDate;
    }

    /**
     * Gets formatted date.
     * @return String of formatted date
     */
    public static String getFormattedDate(int time) {
        // Create a Date object representing the date and time you want to format
        Date date = new Date(time);

        // Create a SimpleDateFormat object with the desired format
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z");

        // Set the time zone to -0800
        formatter.setTimeZone(TimeZone.getTimeZone("GMT-8"));

        // Format the date to a string
        String formattedDate = formatter.format(date);

        return formattedDate;
    }
}
