import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Micro86 {
    private static int memory[] = new int[20];
    private static String filename = null;
    private static int lenOfProgm = 0;
    private static int ip = 0;
    private static int ir = 0;
    private static int acc = 0;
    private static int flags = 0;
    private static int opcode = 0;
    private static int operand = 0;
    private static boolean dump = false;
    private static boolean trace = false;
    private static boolean isHalt = false;



    public static void main (String[] args) {

        processCommandLine(args);
        bootUp();

        while (!isHalt) {
            fetch();
            execute();
        }
        if(isHalt && dump)
            postMortemDumps();


    }

    private static void bootUp() {
        for(int i=0; i<memory.length; i++) {
            memory[i] = 0;

        }

        System.out.println("================================\n"
                + "Micro86 Emulator version 1.0\n================================\n\n");
        loader();
        printMemory();
        printDissembled();

        if(trace)
            System.out.println("===== Execution Trace =====\n\n" + "\t\t\t" + dumpRegister());
    }


    private static void printMemory() {
        String hex;
        System.out.println("-------  Memory  ------");
        for (int j=0; j<memory.length; j++) {
            hex = dumpMemory(memory[j]);
            System.out.println("0000000" + j + ": " + hex);
        }
        System.out.println("----------------------\n\n");
    }

    private static String dumpMemory(int word) {
        return  hexBeautify(Integer.toHexString(word));

    }

    private static String hexBeautify(String hex) {
        String zeros = "";
        if (hex.length() < 8) {
            for (int i=0; i<8-hex.length(); i++)
                zeros += "0";
            hex =zeros + hex.toUpperCase();
            return hex;
        }
        else {
            hex = "0" + hex;
            return hex;
        }

    }


    private static Scanner InstructionScanner() {

        try {
            Scanner sc = new Scanner(new FileReader(filename));
            return sc;

        } catch (FileNotFoundException e) {
            System.err.println("File Not Found");
            System.exit(1);
        }

        return null;

    }


    private static void loader() {
        Scanner scanner = InstructionScanner();

        try {

            while (scanner.hasNextInt(16)) {
                int instruction = scanner.nextInt(16);
                memory[lenOfProgm] = instruction;
                lenOfProgm++;
            }
            scanner.close();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }


    private static String dissembler(int word) {

        int opcode = word >>> 16;
        String operand = Integer.toHexString(word & 0x0000FFFF);

        switch (opcode) {
            case OpCode.LOAD:
                return "LOAD " + hexBeautify(operand);
            case OpCode.STORE:
                return "STORE " + hexBeautify(operand);
            case OpCode.ADD:
                return "ADD " + hexBeautify(operand);
            case OpCode.DIV:
                return "DIV " + hexBeautify(operand);
            case OpCode.DIVI:
                return "DIVI " + hexBeautify(operand);
            case OpCode.HALT:
                return "HALT";
            case 0:
                return hexBeautify(operand);
        }

        return null;

    }

    private static void printDissembled() {
        System.out.println("\n\n===== Dissembled Code =====\n\n");
        for (int i=0; i<memory.length; i++) {
            System.out.println("0000000" + i + ": " + dissembler(memory[i]));

        }
        System.out.println("\n\n===========================\n\n");
    }


    private static void fetch() {


        try {
            if (memory.length > ip) {
                ir = memory[ip];
                opcode = ir >>> 16;
                operand = ir & 0x000FFF;
                ip++;

            }
            else
                throw new ArrayIndexOutOfBoundsException("Memory array out of bounds");

        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Micro86Error: " + e.getMessage());
        }


    }

    private static void execute() {


        try {

            switch (opcode) {
                case OpCode.LOAD:
                    acc = memory[operand];
                    break;
                case OpCode.STORE:
                    memory[operand] = acc;
                    break;
                case OpCode.ADD:
                    acc = acc + memory[operand];
                    break;
                case OpCode.DIV:
                    acc = acc/memory[operand];
                    break;
                case OpCode.DIVI:
                    acc = acc/operand;
                    break;
                case OpCode.HALT:
                    isHalt = true;
                    break;
                default:
                    throw new Exception("Opcode Violation");


            }
        } catch (Exception e) {
            System.err.println("Micro86Error: " + e.getMessage());
        }
        if (trace)
            System.out.println("0000000" + ip + ": " + dumpMemory(ir) + "\t" + dumpRegister());

    }

    private static String dumpRegister() {

        return ("acc: " + hexBeautify(Integer.toHexString(acc)) + " " +
                "ip: " + hexBeautify(Integer.toHexString(ip)) + " " +
                "flags: " + hexBeautify(Integer.toHexString(flags)) + " " +
                "ir: " + "(" + hexBeautify(Integer.toHexString(ir))+ ")");
    }


    private static void postMortemDumps() {
        System.out.println("\n\n==== Post-Mortem Dump (Normal Termination) ====");
        System.out.println("--------------------");
        System.out.println(dumpRegister());
        printMemory();
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

