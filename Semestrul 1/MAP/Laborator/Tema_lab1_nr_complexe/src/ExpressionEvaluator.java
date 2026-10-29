public class ExpressionEvaluator {

    private ExpressionParser parser;
    private OperationFactory factory;

    public ExpressionEvaluator(ExpressionParser parser, OperationFactory factory){
        this.parser = parser;
        this.factory = factory;
    }

    public ComplexNumber calculate(String[] args){
        Expression expresie = parser.parse(args);
        ComplexNumber[] numere = expresie.getOperanzi();
        String[] semne = expresie.getOperatori();

        ComplexNumber rezultatCurent = numere[0];

        for(int i = 0; i < semne.length; i++) {
            Operation op = factory.createOperation(semne[i]);
            rezultatCurent = op.execute(rezultatCurent, numere[i + 1]);
        }
        return rezultatCurent;
    }
}
