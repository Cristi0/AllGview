package Eroare;

public class CompileException extends Eroare {
    public CompileException(String exceptie){
        super("[CompileException] "+exceptie);
    }
}
