package gitlet;

import java.io.File;
import java.io.IOException;

import static gitlet.Utils.*;


/** Represents a gitlet repository.
 * This class does the relevant IO operation to .gitlet directory.
 *  @author Ziqi He
 */
public class Repository {

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));

    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    /** The head pointer pointing to current working branch. */
    public static final File HEAD = join(GITLET_DIR, "HEAD");

    /** References directory. */
    public static final File REFS = join(GITLET_DIR, "refs");

    /** The directory to store branches. */
    public static final File BRANCHES = join(REFS, "branches");

    /** The Objects directory to store copies of file contents in working directory. */
    public static final File OBJECTS = join(GITLET_DIR, "objects");

    /** The file representing staging area of gitlet. */
    public static final File INDEX = join(GITLET_DIR, "index");

    /**
     * checks whether .gitlet directory exists
     * @return a boolean value of .gitlet directory existence
     */
    public static boolean checkGitletExists() {
        return GITLET_DIR.exists();
    }

    /**
     * Creates required directory for gitlet if it not exists.
     */
    public static void createGitletDir() {
        if (GITLET_DIR.exists()) {
            System.out.println(
                    "A Gitlet version-control system already exists in the current directory."
            );
            return;
        }
        GITLET_DIR.mkdir();
        REFS.mkdir();
        OBJECTS.mkdir();
    }

    /**
     * Names it in correct way and puts the blob to the correct directory.
     * Inside the .gitlet/objects directory, the directory names are made by first two characters
     * of hash value of file contents. The rest of the hash is used as the name of the blob file.
     * @param fileName
     */
    public static void setBlob(String fileName) {
        File f = join(CWD, fileName);
        if (!f.exists()) {
            message("File does not exist.");
            System.exit(0);
        }

        byte[] blobContent = readContents(f);

        String hash = sha1(blobContent);
        // first two characters as directory name
        String firstTwoHash = hash.substring(0, 2);
        // rest of hash as file name
        String restOfHash = hash.substring(3, UID_LENGTH);

        // Using the first two hash of blob to create a directory
        // if it doesn't exist
        File objectDirectory = join(OBJECTS, firstTwoHash);
        if (!objectDirectory.exists()) {
            objectDirectory.mkdir();
        }

        // Using the rest of hash as file name
        File blob = join(objectDirectory, restOfHash);
        writeContents(blob, blobContent);
    }

}
