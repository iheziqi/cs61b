package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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

    /** The data structure represents removal area. */
    List<String> removalArea;

    /** Constructor of Index */
    public Index() {
        stagingArea = new HashMap<>();
        removalArea = new ArrayList<>();
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

        String hashOfFile = Repository.getHashOfFileContent(currentFile);
        String currentFilePath = currentFile.getPath();

        // If the file is identical to already-staged file, return.
        if (hashOfFile.equals(this.stagingArea.get(currentFilePath))) {
            System.out.println("You are adding the identical file to the staging area.");
            return;
        }

        // If the current working version of the file is identical to
        // the version in the current commit, will not stage it to be added,
        // and remove it from the staging area if it is already there.
        // (as can happen when a file is changed, added, and the changed back to it's original)
        String hashOfLastCommit = Branch.getLastCommit(Branch.getCurrentBranch());
        Commit lastCommit = Commit.readCommit(hashOfLastCommit);

        String hashOfFileInLastCommit = lastCommit.getIndex().stagingArea.get(currentFilePath);
        if (hashOfFileInLastCommit != null && hashOfFileInLastCommit.equals(hashOfFile)) {
            System.out.println("You are adding the identical file to the version in the current commit.");
            // remove the identical blob in staging area.
            this.stagingArea.remove(currentFilePath);
            this.writeIndex();
            return;
        };

        // If the file already-staged, and the contents are different,
        // overwrites the previous entry in staging area,
        // deletes the old blob in objects folder.
        if (this.stagingArea.containsKey(currentFilePath)) {
            String oldHash = this.stagingArea.get(currentFilePath);
            Repository.deleteBlob(oldHash);
        }

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
        File currentFile = join(Repository.CWD, fileName);
        // If the file doesn't exist.
        if (currentFile == null) {
            message("No reason to remove the file.");
            return;
        }

        String currentFilePath = currentFile.getPath();
        if (!this.stagingArea.containsKey(currentFilePath)) {
            message("No reason to remove the file.");
            return;
        }

        this.removalArea.add(currentFilePath);

        // Un-stage the file if it is currently staged for addition.
        Repository.deleteBlob(this.stagingArea.get(currentFilePath));
        this.stagingArea.remove(currentFilePath);
        this.writeIndex();

        // If the file is tracked in the current commit,
        // remove the file if the user has not already done so.
        Commit lastCommit = Commit.readCommit(Branch.getLastCommit(Branch.getCurrentBranch()));
        if (lastCommit != null && lastCommit.getIndex().stagingArea.get(currentFilePath) != null) {
            restrictedDelete(currentFile);
        }

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
        this.removalArea.clear();
        writeIndex();
    }

    /**
     * Prints the staging area.
     */
    public static void printIndex(){
        Index index = Index.fromFile();
        System.out.println("Following is the content of staging area: ");
        System.out.println(Arrays.asList(index.stagingArea));
        System.out.println("Following is the content of removal area: ");
        for (String element : index.removalArea) {
            System.out.print(element + " ");
        }
        System.out.println();
    }

    public void print() {
        System.out.println("Following is the content of staging area: ");
        System.out.println(Arrays.asList(this.stagingArea));
        System.out.println("Following is the content of removal area: ");
        for (String element : this.removalArea) {
            System.out.print(element + " ");
        }
        System.out.println();
    }
}
