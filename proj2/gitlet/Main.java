package gitlet;

import java.io.File;

import static gitlet.Utils.*;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Ziqi He
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // check whether args is empty
        if (args.length == 0) {
            message("Please enter a command");
            System.exit(0);
        }
        String firstArg = args[0];

        switch(firstArg) {
            case "init":
                Repository.createGitletDir();
                break;
            case "add":
                if (!Repository.checkGitletExists()) {
                    message("Not in an initialized Gitlet directory.");
                }
                Index.fromFile().addFile(args[1]);
                break;
            case "commit":
                if (args[1] == null) {
                    message("Please enter a commit message.");
                }
                Commit thisCommit = new Commit(
                        readContentsAsString(Repository.HEAD), Commit.getFormattedDate(), args[1]
                );
                break;
            default:
                System.out.println("No command with that name exists.");
        }
    }
}
