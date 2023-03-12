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
        // By default, a commit hash the same file contents as its parent.
        index.stagingArea.putAll(Commit.readCommit(hashOfParent).getIndex().stagingArea);
        index.removalArea.addAll(Commit.readCommit(hashOfParent).getIndex().removalArea);
    }

    /** Constructor of initial commit. */
    public Commit(String hashOfParent, String timestamp, String message, Boolean isInitialCommit) {
        this.hashOfParent = hashOfParent;
        this.timestamp = timestamp;
        this.message = message;
        this.index = Index.fromFile();
    }

    /**
     * Getter of index.
     * @return
     */
    public Index getIndex() {
        return this.index;
    }

    /**
     * Calculates the sha1 value of this commit as identifier
     * @return hash value of this commit
     */
    public String getHash() {
        String hash = sha1(message, author, timestamp, hashOfParent, String.valueOf(index.hashCode()));
        return hash;
    }

    /**
     * Reads the commit from object folder.
     * @param hashOfCommit
     * @return Commit object
     */
    public static Commit readCommit(String hashOfCommit) {
        String[] blobPath = Repository.getBlobPath(hashOfCommit);
        String firstTwoHash = blobPath[0];
        String restOfHash = blobPath[1];
        return readObject(join(Repository.OBJECTS, firstTwoHash, restOfHash), Commit.class);
    }

    /**
     * Serializes this commit.
     * Store this commit into directory "Objects".
     * It has to follow the naming rule in this folder.
     * Use first two hash as folder name, and the rest of hash as file name.
     * */
    public void writeCommit() {
       String hashOfCommit = getHash();
       String[] blobPath = Repository.getBlobPath(hashOfCommit);
       String firstTwoHash = blobPath[0];
       String restHash = blobPath[1];

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

        // Update branch pointer pointing to this commit
        Branch.updateBranchPointer(Branch.getCurrentBranch(), hashOfCommit);

        // The staging area is cleared after a commit.
        Index.fromFile().clearIndex();
    }

    /**
     * Takes the version of the file as it exists in the head commit
     * and puts it in the working directory, overwriting the version of the file
     * that's already there if there is one.
     * The new version of the file is not staged.
     * @param fileName
     */
    public static void checkoutFile(String fileName) {
        Commit currentCommit = Commit.readCommit(Branch.getLastCommit(Branch.getCurrentBranch()));
        if (!currentCommit.getIndex().stagingArea.containsKey(fileName)) {
            message("File does not exist in that commit.");
            return;
        }
        String hashOfFileInCurrentCommit = currentCommit.getIndex().stagingArea.get(fileName);
        String[] blobPath = Repository.getBlobPath(hashOfFileInCurrentCommit);
        File blobOfFileInCurrentCommit = join(Repository.OBJECTS, blobPath[0], blobPath[1]);
        writeContents(join(Repository.CWD, fileName), readContents(blobOfFileInCurrentCommit));
    }

    /**
     * Takes the version of the file as it exists in teh commit with the given id,
     * and puts it in the working directory, overwriting the version of the file that's
     * already there if there is one.
     * The new version of the file is not staged.
     * @param commitID
     * @param fileName
     */
    public static void checkoutFile(String commitID, String fileName) {
        Commit targeCommit = Commit.readCommit(commitID);
        if (targeCommit == null) {
            message("No commit with that id exists.");
            return;
        }
        String hashOfFileInTargetCommit = targeCommit.getIndex().stagingArea.get(fileName);
        String[] blobPath = Repository.getBlobPath(hashOfFileInTargetCommit);
        File blobOfFileInTargetCommit = join(Repository.OBJECTS, blobPath[0], blobPath[1]);
        writeContents(join(Repository.CWD, fileName), readContents(blobOfFileInTargetCommit));
    }

    /**
     * Equal method of commit object.
     * @param commit
     * @return
     */
    public boolean equals(Commit commit){
        return this.index.equals(commit.index);
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
     * @param time if you want to output unix epoch time, use 0
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

    public void print() {
        System.out.println("Parent commit: " + this.hashOfParent);
        System.out.println("Commit message: " + this.message);
        System.out.println("Commit timestamp: " + this.timestamp);
        System.out.print("Index: ");
        this.index.print();
    }
}