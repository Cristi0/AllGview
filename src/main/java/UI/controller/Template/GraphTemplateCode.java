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
                break;
            case 4:
                break;
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
        return  "import java.util.ArrayList;\n" +
                "import Service.MainService;\n\n" +
                "public class " + Compile.className + " {\n\n" +
                "   public static int abc(){\n" +
                "       return 3;\n" +
                "   }\n\n" +
                "   public static ArrayList<Integer> run() {\n" +
                "       ArrayList<Integer> a =new ArrayList<>();\n" +
                "       a.add(1);\n" +
                "       a.add(2);\n" +
                "       a.add(abc());\n" +
                "       MainService s =new MainService();\n" +
                "      // a.add(s.srv());\n" +
                "       try {\n" +
                "            Thread.sleep(1000);\n" +
                "        } catch (InterruptedException e) {\n" +
                "            e.printStackTrace();\n" +
                "        }" +
                "       return a;\n" +
                "    }\n" +
                "}";
    }
}
