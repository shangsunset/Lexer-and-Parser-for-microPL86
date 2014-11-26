class ParserException extends Exception {
	ParserException(Lexer lexer, String message) {
        super("At line " + lexer.line + " col " + lexer.col + ": " + message);
    }
}
