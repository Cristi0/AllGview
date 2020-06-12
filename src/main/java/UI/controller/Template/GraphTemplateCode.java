package UI.controller.Template;

import Eroare.UnexpectedException;
import Service.MainService;
import Utils.Compile;

public class GraphTemplateCode {

    /**
     * Selecteaza codul necesar pentru un sablon ales
     * @param templateNumber, numarul sablonului
     * @return String, codul sursa necesar
     * @throws UnexpectedException, In cazul in care nu exista sablonul cerut
     */
    public static String getBy(Integer templateNumber) throws UnexpectedException {
        switch (templateNumber) {
            case 1:
                return "";
            case 2:
                return basicCode();
            case 3:
                return bfs();
            case 4:
                return dfs();
            case 5:
                return dijkstra();
            case 6:
                return bellmanFord();
            default:
                throw new UnexpectedException("Template not found in db");
        }
    }

    /**
     * Returneaza codul necesar pentru sablonul de tip de baza
     * @return codul necesar
     */
    private static String basicCode() {
        return "import java.util.ArrayList;\n" +
                "\n" +
                "public class javademo {\n" +
                "\n" +
                "\tprivate final static ArrayList<Integer> nodes = null;\n" +
                "\tprivate final static ArrayList<ArrayList<Integer>> edges = null;\n" +
                "\tprivate final static ArrayList<Double> weights = null;\n" +
                "\n" +
                "\tpublic static ArrayList<Integer> run() {\n" +
                "\t\tArrayList<Integer> result = new ArrayList<>(nodes);\n" +
                "\t\tfor(int i=0;i<edges.size();i++){\n" +
                "\t\t\tresult.add(edges.get(i).get(0));\n" +
                "\t\t\tresult.add(edges.get(i).get(1));\n" +
                "\t\t}\n" +
                "\t\tfor(int i=0;i<weights.size();i++){\n" +
                "\t\t\tresult.add(weights.get(i).intValue());\n" +
                "\t\t}" +
                "\t\treturn result;\n" +
                "\t}\n" +
                "}";
    }

    /**
     * Returneaza codul necesar pentru sablonul care contine algoritmul breath first search (BFS)
     * @return codul necesar
     */
    private static String bfs() {
        return  "import java.util.*;\n" +
                "\n" +
                "public class javademo {\n" +
                "\n" +
                "    private static ArrayList<Integer> nodes = null;\n" +
                "    private static ArrayList<ArrayList<Integer>> edges = null;\n" +
                "    private static ArrayList<Double> weights = null;\n" +
                "\n" +
                "    public static ArrayList<Integer> run() {\n" +
                "        ArrayList<Integer> result = new ArrayList<>();\n" +
                "        Integer startNode = 0;\n" +
                "\n" +
                "        List<Boolean> visited = new ArrayList<>(Collections.nCopies(nodes.size(), false));\n" +
                "        Queue<Integer> queue = new LinkedList<>();\n" +
                "\n" +
                "        visited.set(startNode, true);\n" +
                "        queue.add(startNode);\n" +
                "        Integer currentNode;\n" +
                "        while (queue.size() != 0) {\n" +
                "            currentNode = queue.poll();\n" +
                "            result.add(currentNode);\n" +
                "            for (int i = 0; i < edges.size(); i++) {\n" +
                "                if (edges.get(i).get(0).equals(currentNode)) {\n" +
                "                    addToQueueAllNonvisitedNodes(i, visited, queue, 1);\n" +
                "                }\n" +
                "                if (edges.get(i).get(1).equals(currentNode)) {\n" +
                "                    addToQueueAllNonvisitedNodes(i, visited, queue, 0);\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "        return result;\n" +
                "    }\n" +
                "\n" +
                "    public static void addToQueueAllNonvisitedNodes(Integer i, List<Boolean> visited, Queue<Integer> queue, int edgeSide) {\n" +
                "        if (!visited.get(edges.get(i).get(edgeSide))) {\n" +
                "            visited.set(edges.get(i).get(edgeSide), true);\n" +
                "            queue.add(edges.get(i).get(edgeSide));\n" +
                "        }\n" +
                "    }\n" +
                "}";
    }

    /**
     * Returneaza codul necesar pentru sablonul care contine algoritmul depth first search (DFS)
     * @return codul necesar
     */
    private static String dfs() {
        return  "import java.util.*;\n" +
                "\n" +
                "public class javademo {\n" +
                "\n" +
                "    private static ArrayList<Integer> nodes = null;\n" +
                "    private static ArrayList<ArrayList<Integer>> edges = null;\n" +
                "    private static ArrayList<Double> weights = null;\n" +
                "\n" +
                "    public static ArrayList<Integer> run() {\n" +
                "        ArrayList<Integer> result = new ArrayList<>();\n" +
                "        Integer startNode = 0;\n" +
                "\n" +
                "        List<Boolean> visited = new ArrayList<>(Collections.nCopies(nodes.size(), false));\n" +
                "        Stack<Integer> queue = new Stack<>();\n" +
                "\n" +
                "        visited.set(startNode, true);\n" +
                "        queue.add(startNode);\n" +
                "        Integer currentNode;\n" +
                "        while (queue.size() != 0) {\n" +
                "            currentNode = queue.pop();\n" +
                "            result.add(currentNode);\n" +
                "            for (int i = 0; i < edges.size(); i++) {\n" +
                "                if (edges.get(i).get(0).equals(currentNode)) {\n" +
                "                    addToQueueAllNonvisitedNodes(i, visited, queue, 1);\n" +
                "                }\n" +
                "                if (edges.get(i).get(1).equals(currentNode)) {\n" +
                "                    addToQueueAllNonvisitedNodes(i, visited, queue, 0);\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "        return result;\n" +
                "    }\n" +
                "\n" +
                "    public static void addToQueueAllNonvisitedNodes(Integer i, List<Boolean> visited, Stack<Integer> queue, int edgeSide) {\n" +
                "        if (!visited.get(edges.get(i).get(edgeSide))) {\n" +
                "            visited.set(edges.get(i).get(edgeSide), true);\n" +
                "            queue.add(edges.get(i).get(edgeSide));\n" +
                "        }\n" +
                "    }\n" +
                "}";
    }
    /**
     * Returneaza codul necesar pentru sablonul care contine algoritmul lui Dijkstra
     * @return codul necesar
     */
    private static String dijkstra(){
        return "import java.util.*;\n" +
                "\n" +
                "public class javademo {\n" +
                "\n" +
                "    private static ArrayList<Integer> nodes = null;\n" +
                "    private static ArrayList<ArrayList<Integer>> edges = null;\n" +
                "    private static ArrayList<Double> weights = null;\n" +
                "\n" +
                "    public static ArrayList<Integer> run() {\n" +
                "        ArrayList<Integer> result = new ArrayList<>();\n" +
                "        Integer startNode = 0;\n" +
                "\n" +
                "        List<Boolean> visited = new ArrayList<>(Collections.nCopies(nodes.size(), false));\n" +
                "        List<Double> distances = new ArrayList<>(Collections.nCopies(nodes.size(), Double.MAX_VALUE));\n" +
                "\n" +
                "        distances.set(startNode, 0.0);\n" +
                "        result.add(startNode);\n" +
                "\n" +
                "        for (int count = 0; count < nodes.size() - 1; count++) {\n" +
                "            int u = minDistance(distances, visited);\n" +
                "            visited.set(u, true);\n" +
                "\n" +
                "            for (int i = 0; i < edges.size(); i++) {\n" +
                "                int neighbour = -1;\n" +
                "                if (edges.get(i).get(0).equals(u)) {\n" +
                "                    neighbour = edges.get(i).get(1);\n" +
                "                }\n" +
                "                if (edges.get(i).get(1).equals(u)) {\n" +
                "                    neighbour = edges.get(i).get(0);\n" +
                "                }\n" +
                "                if (neighbour != -1 &&\n" +
                "                        !visited.get(neighbour) &&\n" +
                "                        distances.get(u) != Double.MAX_VALUE\n" +
                "                        && distances.get(u) + weights.get(i) < distances.get(neighbour)) {\n" +
                "                    result.add(neighbour);\n" +
                "                    distances.set(neighbour, distances.get(u) + weights.get(i));\n" +
                "                }\n" +
                "            }\n" +
                "\n" +
                "        }\n" +
                "        return result;\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    private static int minDistance(List<Double> distances, List<Boolean> visited) {\n" +
                "        double min = Integer.MAX_VALUE;\n" +
                "        int minIndex = -1;\n" +
                "\n" +
                "        for (int v = 0; v < nodes.size(); v++) {\n" +
                "            if (!visited.get(v) && distances.get(v) <= min) {\n" +
                "                min = distances.get(v);\n" +
                "                minIndex = v;\n" +
                "            }\n" +
                "        }\n" +
                "        return minIndex;\n" +
                "    }";
    }
    /**
     * Returneaza codul necesar pentru sablonul care contine algoritmul lui Bellman Ford
     * @return codul necesar
     */
    private static String bellmanFord() {
        return "import java.util.*;\n" +
                "\n" +
                "public class javademo {\n" +
                "\n" +
                "    private static ArrayList<Integer> nodes = new ArrayList<>();\n" +
                "    private static ArrayList<ArrayList<Integer>> edges = new ArrayList<>();\n" +
                "    private static ArrayList<Double> weights = new ArrayList<>();\n" +
                "\n" +
                "    public static ArrayList<Integer> run() {\n" +
                "        ArrayList<Integer> result = new ArrayList<>();\n" +
                "        Integer startNode = 0;\n" +
                "\n" +
                "        List<Double> distances = new ArrayList<>(Collections.nCopies(nodes.size(), Double.MAX_VALUE));\n" +
                "        distances.set(startNode, 0.0);\n" +
                "        result.add(startNode);\n" +
                "\n" +
                "        for (int i = 1; i < nodes.size(); ++i) {\n" +
                "            for (int j = 0; j < edges.size(); ++j) {\n" +
                "                int u = edges.get(j).get(0);\n" +
                "                int v = edges.get(j).get(1);\n" +
                "                double weight = weights.get(j);\n" +
                "                if (distances.get(u) != Double.MAX_VALUE && distances.get(u) + weight < distances.get(v)) {\n" +
                "                    distances.set(v, distances.get(u) + weight);\n" +
                "                    result.add(v);\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "       return result;\n" +
                "   }\n" +
                "}";
    }
}
