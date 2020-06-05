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

public class Compile {
    public static final String className = "javademo";

    private static Float CompileProgress = 0.0f;
    public static SimpleFloatProperty CompileProgressProperty = new SimpleFloatProperty(CompileProgress);
    public static Integer NumberOfCompileSteps = 3;

    private static Path javaSourceFile;
    private final static Path temp = Paths.get(System.getProperty("java.io.tmpdir"), className);

    public static ArrayList<Integer> nodes = new ArrayList<>();
    public static ArrayList<ArrayList<Integer>> edges = new ArrayList<>();

    public static List<Integer> runCode(String code) throws CompileException {
        Object rez = null;
        try {
            validate(code);
            code=addDependences(code);
            System.out.println(code);
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

    private static void validate(String code) {
    }

    private static String addDependences(String code) {
        code=code.replaceFirst("nodes[ ]*=[ ]*null","nodes=Utils.Compile.nodes");
        return code.replaceFirst("edges[ ]*=[ ]*null","edges=Utils.Compile.edges");
    }

    private static void saveInTemp(String code) throws IOException {
        Files.createDirectories(temp);
        javaSourceFile = Paths.get(temp.normalize().toAbsolutePath().toString(), className + ".java");
        System.out.println("The java source file is loacted at " + javaSourceFile);

        Files.write(javaSourceFile, code.getBytes());
    }

    private static void compile() throws IOException {
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
        for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
            System.err.format("Error on line %d in %s%n",
                    diagnostic.getLineNumber(),
                    diagnostic.getSource());
        }
        // Close the compile resources
        fileManager.close();
    }

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

    public static class Pair {
        private Integer node1;
        private Integer node2;

        public Pair(Integer first, Integer second) {
            this.node1 = first;
            this.node2 = second;
        }

        public Integer getFirst() {
            return node1;
        }

        public void setFirst(Integer first) {
            this.node1 = node1;
        }

        public Integer getSecond() {
            return node2;
        }

        public void setSecond(Integer second) {
            this.node2 = node2;
        }
    }
}
