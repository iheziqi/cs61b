package gitlet;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    /**
     * Switches branches.
     * @param branchName
     */
    public static void checkoutBranch(String branchName) {
        File currentBranch = join(Repository.BRANCHES, Branch.getCurrentBranch());
        File branchCheckoutTo = join(Repository.BRANCHES, branchName);

        // If no branch with that name exists.
        if (!currentBranch.exists()) {
            System.out.println("No such branch exists.");
            return;
        }

        // If that branch is the current branch.
        if (branchName.equals(getCurrentBranch())) {
            System.out.println("No need to checkout the current branch.");
            return;
        }

        Commit currentCommit = Commit.readCommit(readContentsAsString(currentBranch));
        Commit commitCheckoutTo = Commit.readCommit(readContentsAsString(branchCheckoutTo));
        Index stagingArea = Index.fromFile();
        Index indexCheckoutTo = commitCheckoutTo.getIndex();
        Index indexCurrentCommit = currentCommit.getIndex();

        // If a working file is untracked in the current branch and would be overwritten by the checkout.
        List<String> files = plainFilenamesIn(Repository.CWD);
        for (String file : files) {
            if (
                    !indexCurrentCommit.stagingArea.containsKey(file)
                    // && indexCheckoutTo.stagingArea.containsKey(file)
            ) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                return;
            }
        }

        // Remove all files.
        // All the files that are tacked in the current branch
        // but are not present in the check-out branch are deleted.
        for (String file : files) {
            restrictedDelete(join(Repository.CWD, file));
        }

        // Takes all files in the head commit of the given branch, and puts them in the working directory.
        // Overrides the versions of the files that are already there if they exist.
        for (Map.Entry<String, String> entry : indexCheckoutTo.stagingArea.entrySet()) {
            String fileName = entry.getKey();
            String hashOfFile = entry.getValue();
            String[] blobPath = Repository.getBlobPath(hashOfFile);
            writeContents(join(Repository.CWD, fileName), readContents(join(Repository.OBJECTS, blobPath[0], blobPath[1])));
        }
        // The staging area is cleared.
        stagingArea.clearIndex();
        stagingArea.writeIndex();

        // the given branch is now considered the current branch (HEAD)
        writeContents(Repository.HEAD, branchName);
    }
}
