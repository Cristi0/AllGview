package Eroare;

/**
 * Clasa creata pentru tratarea exceptiilor de compilare
 */
public class CompileException extends Eroare {
    public CompileException(String exceptie){
        super("[CompileException] "+exceptie);
    }
}
