package Service;

import Eroare.CompileException;
import Repository.Repo;
import Repository.LocalRepo;
import Utils.Compile;
import javafx.util.Pair;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MainService {

    private CompletableFuture<List<Integer>> compile;

    private Repo repo = LocalRepo.getRepository();
    private static MainService srv = null;

    private MainService() {

    }

    /**
     * Creeaza o singura data obiectul MainService, daca nu este deja creat, si trimite obiectul
     * @return MainService
     */
    public static MainService getMainService() {
        if (srv == null) {
            srv = new MainService();
        }
        return srv;
    }

    /**
     * Salveaza intr-un fisier informatiile legate de un graf:
     * @param nodes lista de pozitii ale unui nod
     * @param conections lista de conexiuni intre noduri
     * @param weight ponderile acestor conexiuni
     * @param text codul sursa
     * @param file locatia unde se vor salva impreuna cu numele fisierului si extensia
     */
    public void saveLocalFile(List<Pair<Double, Double>> nodes, List<Pair<Integer, Integer>> conections, List<Double> weight, String text, File file) {
        repo.save(file, nodes, conections, weight, text);
    }

    /**
     * Incarca un fisier local:
     * data[0] - List<Pair<Double,Double>>> unde fiecare pair este pozitia x, respectiv y a unui nod pe ecran
     * data[1] - List<Pair<Integer,Integer>>, fiecare pair este reprezentate de id-ul primului nod respectiv id-ul celui de-al doilea nod, ea reprezinta legatura dintre aceste noduri
     * data[2] - List<Double>, fiecare valoare, reprezinta ponderea fiecarui legauri in aceeasi oridine cu data[1], poate fi optional
     * data[3] - String, este codul sursa
     * @param file locatia unde se vor salva impreuna cu numele fisierului si extensia
     * @return
     */
    public Object[] loadLocalFile(File file) {
        return repo.load(file);
    }
}
