import java.util.Scanner;
import java.util.regex.*;

public class Calculadora {
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int decision;
        while (true) {
            System.out.println("Digite a conta aritmética que deseja realizar:");
            String line = sc.nextLine();
            float resultado = identificarPadrao(line);
            System.out.printf("%s = %.5f%n\nDeseja continuar?\n\n1. Sim\n2. Não\n\nOpção: ", line, resultado);
            decision = sc.nextInt();
            sc.nextLine();
            if(decision == 1)
                continue;
            else break;
        }
        sc.close();
    }

    private static float identificarPadrao(String line) {
        Pattern p = Pattern.compile("(\\d+(\\.\\d+)?)([+\\-*/])(\\d+(\\.\\d+)?)");
        Matcher m = p.matcher(line);
        
        if (m.matches()) {
            float n1 = Float.parseFloat(m.group(1));
            char operador = m.group(3).charAt(0);
            float n2 = Float.parseFloat(m.group(4));
            return calcular(n1, n2, operador);
        } else {
            System.out.println("Expressão inválida!");
            return Float.NaN;
        }
    }

    public static float calcular(float n1, float n2, char operador) {
        switch (operador) {
            case '+': return n1 + n2;
            case '-': return n1 - n2;
            case '*': return n1 * n2;
            case '/': return (n2 != 0) ? n1 / n2 : Float.NaN; // Evita divisão por zero
            default:
                System.out.println("Operador inválido!");
                return Float.NaN;
        }
    }
}