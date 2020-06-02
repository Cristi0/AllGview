package Repository;

import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LocalRepo implements Repository {

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
            String code = (String) data[2];
            bw.write(code);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object[] load(File file) {
        Object[] data = new Object[3];
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
            for (int i = 0; i < n.length; i+=2) {
                connections.add(new Pair<>(Integer.valueOf(n[i]), Integer.valueOf(n[i + 1])));
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
            data[2] = code.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
