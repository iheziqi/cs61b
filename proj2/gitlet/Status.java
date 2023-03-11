package gitlet;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static gitlet.Utils.*;

/**
 * This class deals with gitlet.Main status command.
 * @author Ziqi He
 */
public class Status {
    /**
     * Prints all existing branches.
     */
    public static void printBranches() {
        String currentBranch = Branch.getCurrentBranch();
        List<String> branches = plainFilenamesIn(Repository.BRANCHES);
        Collections.sort(branches);
        branches.remove(currentBranch);

        System.out.println("=== Branches ===");
        System.out.println("*" + currentBranch);
        for (String branch : branches) {
            System.out.println(branch);
        }
        System.out.println(" ");
    }

    /**
     * prints all files have been staged for addition (in staging area).
     */
    public static void printStagedFiles() {
        Index index = Index.fromFile();
        HashMap<String, String> stagingArea = index.stagingArea;
    }
}
