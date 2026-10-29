public class DivideOperation implements Operation {

    @Override
    public ComplexNumber execute(ComplexNumber n1, ComplexNumber n2) {
        double c = n2.getReal();
        double d = n2.getImaginary();
        double numitor = (c * c) + (d * d);

        if (numitor == 0) {
            throw new ArithmeticException("Impartire la zero!");
        }

        double a = n1.getReal();
        double b = n1.getImaginary();

        double newReal = (a * c + b * d) / numitor;
        double newImaginary = (b * c - a * d) / numitor;

        return new ComplexNumber(newReal, newImaginary);
    }
}
