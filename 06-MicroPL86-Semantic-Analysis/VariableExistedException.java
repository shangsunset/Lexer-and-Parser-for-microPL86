class VariableExistedException extends Exception {
	VariableExistedException(Lexer lexer, String message) {
        super("At line " + lexer.line + " col " + lexer.col + ": " + message);
    }
}
