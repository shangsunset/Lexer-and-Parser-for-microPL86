import java.util.*;

class Token {

    int type;
	String lexeme;

	static final Map<Integer, String> tokMap = new TreeMap<Integer, String>();
    static final Map<String, Token> keywordMap = new TreeMap<String, Token>();

    Token(int type, String lexeme) {
        this.type = type;
        this.lexeme = lexeme;
    }

    Token(int type) {this(type, null);}

    public String toString() {
        return tokMap.get(type) + (lexeme == null ? "" : " (" + lexeme + ")");
    }

    static final int 
            EOI = 0,
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
            GT_OP = 17,
            GE_OP = 18,
            LT_OP = 19,
            LE_OP = 20,
            NE_OP = 21,
            PROGRAM = 22,
            END = 23,
            INT = 24,
            IF = 25,
            WHILE = 26,
            FOR = 27,
            PRINT = 28,
            READ = 29,
            COMMENT = 30,
            ELSE = 31;

    static {
        tokMap.put(EOI, "EOI");
        tokMap.put(ID, "ID");
        tokMap.put(PLUS_OP, "PLUS_OP");
        tokMap.put(MINUS_OP, "MINUS_OP");
        tokMap.put(MUL_OP, "MUL_OP");
        tokMap.put(DIV_OP, "DIV_OP");
        tokMap.put(EQ_OP, "EQ_OP");
        tokMap.put(MOD_OP, "MOD_OP");
        tokMap.put(ASSIGN_SYMBOL, "ASSIGN_SYMBOL");
        tokMap.put(LPAREN, "LPAREN");
        tokMap.put(RPAREN, "RPAREN");
        tokMap.put(COMMA, "COMMA");
        tokMap.put(SEMICOLON, "SEMICOLON");
        tokMap.put(DOT, "DOT");
        tokMap.put(LBRACE, "LBRACE");
        tokMap.put(RBRACE, "RBRACE");
        tokMap.put(INT, "INT");
        tokMap.put(INT_LITERAL, "INT_LITERAL");
        tokMap.put(GT_OP, "GT_OP");
        tokMap.put(GE_OP, "GE_OP");
        tokMap.put(LT_OP, "LT_OP");
        tokMap.put(LE_OP, "LE_OP");
        tokMap.put(NE_OP, "NE_OP");
        tokMap.put(PROGRAM, "PROGRAM");
        tokMap.put(END, "END");
        tokMap.put(IF, "IF");
        tokMap.put(WHILE, "WHILE");
        tokMap.put(FOR, "FOR");
        tokMap.put(PRINT, "PRINT");
        tokMap.put(READ, "READ");
        tokMap.put(ELSE, "ELSE");
    }


    static {
        keywordMap.put("program", new Token(PROGRAM));
        keywordMap.put("end", new Token(END));
        keywordMap.put("int", new Token(INT));
        keywordMap.put("print", new Token(PRINT));
        keywordMap.put("while", new Token(WHILE));
        keywordMap.put("if", new Token(IF));
        keywordMap.put("for", new Token(FOR));
        keywordMap.put("read", new Token(READ));
        keywordMap.put("else", new Token(ELSE));
        
    }
}
