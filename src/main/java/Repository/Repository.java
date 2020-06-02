package Repository;

import java.io.File;

public interface Repository {

    public void save(File file, Object... data);
    public Object[] load(File file);
}
