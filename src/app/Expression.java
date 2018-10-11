package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";

	/**
	 * Populates the vars list with simple variables, and arrays lists with arrays
	 * in the expression. For every variable (simple or array), a SINGLE instance is
	 * created and stored, even if it appears more than once in the expression. At
	 * this time, values for all variables and all array items are set to zero -
	 * they will be loaded from a file in the loadVariableValues method.
	 * 
	 * @param expr   The expression
	 * @param vars   The variables array list - already created by the caller
	 * @param arrays The arrays array list - already created by the caller
	 */
	public static void makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
		/** COMPLETE THIS METHOD **/
		/**
		 * DO NOT create new vars and arrays - they are already created before being
		 * sent in to this method - you just need to fill them in.
		 **/
		
		expr = "2 +        3 + 5 + 6";
		
		while (expr.contains(" ")) { //Removes spaces
			expr = expr.substring(0, expr.indexOf(" ")) + expr.substring(expr.indexOf(" ") + 1, expr.length()); 
		}
		
		System.out.println(expr);
		
		vars.add(new Variable("a"));
		arrays.add(new Array("A"));
//		l: for (int i = 0; i < expr.length(); i++) { // go through the expression
//			String var = "";
//			if (Character.isAlphabetic(expr.charAt(i))) {
//				var += String.valueOf(expr.charAt(i));
//				for (int j = i + 1; j < expr.length() && Character.isAlphabetic(expr.charAt(j)); j++) {
//					var += String.valueOf(expr.charAt(j));
//					i++;
//				}
//				Variable v = new Variable(var);
//				if (!checkIfExistsV(v, vars)) {
//					vars.add(v);
//				}
//			} else if (Character.isDigit(expr.charAt(i))) {
//				var += String.valueOf(expr.charAt(i));
//				for (int j = i + 1; j < expr.length() && Character.isDigit(expr.charAt(j)); j++) {
//					var += String.valueOf(expr.charAt(j));
//					i++;
//				}
//				Variable v = new Variable(var);
//				if (!checkIfExistsV(v, vars)) {
//					vars.add(v);
//					v.value = Integer.parseInt(v.name);
//				}
//			} else {
//				continue l;
//			}
//
//		}
	}

	/*
	 * Returns true if the Variable v already exists within the ArrayList<Variable>
	 * vars
	 */
	private static boolean checkIfExistsV(Variable v, ArrayList<Variable> vars) {
		for (Variable var : vars) {
			if (var.equals(v)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Returns true if the Array a already exists within the ArrayList<Variable>
	 * arrays
	 */
	private static boolean checIfExistsA(Array A, ArrayList<Array> arrays) {
		return false;
	}

	/**
	 * Loads values for variables and arrays in the expression
	 * 
	 * @param sc Scanner for values input
	 * @throws IOException If there is a problem with the input
	 * @param vars   The variables array list, previously populated by
	 *               makeVariableLists
	 * @param arrays The arrays array list - previously populated by
	 *               makeVariableLists
	 */
	public static void loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays)
			throws IOException {
		while (sc.hasNextLine()) {
			StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
			int numTokens = st.countTokens();
			String tok = st.nextToken();
			Variable var = new Variable(tok);
			Array arr = new Array(tok);
			int vari = vars.indexOf(var);
			int arri = arrays.indexOf(arr);
			if (vari == -1 && arri == -1) {
				continue;
			}
			int num = Integer.parseInt(st.nextToken());
			if (numTokens == 2) { // scalar symbol
				vars.get(vari).value = num;
			} else { // array symbol
				arr = arrays.get(arri);
				arr.values = new int[num];
				// following are (index,val) pairs
				while (st.hasMoreTokens()) {
					tok = st.nextToken();
					StringTokenizer stt = new StringTokenizer(tok, " (,)");
					int index = Integer.parseInt(stt.nextToken());
					int val = Integer.parseInt(stt.nextToken());
					arr.values[index] = val;
				}
			}
		}
	}

	/**
	 * Evaluates the expression.
	 * 
	 * @param vars   The variables array list, with values for all variables in the
	 *               expression
	 * @param arrays The arrays array list, with values for all array items
	 * @return Result of evaluation
	 */
	public static float evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
		/** COMPLETE THIS METHOD **/
		// following line just a placeholder for compilation
		String[] a = { "45", "+", "a" };
		String test = "45+a*A[2]";
		System.out.println(substituteVariables(test, vars, arrays));

		Stack<Integer> numbers = new Stack<>();
		Stack<Character> ops = new Stack<>();

		for (String s : a) {
			if (Character.isDigit(s.charAt(0))) {
				numbers.push(Integer.parseInt(s));
			} else if (Character.isAlphabetic(s.charAt(0))) {
				numbers.push(getVariable(s, vars).value);
			} else {
				if (s.equals("+")) {

				} else if (s.equals("-")) {

				} else if (s.equals("*")) {

				} else if (s.equals("/")) {

				}
			}
		}

		System.out.println(vars);
		System.out.println(arrays);

		return 0; // should never return 0 when the variable exists
	}

	//Replaces all variables with their respective values
	private static String substituteVariables(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
		String subExpr = "";
		for (int i = 0; i < expr.length(); i++) {
			if (Character.isAlphabetic(expr.charAt(i))) {
				String varName = String.valueOf(expr.charAt(i));
				for (int j = i + 1; j < expr.length() && Character.isAlphabetic(expr.charAt(j)); j++) {
					varName += String.valueOf(expr.equals(j));
					i++;
				}
				if (getVariable(varName, vars) != null) {
					subExpr += String.valueOf(getVariable(varName, vars).value);
				} else if (getArray(varName, arrays) != null) {
					subExpr += varName; //For now, just adds the name of array into expression
				}

			} else {
				subExpr += String.valueOf(expr.charAt(i));
			}
		}
		return subExpr;
	}

	private static String evaluateArrays(String expr, ArrayList<Array> arrays) {
		Stack<Integer> numbers = new Stack<>();
		Stack<String> ops = new Stack<>();
		
		if (expr.contains("[")) {
			evaluateArrays(expr.substring(expr.indexOf("[") + 1, expr.lastIndexOf("]")));
		} else {
			
		}
		
		return "";
	}

	// Solely numbers, parentheses, and operators
//	private static int evalauteExpression(String expr) {
//		Stack<Integer> numbers = new Stack<>();
//		Stack<Character> ops = new Stack<>();
//		if (expr.contains("(")) {
//
//		} else {
//			if (expr.contains("+")) {
//				
//			} else if (expr.contains("-")) {
//
//			} else if (expr.contains("*")) {
//
//			} else if (expr.contains("/")) {
//
//			} 
//		}
//		return Integer.parseInt(expr);
//	}

	private static Variable getVariable(String varName, ArrayList<Variable> vars) {
		for (Variable v : vars) {
			if (v.name.equals(varName)) {
				return v;
			}
		}
		return null; //Technically will never return null unless my code sucks
	}
	
	private static Array getArray(String arrayName, ArrayList<Array> arrays) {
		for (Array a : arrays) {
			if (a.name.equals(arrayName)) {
				return a;
			}
		}
		return null; //Technically will never return null unless my code sucks
	}
}
