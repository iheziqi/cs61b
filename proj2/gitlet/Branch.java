package gitlet;

import java.io.File;

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
     * Creates a new branch with the given name, and points it at the current head commit.
     * @param branchName
     * @param commitID
     */
    public static void createNewBranch(String branchName, String commitID) {
        File branch = join(Repository.BRANCHES, branchName);
        if (branch.exists()) {
            message("A branch with that name already exists");
            return;
        }
        writeContents(join(Repository.BRANCHES, branchName), commitID);
    }

    /**
     * Deletes the branch with the given name.
     * Only delete the pointer associated with the branch.
     * @param branchName
     */
    public static void removeBranch(String branchName) {
        File branchToRemove = join(Repository.BRANCHES, branchName);
        if (!branchToRemove.exists()) {
            message("A branch with that name does not exist.");
            return;
        }
        if (branchName.equals(getCurrentBranch())) {
            message("Cannot remove the current branch.");
            return;
        }

        branchToRemove.delete();
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

    /**
     * Gets the current branch name.
     * @return the branch name
     */
    public static String getCurrentBranch() {
        return readContentsAsString(Repository.HEAD);
    }
}
