package gitlet;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static gitlet.Utils.*;

/**
 * @author ytsun
 */
public class Repository {
    /*
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /**
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    /**
     * Directory that stores object files.
     */
    public static final File OBJECTS_DIR = join(GITLET_DIR, "objects");

    /**
     * Directory of staging area.
     */
    public static final File STAGING_DIR = join(GITLET_DIR, "staging");

    /**
     * Stage file.
     */
    public static final File STAGE = join(GITLET_DIR, "STAGE");

    /**
     * Directory that stores heads of branches.
     */
    public static final File HEADS_DIR = join(GITLET_DIR, "heads");

    /**
     * Head pointer.
     */
    public static final File HEAD = join(GITLET_DIR, "HEAD");

    public static void init() {
        if (GITLET_DIR.exists() && GITLET_DIR.isDirectory()) {
            System.out.println(
                    "A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }

        /* Set up directory for persistence. */
        GITLET_DIR.mkdir();
        OBJECTS_DIR.mkdir();
        HEADS_DIR.mkdir();
        STAGING_DIR.mkdir();
        writeObject(STAGE, new StagingArea());

        /* Save initial Commit in "objects" directory by its sha1 ID. */
        Commit initialCommit = new Commit();
        File commitFile = join(OBJECTS_DIR, initialCommit.getID());
        writeObject(commitFile, initialCommit);

        String id = initialCommit.getID();

        // create branch: master
        String branchName = "master";

        writeContents(HEAD, branchName);
        File master = join(HEADS_DIR, branchName);
        writeContents(master, id);

        // create HEAD
        writeContents(HEAD, branchName);
    }

    public static void add(String fileName) {
        File file = join(CWD, fileName);
        if (!file.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        Commit head = getHeadCommit();
        StagingArea stage = readObject(STAGE, StagingArea.class);
        Blob blob = new Blob(file);

        String blobID = blob.getId();

        if (head.getBlobs().containsKey(fileName)) {
            if (!head.getBlobs().get(fileName).equals(blobID)) {
                if (!stage.getAdded().containsKey(fileName)) {
                    stage.addFile(fileName, blobID);
                    File stagedFile = join(STAGING_DIR, blobID);
                    writeObject(stagedFile, blob);
                    writeObject(STAGE, stage);
                } else {
                    if (!stage.getAdded().get(fileName).equals(blobID)) {
                        stage.addFile(fileName, blobID);
                        File stagedFile = join(STAGING_DIR, blobID);
                        writeObject(stagedFile, blob);
                        writeObject(STAGE, stage);
                    }
                }
            }
        } else {
            if (!stage.getAdded().containsKey(fileName)) {
                stage.addFile(fileName, blobID);
                File stagedFile = join(STAGING_DIR, blobID);
                writeObject(stagedFile, blob);
                writeObject(STAGE, stage);
            } else {
                if (!stage.getAdded().get(fileName).equals(blobID)) {
                    stage.addFile(fileName, blobID);
                    File stagedFile = join(STAGING_DIR, blobID);
                    writeObject(stagedFile, blob);
                    writeObject(STAGE, stage);
                }
            }
        }
    }

    public static void commit(String message) {
        if (message.equals("")) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }
        Commit head = getHeadCommit();
        Map<String, String> originBlobs = head.getBlobs();
        List<String> newParents = head.getParents();
        String headID = head.getID();
        newParents.add(0, headID);
        StagingArea stage = readObject(STAGE, StagingArea.class);
        Map<String, String> stagedBlobs = stage.getAdded();
        if (stage.isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        originBlobs.putAll(stagedBlobs);
        Commit newCommit = new Commit(message, newParents, originBlobs);


        File commitFile = join(OBJECTS_DIR, newCommit.getID());
        writeObject(commitFile, newCommit);

        String id = newCommit.getID();

        String branchName = readContentsAsString(HEAD);

        File branchFile = join(HEADS_DIR, branchName);
        writeContents(branchFile, id);

        stage = new StagingArea();
        writeObject(STAGE, stage);
    }

    public static void rm(String fileName) {
        File file = join(CWD, fileName);
        Commit head = getHeadCommit();
        String headID = head.getID();
        StagingArea stage = readObject(STAGE, StagingArea.class);

        if (!file.exists()) {
            if (stage.getAdded().containsKey(fileName)) {
                stage.getAdded().remove(fileName);
            }
        } else {

            /* Failure cases: The file is neither staged nor tracked by the head commit. */
            if (!head.getBlobs().containsKey(fileName) && !stage.getAdded().containsKey(fileName)) {
                System.out.println("No reason to remove the file.");
                System.exit(0);
            }

            /* If the file is staged to be added, remove it from added stage.
             *  If the file is not staged to be added, add it to removed stage.
             */
            if (stage.getAdded().containsKey(fileName)) {
                stage.getAdded().remove(fileName);
            } else {
                stage.getRemoved().add(fileName);
            }

            Blob blob = new Blob(file);
            String blobID = blob.getId();

            if (head.getBlobs().containsKey(fileName)
                    && head.getBlobs().get(fileName).equals(blobID)) {
                restrictedDelete(file);
            }
        }
        writeObject(STAGE, stage);
    }

    public static void log() {
        Commit head = getHeadCommit();
        printLog(head);
        List<String> parents = head.getParents();
        while (!parents.isEmpty()) {
            File file = join(OBJECTS_DIR, parents.get(0));
            Commit commit = readObject(file, Commit.class);
            printLog(commit);
            parents.remove(0);
        }
    }

    public static void globalLog() {
        List<String> fileNameList = plainFilenamesIn(OBJECTS_DIR);
        for (String fileName : fileNameList) {
            File file = join(OBJECTS_DIR, fileName);
            Commit commit = readObject(file, Commit.class);
            printLog(commit);
        }
    }

    public static void find(String message) {
        List<String> fileNameList = plainFilenamesIn(OBJECTS_DIR);
        int count = 0;
        for (String fileName : fileNameList) {
            File file = join(OBJECTS_DIR, fileName);
            Commit commit = readObject(file, Commit.class);
            String commitMessage = commit.getMessage();
            String commitID = commit.getID();
            if (commitMessage.toLowerCase().contains(message.toLowerCase())) {
                System.out.println(commitID);
                count += 1;
            }
        }
        if (count == 0) {
            System.out.println("Found no commit with that message.");
        }
    }

    public static void status() {
        List<String> branches = plainFilenamesIn(HEADS_DIR);
        System.out.println("=== Branches ===");
        for (String branch : branches) {
            if (readContentsAsString(HEAD).equals(branch)) {
                System.out.println("*" + branch);
            } else {
                System.out.println(branch);
            }
        }
        System.out.println();
        StagingArea stage = readObject(STAGE, StagingArea.class);
        Set<String> stagedSet = stage.getAdded().keySet();
        System.out.println("=== Staged Files ===");
        for (String staged : stagedSet) {
            System.out.println(staged);
        }
        System.out.println();

        Set<String> removedSet = stage.getRemoved();
        System.out.println("=== Removed Files ===");
        for (String removed : removedSet) {
            System.out.println(removed);
        }
        System.out.println();

        Commit head = getHeadCommit();
        List<String> filesInCWD = plainFilenamesIn(CWD);
        Set<String> blobsSet = head.getBlobs().keySet();
        System.out.println("=== Modifications Not Staged For Commit ===");
        for (String staged : stagedSet) {
            /* Staged for addition, but deleted in the working directory */
            if (!filesInCWD.contains(staged)) {
                System.out.println(staged + " (deleted)");
            } else {
                Blob check = new Blob(join(CWD, staged));
                /* Staged for addition, but with different contents than in the working directory */
                if (!check.getId().equals(stage.getAdded().get(staged))) {
                    System.out.println(staged + " (modified)");
                }
            }
        }

        for (String blob : blobsSet) {
            /*
             * Not staged for removal,
             * but tracked in the current commit and deleted from the working directory.
             */
            if (!filesInCWD.contains(blob)) {
                if (!removedSet.contains(blob)) {
                    System.out.println(blob + " (deleted)");
                }
            } else {
                Blob check = new Blob(join(CWD, blob));
                /*
                 * Tracked in the current commit,
                 * changed in the working directory, but not staged.
                 */
                if (!check.getId().equals(head.getBlobs().get(blob)) && !stagedSet.contains(blob)) {
                    System.out.println(blob + " (modified)");
                }
            }
        }
        System.out.println();

        System.out.println("=== Untracked Files ===");
        for (String file : filesInCWD) {
            if (!blobsSet.contains(file) && !stagedSet.contains(file)) {
                System.out.println(file);
            }
        }
        System.out.println();
    }

    public static void checkoutFile(String fileName) {
        Commit head = getHeadCommit();
        if (!head.getBlobs().containsKey(fileName)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        File file = join(CWD, fileName);
        String blobID = head.getBlobs().get(fileName);
        File targetFile = join(STAGING_DIR, blobID);
        Blob targetBlob = readObject(targetFile, Blob.class);
        writeContents(file, targetBlob.getContent());
    }

    public static void checkoutBranch(String branchName) {
        File branch = join(HEADS_DIR, branchName);
        if (!branch.exists()) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        String currentBranch = readContentsAsString(HEAD);
        if (currentBranch.equals(branchName)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }
        checkUntracked();
        checkoutBranchFiles(branchName);
        writeContents(HEAD, branchName);
    }

    public static void checkoutCommit(String commitID, String fileName) {
        File commitFile = join(OBJECTS_DIR, commitID);
        if (!commitFile.exists()) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        Commit commit = readObject(commitFile, Commit.class);
        if (!commit.getBlobs().containsKey(fileName)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        String blobID = commit.getBlobs().get(fileName);
        File file = join(CWD, fileName);
        File targetFile = join(STAGING_DIR, blobID);
        Blob targetBlob = readObject(targetFile, Blob.class);
        writeContents(file, targetBlob.getContent());
    }

    public static void branch(String branchName) {
        Commit head = getHeadCommit();
        String headID = head.getID();
        File newBranch = join(HEADS_DIR, branchName);
        if (newBranch.exists()) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        writeContents(newBranch, headID);
    }

    public static void rmBranch(String branchName) {
        File branch = join(HEADS_DIR, branchName);
        if (!branch.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        if (readContentsAsString(HEAD).equals(branchName)) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }
        branch.delete();
    }

    public static void reset(String commitID) {
        File commitFile = join(OBJECTS_DIR, commitID);
        if (!commitFile.exists()) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        checkUntracked();
        Commit commit = readObject(commitFile, Commit.class);
        checkoutCommitFiles(commit);
        /* Moves the current branch’s head to that commit node. */
        File branch = join(HEADS_DIR, readContentsAsString(HEAD));
        writeContents(branch, commitID);
    }

    public static void merge(String branchName) {
        StagingArea stage = readObject(STAGE, StagingArea.class);
        if (!stage.isEmpty()) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
        File branch = join(HEADS_DIR, branchName);
        if (!branch.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        if (readContentsAsString(HEAD).equals(branchName)) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }
    }

    public static void checkInitialDir() {
        if (!GITLET_DIR.isDirectory()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }

    private static Commit getHeadCommit(String branchName) {
        File branchFile = join(HEADS_DIR, branchName);
        String headId = readContentsAsString(branchFile);
        File file = join(OBJECTS_DIR, headId);
        return readObject(file, Commit.class);
    }

    private static Commit getHeadCommit() {
        String branchName = readContentsAsString(HEAD);
        File branchFile = join(HEADS_DIR, branchName);
        String headId = readContentsAsString(branchFile);
        File file = join(OBJECTS_DIR, headId);
        return readObject(file, Commit.class);
    }

    private static void printLog(Commit commit) {
        System.out.println("===");
        System.out.println("commit " + commit.getID());
        System.out.println("Date: " + commit.getTimestamp());
        System.out.println(commit.getMessage());
        System.out.println();
    }

    private static void checkUntracked() {
        Commit head = getHeadCommit();
        Set<String> currentBlobsSet = head.getBlobs().keySet();
        List<String> filesInCWD = plainFilenamesIn(CWD);
        for (String file : filesInCWD) {
            if (!currentBlobsSet.contains(file)) {
                System.out.println(
                        "There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            }
        }
    }

    private static void checkoutBranchFiles(String branchName) {
        Commit branchHead = getHeadCommit(branchName);
        List<String> filesInCWD = plainFilenamesIn(CWD);
        Set<String> blobsSet = branchHead.getBlobs().keySet();
        for (String file : filesInCWD) {
            if (!blobsSet.contains(file)) {
                restrictedDelete(join(CWD, file));
            }
        }
        for (String fileName : blobsSet) {
            File file = join(CWD, fileName);
            String blobID = branchHead.getBlobs().get(fileName);
            File targetFile = join(STAGING_DIR, blobID);
            Blob targetBlob = readObject(targetFile, Blob.class);
            writeContents(file, targetBlob.getContent());
        }
    }

    private static void checkoutCommitFiles(Commit commit) {
        List<String> filesInCWD = plainFilenamesIn(CWD);
        Set<String> blobsSet = commit.getBlobs().keySet();
        for (String file : filesInCWD) {
            if (!blobsSet.contains(file)) {
                restrictedDelete(join(CWD, file));
            }
        }
        for (String fileName : blobsSet) {
            File file = join(CWD, fileName);
            String blobID = commit.getBlobs().get(fileName);
            File targetFile = join(STAGING_DIR, blobID);
            Blob targetBlob = readObject(targetFile, Blob.class);
            writeContents(file, targetBlob.getContent());
        }
    }
}
