import java.io.*;
import java.util.Scanner;
import java.util.Vector;

class MicroPL86 {
	public static void main(String [] args) throws Exception {
        long startTime = System.nanoTime();
		processCommandLine(args);

		if (filename == null) {
			System.out.println("Usage: MicroPL86 <filename>");
			System.exit(2);
		}

		try {
			new Parser(new Lexer(new File(filename))).parse();
            long stopTime = System.nanoTime();
            long elapsedTime = stopTime - startTime;
			System.out.println("*** Success ***");
            System.out.println("*** Finished in " + elapsedTime/1000000 + " milliseconds. ***");
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (ParserException e) {
			System.err.println(e.getMessage());
		}
	}

	static void processCommandLine(String [] args) {
		for (int i = 0; i < args.length; i++)
			if (args[i].startsWith("-")) {
			}
			else
				filename = args[i];
	}

	static String filename = null;
}
