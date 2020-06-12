package Eroare;

/**
 * Clasa creata pentru tratarea erorilor neasteptate
 */
public class UnexpectedException extends Eroare {

    public UnexpectedException(String exceptie) {
        super("[UnexpectedException] "+exceptie);
    }
}
