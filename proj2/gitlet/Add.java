package gitlet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static gitlet.Utils.*;

/** Handles command gitlet add [file name]
 * @author Ziqi He
 */
public class Add {

    /**
     * Adds a copy of the file as it currently exists to the staging area
     * @param fileName a string of file name to be added to staging area
     */
    public static void stageFile(String fileName) {
        List<String> fileNames = new ArrayList<>();
        fileNames = plainFilenamesIn(fileName);

    }
}
