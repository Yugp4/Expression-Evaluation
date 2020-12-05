package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

    public static String delims = " \t*+-/()[]";
            
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
        /** COMPLETE THIS METHOD **/
        /** DO NOT create new vars and arrays - they are already created before being sent in
         ** to this method - you just need to fill them in.
         **/
        String newExpr = expr.replaceAll(" ","");
        StringTokenizer st = new StringTokenizer(newExpr, delims, true);
      int index = 0;
        while(st.hasMoreTokens()) {
            String s = st.nextToken();
            if(s.equals("[")||s.equals("*")||s.equals("-")|| s.equals("/")|| s.equals("+")||s.equals("(")||s.equals(")")||s.equals("]")) {
                continue;
            }
            if(Character.isDigit(s.charAt(0))){
                continue;
            }
         if(newExpr.indexOf(s, index) + s.length() < newExpr.length()){
            if(newExpr.charAt(newExpr.indexOf(s, index) + s.length()) == '[') {
            	Array a = new Array(s);
            	if(arrays.contains(a)) {
            		continue;
            	}else {
                arrays.add(new Array(s));
                index = newExpr.indexOf(s, index)+s.length();
                continue;
            	}
            }
         }
         	Variable v = new Variable(s);
         	if(vars.contains(v)) {
         		continue;
         	}else {
            vars.add(new Variable(s));
            index = newExpr.indexOf(s, index)+s.length();
         	}
        }
    }
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
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
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
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
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
        /** COMPLETE THIS METHOD **/
        // following line just a placeholder for compilation
    	String newExpr = expr.replaceAll(" ", "");
        Stack<String> operators = new Stack<String>();
        Stack<Float> numbers = new Stack<Float>();
        StringTokenizer st = new StringTokenizer(newExpr, delims, true);
        ArrayList<String> tokens = new ArrayList<String>();
        while(st.hasMoreTokens()) {
        	tokens.add(st.nextToken());
        }
        for(int i = 0; i < tokens.size(); i++) {
        	String s = tokens.get(i);
        	if(Character.isDigit(s.charAt(0))) {
        		numbers.push(Float.parseFloat(s));
                if(!(operators.isEmpty())) {
                    if(operators.peek().equals("*") || operators.peek().equals("/")) {
                        float v2 = numbers.pop();
                        float v1 = numbers.pop();
                        String op = operators.pop();
                        if(op.equals("*")) {
                            numbers.push(v1*v2);
                        }else {
                            numbers.push(v1/v2);
                        }
                    }
                }
        	}else if(s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/")) {
        		operators.push(s);
        	}else if(s.equals("(")) {
        		int beginP = 1;
        		int endP = 0;
        		int recurseBegin = i;
        		int recurseEnd = 0;
        		while(beginP != endP) {
        			i++;
        			s = tokens.get(i);
        			if(s.equals("(")) {
        				beginP++;
        			}
        			if(s.equals(")")) {
        				recurseEnd = i;
        				endP++;
        			}
        		}
        		String toBeRecursed = null;
        		for(int b = recurseBegin + 1; b <= recurseEnd; b++) {
        			if(toBeRecursed == null) {
        				toBeRecursed = tokens.get(b);
        			}else {
        				toBeRecursed = toBeRecursed.concat(tokens.get(b));
        			}
        		}
        		numbers.push(evaluate(toBeRecursed, vars, arrays));
        		if(!(operators.isEmpty())) {
                    if(operators.peek().equals("*") || operators.peek().equals("/")) {
                        float v2 = numbers.pop();
                        float v1 = numbers.pop();
                        String op = operators.pop();
                        if(op.equals("*")) {
                            numbers.push(v1*v2);
                        }else {
                            numbers.push(v1/v2);
                        }
                    }
                }
        		
        	}else if(s.equals(")") || s.equals("]")) {
        		continue;
        	}else if(Character.isLetter(s.charAt(0))) {
        		if(i + 1 < tokens.size() && (tokens.get(tokens.indexOf(s) + 1)).equals("[")) {
        			String name = new String(s);
        			int beginBr = 1;
        			int endBr = 0;
        			int recurseBegin = i + 1;
        			int recurseEnd = 0;
        			i++;
        			while(beginBr != endBr) {
        				i++;
        				s = tokens.get(i);
        				if(s.equals("[")) {
        					beginBr++;
        				}
        				if(s.equals("]")) {
        					recurseEnd = i;
        					endBr++;
        				}
        			}
        			Array a = new Array(name);
        			String toBeRecursedA = null;
            		for(int b = recurseBegin + 1; b <= recurseEnd; b++) {
            			if(toBeRecursedA == null) {
            				toBeRecursedA = tokens.get(b);
            			}else {
            				toBeRecursedA = toBeRecursedA.concat(tokens.get(b));
            			}
            		}
        			numbers.push((float)arrays.get(arrays.indexOf(a)).values[(int)evaluate(toBeRecursedA, vars, arrays)]);
        			if(!(operators.isEmpty())) {
                        if(operators.peek().equals("*") || operators.peek().equals("/")) {
                            float v2 = numbers.pop();
                            float v1 = numbers.pop();
                            String op = operators.pop();
                            if(op.equals("*")) {
                                numbers.push(v1*v2);
                            }else {
                                numbers.push(v1/v2);
                            }
                        }
                    }
        		}else {
        			Variable t = new Variable(s);
        			numbers.push((float)vars.get(vars.indexOf(t)).value);
        			if(!(operators.isEmpty())) {
                        if(operators.peek().equals("*") || operators.peek().equals("/")) {
                            float v2 = numbers.pop();
                            float v1 = numbers.pop();
                            String op = operators.pop();
                            if(op.equals("*")) {
                                numbers.push(v1*v2);
                            }else {
                                numbers.push(v1/v2);
                            }
                        }
        			}
        		}
        	}
        }
        Stack<String> revop = new Stack<String>();
        Stack<Float> revnum = new Stack<Float>();
        while(!(operators.isEmpty())) {
            revop.push(operators.pop());
        }
        while(!(numbers.isEmpty())) {
            revnum.push(numbers.pop());
        }
        while(!(revop.isEmpty())) {
            float v1 = revnum.pop();
            float v2 = revnum.pop();
            String op = revop.pop();
            if(op.equals("+")) {
                revnum.push(v1 + v2);
            }else if(op.equals("-")) {
                revnum.push(v1-v2);
            }else if(op.equals("*")) {
                revnum.push(v1*v2);
            }else {
                revnum.push(v1/v2);
            }
        }
        return revnum.pop();
    }
}