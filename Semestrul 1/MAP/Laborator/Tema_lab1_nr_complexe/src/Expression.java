public class Expression {

    private String[] operatori;
    private ComplexNumber[] operanzi;

    public Expression(String[] operatori, ComplexNumber[] operanzi){
        this.operatori = operatori;
        this.operanzi = operanzi;
    }

    public String[] getOperatori(){
        return operatori;
    }

    public ComplexNumber[] getOperanzi(){
        return operanzi;
    }
}
