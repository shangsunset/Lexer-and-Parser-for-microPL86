import java.util.*;

class OpCode {
    static Map<String, Integer> opCodeTable = new TreeMap<String, Integer>();
    static Map<Integer, String> mnemonicTable = new TreeMap<Integer, String>();

    static final int
            HALT  = 	0x0100,
            LOAD  = 	0x0202,
            LOADI = 	0x0201,
            STORE = 	0x0302,
            ADD   = 	0x0402,
            ADDI  = 	0x0401,
            SUB   = 	0x0502,
            SUBI  = 	0x0501,
            MUL   = 	0x0602,
            MULI  = 	0x0601,
            DIV   = 	0x0702,
            DIVI  = 	0x0701,
            MOD   = 	0x0802,
            MODI  = 	0x0801,
            CMP   = 	0x0902,
            CMPI  = 	0x0901,
            JMPI  = 	0x0A01,
            JEI   = 	0x0B01,
            JNEI  = 	0x0C01,
            JLI   = 	0x0D01,
            JLEI  = 	0x0E01,
            JGI   = 	0x0F01,
            JGEI  = 	0x1001,
            IN    =	    0x1100,
            OUT   = 	0x1200;

    static {
        opCodeTable.put("HALT", HALT);
        opCodeTable.put("LOAD", LOAD);
        opCodeTable.put("LOADI",LOADI);
        opCodeTable.put("STORE",STORE);
        opCodeTable.put("ADD",	ADD);
        opCodeTable.put("ADDI",	ADDI);
        opCodeTable.put("SUB",	SUB);
        opCodeTable.put("SUBI",	SUBI);
        opCodeTable.put("MUL",	MUL);
        opCodeTable.put("MULI",	MULI);
        opCodeTable.put("DIV",	DIV);
        opCodeTable.put("DIVI",	DIVI);
        opCodeTable.put("MOD",	MOD);
        opCodeTable.put("MODI",	MODI);
        opCodeTable.put("CMP",	CMP);
        opCodeTable.put("CMPI",	CMPI);
        opCodeTable.put("JMPI",	JMPI);
        opCodeTable.put("JEI",	JEI);
        opCodeTable.put("JNEI",	JNEI);
        opCodeTable.put("JLI",	JLI);
        opCodeTable.put("JLEI",	JLEI);
        opCodeTable.put("JGI",	JGI);
        opCodeTable.put("JGEI",	JGEI);
        opCodeTable.put("IN",	IN);
        opCodeTable.put("OUT",	OUT);

        for (String mnemonic : opCodeTable.keySet())
            mnemonicTable.put(opCodeTable.get(mnemonic), mnemonic);
    }

}