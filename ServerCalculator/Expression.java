import java.text.ParseException;
import java.util.Scanner;

/**
 * Recursive descent parser/evaluator for mathematical expressions.
 * The expression needs to follow the grammar
 * <expr>   ::= <term> 
 *            | <term> + <expr> 
 *            | <term> - <expr>
 * <term>   ::= <factor> 
 *            | <factor> * <term> 
 *            | <factor> / <term>
 * <factor> ::= ( <expr> ) 
 *            | <value>
 *            | -<value>
 * <value>  ::= <posetive double>
 * @author holger
 *
 */
public class Expression {
	Scanner scanner;
	private StringBuilder soFar = new StringBuilder();

	/**
	 * Parses and calculates a mathematical expression and returns the result.
	 * Throws ParseException if the parsing failed.
	 * @param expressionString
	 * @return
	 * @throws ParseException
	 */
	public static double calc(String expressionString) throws ParseException {
		expressionString = padWithSpace(expressionString);
		Expression expression = new Expression(new Scanner(expressionString));

		double result = expression.readExpr();
		return result;
		// System.out.println("Parsed line: " + p.soFar);
		// System.out.println(result);

	}

	/**
	 * Pad a mathematical expression with spaces.
	 * Eg "(1+2)*138/5" -> " ( 1 + 2 ) * 138 / 5"
	 * @param expr
	 */
	private static String padWithSpace(String expr) {
		expr = expr.replaceAll("\\+", " + ");
		expr = expr.replaceAll("-", " - ");
		expr = expr.replaceAll("\\*", " * ");
		expr = expr.replaceAll("/", " / ");
		expr = expr.replaceAll("\\)", " ) ");
		expr = expr.replaceAll("\\(", " ( ");
		return expr;
	}

	/**
	 * Construct a Expression from a Scanner
	 * @param scanner
	 */
	private Expression(Scanner scanner) {
		this.scanner = scanner;
	}
	/**
	 * Pop the next positive or negative double and return it.
	 * @return
	 * @throws ParseException
	 */
	private double popValue() throws ParseException {
		if (peekToken('-')) {
			popToken();
			return popNegativeValue();
		}
		String next = popToken();
		try {
			return Double.parseDouble(next);
		} catch (NumberFormatException e) {
			throw new ParseException(next + " is not a number", 0);
		}
	}

	/**
	 * Pop the next negative double and return it.
	 * @return
	 * @throws ParseException
	 */
	private double popNegativeValue() throws ParseException {
		String next = popToken();
		try {
			return -1 * Double.parseDouble(next);
		} catch (NumberFormatException e) {
			throw new ParseException(next + " is not a number", 0);
		}
	}

	/**
	 * Pop and return the next token.
	 * @return
	 */
	private String popToken() {
		String next = scanner.next();
		soFar.append(" " + next);
		return next;
	}

	/**
	 * Check if the next token is equal to s.
	 * @param s
	 * @return
	 */
	private boolean peekToken(char s) {
		if (scanner.hasNext()) {
			if (scanner.hasNext("\\" + s)) // if s = '*' then must prefix "\\*"
				return true;
		}
		return false;
	}

	/**
	 * Read and evaluate an expression. Return the result.
	 * expr ::= term | term + expr | term - expr
	 * @return
	 * @throws ParseException
	 */
	private double readExpr() throws ParseException {
		double x = readTerm(); // expr ::= term ...

		if (peekToken('+')) { // expr ::= term + expr
			popToken();
			x += readExpr();
		}
		if (peekToken('-')) {
			x += readExpr();
		}

		return x;
	}

	/**
	 * Read and evaluate a term. Return the result.
	 * term ::= factor | factor * term | factor / term
	 * @return
	 * @throws ParseException
	 */
	private double readTerm() throws ParseException {
		double x = readFactor(); // term ::= factor

		if (peekToken('*')) { // term ::= factor * term
			popToken();
			return x * readTerm();
		}
		if (peekToken('/')) {
			popToken();
			return x / readTerm();
		}
		return x;
	}

	/**
	 * Read and evaluate a factor. Return the result.
	 * factor ::= ( expr ) | value
	 * @return
	 * @throws ParseException
	 */
	private double readFactor() throws ParseException {
		if (peekToken('(')) {
			popToken(); // '('
			double x = readExpr();
			if (peekToken(')')) {
				popToken();
			} else {
				String s = "EOF";
				if (scanner.hasNext()) {
					s = popToken();
				}
				throw new ParseException(s, 0);
			}
			return x;
		} else if (peekToken('-')) {
			popToken();
			if (peekToken('(')) {
				return -readFactor();
			} else {
				return popNegativeValue();
			}
		} else {
			return readVal();
		}
	}

	/**
	 * Read a double value and return it.
	 * @return
	 * @throws ParseException
	 */
	private double readVal() throws ParseException {
		if (!scanner.hasNext()) {
			throw new ParseException("Expected a value", 0);
		}
		return popValue();
	}
}
