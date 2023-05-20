package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Utils.join;
import static gitlet.Utils.writeObject;

public class MyUtils {
    public static File setObjectFile(String id) {
        String dirName = id.substring(0, 2);
        String fileName = id.substring(2);
        return join(Repository.OBJECTS_DIR, dirName, fileName);
    }

    public static void saveObjectFile(File file, Serializable obj) {
        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdir();
        }
        writeObject(file, obj);
    }
}
