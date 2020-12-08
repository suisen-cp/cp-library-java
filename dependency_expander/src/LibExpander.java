import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

class LibExpander {
    static final String INNER_CLASS_SYMBOL = "\\$";
    static final String DOT_ARROW = "->";

    static final int ARG_LIB_DEP_PATH = 0;
    static final int ARG_STD_DEP_PATH = 1;

    static final HashSet<String> EMPTY_SET = new HashSet<>(0);

    public static void main(String[] args) throws Exception {
        String libDepPath = args[ARG_LIB_DEP_PATH];
        String stdDepPath = args[ARG_STD_DEP_PATH];
        File libIn = new File(libDepPath);
        File stdIn = new File(stdDepPath);
        Scanner libSc = new Scanner(new FileInputStream(libIn));
        Scanner stdSc = new Scanner(new FileInputStream(stdIn));
        HashMap<String, HashSet<String>> libDeps = new HashMap<>();
        HashMap<String, HashSet<String>> stdDeps = new HashMap<>();
        Digraph<String> libDepGraph = new Digraph<>();
        while (libSc.hasNext()) {
            String line = libSc.nextLine();
            String[] dep = line.split(DOT_ARROW);
            if (dep.length >= 2) {
                String fr = dep[0].split(INNER_CLASS_SYMBOL)[0];
                String to = dep[1].split(INNER_CLASS_SYMBOL)[0];
                libDepGraph.addEdge(fr, to);
            }
        }
        while (stdSc.hasNext()) {
            String line = stdSc.nextLine();
            String[] dep = line.split(DOT_ARROW);
            if (dep.length >= 2) {
                String fr = dep[0].split(INNER_CLASS_SYMBOL)[0];
                String to = dep[1].split(INNER_CLASS_SYMBOL)[0];
                stdDeps.compute(fr, (k, v) -> {
                   HashSet<String> set = v == null ? new HashSet<>() : v;
                   set.add(to);
                   return set;
                });
            }
        }
        libDepGraph.getNodes().forEach(node -> {
            libDeps.put(node, BreadthFirstSearch.reachableNodes(libDepGraph, node));
        });
        stdDeps.forEach((k, v) -> v.remove(k));
        stdDeps.forEach((pLib, v) -> {
            libDeps.getOrDefault(pLib, EMPTY_SET).forEach(cLib -> {
                if (!pLib.equals(cLib)) {
                    v.addAll(stdDeps.getOrDefault(cLib, EMPTY_SET));
                }
            });
        });
        File libOut = new File(libDepPath + ".object");
        File stdOut = new File(stdDepPath + ".object");
        ObjectOutputStream libOs = new ObjectOutputStream(new FileOutputStream(libOut));
        ObjectOutputStream stdOs = new ObjectOutputStream(new FileOutputStream(stdOut));
        libOs.writeObject(libDeps);
        stdOs.writeObject(stdDeps);
        libOs.flush();
        libOs.close();
        stdOs.flush();
        stdOs.close();

        PrintWriter libPw = new PrintWriter(new File(libDepPath + ".object.json"));
        PrintWriter stdPw = new PrintWriter(new File(stdDepPath + ".object.json"));
        libPw.println(jsonify(libDeps));
        stdPw.println(jsonify(stdDeps));
        libPw.flush();
        libPw.close();
        stdPw.flush();
        stdPw.close();
    }

    static final String INDENT = "    ";

    static String jsonify(HashMap<String, HashSet<String>> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        map.forEach((key, set) -> {
            sb.append(INDENT).append(String.format("\"%s\" : [\n", key));
            set.forEach(v -> {
                sb.append(INDENT).append(INDENT).append(String.format("\"%s\",\n", v));
            });
            sb.deleteCharAt(sb.length() - 2);
            sb.append(INDENT).append("],\n");
        });
        sb.deleteCharAt(sb.length() - 2);
        sb.append("}\n");
        return sb.toString();
    }
}