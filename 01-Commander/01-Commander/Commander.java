import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Commander {

    static ArrayList <Obj> variables = new ArrayList<Obj>();
    static String filename = null;
    static boolean dump = false;
    static boolean trace = false;
    static boolean override = false;

    public static void main (String[] args) {
        processCommandLine(args);
        myCompiler(filename);

    }



    static void processCommandLine(String [] args) {
        boolean sawAnError = false;

        for (String arg : args) {
            if (arg.startsWith("-")) {
                if (arg.substring(1).equals("dump"))
                    dump = true;

                else if (arg.substring(1).equals("trace")) {
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
            System.err.println("Usage: Commander {-dump} {-trace} <filename>");
            System.exit(1);
        }
    }


    private static void myCompiler(String filename) {
        try{
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String currentLine;
            while ((currentLine = br.readLine()) != null) {

                    if(currentLine.length() == 0)
                        continue;
                    if(currentLine.contains("=")) {
                        String[] parts = currentLine.split(" = ");
                            if(parts.length != 2)
                                throw new IOException("the assignment is not valid ");
                            handleInt(parts);

                    }
                    else if(currentLine.startsWith("PRINT")) {
                        printResult(currentLine);


                    }
                    else {
                        throw new IOException("your statement does not make sense");

                    }

            }

            if(dump) {
                System.out.println("\nDump\n----");
                for(Obj var : variables) {
                    System.out.println(var.getVarName() + ": " + var.getVarValue());

                }
            }


        } catch (FileNotFoundException e) {
            System.err.println("File Not Found");
            System.exit(1);


        } catch(IOException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }

    }

    private static void handleInt(String[] parts) {
        if(variables.size() >= 1) {

            for(Obj var : variables) {
                if(parts[0].equals(var.getVarName())) {
                    if(trace)
                        System.out.println(var.getVarName() + " changed from " + var.getVarValue() + " to " + parts[1]);
                    assignmentForInt(var, parts[0].trim(), Integer.parseInt(parts[1].trim()));
                    override = true;
                    break;
                }
                else
                    override = false;

            }

            if(!override) {
                Obj intObj = new Obj(0, null);
                assignmentForInt(intObj, parts[0].trim(), Integer.parseInt(parts[1].trim()));
                variables.add(intObj);

            }

        }
        else{
            Obj intObj = new Obj(0, null);
            assignmentForInt(intObj, parts[0].trim(), Integer.parseInt(parts[1].trim()));
            variables.add(intObj);

        }
    }


    private static void assignmentForInt(Obj intObj, String name, int value) {

        intObj.setVarName(name);
        intObj.setVarValue(value);

    }



    private static void printResult(String line) {
        String[] parts = line.split(" ");
        for (Obj variable : variables) {
            if(parts[1].equals(variable.getVarName())) {
                System.out.println(variable.getVarValue());


            }

        }
    }

}

class Obj {
    private int varValue;
    private String varName;


    Obj(int val, String name) {
        this.varValue = val;

        this.varName = name;
    }


    int getVarValue() {
        return varValue;
    }


    void setVarValue(int varValue) {
        this.varValue = varValue;
    }

    String getVarName() {
        return varName;
    }

    void setVarName(String varName) {
        this.varName = varName;
    }


}





