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
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    public static final File STAGING_AREA = join(GITLET_DIR, "objects");

    /**
     * Creates required directory for gitlet if it not exists
     */
    public static void createGitletDir() {
        if (GITLET_DIR.exists()) {
            System.out.println(
                    "A Gitlet version-control system already exists in the current directory."
            );
            return;
        }
        GITLET_DIR.mkdir();
        STAGING_AREA.mkdir();
    }

    /**
     * Creates directory to store objects
     * with the name based on the first two SHA-1 hash value.
     * @param f file object to be stored
     */
    public static void createObjectDir(File f) {
        // calculates the hash value of the file
        String hashVal = sha1(f);
        // extracts first two characters of hash value
        String firstTwoHashVal = hashVal.substring(0, 2);

        File newObjectDir = join(STAGING_AREA, firstTwoHashVal);
        if (!newObjectDir.exists()) {
            newObjectDir.mkdir();
        }
    }

}
