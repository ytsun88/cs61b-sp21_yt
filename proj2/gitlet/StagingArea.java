package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StagingArea implements Serializable {
    /**
     * Key is file name and value is blob id.
     */
    private Map<String, String> added;

    private Set<String> removed;

    public StagingArea() {
        this.added = new HashMap<>();
        this.removed = new HashSet<>();
    }

    public void addFile(String fileName, String blobId) {
        added.put(fileName, blobId);
        removed.remove(fileName);
    }

    public void removeFile(String fileName) {
        added.remove(fileName);
        removed.add(fileName);
    }

    public boolean isEmpty() {
        return added.isEmpty() && removed.isEmpty();
    }

    public Map<String, String> getAdded() {
        return added;
    }

    public Set<String> getRemoved() {
        return removed;
    }
}
