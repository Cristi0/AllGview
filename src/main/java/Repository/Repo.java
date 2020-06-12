package Repository;

import java.io.File;

public interface Repo {

    /**
     * Salveaza un fiser local.
     * @param file locatia fisierului impreuna cu numele si extensia sa
     * @param data Obiectele ce vor fi salvate
     */
    public void save(File file, Object... data);

    /**
     * Incarca un fisier local.
     * @param file locatia fisierului impreuna cu numele si extensia sa
     * @return Obiectele din fisier
     */
    public Object[] load(File file);
}
