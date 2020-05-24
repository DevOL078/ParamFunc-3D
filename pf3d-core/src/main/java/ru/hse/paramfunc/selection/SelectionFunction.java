package ru.hse.paramfunc.selection;

public class SelectionFunction implements IFunction {

    private String expression;
    private IFunction function;

    public SelectionFunction(String expression) {
        this.expression = expression;
        buildFunction();
    }

    private void buildFunction() {
        if(expression.isEmpty()) {
            throw new IllegalArgumentException("Illegal function format");
        }

        //Find +
        int index = expression.indexOf('+');
        while(index >= 0) {
            String leftOperandExpr = expression.substring(0, index);
            String rightOperandExpr = expression.substring(index + 1);
            if(checkParentheses(leftOperandExpr) &&
                    checkParentheses(rightOperandExpr)) {
                IFunction leftOperand = new SelectionFunction(leftOperandExpr);
                IFunction rightOperand = new SelectionFunction(rightOperandExpr);
                this.function = (value) -> leftOperand.calculate(value) + rightOperand.calculate(value);
                return;
            }
            index = expression.indexOf('+', index + 1);
        }

        //Find -
        index = expression.indexOf('-');
        while(index >= 0) {
            String leftOperandExpr = expression.substring(0, index);
            String rightOperandExpr = expression.substring(index + 1);
            if(checkParentheses(leftOperandExpr) &&
                    checkParentheses(rightOperandExpr)) {
                IFunction leftOperand = new SelectionFunction(leftOperandExpr);
                IFunction rightOperand = new SelectionFunction(rightOperandExpr);
                this.function = (value) -> leftOperand.calculate(value) - rightOperand.calculate(value);
                return;
            }
            index = expression.indexOf('-', index + 1);
        }

        //Find *
        index = expression.indexOf('*');
        while(index >= 0) {
            String leftOperandExpr = expression.substring(0, index);
            String rightOperandExpr = expression.substring(index + 1);
            if(checkParentheses(leftOperandExpr) &&
                    checkParentheses(rightOperandExpr)) {
                IFunction leftOperand = new SelectionFunction(leftOperandExpr);
                IFunction rightOperand = new SelectionFunction(rightOperandExpr);
                this.function = (value) -> leftOperand.calculate(value) * rightOperand.calculate(value);
                return;
            }
            index = expression.indexOf('*', index + 1);
        }

        //Find ^
        index = expression.indexOf('^');
        while(index >= 0) {
            String leftOperandExpr = expression.substring(0, index);
            String rightOperandExpr = expression.substring(index + 1);
            if(checkParentheses(leftOperandExpr) &&
                    checkParentheses(rightOperandExpr)) {
                IFunction leftOperand = new SelectionFunction(leftOperandExpr);
                IFunction rightOperand = new SelectionFunction(rightOperandExpr);
                this.function = (value) -> (int)Math.pow(leftOperand.calculate(value), rightOperand.calculate(value));
                return;
            }
            index = expression.indexOf('^', index + 1);
        }

        // Find logA(), A - log base
        if (expression.startsWith("log")) {
            int openParenthesesIndex = expression.indexOf("(");
            int closeParenthesesIndex = expression.length() - 1;
            String logBase = expression.substring(3, openParenthesesIndex);
            String operandExpr = expression.substring(openParenthesesIndex + 1, closeParenthesesIndex);
            IFunction operand = new SelectionFunction(operandExpr);
            this.function = (value) -> (int)(Math.log(operand.calculate(value)) / Math.log(Double.parseDouble(logBase)));
            return;
        }

        //Find ()
        if(expression.charAt(0) == '(' && expression.charAt(expression.length() - 1) == ')') {
            String expr = expression.substring(1, expression.length() - 1);
            this.function = new SelectionFunction(expr);
            return;
        }

        //Find variable
        if(expression.equals("n")) {
            this.function = (value) -> value;
            return;
        }

        //Find number
        try{
            int number = Integer.parseInt(expression);
            this.function = (value) -> number;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Illegal function format: " + expression);
        }
    }

    private boolean checkParentheses(String expr) {
        int openPar = 0;
        int closePar = 0;
        for(int i = 0; i < expr.length(); ++i) {
            char symbol = expr.charAt(i);
            if(symbol == '(') {
                openPar++;
            }
            if(symbol == ')') {
                closePar++;
            }
        }
        return openPar == closePar;
    }

    @Override
    public int calculate(int value) {
        return this.function.calculate(value);
    }
}
