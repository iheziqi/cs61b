package gitlet;

import java.util.*;

import static gitlet.Utils.*;

/**
 * This class deals with gitlet.Main status command.
 * @author Ziqi He
 */
public class Status {
    private Index index;
    public Status() {
        index = Index.fromFile();
        System.out.println("=== Branches ===");
        printBranches();
        System.out.println("=== Staged Files ===");
        printStagedFiles();
        System.out.println("=== Removed Files ===");
        printRemovedFiles();
        System.out.println("=== Modifications Not Staged For Commit ===");
        modificationNotStagedForCommit();
        System.out.println("=== Untracked Files ===");
        printStagedFiles();
    }

    /**
     * Prints all existing branches.
     */
    public void printBranches() {
        String currentBranch = Branch.getCurrentBranch();
        List<String> branches = new LinkedList<>(plainFilenamesIn(Repository.BRANCHES));
        Collections.sort(branches);
        branches.remove(currentBranch);

        System.out.println("*" + currentBranch);
        for (String branch : branches) {
            System.out.println(branch);
        }
        System.out.println(" ");
    }

    /**
     * Prints all files have been staged for addition (in staging area).
     */
    public void printStagedFiles() {
        HashMap<String, String> stagingArea = index.stagingArea;
        String[] files = stagingArea.keySet().toArray(new String[0]);
        Arrays.sort(files, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        for (String file : files) {
            System.out.println(file);
        }
        System.out.println(" ");
    }

    /**
     * Prints all files have been staged for removal.
     */
    public void printRemovedFiles() {
        List<String> removalArea = index.removalArea;
        Collections.sort(removalArea);
        for(String file : removalArea) {
            System.out.println(file);
        }
        System.out.println(" ");
    }

    /**
     * A file in the working directory is “modified but not staged” if it is
     * Tracked in the current commit, changed in the working directory, but not staged;
     * or Staged for addition, but with different contents than in the working directory;
     * or Staged for addition, but deleted in the working directory;
     * or Not staged for removal, but tracked in the current commit and deleted from the working directory.
     */
    public void modificationNotStagedForCommit() {
        System.out.println(" ");
    }

    /**
     * The final category (“Untracked Files”) is for files present in the working directory
     * but neither staged for addition nor tracked.
     * This includes files that have been staged for removal, but then re-created without Gitlet’s knowledge.
     * Ignore any subdirectories that may have been introduced, since Gitlet does not deal with them.
     */
    public void printUntrackedFiles() {
        System.out.println(" ");
    }
}
