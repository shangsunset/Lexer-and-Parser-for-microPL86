import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Assembler {

    private static String filename = null;
    private static boolean m86 = false;
    private static boolean cpp = false;
    private static int numOfInstructions = 0;
    private static Vector<String> instructionVector = new Vector<String>();
    private static Vector<Integer> machineCodeInstructionVector = new Vector<Integer>();
    private static Map<String, Integer> declarationTable = new HashMap<String, Integer>();
    private static Map<String, Integer> variableAddressTable = new HashMap<String, Integer>();
    private static Map<String, Integer> labelAddressTable = new HashMap<String, Integer>();


    public static void main(String[] args) {

        processCommandLine(args);
        instructionReader();
//        for (String line:instructionVector)
//            System.out.println(line);
//        for (String i:variableAddressTable.keySet())
//            System.out.println(i + ":" + variableAddressTable.get(i));
//        for (String l:labelAddressTable.keySet())
//            System.out.println(l + ":" + labelAddressTable.get(l));
        instructionProcessor();
        for (int word : machineCodeInstructionVector)
            System.out.println(hexBeautify(Integer.toHexString(word)));
    }


    static void instructionReader() {

        try {
            Scanner sc = new Scanner(new FileReader(filename));
            while (sc.hasNextLine()) {
                String instruction = sc.nextLine().trim();
                String[] symbols = instruction.trim().split("\\s+");

                if (!instruction.startsWith("VAR") && !instruction.isEmpty() && instruction.charAt(0) != ';') {

                    instructionVector.add(instruction);
                    numOfInstructions++;
                    if (instruction.charAt(0) == ':') {
                        String[] labelsChar = symbols[0].split(":");
                        if (symbols.length > 1) {
                            labelAddressTable.put(labelsChar[1], numOfInstructions - 1);
                        }
                        else {
                            labelAddressTable.put(labelsChar[1], numOfInstructions);
                            numOfInstructions--;

                        }

                    }
                }
                if (instruction.startsWith("VAR")){
                    instructionVector.add(instruction);
                    numOfInstructions++;
                    declarationTable.put(symbols[1], Integer.parseInt(symbols[2]));
                    variableAddressTable.put(symbols[1], numOfInstructions-1);

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
                if (!instruction.startsWith("VAR") && !instruction.trim().isEmpty() && instruction.charAt(0) != ';') {


                    if (!tokens[0].equals("HALT") && !tokens[0].equals("IN") && !tokens[0].equals("OUT")){

                        if (instruction.charAt(0) == 'J') {
                            opCode = OpCode.opCodeTable.get(tokens[0]);
                            operand = (Character.isLetter(tokens[1].charAt(0))) ? labelAddressTable.get(tokens[1]) : Integer.parseInt(tokens[1]);


                        }
                        else if (instruction.charAt(0) == ':') {
                            if (tokens.length > 2) {
                                opCode = OpCode.opCodeTable.get(tokens[1]);
                                operand = (Character.isLetter(tokens[2].charAt(0))) ? variableAddressTable.get(tokens[2]) : Integer.parseInt(tokens[2]);
                            }
                            else if (tokens.length > 1 && tokens.length < 3) {
                                opCode = OpCode.opCodeTable.get(tokens[1]);
                                operand = 0;

                            }
                            else continue;

                        }
                        else {
                            opCode = OpCode.opCodeTable.get(tokens[0]);
                            operand = (Character.isLetter(tokens[1].charAt(0))) ? variableAddressTable.get(tokens[1]) : Integer.parseInt(tokens[1]);

                        }

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



    static String hexBeautify(String hex) {
        String zeros = "";
        if (hex.length() < 8) {
            for (int i=0; i<8-hex.length(); i++)
                zeros += "0";
            hex =zeros + hex.toUpperCase();
            return hex;
        }
        else if (!hex.startsWith("0"))
            return hex;
        else {
            hex = "0" + hex;
            return hex;
        }

    }

    static void processCommandLine(String [] args) {
        boolean sawAnError = false;

        for (String arg : args) {
            if (arg.startsWith("-")) {
                if (arg.substring(1).equals("m86"))
                    m86 = true;


                else if (arg.substring(1).equals("c++")) {
                    cpp = true;

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
