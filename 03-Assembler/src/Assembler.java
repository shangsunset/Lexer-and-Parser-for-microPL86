import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Assembler {

    private static String filename = null;
    private static boolean dump = false;
    private static boolean trace = false;
    private static Vector<String> instructionVector = new Vector<String>();
    private static int location = 0;
    private static Map<String, Integer> declarationTable = new TreeMap<String, Integer>();
    private static Map<String, String> instructionTable = new TreeMap<String, String>();


    public static void main(String[] args) {

        processCommandLine(args);
        instructionReader();
    }


    static void instructionReader() {

        try {
            Scanner sc = new Scanner(new FileReader(filename));
            while (sc.hasNextLine()) {
                String instruction = sc.nextLine();
                instructionVector.add(instruction);
                if (!instruction.startsWith("VAR")) {
                    location++;
                    String[] tokens = instruction.trim().split("\\s+");
                    if (!tokens[0].equals("HALT")) {
                        instructionTable.put(tokens[0], tokens[1]);
//                        for (String token : tokens)
//                            System.out.println(token);
                    }
                }
                else {

                    String[] declaration = instruction.trim().split("\\s+");
                    declarationTable.put(declaration[1], Integer.parseInt(declaration[2]));
                }

            }
            for (String var:instructionTable.keySet())
                System.out.println(var + ":" + instructionTable.get(var));

        }
        catch(FileNotFoundException e) {
            System.err.println("Assembler Error: " + e.getMessage());
            System.exit(1);
        }

    }


    static void instructionProccessor () {

        for (String instruction : instructionVector) {

            Scanner instructionScanner = new Scanner(instruction);
            while (instructionScanner.hasNext()) {

            }
        }
    }

    static void processCommandLine(String [] args) {
        boolean sawAnError = false;

        for (String arg : args) {
            if (arg.startsWith("-")) {
                if (arg.substring(1).equals("d"))
                    dump = true;


                else if (arg.substring(1).equals("t")) {
                    trace = true;

                }

                else {
                    System.err.println("Unknown option " + arg);
                    sawAnError = true;
                }
            }
            else
                filename = arg;
        }

        if (filename == null) {		// filename MUST be present on command-line
            System.err.println("Missing filename");
            sawAnError = true;
        }

        if (sawAnError) {
            System.err.println("Usage: Commander {-d} {-t} <flename>");
            System.exit(1);
        }
    }
}
