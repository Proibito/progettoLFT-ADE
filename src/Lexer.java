import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Lexer {

	public static int line = 1;
	private char peek = ' ';

	public static void main(String[] args) {
		Lexer lex = new Lexer();
		String path = "src/test.txt"; // il percorso del file da leggere
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			Token tok;
			do {
				tok = lex.lexical_scan(br);
				System.out.println("Scan: " + tok);
			} while (tok.tag != Tag.EOF);
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void readch(BufferedReader br) {
		try {
			peek = (char) br.read();
		} catch (IOException exc) {
			peek = (char) -1; // ERROR
		}
	}

	public Token lexical_scan(BufferedReader br) {
		while (peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r') {
			if (peek == '\n') line++;
			readch(br);
		}

		switch (peek) {
			case '!':
				peek = ' ';
				return Token.not;

			case '(':
				peek = ' ';
				return Token.lpt;


			case ')':
				peek = ' ';
				return Token.rpt;


			case '[':
				peek = ' ';
				return Token.lpq;


			case '{':
				peek = ' ';
				return Token.lpg;


			case '}':
				peek = ' ';
				return Token.rpg;


			case '+':
				peek = ' ';
				return Token.plus;


			case '-':
				peek = ' ';
				return Token.minus;


			case '*':
				peek = ' ';
				return Token.mult;


			case '/':
				peek = ' ';
				return Token.div;


			case ';':
				peek = '\n';
				return Token.semicolon;


			case ',':
				peek = ' ';
				return Token.comma;


			case '&':
				readch(br);
				if (peek == '&') {
					peek = ' ';
					return Word.and;
				} else {
					System.err.println("Erroneous character" + " after & : " + peek);
					return null;
				}

				// ... gestire i casi di || < > <= >= == <> ... //
			case '|':
				readch(br);
				if (peek == '|') {
					peek = ' ';
					return Word.or;
				} else {
					System.err.println("\n\nErroneous character after | : " + peek);
					return null;
				}
			case '<':
				readch(br);
				if (peek == ' ') {
					return Word.lt;
				} else if (peek == '=') {
					peek = ' ';
					return Word.le;
				} else if (peek == '>') {
					peek = ' ';
					return Word.ne;
				} else {
					System.err.println("\n\nErroneous character after < : " + peek);
					return null;
				}
			case '>':
				readch(br);
				if (peek == ' ') {
					return Word.gt;
				} else if (peek == '=') {
					peek = ' ';
					return Word.ge;
				} else {
					System.err.println("\n\nErroneous character after > : " + peek);
					return null;
				}
			case '=':
				readch(br);
				if (peek == '=') {
					peek = ' ';
					return Word.eq;
				} else {
					System.err.println("\n\nErroneous character after = : " + peek);
					return null;
				}

			case (char) -1:
				return new Token(Tag.EOF);

			default:
				if (Character.isLetter(peek)) {
					return toWord(br);

				} else if (Character.isDigit(peek)) {

					return toNumberToken(br);

				} else {
					System.err.println("Erroneous character: " + peek);
					return null;
				}
		}
	}

	private Token toNumberToken(BufferedReader br) {
		int val = peek - '0';
		readch(br);

		// CHECK NO 0n
		if (val == 0 && Character.isDigit(peek)) {
			System.err.println("\n\nErroneous character after 0 : " + peek);
			return null;
		}

		// OBTAIN NUMBER
		while (Character.isDigit(peek)) {
			val = val * 10 + peek - '0';
			readch(br);
		}

		// CHECK NO NUMBER-CHAR OR NUM_
		if (Character.isLetter(peek) || peek=='_') {
			System.err.println("\n\nErroneous character after number : '" + peek+"'");
			return null;
		}

		// Else don't clean peek
		return new NumberTok(val);
	}


	private Token toWord(BufferedReader br){
		String s = "";
		boolean onlySlash = true;

		do {
			s += "" + peek;
			if(peek!='_')
				onlySlash=false;
			readch(br);
		} while (Character.isLetterOrDigit(peek) || peek=='_');

		if(s.charAt(0)=='_'){
			if(onlySlash){
				System.err.println("\n\nErroneous identification (only '_') in: " +s);
				return null;
			}
		}
		// KEY WORD
		else if (s.equals("assign"))
			return Word.assign;
		else if (s.equals("to"))
			return Word.to;
		else if (s.equals("conditional"))
			return Word.conditional;
		else if (s.equals("option"))
			return Word.option;
		else if (s.equals("do"))
			return Word.dotok;
		else if (s.equals("else"))
			return Word.elsetok;
		else if (s.equals("while"))
			return Word.whiletok;
		else if (s.equals("begin"))
			return Word.begin;
		else if (s.equals("end"))
			return Word.end;
		else if (s.equals("print"))
			return Word.print;
		else if (s.equals("read"))
			return Word.read;

		// IDENTIFIER
		// don't clean peek
		return new Word(Tag.ID, s);
	}

}
