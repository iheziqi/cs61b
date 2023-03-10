package gitlet;

import java.io.File;

import static gitlet.Utils.*;


/** Represents a gitlet repository.
 * This class does the relevant IO operation to .gitlet directory.
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
     * checks whether .gitlet directory exists
     * @return a boolean value of .gitlet directory existence
     */
    public static boolean checkGitletExists() {
        return GITLET_DIR.exists();
    }

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

        // create relevant folders
        GITLET_DIR.mkdir();
        REFS.mkdir();
        OBJECTS.mkdir();

        // create index(staging area)
        new Index();

        // create head pointer pointing to default branch master
        writeContents(HEAD, "master");

        // create initial commit
        // the time of initial commit is Unix epoch time
        Commit initalCommit = new Commit(
                "0000",
                Commit.getFormattedDate(0),
                "default initial commit",
                true
        );

        // create default branch: master branch
        BRANCHES.mkdir();
        Branch.createDefaultBranch(initalCommit.getHash());

        initalCommit.writeCommit();
    }

    /**
     * Calculates the hash value of the file contents.
     * @param file
     * @return hash
     */
    public static String getHashOfFileContent(File file) {
        byte[] blobContent = readContents(file);
        return sha1(blobContent);
    }

    /**
     * Names the blob in correct way and puts it into the correct directory.
     * Inside the .gitlet/objects directory, the directory names are made by first two characters
     * of hash value of file contents(byte array). The rest of the hash is used as the name of the blob file.
     * @param fileName
     * @return a string array including the path of blob,
     * the first element is the folder name(first two hash), the second is the file name (rest of hash).
     */
    public static void setBlob(String fileName) {
        File f = join(CWD, fileName);
        if (!f.exists()) {
            message("File does not exist.");
            System.exit(0);
        }

        // Use byte array of file as blobContent.
        byte[] blobContent = readContents(f);

        // Calculate the hash of byte array.
        String hash = getHashOfFileContent(f);

        // First two characters as directory name.
        // Rest of hash as file name.
        String[] blobPath = getBlobPath(hash);
        String firstTwoHash = blobPath[0];
        String restOfHash = blobPath[1];

        // Using the first two hash of blob to create a directory
        // if it doesn't exist
        File objectDirectory = join(OBJECTS, firstTwoHash);
        if (!objectDirectory.exists()) {
            objectDirectory.mkdir();
        }

        // Using the rest of hash as file name
        File blob = join(objectDirectory, restOfHash);
        // Write the content into blob
        writeContents(blob, blobContent);
    }

    /**
     * Deletes the blob of given file.
     * @param hashOfFile
     */
    public static void deleteBlob(String hashOfFile){
        String[] blobPath = getBlobPath(hashOfFile);
        // Just delete the file, do not delete the folder!
        File blob = join(Repository.OBJECTS, blobPath[0], blobPath[1]);
        blob.delete();
    }

    /**
     * Gets the blob path inside Object directory.
     * @param hash
     * @return a list of strings. The first string is the first two characters of hash value.
     * The second string is the rest of hash value, i.e. the file name.
     */
    public static String[] getBlobPath(String hash) {
        String firstTwoHash = hash.substring(0, 2);
        String restHash = hash.substring(2, UID_LENGTH);
        String[] blobPath = new String[]{firstTwoHash, restHash};
        return blobPath;
    }

    /**
     * Takes a full file path as input and returns the file name.
     * @param filePath
     * @return
     */
    public static String getFileNameFromPath(String filePath) {
        File file = new File(filePath);
        return file.getName();
    }
}
