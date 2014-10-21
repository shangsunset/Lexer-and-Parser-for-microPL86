import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Assembler {

    private static String filename = null;
    private static boolean dump = false;
    private static boolean trace = false;
    private static Vector<String> instructionVector = new Vector<String>();
    private static Vector<Integer> machineCodeInstructionVector = new Vector<Integer>();
    private static int numOfInstructions = 0;
    private static Map<String, Integer> declarationTable = new TreeMap<String, Integer>();
    private static Map<String, String> instructionTable = new TreeMap<String, String>();


    public static void main(String[] args) {

        processCommandLine(args);
        instructionReader();
        instructionProcessor();
        for (int word : machineCodeInstructionVector)
            System.out.println(word);
    }


    static void instructionReader() {

        try {
            Scanner sc = new Scanner(new FileReader(filename));
            while (sc.hasNextLine()) {
                String instruction = sc.nextLine();
                instructionVector.add(instruction);
                if (!instruction.startsWith("VAR")) {
                    numOfInstructions++;
                    String[] tokens = instruction.trim().split("\\s+");
                    if (!tokens[0].equals("HALT")) {
                        instructionTable.put(tokens[0], tokens[1]);
                        /* for (String token : tokens) */
                        /*     System.out.println(token); */
                    }
                }
                else {

                    String[] declaration = instruction.trim().split("\\s+");
                    declarationTable.put(declaration[1], Integer.parseInt(declaration[2]));
                }

            }

        }
        catch(FileNotFoundException e) {
            System.err.println("Assembler Error: " + e.getMessage());
            System.exit(1);
        }

    }


    static void instructionProcessor () {

        int opCode, operand, word;
        for (String instruction : instructionVector) {

            String tokens[] = instruction.trim().split("\\s+");
                if (!instruction.startsWith("VAR")) {


                    if (!tokens[0].equals("HALT")) {

                        opCode = OpCode.opCodeTable.get(tokens[0]);
                        operand = (Character.isLetter(tokens[1].charAt(0))) ? numOfInstructions+1 : Integer.parseInt(tokens[1]);
                        numOfInstructions++;

                    }
                    else {
                        opCode = OpCode.opCodeTable.get(tokens[0]);
                        operand = 0;
                    }


                    word = (opCode << 16) | operand;
                    machineCodeInstructionVector.add(word);
                }
                if (instruction.startsWith("VAR")) {
                    opCode = 0;
                    operand = declarationTable.get(tokens[1]);
                    word = (opCode << 16) | operand;
                    machineCodeInstructionVector.add(word);
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
