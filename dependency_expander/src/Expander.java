import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

public class Expander {
    static final HashSet<String> EMPTY_MAP = new HashSet<>(0);

    static final int ARG_LIB_OUT_PATH = 0;
    static final int ARG_STD_OUT_PATH = 1;
    static final int ARG_LIB_DEPS = 2;
    static final int ARG_STD_DEPS = 3;
    static final int ARG_LIB_OBJ_PATH = 4;
    static final int ARG_STD_OBJ_PATH = 5;

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        ObjectInputStream inLib = new ObjectInputStream(new FileInputStream(new File(args[ARG_LIB_OBJ_PATH])));
        ObjectInputStream inStd = new ObjectInputStream(new FileInputStream(new File(args[ARG_STD_OBJ_PATH])));
        HashMap<String, HashSet<String>> depMapLib = (HashMap<String, HashSet<String>>) inLib.readObject();
        HashMap<String, HashSet<String>> depMapStd = (HashMap<String, HashSet<String>>) inStd.readObject();
        TreeSet<String> depsLib = new TreeSet<>();
        TreeSet<String> depsStd = new TreeSet<>();
        Arrays.stream(args[ARG_LIB_DEPS].split("\n"))
                .forEach(dep -> {
                    String lib = dep.split(LibExpander.INNER_CLASS_SYMBOL)[0];
                    if (depMapLib.containsKey(lib)) {
                        depsLib.addAll(depMapLib.get(lib));
                    } else {
                        depsLib.add(lib);
                    }
                });
        depsLib.forEach(lib -> depsStd.addAll(depMapStd.getOrDefault(lib, EMPTY_MAP)));
        Arrays.stream(args[ARG_STD_DEPS].split("\n")).forEach(dep -> {
            depsStd.add(dep.split(LibExpander.INNER_CLASS_SYMBOL)[0]);
        });

        File outLib = new File(args[ARG_LIB_OUT_PATH]);
        File outStd = new File(args[ARG_STD_OUT_PATH]);
        PrintWriter pwLib = new PrintWriter(outLib);
        PrintWriter pwStd = new PrintWriter(outStd);
        depsLib.forEach(libPkg -> pwLib.println(pkgToPath(libPkg)));
        depsStd.forEach(stdPkg -> {
            if (stdPkg.startsWith("java")) {
                pwStd.println(toImportStatement(stdPkg));
            }
        });
        pwLib.flush();
        pwLib.close();
        pwStd.flush();
        pwStd.close();
    }

    static String pkgToPath(String pkg) {
        return pkg.replaceAll("\\.", "/");
    }

    static String toImportStatement(String std) {
        return "import " + std + ";";
    }
}
