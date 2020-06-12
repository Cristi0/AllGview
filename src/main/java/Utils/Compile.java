package Utils;

import Eroare.CompileException;
import Service.MainService;
import javafx.beans.property.SimpleFloatProperty;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Compile {
    public static final String className = "javademo";

    private static Float CompileProgress = 0.0f;
    public static SimpleFloatProperty CompileProgressProperty = new SimpleFloatProperty(CompileProgress);
    public static Integer NumberOfCompileSteps = 3;

    private static Path javaSourceFile;
    private final static Path temp = Paths.get(System.getProperty("java.io.tmpdir"), className);

    public static ArrayList<Integer> nodes = new ArrayList<>();
    public static ArrayList<ArrayList<Integer>> edges = new ArrayList<>();
    public static ArrayList<Double> weight = new ArrayList<>();

    private static String errors ="";
    private static Boolean isErrors =false;

    /**
     * Executa un cod sursa
     * @param code String, codul sursa
     * @return rezultatul sub forma de lista, in urma rularii
     * @throws CompileException, in caz de erori de compilare
     */
    public static List<Integer> runCode(String code) throws CompileException {

        Object rez = null;
        try {
            validate(code);
            code = addDependences(code);
            saveInTemp(code);
            CompileProgress++;
            CompileProgressProperty.setValue(CompileProgress);
            compile();
            CompileProgress++;
            CompileProgressProperty.setValue(CompileProgress);
            rez = run();
            CompileProgress++;
            CompileProgressProperty.setValue(CompileProgress);
            CompileProgress = 0.0f;
            CompileProgressProperty.setValue(CompileProgress);

        } catch (IOException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
            throw new CompileException(e.getMessage());
        }
        System.out.println(rez);
        List<Integer> list = (ArrayList<Integer>) rez;
        return list;
    }
    public static String getWarnings(){
        return errors;
    }

    /**
     * Valideaza codul sursa astfel incat ssa nu poata fi introduse alte clase decat cele din java.utils sau clasele intern.
     * Se verifica daca se incearca accestul la o clasa din executia programului
     * @param code, codul sursa
     * @throws CompileException, daca nu sunt respectate validarile se arunta exceptie
     */
    private static void validate(String code) throws CompileException {
        String[] lines = code.split("\\n");
        code.split("\\n");
        code.split("");
        String validationError = "";
        String[] forbiddenWords = getForbiddenWords();

        for (int i = 0; i < lines.length; i++) {
            if(lines[i].matches("[ ]*import [ ]*(?!java\\.util\\.).*[ ]*;")){   //import validation
                validationError+="["+i+"]Import not allowed!\n";
            }
            for (String forbiddenWord : forbiddenWords) {
                if(lines[i].matches(".*"+forbiddenWord+"*")){
                    validationError+="["+i+"]Not found!\n";
                }
            }

        }
        if(!validationError.equals("")) {
            System.out.println(validationError);
            throw new CompileException(validationError);
        }
    }

    /**
     * Returneaza un array care contine toate cuvintele interzise cu scopul de a se executa static
     * @return String[]
     */
    private static String[] getForbiddenWords() {
        return new String[]{"Utils.Compile.","Eroare.","Repository.","Service.","UI."};
    }

    /**
     * Adaugam dependetele la cele 3 valori: noduri, muchii si ponderi, in cazul in care exista
     * @param code, codul sursa
     * @return, codul modificat
     */
    private static String addDependences(String code) {
        code = code.replaceFirst("nodes[ ]*=.*;", "nodes=Utils.Compile.nodes;");
        code = code.replaceFirst("edges[ ]*=.*;", "edges=Utils.Compile.edges;");
        return code.replaceFirst("weights[ ]*=.*;", "weights=Utils.Compile.weight;");
    }

    /**
     * Salveaza fiserului in fisierul temporal din windows
     * @param code, codul sursa
     * @throws IOException, eroare de scriere
     */
    private static void saveInTemp(String code) throws IOException {
        Files.createDirectories(temp);
        javaSourceFile = Paths.get(temp.normalize().toAbsolutePath().toString(), className + ".java");
        System.out.println("The java source file is loacted at " + javaSourceFile);

        Files.write(javaSourceFile, code.getBytes());
    }

    /**
     * Compileaza codul
     * @throws IOException eroare daca nu s-a putut gasi fisierul
     * @throws CompileException, Eroare de compilare
     */
    private static void compile() throws IOException, CompileException {
        final String toolsJarFileName = "tools.jar";
        final String javaHome = System.getProperty("java.home");
        Path toolsJarFilePath = Paths.get(javaHome, "lib", toolsJarFileName);
        if (!Files.exists(toolsJarFilePath)) {
            System.out.println("The tools jar file (" + toolsJarFileName + ") could not be found at (" + toolsJarFilePath + ").");
        }


        // Definition of the files to compile
        File[] files1 = {javaSourceFile.toFile()};
        // Get the compiler
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        // Get the file system manager of the compiler
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        // Create a compilation unit (files)
        Iterable<? extends JavaFileObject> compilationUnits =
                fileManager.getJavaFileObjectsFromFiles(Arrays.asList(files1));
        // A feedback object (diagnostic) to get errors
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        // Compilation unit can be created and called only once
        JavaCompiler.CompilationTask task = compiler.getTask(
                null,
                fileManager,
                diagnostics,
                null,
                null,
                compilationUnits
        );
        // The compile task is called
        task.call();
        // Printing of any compile problems
        errors="";
        isErrors=false;
        for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
            errors+="Line ["+diagnostic.getLineNumber()+"], pozition ["+diagnostic.getColumnNumber()+"] "+diagnostic.getSource()+", "+diagnostic.getMessage(Locale.ENGLISH)+"\n";
            if(diagnostic.getKind()== Diagnostic.Kind.ERROR){
                isErrors=true;
            }
            System.err.format("Line ["+diagnostic.getLineNumber()+"], pozition ["+diagnostic.getColumnNumber()+"] "+diagnostic.getSource()+", "+diagnostic.getMessage(Locale.ENGLISH)+"\n");
        }
        // Close the compile resources
        fileManager.close();
        if(isErrors){
            throw new CompileException(errors);
        }
    }

    /**
     * Executa codul compilat
     * @return Rezultatul in urma rularii codului
     * @throws MalformedURLException, locatie inexistenta a codului compilat
     * @throws ClassNotFoundException, nu exista clasa
     * @throws NoSuchMethodException, nu exista metoda
     * @throws InvocationTargetException, Nu s-a putu invoca metoda
     * @throws IllegalAccessException, Acces invalid
     */
    private static Object run() throws MalformedURLException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ClassLoader classLoader = MainService.class.getClassLoader();
        URLClassLoader urlClassLoader = new URLClassLoader(
                new URL[]{temp.toUri().toURL()},
                classLoader);
        Class javaDemoClass = urlClassLoader.loadClass(className);
        Method method = javaDemoClass.getMethod("run");
        Object obj = new Object();
        Object ret = method.invoke(obj);
        return ret;
    }
}
