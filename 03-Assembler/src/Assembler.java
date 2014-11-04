import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

public class Assembler {

    private static String filename = null;
    private static boolean m86 = false;
    private static boolean cpp = false;
    private static int numOfInstructions = 0;
    private static Vector<String> instructionVector = new Vector<String>();
    private static Vector<String> cppCode = new Vector<String>();
    private static Vector<Integer> m86HexCodeVector = new Vector<Integer>();
    private static Map<String, Integer> declarationTable = new HashMap<String, Integer>();
    private static Map<String, Integer> variableAddressTable = new HashMap<String, Integer>();
    private static Map<String, Integer> labelAddressTable = new HashMap<String, Integer>();



    public static void main(String[] args) {

        processCommandLine(args);
        instructionReader();
        if (m86) {
            m86Generator();
            for (int word : m86HexCodeVector)
                System.out.println(hexBeautify(Integer.toHexString(word)));
        }
        if (cpp)
            try {
                PrintWriter writer = new PrintWriter(getFileName() + ".cpp");
                cppGenerator();
                cppCode.add("\n}");
                for (String s : cppCode) {
                    System.out.println(s);
                    writer.println(s);
                }
                writer.close();

            }
            catch (FileNotFoundException e) {
                System.err.println("Assembler Error: " + e.getMessage());
            }
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


    static void cppGenerator() {

        //ordering of declarations
        int index = 5;
        //to identify different inputs
        int num = 1;

        cppCode.add("#include <iostream>");
        cppCode.add("#include <cstdlib>");
        cppCode.add("using namespace std;");
        cppCode.add("int main () {\n");
        cppCode.add("\t//declarations-------------");
        cppCode.add("\tint _acc;");
        cppCode.add("\tint _result;");
        cppCode.add("\t//code---------------------");
        for (String instruction : instructionVector) {
            String symbols[] = instruction.trim().split("\\s+");
            if (instruction.charAt(0) != ':' && !instruction.startsWith("VAR")) {
                switch (OpCode.opCodeTable.get(symbols[0])) {

                    case OpCode.LOAD:
                    case OpCode.LOADI:
                        cppCode.add("\t_acc = " + symbols[1] + ";" );
                        break;
                    case OpCode.STORE:
                        cppCode.add("\t" + symbols[1] + " = _acc;");
                        break;
                    case OpCode.ADD:
                    case OpCode.ADDI:
                        cppCode.add("\t_acc += " + symbols[1] + ";");
                        break;
                    case OpCode.DIV:
                    case OpCode.DIVI:
                        cppCode.add("\t_acc = _acc/" + symbols[1] + ";");
                        break;
                    case OpCode.MULI:
                    case OpCode.MUL:
                        cppCode.add("\t_acc = _acc*" + symbols[1] + ";");
                        break;
                    case OpCode.SUB:
                    case OpCode.SUBI:
                        cppCode.add("\t_acc -= " + symbols[1] + ";");
                        break;
                    case OpCode.CMP:
                    case OpCode.CMPI:
                        cppCode.add("\t_result = _acc - " + symbols[1] + ";");
                        break;
                    case OpCode.JGEI:
                        cppCode.add("\tif (_result >= 0) {");
                        cppCode.add("\t\tgoto " + symbols[1] + ";");
                        cppCode.add("\t}");
                        break;
                    case OpCode.JGI:
                        cppCode.add("\tif (_result > 0) {");
                        cppCode.add("\t\tgoto " + symbols[1] + ";");
                        cppCode.add("\t}");
                        break;
                    case OpCode.JLEI:
                        cppCode.add("\tif (_result < 0) {");
                        cppCode.add("\t\tgoto " + symbols[1] + ";");
                        cppCode.add("\t}");
                        break;
                    case OpCode.JMPI:
                        cppCode.add("\tgoto " + symbols[1] + ";");
                        break;
                    case OpCode.IN:
                        cppCode.add("\tint input" + num + ";");
                        cppCode.add("\tcin >> input" + num + ";");
                        cppCode.add("\t_acc = input" + num + ";");
                        num++;
                        break;
                    case OpCode.HALT:
                        cppCode.add("\texit(0);");
                        break;
                }
            }

            if (instruction.startsWith("VAR")) {
                cppCode.add(index, "\tint " + symbols[1] + " = " + symbols[2] + ";");
                index++;

            }
            if (instruction.charAt(0) == ':') {
                String[] parts = instruction.split(":");
                cppCode.add(parts[1] + ":");
                if (parts.length > 1) {

                    switch (OpCode.opCodeTable.get(symbols[1])) {
                        case OpCode.STORE:
                            cppCode.add("\t" + symbols[2] + " = _acc;");
                            break;
                        case OpCode.HALT:
                            cppCode.add("\texit(0);");
                            break;

                    }
                }
            }
        }
    }

    static void m86Generator() {

        int opCode, operand, word;
        for (String instruction : instructionVector) {

            String symbols[] = instruction.trim().split("\\s+");

                if (!instruction.startsWith("VAR") && !instruction.trim().isEmpty() && instruction.charAt(0) != ';') {

                    if (!symbols[0].equals("HALT") && !symbols[0].equals("IN") && !symbols[0].equals("OUT")){

                        if (instruction.charAt(0) == 'J') {
                            opCode = OpCode.opCodeTable.get(symbols[0]);
                            operand = (Character.isLetter(symbols[1].charAt(0))) ? labelAddressTable.get(symbols[1]) : Integer.parseInt(symbols[1]);
                        }
                        else if (instruction.charAt(0) == ':') {

                            if (symbols.length > 2) {
                                opCode = OpCode.opCodeTable.get(symbols[1]);
                                operand = (Character.isLetter(symbols[2].charAt(0))) ? variableAddressTable.get(symbols[2]) : Integer.parseInt(symbols[2]);
                            }
                            else if (symbols.length > 1 && symbols.length < 3) {
                                opCode = OpCode.opCodeTable.get(symbols[1]);
                                operand = 0;
                            }
                            else continue;

                        }
                        else {
                            opCode = OpCode.opCodeTable.get(symbols[0]);
                            operand = (Character.isLetter(symbols[1].charAt(0))) ? variableAddressTable.get(symbols[1]) : Integer.parseInt(symbols[1]);
                        }
                    }
                    else {
                        opCode = OpCode.opCodeTable.get(symbols[0]);
                        operand = 0;
                    }

                    word = (opCode << 16) | operand;
                    m86HexCodeVector.add(word);
                }
                if (instruction.startsWith("VAR")) {
                    opCode = 0;
                    operand = declarationTable.get(symbols[1]);
                    word = (opCode << 16) | operand;
                    m86HexCodeVector.add(word);
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

    static String getFileName() {

        String wholeName[] = filename.split("/");
        String nameWithExt = wholeName[wholeName.length-1];
        String nameParts[] = nameWithExt.split(".a86");
        return nameParts[0];

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
            System.err.println("Usage: Commander {-m86} or {-c++} <filename>");
            System.exit(1);
        }
    }
}
