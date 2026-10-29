public class OperationFactory {

    public Operation createOperation(String operator){
        if(operator.equals("+")){
            return new AddOperation();
        }
        else if(operator.equals("-")){
            return new SubtractOperation();
        }
        else if(operator.equals("*")){
            return new MultiplyOperation();
        }
        else if(operator.equals("/")){
            return new DivideOperation();
        }
        else{
            throw new IllegalArgumentException("Operator invalid: " + operator);
        }
    }
}
