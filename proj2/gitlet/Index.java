package gitlet;

import java.io.File;
import java.io.Serializable;
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
    private class IndexNode {
        String blobPath;
        String folderNameInObject;
        String hashOfFile;

        public IndexNode(String blobPath, String folderNameInObject) {
            this.blobPath = blobPath;
            this.folderNameInObject = folderNameInObject;
            this.hashOfFile = blobPath + folderNameInObject;
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
        IndexNode node = new IndexNode(blobPath[0], blobPath[1]);
        stagingArea.put(currentFile.getPath(), node);

        // serialize the index to store information
        writeIndex();
    }

    /** Creates a new index (staging area). */
    public static Index createIndex() {
        Index index = new Index();
        index.writeIndex();
        return index;
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
    }
}
