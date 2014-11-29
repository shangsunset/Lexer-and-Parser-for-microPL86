import java.io.*;
import java.util.*;


class Parser {
    Lexer lexer;
    Map<String, Token> varTable = new HashMap<String, Token>();


    Parser(Lexer lexer) throws IOException, ParserException {
        this.lexer = lexer;
    }


    void parse()  throws IOException, ParserException, VariableExistedException, VariableNotExistException {
		program();
		match(Token.EOI);
	}


    void program() throws IOException, ParserException, VariableExistedException, VariableNotExistException {
        match(Token.PROGRAM);
        while (optMatch(Token.INT)) 
			declaration();
		while (!optMatch(Token.END)) 
			statement();
    }


    void declaration() throws IOException, ParserException, VariableExistedException, VariableNotExistException {
        if (varTable.get(lexer.token().lexeme) == null) {

            varTable.put(lexer.token().lexeme, lexer.token());
        }
        else {
            throw new VariableExistedException(lexer, "variable " + "\'" + lexer.token().lexeme + "\'" + " has already been declared." );
        }
        match(Token.ID);
        while (optMatch(Token.COMMA)) {
            if (varTable.get(lexer.token().lexeme) == null) {

                varTable.put(lexer.token().lexeme, lexer.token());
            }
            else {
                throw new VariableExistedException(lexer, "variable " + lexer.token().lexeme + "has already been declared." );
                
            }
            match(Token.ID);
        }
        match(Token.SEMICOLON);
    }


    void statement() throws IOException, ParserException, VariableNotExistException {
		if (optMatch(Token.ID)) 
			assignmentStatement();
		else if (optMatch(Token.IF))
			ifStatement();
        else if (optMatch(Token.READ)) {
            read(); 
        }
        else if (optMatch(Token.FOR)) {
            forStatement(); 
        }
        else if (optMatch(Token.LBRACE)) {
            compoundStatement();
        }
        else if (optMatch(Token.WHILE)) {
            whileStatement();
        }
        else if (optMatch(Token.PRINT)) {
            printStatement();
        }
		else
			throw new ParserException(lexer, "Expecting statement, found " + lexer.token());

	}


    void assignmentStatement() throws IOException, ParserException, VariableNotExistException {
		match(Token.ASSIGN_SYMBOL);
		expression();
		match(Token.SEMICOLON);
	}
    
    void read() throws IOException, ParserException, VariableNotExistException {
        match(Token.ID);
        match(Token.SEMICOLON);
    }


    void forStatement() throws IOException, ParserException, VariableNotExistException {
        match(Token.LPAREN);
        match(Token.ID);
        match(Token.ASSIGN_SYMBOL);
        expression();
        match(Token.COMMA);
        expression();
        match(Token.RPAREN);
        statement();
    }

    void compoundStatement() throws IOException, ParserException, VariableNotExistException {
        while (!optMatch(Token.RBRACE)) {
            statement();
        }
    }


    void whileStatement() throws IOException, ParserException, VariableNotExistException {
        match(Token.LPAREN);
        expression();
        matchRelop();
        expression();
        match(Token.RPAREN);
        statement();
    }


    void ifStatement() throws IOException, ParserException, VariableNotExistException {
        match(Token.LPAREN);
        expression();
        matchRelop();
        expression();
        match(Token.RPAREN);
        statement();
        if (optMatch(Token.ELSE)) {
            statement();
        }

    }


    void printStatement() throws IOException, ParserException, VariableNotExistException {
        expression();
        match(Token.SEMICOLON);
    }

    void expression() throws IOException, ParserException, VariableNotExistException {
		term();
		while (optMatch(Token.PLUS_OP) || optMatch(Token.MINUS_OP)) 
			term();
	}


    void term() throws IOException, ParserException, VariableNotExistException {
		factor();
		while (optMatch(Token.MUL_OP) || optMatch(Token.DIV_OP) || optMatch(Token.MOD_OP)) 
			factor();
	}


    void factor() throws IOException, ParserException, VariableNotExistException {
        if (optMatch(Token.ID) || optMatch(Token.INT_LITERAL)) {
			return;
        }
        else if (optMatch(Token.LPAREN)) {
            expression();
            if (!optMatch(Token.RPAREN)) {
                throw new ParserException(lexer, "Missing");
            }
        }
		else
			throw new ParserException(lexer, "Expecting factor, found " + lexer.token());
	}


    void matchRelop() throws IOException, ParserException, VariableNotExistException {
        if (!optMatch(Token.EQ_OP) && !optMatch(Token.GT_OP) &&
            !optMatch(Token.LT_OP) && !optMatch(Token.LE_OP) &&
            !optMatch(Token.GE_OP) && !optMatch(Token.NE_OP)) {
            
                throw new ParserException(lexer, "Expecting Relational, found " + lexer.token());
            }
    }

    boolean optMatch(int tokenType) throws IOException, VariableNotExistException {
		if (lexer.token().type == tokenType) {
            if (lexer.token().type == Token.ID && varTable.get(lexer.token().lexeme) == null) {
                throw new VariableNotExistException(lexer, "variable " + "\'" + lexer.token().lexeme + "\'" + " has not yet been declared.");
                
            }
			lexer.next();
			return true;
		}
		return false;
	}


	void match(int tokenType) throws IOException, ParserException, VariableNotExistException {
		if (!optMatch(tokenType))
            throw new ParserException(lexer, "Expecting '" + new Token(tokenType) + "', found " + lexer.token());
	}



}
