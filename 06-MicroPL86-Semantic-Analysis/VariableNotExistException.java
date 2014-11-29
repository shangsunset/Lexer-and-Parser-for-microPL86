class VariableNotExistException extends Exception {
    VariableNotExistException(Lexer lexer, String message) {
        super("At line " + lexer.line + " col " + lexer.col + ": " + message);
    }

}
