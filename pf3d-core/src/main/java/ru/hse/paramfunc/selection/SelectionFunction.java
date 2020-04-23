package ru.hse.paramfunc.selection;

public class SelectionFunction implements Function {

    private String expression;
    private Function function;

    public SelectionFunction(String expression) {
        this.expression = expression;
        buildFunction();
    }

    private void buildFunction() {
        if(expression.isEmpty()) {
            throw new IllegalArgumentException("Illegal format of function");
        }

        //Find +
        int index = expression.indexOf('+');
        while(index >= 0) {
            String leftOperandExpr = expression.substring(0, index);
            String rightOperandExpr = expression.substring(index + 1);
            if(checkParentheses(leftOperandExpr) &&
                    checkParentheses(rightOperandExpr)) {
                Function leftOperand = new SelectionFunction(leftOperandExpr);
                Function rightOperand = new SelectionFunction(rightOperandExpr);
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
                Function leftOperand = new SelectionFunction(leftOperandExpr);
                Function rightOperand = new SelectionFunction(rightOperandExpr);
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
                Function leftOperand = new SelectionFunction(leftOperandExpr);
                Function rightOperand = new SelectionFunction(rightOperandExpr);
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
                Function leftOperand = new SelectionFunction(leftOperandExpr);
                Function rightOperand = new SelectionFunction(rightOperandExpr);
                this.function = (value) -> (int)Math.pow(leftOperand.calculate(value), rightOperand.calculate(value));
                return;
            }
            index = expression.indexOf('^', index + 1);
        }

        //Find ()
        if(expression.charAt(0) == '(' && expression.charAt(expression.length() - 1) == ')') {
            String expr = expression.substring(1, expression.length() - 1);
            Function operand = new SelectionFunction(expr);
            this.function = operand::calculate;
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
            throw new IllegalArgumentException("Неверный формат функции: " + expression);
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
