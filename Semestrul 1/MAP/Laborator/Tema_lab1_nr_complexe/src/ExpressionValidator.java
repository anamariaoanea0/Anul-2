public class ExpressionValidator {

    public void validate(String[] args){
        if(args.length < 3){
            throw new IllegalArgumentException("Expresia trebuie sa contina cel putin doua numere complexe si un operator.");
        }

        if(args.length % 2 ==0){
            throw new IllegalArgumentException("Expresia trebuie sa aiba numar impar de argumente ordonate astfel: nr operator nr...");
        }

        for(int i = 1; i < args.length; i += 2){
            String op = args[i];
            if(!op.equals("+") && !op.equals("-") && !op.equals("*") && !op.equals("/")){
                throw new IllegalArgumentException("Operator invalid la pozitia " + i + ": " + op);
            }
        }
    }
}
