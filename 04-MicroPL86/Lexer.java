import java.io.*;

class Lexer {
    InputStream is;
	char currChar;
	Token currentToken = null;
	int line = 1, col = 1;

    Lexer(File file) throws IOException {
        is = new FileInputStream(file);
        advance();
        next();
    }


    Lexer(String s) throws IOException {
		is = new ByteArrayInputStream(s.getBytes("UTF-8"));
        advance();
    }

    void advance() throws IOException {
        int i = is.read();
        currChar = (i < 0) ? '\0' : (char)i;
        if (currChar == '\n') {
            line ++;
            col = 0;
        }
        col++;
    }

    public Token token() {return currentToken;}

    static final int 
            EOI = -2,
            ERROR = -1,
            START = 0,
            ID = 1,
            PLUS_OP = 2,
            MINUS_OP = 3,
            MUL_OP = 4,
            DIV_OP = 5,
            EQ_OP = 6,
            MOD_OP = 7,
            ASSIGN_SYMBOL = 8,
            LPAREN = 9,
            RPAREN = 10,
            COMMA = 11,
            SEMICOLON = 12,
            DOT = 13,
            LBRACE = 14,
            RBRACE = 15,
            INT_LITERAL = 16,
            PROGRAM = 17,
            END = 18,
            INT = 19,
            IF = 20,
            WHILE = 21,
            FOR = 22,
            PRINT = 23,
            READ = 24,
            COMMENT = 25,
            GT_OR_GE = 26,
            LT_OR_LE = 27,
            EQ_OR_ASSIGN = 28;



    public Token next() throws IOException {
        currentToken = null;
        String lexeme = null;
        int state = START;


        while (currentToken == null) {
            boolean shouldAdvance = true;
            switch(state) {
                case START:
                    lexeme = "";
                    if (Character.isWhitespace(currChar)) state = START;
                    else if (currChar == '#') state = COMMENT;
                    else if (Character.isLetter(currChar)) state = ID;
                    else if (Character.isDigit(currChar)) state = INT_LITERAL;
                    else if (currChar == '>') state = GT_OR_GE;
                    else if (currChar == '<') state = LT_OR_LE;
                    else if (currChar == '=') state = EQ_OR_ASSIGN;
                    else if (currChar == '+') currentToken = new Token(Token.PLUS_OP);
                    else if (currChar == '-') currentToken = new Token(Token.MINUS_OP);
                    else if (currChar == '/') currentToken = new Token(Token.DIV_OP);
                    else if (currChar == '%') currentToken = new Token(Token.MOD_OP);
                    else if (currChar == ')') currentToken = new Token(Token.RPAREN);
                    else if (currChar == '(') currentToken = new Token(Token.LPAREN);
                    else if (currChar == '{') currentToken = new Token(Token.LBRACE);
                    else if (currChar == '}') currentToken = new Token(Token.RBRACE);
                    else if (currChar == ',') currentToken = new Token(Token.COMMA);
                    else if (currChar == ';') currentToken = new Token(Token.SEMICOLON);
                    else if (currChar == '.') currentToken = new Token(Token.DOT);
                    else if (currChar == '\0') currentToken = new Token(Token.EOI);
                    else state = ERROR;
                    break;


                case COMMENT:
                    if (currChar == '\n') {
                        currentToken = new Token(Token.COMMENT);
                    }
                    else {
                        state = COMMENT;
                    }
                    break;

                case ID:
                    if (Character.isLetterOrDigit(currChar))
						state = ID;
					else {
                        if (Token.keywordMap.get(lexeme) != null) {
							currentToken = Token.keywordMap.get(lexeme);
                        }
						else 
							currentToken = new Token(Token.ID, lexeme);
						shouldAdvance = false;
					}
					break;


                case INT_LITERAL:
                    if (Character.isDigit(currChar)) {
                        state = INT_LITERAL;
                    }
                    else {
                        currentToken = new Token(Token.INT_LITERAL, lexeme);
                    } 
                    break;


                case EQ_OR_ASSIGN:
                    if (currChar == '=') {
                        currentToken = new Token(Token.EQ_OP);
                    }
                    else {
                        currentToken = new Token(Token.ASSIGN_SYMBOL);
                    }
                    break;

                case GT_OR_GE:  
					if (currChar == '=') 
						currentToken = new Token(Token.GE_OP); 
					else {
						currentToken = new Token(Token.GT_OP);
						shouldAdvance = false;
					}
					break;

                case ERROR:
					System.err.println("Unexpected character " + currChar + " on line " + line + " at column " + col);
					state = START;
					break;
					
				case EOI:  currentToken = new Token(Token.EOI);
                           break;

				default:
					System.err.println("Unknown state " + state);
					System.exit(0);
            }

            lexeme += currChar;
			if (shouldAdvance && state != ERROR) advance();
        }
        return currentToken;

    }

}
