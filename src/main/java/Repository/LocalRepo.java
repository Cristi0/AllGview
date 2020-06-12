package Repository;

import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LocalRepo implements Repo {

    private static LocalRepo repo = null;

    private LocalRepo() {
    }

    /**
     * Creeaza o singura data obiectul LocalRepository, daca nu este deja creat, si trimite obiectul
     * @return  LocalRepo
     */
    public static LocalRepo getRepository() {
        if (repo == null) {
            repo = new LocalRepo();
        }
        return repo;
    }

    /**
     * Salveaza intr-un fisier local datele:
     * data[0] - List<Pair<Double,Double>>> unde fiecare pair este pozitia x, respectiv y a unui nod pe ecran
     * data[1] - List<Pair<Integer,Integer>>, fiecare pair este reprezentate de id-ul primului nod respectiv id-ul celui de-al doilea nod, ea reprezinta legatura dintre aceste noduri
     * data[2] - List<Double>, fiecare valoare, reprezinta ponderea fiecarui legauri in aceeasi oridine cu data[1], poate fi optional,
     * data[3] - String, este codul sursa
     * @param file, locatia unde se vor salva datele, contine si numele fisierului, impreuna cu extensia
     * @param data, size = 4
     */
    @Override
    public void save(File file, Object... data) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            List<Pair<Double, Double>> nodes = (List<Pair<Double, Double>>) data[0];
            for (Pair<Double, Double> node : nodes) {
                bw.write(node.getKey() + "," + node.getValue() + ",");
            }
            bw.newLine();
            List<Pair<Integer, Integer>> connections = (List<Pair<Integer, Integer>>) data[1];
            for (Pair<Integer, Integer> connection : connections) {
                bw.write(connection.getKey() + "," + connection.getValue() + ",");
            }
            bw.newLine();
            System.out.println(data[2]);
            List<Double> weight = (List<Double>) data[2];
            for (Double w : weight) {
                bw.write(w + ",");
            }
            bw.newLine();
            String code = (String) data[3];
            bw.write(code);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Incarca datale din fisier. La returnare:
     * data[0] - List<Pair<Double,Double>>> unde fiecare pair este pozitia x, respectiv y a unui nod pe ecran
     * data[1] - List<Pair<Integer,Integer>>, fiecare pair este reprezentate de id-ul primului nod respectiv id-ul celui de-al doilea nod, ea reprezinta legatura dintre aceste noduri
     * data[2] - List<Double>, fiecare valoare, reprezinta ponderea fiecarui legauri in aceeasi oridine cu data[1], poate fi optional
     * data[3] - String, este codul sursa
     * @param file locatia unde se vor salva datele, contine si numele fisierului, impreuna cu extensia
     * @return un array de 4 elemente
     */
    @Override
    public Object[] load(File file) {
        Object[] data = new Object[4];
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            List<Pair<Double, Double>> nodes = new ArrayList<>();
            String first = br.readLine();
            String[] n = first.split(",");
            for (int i = 0; i < n.length; i += 2) {
                nodes.add(new Pair<>(Double.valueOf(n[i]), Double.valueOf(n[i + 1])));
            }
            String second = br.readLine();
            n = second.split(",");
            List<Pair<Integer, Integer>> connections = new ArrayList<>();
            for (int i = 0; i < n.length; i += 2) {
                connections.add(new Pair<>(Integer.valueOf(n[i]), Integer.valueOf(n[i + 1])));
            }
            String third = br.readLine();
            List<Double> weights = new ArrayList<>();
            if(!third.equals("")) {
                n = third.split(",");
                for (String s : n) {
                    weights.add(Double.valueOf(s));
                }
            }
            StringBuilder code = new StringBuilder();
            String s;
            while ((s = br.readLine()) != null) {
                code.append(s);
                code.append("\n");
            }
            br.close();
            data[0] = nodes;
            data[1] = connections;
            data[2] = weights;
            data[3] = code.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
