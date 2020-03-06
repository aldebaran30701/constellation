/*
 * Copyright 2010-2019 Australian Signals Directorate
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package au.gov.asd.tac.constellation.utilities.postfix;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author aldebaran30701
 */
public class ShuntingYard {

    private enum Operator
    {
        AND(1), OR(2);
        final int precedence;
        Operator(int p) { precedence = p; }
    }

    private static final Map<String, Operator> OPERATORS = new HashMap<String, Operator>() {{
        put("&&", Operator.AND);
        put("||", Operator.OR);
    }};

    private static boolean isHigerPrec(String op, String sub)
    {
        return (OPERATORS.containsKey(sub) && OPERATORS.get(sub).precedence >= OPERATORS.get(op).precedence);
    }

    public static String postfix(String infix)
    {
        StringBuilder output = new StringBuilder();
        Deque<String> stack  = new LinkedList<>();

        for (String token : infix.split("\\s")) {
            // operator adding to stack
            if (OPERATORS.containsKey(token)) {
                while ( ! stack.isEmpty() && isHigerPrec(token, stack.peek()))
                    output.append(stack.pop()).append(' ');
                stack.push(token);

            // left parenthesis
            } else if (token.equals("(")) {
                stack.push(token);

            // right parenthesis
            } else if (token.equals(")")) {
                while ( ! stack.peek().equals("("))
                    output.append(stack.pop()).append(' ');
                stack.pop();

            // digit writing
            } else {
                if(false){ // if (evaluateStatement(token)) { // implement this to grab attributes and check if their value meets the statement within token
                    // can append a string repr of a bool here so evaluation is done easily.
                    //output.append("True").append(' ');
                }else{
                    // output.append("False").append(' '); // this will write false when the statement does not hold true
                    output.append(token).append(' ');
                }
            }
        }

        // appending to output string
        while ( ! stack.isEmpty()){
            output.append(stack.pop()).append(' ');
        }

        return output.toString();
    }

}

