public class Main{
    public static void main(String[] args){

        if (args.length == 0) {
            System.out.println("Introduceti o expresie matematica!");
            return;
        }

        try {

            ExpressionValidator validator = new ExpressionValidator();
            validator.validate(args);

            ExpressionParser parser = new CommandLineParser();
            OperationFactory factory = new OperationFactory();
            ExpressionEvaluator evaluator = new ExpressionEvaluator(parser, factory);

            ComplexNumber rezultatFinal = evaluator.calculate(args);

            System.out.println("Rezultat final: " + rezultatFinal.toString());

        } catch (IllegalArgumentException e) {
            System.out.println("Eroare de validare a expresiei: " + e.getMessage());
        } catch (ArithmeticException e) {
            System.out.println("Eroare matematica: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("A aparut o eroare la parsare sau calcul: " + e.getMessage());
        }
    }
}