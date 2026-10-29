public class CommandLineParser implements ExpressionParser{

    @Override
    public Expression parse(String[] args) {

        int nrTotalElemente = args.length;
        int nrOperatori = nrTotalElemente / 2;
        int nrOperanzi = nrOperatori + 1;

        String[] operatori = new String[nrOperatori];
        ComplexNumber[] operanzi = new ComplexNumber[nrOperanzi];

        int indexOp = 0;
        int indexNr = 0;

        for(int i = 0; i < nrTotalElemente; i++){
            if(i % 2 == 0) {
                operanzi[indexNr] = parseComplexNumber(args[i]);
                indexNr++;
            }
            else{
                operatori[indexOp] = args[i];
                indexOp++;
            }
        }
        return new Expression(operatori, operanzi);
    }

    private ComplexNumber parseComplexNumber(String text) {
        boolean areParteaImaginara = text.contains("i");

        text = text.replace("*i", "").replace("i", "");

        int splitIndex = Math.max(text.lastIndexOf('+'), text.lastIndexOf('-'));

        if (splitIndex == 0) {
           splitIndex = -1;
        }

        if (splitIndex == -1) {
            if (areParteaImaginara) {
                if (text.isEmpty() || text.equals("+")) {
                    text = "1";
                }
                else if (text.equals("-")) {
                    text = "-1";
                }
                return new ComplexNumber(0.0, Double.parseDouble(text));
            } else {
                return new ComplexNumber(Double.parseDouble(text), 0.0);
            }
        }

        String realPart = text.substring(0, splitIndex);
        String imagPart = text.substring(splitIndex);

        if (imagPart.isEmpty() || imagPart.equals("+")) {
            imagPart = "1";
        } else if (imagPart.equals("-")) {
            imagPart = "-1";
        }

        return new ComplexNumber(Double.parseDouble(realPart), Double.parseDouble(imagPart));
    }
}


