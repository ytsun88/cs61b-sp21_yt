package gitlet;

/**
 * Driver class for Gitlet, a subset of the Git version-control system.
 *
 * @author TODO
 */
public class Main {

    /**
     * Usage: java gitlet.Main ARGS, where ARGS contains
     * <COMMAND> <OPERAND1> <OPERAND2> ...
     */
    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }

        String firstArg = args[0];
        switch (firstArg) {
            case "init" -> {
                validateNumArgs(args, 1);
                Repository.init();
            }
            case "add" -> {
                validateNumArgs(args, 2);
                Repository.checkInitialDir();
                Repository.add(args[1]);
            }
            case "commit" -> {
                validateNumArgs(args, 2);
                Repository.checkInitialDir();
                Repository.commit(args[1]);
            }
            case "rm" -> {
                validateNumArgs(args, 2);
                Repository.checkInitialDir();
                Repository.rm(args[1]);
            }
            case "log" -> {
                validateNumArgs(args, 1);
                Repository.checkInitialDir();
                Repository.log();
            }
            case "global-log" -> {
                validateNumArgs(args, 1);
                Repository.checkInitialDir();
                Repository.globalLog();
            }
            case "find" -> {
                validateNumArgs(args, 2);
                Repository.checkInitialDir();
                Repository.find(args[1]);
            }
            case "status" -> {
                validateNumArgs(args, 1);
                Repository.checkInitialDir();
                Repository.status();
            }
            case "checkout" -> {
                Repository.checkInitialDir();
                int len = args.length;  // 2 3 4
                if (len == 2) {
                    Repository.checkoutBranch(args[1]);
                } else if (len == 3 && args[1].equals("--")) {
                    Repository.checkoutFile(args[2]);
                } else if (len == 4 && args[2].equals("--")) {
                    Repository.checkoutCommit(args[1], args[3]);
                } else {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
            }
            case "branch" -> {
                validateNumArgs(args, 2);
                Repository.checkInitialDir();
                Repository.branch(args[1]);
            }
            case "rm-branch" -> {
                validateNumArgs(args, 2);
                Repository.checkInitialDir();
                Repository.rmBranch(args[1]);
            }
            case "reset" -> {
                validateNumArgs(args, 2);
                Repository.checkInitialDir();
                Repository.reset(args[1]);
            }
            case "merge" -> {
                validateNumArgs(args, 2);
                Repository.checkInitialDir();
                Repository.merge(args[1]);
            }
            default -> {
                System.out.println("No command with that name exists.");
                System.exit(0);
            }
        }
    }

    public static void validateNumArgs(String[] args, int n) {
        if (args.length != n) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }
}
