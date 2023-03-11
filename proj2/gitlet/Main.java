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
                    System.exit(0);
                }
                if (args.length < 2) {
                    message("File does not exist.");
                    System.exit(0);
                }
                Index.fromFile().addFile(args[1]);
                break;
            case "commit":
                if (args.length < 2) {
                    message("Please enter a commit message.");
                    System.exit(0);
                }
                // If no files have been staged, abort.
                if (Index.fromFile().stagingArea.isEmpty()) {
                    message("No changes added to the commit");
                    System.exit(0);
                }
                Commit thisCommit = new Commit(
                        Branch.getLastCommit(Branch.getCurrentBranch()), Commit.getFormattedDate(), args[1]
                );
                thisCommit.writeCommit();
                break;
            case "rm":
                if (args.length < 2) {
                    message("Incorrect operands.");
                    System.exit(0);
                }
                Index.fromFile().removeFile(args[1]);
                break;
            case "branch":
                if (args.length < 2) {
                    message("Incorrect operands.");
                    System.exit(0);
                }
                Branch.createNewBranch(args[1], Branch.getLastCommit(Branch.getCurrentBranch()));
                break;
            case "rm-branch":
                if (args.length < 2) {
                    message("Incorrect operands.");
                    System.exit(0);
                }
                Branch.removeBranch(args[1]);
                break;
            case "cat-index":
                Index.printIndex();
                break;
            case "cat-commit":
                if(args.length < 3) {
                    message("Please enter the filename of commit");
                    System.exit(0);
                }
                // Commit commit = readObject(join(Repository.OBJECTS, args[1], args[2]), Commit.class);
                Commit commit = Commit.readCommit(args[1] + args[2]);
                if (commit == null){
                    System.out.println("error!");
                    System.exit(0);
                }
                System.out.println(commit);
                commit.print();
                break;
            default:
                System.out.println("No command with that name exists.");
        }
    }
}
