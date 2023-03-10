package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;

import static gitlet.Utils.*;

/**
 * Handles command gitlet add [file name]
 * Index represents the staging area in Gitlet.
 * When the user use command gitlet add [file name], the file path and the blob path of file
 * are stored in a HashMap i.e. staging area.
 * Once the user commits, clean the staging area to empty.
 * @author Ziqi He
 */
public class Index implements Serializable {
    /** The data structure represents staging area. */
    HashMap<String, String> stagingArea;

    /** Constructor of Index */
    public Index() {
        stagingArea = new HashMap<>();
        this.writeIndex();
    }

    /**
     * Adds file to staging area.
     * @param fileName
     */
    public void addFile(String fileName) {
        File currentFile = join(Repository.CWD, fileName);
        // If the file doesn't exist.
        if (!currentFile.exists()) {
            message("File does not exist.");
            return;
        }

        // If the file already-staged, overwrites the previous entry in staging area.
        String hashOfFile = Repository.getHashOfFileContent(currentFile);
        String currentFilePath = currentFile.getPath();
        Index index = Index.fromFile();

        if (hashOfFile.equals(index.stagingArea.get(currentFilePath))) {
            System.out.println("You are adding the identical file to the staging area.");
            return;
        }

        // If the current working version of the file is identical to
        // the version in the current commit, will not stage it to be added,
        // and remove it from the staging area if it is already there.
        // (as can happen when a file is changed, added, and the changed back to it's original)
        // TODO: Here I hardcoded the branch name, change it in the future to dynamic.
        String hashOfLastCommit = Branch.getLastCommit("master");
        Commit lastCommit = Commit.readCommit(hashOfLastCommit);

        String hashOfFileInLastCommit = lastCommit.getIndex().stagingArea.get(currentFilePath);
        if (hashOfFileInLastCommit != null && hashOfFileInLastCommit.equals(hashOfFile)) {
            System.out.println("You are adding the identical file to the version in the current commit.");
            // remove the identical blob in staging area.
            index.stagingArea.remove(currentFilePath);
            index.writeIndex();
            return;
        };

        // Get the hash value of the file.
        // The blob path is made up of the hash value of blob, and it is in the "object" directory.
        // The first two is directory name, the rest is file name.
        Repository.setBlob(currentFile.getName());

        // The index has been created in gitlet initial,
        // so here every time we read from file and put new file into map.
        this.stagingArea.put(currentFile.getPath(), hashOfFile);

        // Serialize the index to store information.
        writeIndex();
    }


    /**
     * Unstage the file if it is currently staged for addition.
     * If the file is tracked in the current commit, stage it for removal,
     * and remove the file from the working directory if the user has not already done so.
     * Will not remove it unless it is tracked in the current commit.
     * @param fileName
     */
    public void removeFile(String fileName) {

    }

    /**
     * Reads in and deserializes the index from file.
     * @return Index object
     */
    public static Index fromFile() {
        return readObject(Repository.INDEX, Index.class);
    }

    /**
     * Saves the index to file.
     */
    public void writeIndex() {
        writeObject(Repository.INDEX, this);
    }

    /**
     * Clears the index, i.e. the staging area.
     */
    public void clearIndex() {
        this.stagingArea.clear();
        writeIndex();
    }

    /**
     * Prints the staging area.
     */
    public static void printIndex(){
        System.out.println(Index.fromFile());
        System.out.println("Following is the content of staging area: ");
        System.out.println(Arrays.asList(Index.fromFile().stagingArea));
    }

    public void print() {
        System.out.println("Following is the content of staging area: ");
        System.out.println(Arrays.asList(this.stagingArea));
    }
}
