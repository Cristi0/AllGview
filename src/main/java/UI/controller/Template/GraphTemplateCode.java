package UI.controller.Template;

import Eroare.UnexpectedException;
import Service.MainService;
import Utils.Compile;

public class GraphTemplateCode {

    public static String getBy(Integer templateNumber) throws UnexpectedException {
        switch (templateNumber){
            case 1:
                return "";
            case 2:
                return basicCode();
            case 3:
                return bfs();
            case 4:
                return dfs();
            case 5:
                break;
            case 6:
                break;
            default:
                throw new UnexpectedException("Template not found in db");
        }
        return null;
    }

    private static String basicCode() {
//        return  "import java.util.ArrayList;\n" +
//                "import Service.MainService;\n\n" +
//                "public class " + Compile.className + " {\n\n" +
//                "   public static int abc(){\n" +
//                "       return 3;\n" +
//                "   }\n\n" +
//                "   public static ArrayList<Integer> run() {\n" +
//                "       ArrayList<Integer> a =new ArrayList<>();\n" +
//                "       a.add(1);\n" +
//                "       a.add(2);\n" +
//                "       a.add(abc());\n" +
//                "       MainService s =new MainService();\n" +
//                "      // a.add(s.srv());\n" +
//                "       try {\n" +
//                "            Thread.sleep(1000);\n" +
//                "        } catch (InterruptedException e) {\n" +
//                "            e.printStackTrace();\n" +
//                "        }" +
//                "       return a;\n" +
//                "    }\n" +
//                "}";
        return "import java.util.ArrayList;\n" +
                "\n" +
                "public class javademo {\n" +
                "\n" +
                "\tprivate final static ArrayList<Integer> nodes=null;\n" +
                "\tprivate final static ArrayList<ArrayList<Integer>> edges=null;\n" +
                "\n" +
                "\tpublic static ArrayList<Integer> run() {\n" +
                "\t\tArrayList<Integer> result = new ArrayList<>(nodes);\n" +
                "\t\tfor(int i=0;i<edges.size();i++){\n" +
                "\t\t\tresult.add(edges.get(i).get(0));\n" +
                "\t\t\tresult.add(edges.get(i).get(1));\n" +
                "\t}\n" +
                "\treturn result;\n" +
                "\t}\n" +
                "}";
    }

    private static String bfs() {
        return "import Utils.Compile;\n" +
                "\n" +
                "import java.util.*;\n" +
                "\n" +
                "public class javademo {\n" +
                "\n" +
                "    private static ArrayList<Integer> nodes = Utils.Compile.nodes;\n" +
                "    private static ArrayList<ArrayList<Integer>> edges = Compile.edges;\n" +
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

    private static String dfs() {
        return "import Utils.Compile;\n" +
                "\n" +
                "import java.util.*;\n" +
                "\n" +
                "public class javademo {\n" +
                "\n" +
                "    private static ArrayList<Integer> nodes = Utils.Compile.nodes;\n" +
                "    private static ArrayList<ArrayList<Integer>> edges = Compile.edges;\n" +
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
}
