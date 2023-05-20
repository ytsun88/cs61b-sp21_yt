package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Utils.readContents;
import static gitlet.Utils.sha1;

public class Blob implements Serializable {
    /**
     * Source file of this Blob.
     */
    private File sourceFile;

    /**
     * The SHA1 id generated from the source file content.
     */
    private String id;

    private byte[] content;

    /**
     * Constructor of blob with the given source file.
     *
     * @param source
     */
    public Blob(File source) {
        this.sourceFile = source;
        this.content = readContents(source);
        this.id = sha1(source.getPath(), readContents(source));
    }

    public File getSourceFile() {
        return sourceFile;
    }

    public String getId() {
        return id;
    }

    public byte[] getContent() {
        return content;
    }
}
