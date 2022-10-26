import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Lexer {

	public static int line = 1;
	private char peek = ' ';

	public static void main(String[] args) {
		Lexer lex = new Lexer();
		String path = "...path..."; // il percorso del file da leggere
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

	/**
	 * esempio
	 *
	 * @param br
	 * @return
	 */
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

//			case '&&':
//				readch(br);
//				if (peek == '&') {
//					peek = ' ';
//					return Word.and;
//				} else {
//					System.err.println("Erroneous character" + " after & : " + peek);
//					return null;
//				}

				// ... gestire i casi di || < > <= >= == <> ... //

			case (char) -1:
				return new Token(Tag.EOF);

			default:
				if (Character.isLetter(peek)) {

					// ... gestire il caso degli identificatori e delle parole chiave //

				} else if (Character.isDigit(peek)) {

					// ... gestire il caso dei numeri ... //

				} else {
					System.err.println("Erroneous character: " + peek);
					return null;
				}
		}
		return null;
	}

}
