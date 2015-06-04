import java.io.*;
import java.util.Scanner;
import java.util.Vector;

class MicroPL86 {
	public static void main(String [] args) throws Exception {
		processCommandLine(args);

		if (filename == null) {
			System.out.println("Usage: MicroPL86 <filename>");
			System.exit(2);
		}

		Lexer lexer = new Lexer(new File(filename));

		while (lexer.token().type !=  Token.EOI) {
			System.out.println(lexer.token());
			lexer.next();
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
