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

    /**
     * Updates the branch pointer to the latest commit.
     * @param branchName name of the branch
     * @param commitID hash value of the last commit
     */
    public static void updateBranchPointer(String branchName, String commitID) {
        writeContents(join(Repository.BRANCHES, branchName), commitID);
    }

    /**
     * Gets the last commit ID of the given branch.
     * @param branchName
     * @return the hash value of last commit
     */
    public static String getLastCommit(String branchName) {
        return readContentsAsString(join(Repository.BRANCHES, branchName));
    }

    public static void createNewBranch(String commitID) {

    }
}
