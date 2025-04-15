import javax.swing.*;
import java.awt.*;
import java.util.Stack;
public class Calculadora {
    
    public static String operador;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->createWindow());
    }
    private static void createWindow(){
        JFrame f = new JFrame("Calculadora");
        f.getContentPane().setBackground(Color.DARK_GRAY);
        JTextArea t = new JTextArea("0");
        t.setPreferredSize(new Dimension(450, 100));
        t.setFont(new Font("Arial", Font.PLAIN, 48)); 
        t.setBackground(new Color(30, 30, 30)); 
        t.setForeground(Color.WHITE);
        t.setEditable(false);
        t.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2)); 
 
        f.add(t, BorderLayout.NORTH);
        JPanel p = new JPanel(new GridLayout(6, 4)); 
        
        createButton(p, t, "mod");
        createButton(p, t, "(");
        createButton(p, t, ")");
        createButton(p, t, "%");
        createButton(p, t, "x²");
        createButton(p, t,"CE");
        createButton(p, t, "C");
        createButton(p, t, "/");
        createButton(p, t, "7");
        createButton(p, t, "8");
        createButton(p, t, "9");
        createButton(p, t, "*");
        createButton(p, t, "4");
        createButton(p, t,"5");
        createButton(p, t,"6");
        createButton(p, t,"-");
        createButton(p, t,"1");
        createButton(p, t,"2");
        createButton(p, t, "3");
        createButton(p, t,"+");
        createButton(p, t,"+/-");
        createButton(p, t, "0");
        createButton(p, t,".");
        createButton(p, t,"=");
        f.add(p);
        f.setSize(450, 600);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        f.setVisible(true);
    }
    private static void createButton(JPanel panel, JTextArea display, String bName){
        JButton b = new JButton(bName);
        b.addActionListener(e -> {
            if (bName.equals("=")) {
                String a = display.getText();
                String posfixa = converterParaNotacaoPosFixa(a);
                float resultado = calcular(posfixa);
                display.setText(String.valueOf(resultado)); 
                if (Float.isNaN(resultado))
                    display.setText("SyntaxError");
            } else if(bName.equals("CE")){
                display.setText("0");
            } else if(bName.equals("C")){
                String operacaoAtual = display.getText();
                if(operacaoAtual.length() > 1)
                    display.setText(operacaoAtual.substring(0, operacaoAtual.length()-1));
                else
                    display.setText("0");
            } else if (bName.equals("x²")) {
                        float value = Float.parseFloat(display.getText());
                        display.setText(String.valueOf(value * value)); 
            } else if(bName.equals("mod")){
                display.setText(display.getText() + "%");
            }
            else {
                if (display.getText().equals("0")) {
                    display.setText(bName);
                } else {
                    display.setText(display.getText() + bName);
                }
            }
        });

        Color customColor = new Color(71,71,71);
        b.setBackground(customColor);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 18));
        b.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 4, true));
        panel.add(b);
    }
    public static float calcular(String expression) {
        Stack<Float> stack = new Stack<>();
        String[] tokens = expression.split(" ");

        for (String token : tokens) {
            if (token.matches("-?\\d+(\\.\\d+)?")) { 
                stack.push(Float.parseFloat(token));
            } else {
                if (stack.size() < 2) return Float.NaN; 
                float b = stack.pop();
                float a = stack.pop();
                switch (token) {
                    case "+": stack.push(a + b); break;
                    case "-": stack.push(a - b); break;
                    case "*": stack.push(a * b); break;
                    case "/": stack.push(b != 0 ? a / b : Float.NaN); break;
                    case "%": stack.push(a % b); break;
                }
            }
        }
        return stack.isEmpty() ? Float.NaN : stack.pop();
    }

    public static int precedencia(char ch) {
        switch (ch) {
            case '+': case '-': return 1;
            case '*': case '/': return 2;
            case '^': return 3;
            default: return -1;
        }
    }
    public static String converterParaNotacaoPosFixa(String expression) {
        StringBuilder result = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        String tempNumber = "";
    
        for (char ch : expression.toCharArray()) {
            if (Character.isDigit(ch) || ch == '.') { 
                tempNumber += ch;
            } else {
                if (!tempNumber.isEmpty()) {
                    result.append(tempNumber).append(" ");
                    tempNumber = "";
                }
                if (ch == '(') {
                    stack.push(ch);
                } else if (ch == ')') {
                    while (!stack.isEmpty() && stack.peek() != '(') {
                        result.append(stack.pop()).append(" ");
                    }
                    stack.pop();
                } else {
                    while (!stack.isEmpty() && precedencia(ch) <= precedencia(stack.peek())) {
                        result.append(stack.pop()).append(" ");
                    }
                    stack.push(ch);
                }
            }
        }
    
        if (!tempNumber.isEmpty()) {
            result.append(tempNumber).append(" ");
        }
    
        while (!stack.isEmpty()) {
            result.append(stack.pop()).append(" ");
        }
    
        return result.toString().trim(); 
    }
}
