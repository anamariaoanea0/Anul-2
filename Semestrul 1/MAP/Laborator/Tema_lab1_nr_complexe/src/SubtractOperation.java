public class SubtractOperation implements Operation{

    @Override
    public ComplexNumber execute(ComplexNumber n1, ComplexNumber n2){

        double newReal = n1.getReal() - n2.getReal();
        double newImaginary = n1.getImaginary() - n2.getImaginary();

        return new ComplexNumber(newReal, newImaginary);
    }
}
