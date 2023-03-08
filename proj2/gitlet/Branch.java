package gitlet;

import static gitlet.Utils.*;

/**
 * Represents a gitlet branch.
 * Branch is nothing more than a pointer pointing to the last commit of current branch.
 * @author Ziqi He
 */
public class Branch {
    /**
     * Creates the default branch named master.
     * @param commitID hash value of default commit
     */
    public static void createDefaultBranch(String commitID) {
        // the name of default branch is master
        // the content in branch file is the hash value of a commit
        writeContents(join(Repository.BRANCHES, "master"), commitID);
    }
}
