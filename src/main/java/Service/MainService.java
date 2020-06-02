package Service;

import Eroare.CompileException;
import Repository.Repository;
import Repository.LocalRepo;
import Utils.Compile;
import javafx.util.Pair;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MainService {

    private CompletableFuture<List<Integer>> compile;

    private Repository repo = new LocalRepo();

    public CompletableFuture<List<Integer>> run(String code){
        MainService s= new MainService();
        try {
            compile =CompletableFuture.supplyAsync(() -> {
                try {
                    return Compile.runCode(code);
                } catch (CompileException e) {
                    e.printStackTrace();
                }
                return null;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return compile;
    }

    public  int srv(){
        return -1;
    }

    public void saveLocalFile(List<Pair<Double, Double>> nodes, List<Pair<Integer, Integer>> conections, String text, File file) {
        //todo: validare text aceeasi pt compile time

        repo.save(file,nodes,conections,text);
    }

    public Object[] loadLocalFile(File file) {
        return repo.load(file);
    }
}
