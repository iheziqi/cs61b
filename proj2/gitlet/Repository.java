package gitlet;

import java.io.File;
import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
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
     * @param f
     */
    private void setBlob(File f) {
        // TODO
    }

    /**
     * Creates the blob of given file or object.
     * @param f
     */
    public static void createBlob(File f) {
        // TODO
    }
}
