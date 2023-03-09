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
    HashMap<String, IndexNode> stagingArea;

    /** Constructor of Index */
    public Index() {
        stagingArea = new HashMap<>();
        this.writeIndex();
    }

    /**
     * Private helper class.
     * This class represents Node in the index tree.
     * */
    private class IndexNode implements Serializable{
        String blobPath;
        String folderNameInObject;
        String hashOfFile;

        public IndexNode(String blobPath, String folderNameInObject) {
            this.blobPath = blobPath;
            this.folderNameInObject = folderNameInObject;
            this.hashOfFile = blobPath + folderNameInObject;
        }

        @Override
        public String toString() {
           return "Hash of the file: " + blobPath + "/" + folderNameInObject;
        }
    }

    /**
     * Adds file to staging area.
     * @param fileName
     */
    public void addFile(String fileName) {
        File currentFile = join(Repository.CWD, fileName);

        // get the hash value of the file.
        // the blob path is made up of the hash value of blob, and it is in the "object" directory.
        // the first two is directory name, the rest is file name.
        String[] blobPath = Repository.setBlob(currentFile.getName());

        // use node to record which files have been added
        IndexNode node = new IndexNode(blobPath[0], blobPath[1]);

        // the index has been created in gitlet initial,
        // so here every time we read from file and put new file into map.
        this.stagingArea.put(currentFile.getPath(), node);

        // serialize the index to store information
        writeIndex();
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
     * Overrides the .equals() method.
     * This method compares the staging area (hashmap) of two index,
     * If both of them have the same contents, then it outputs true.
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
       if (obj == null || !(obj instanceof Index)) {
           return false;
       }

       Index other = (Index) obj;
       if (this.stagingArea.size() != other.stagingArea.size()) {
           return false;
       }

       for (String key : this.stagingArea.keySet()) {
           IndexNode thisNode = this.stagingArea.get(key);
           IndexNode otherNode = other.stagingArea.get(key);

           if (otherNode == null || !thisNode.hashOfFile.equals(otherNode.hashOfFile)) {
               return false;
           }
       }

       return true;
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
        System.out.println(this.stagingArea);
        System.out.println("Following is the content of staging area: ");
        System.out.println(Arrays.asList(this.stagingArea));
    }
}
