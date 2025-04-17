import javax.swing.*;
import java.awt.*;
import java.util.Stack;
public class Calculadora {
    public static String operador;

    public static String[] buttons = {"sin","cos","tan","π",
                                      "mod", "(", ")", "%",
                                      "x²", "CE", "C", "/",
                                      "7", "8", "9", "x",
                                      "4", "5", "6", "-",
                                      "1", "2", "3", "+",
                                      "+/-","0",",","="};

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createWindow());
    }

    private static void createWindow() {
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
        JPanel p = new JPanel(new GridLayout(7, 4));
        for (String button : buttons)
            createButton(p, t, button);
        f.add(p);
        f.setSize(450, 600);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        f.setVisible(true);
    }

    private static void createButton(JPanel panel, JTextArea display, String bName) {
        JButton b = new JButton(bName);
        b.addActionListener(e -> {
            try {
                String currentText = display.getText();

                if (bName.equals("=")) {
                    if (!currentText.matches("[0-9+\\-*/().,x²sincostanmodπ%]+")) {
                        display.setText("Error: Unsupported Character");
                        return;
                    }
                    String posfixa = converterParaNotacaoPosFixa(currentText);
                    float resultado = calcular(posfixa);
                    display.setText(Float.isNaN(resultado) ? "Syntax Error" : String.valueOf(resultado));
                } else if (bName.equals("CE")) {
                    display.setText("0");
                } else if (bName.equals("C")) {
                    display.setText(currentText.length() > 1 ? currentText.substring(0, currentText.length()-1) : "0");
                } else if (bName.equals("x²")) {
                    try {
                        float value = Float.parseFloat(currentText);
                        display.setText(String.valueOf(value * value));
                    } catch (NumberFormatException ex) {
                        display.setText("Error: Invalid Input");
                    }
                } else if (bName.equals("sin")) {
                    try {
                        float value = Float.parseFloat(currentText);
                        display.setText(String.valueOf(Math.sin(value)));
                    } catch (NumberFormatException ex) {
                        display.setText("Error: Invalid Input");
                    }
                } else if (bName.equals("cos")) {
                    try {
                        float value = Float.parseFloat(currentText);
                        display.setText(String.valueOf(Math.cos(value)));
                    } catch (NumberFormatException ex) {
                        display.setText("Error: Invalid Input");
                    }
                } else if (bName.equals("tan")) {
                    try {
                        float value = Float.parseFloat(currentText);
                        display.setText(String.valueOf(Math.tan(value)));
                    } catch (NumberFormatException ex) {
                        display.setText("Error: Invalid Input");
                    }
                } else if(bName.equals("π")) {
                    display.setText("3.1415926536");
                }
                else{
                    if (currentText.length() < 20) {
                        display.setText(currentText.equals("0") ? bName : currentText + bName);
                    }
                }
            } catch(Exception ex) {
                display.setText("Syntax Error");
            }
        });

        Color customColor = new Color(71,71,71);
        b.setBackground(customColor);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 18));
        b.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 4, true));
        panel.add(b);
    }

    private static float calcular(String expression) {
        Stack<Float> stack = new Stack<>();
        String[] tokens = expression.split(" ");

        for (String token : tokens) {
            if (token.matches("-?\\d+(\\.\\d+)?")) {
                stack.push(Float.parseFloat(token));
            } else {
                if (stack.size() < 2) {
                    return Float.NaN;
                }
                float b = stack.pop();
                float a = stack.pop();
                switch (token) {
                    case "+": stack.push(a + b); break;
                    case "-": stack.push(a - b); break;
                    case "x": stack.push(a * b); break;
                    case "/":
                        if (b == 0) {
                            return Float.NaN;
                        }
                        stack.push(a / b);
                        break;
                    case "%": stack.push(a % b); break;
                    case "mod": stack.push(Math.abs(a % b)); break;
                }
            }
        }
        return stack.isEmpty() ? Float.NaN : stack.pop();
    }

    private static int precedencia(char ch) {
        switch (ch) {
            case '+': case '-': return 1;
            case '*': case '/': case '%': case 'm': return 2; 
            case '^': return 3;
            default: return -1;
        }
    }

    private static String converterParaNotacaoPosFixa(String expression) {
        StringBuilder result = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        String numeroTemporario = "";

        for (char ch : expression.toCharArray()) {
            if (Character.isDigit(ch) || ch == '.') {
                numeroTemporario += ch;
            } else {
                if (!numeroTemporario.isEmpty()) {
                    result.append(numeroTemporario).append(" ");
                    numeroTemporario = "";
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

        if (!numeroTemporario.isEmpty()) {
            result.append(numeroTemporario).append(" ");
        }

        while (!stack.isEmpty()) {
            result.append(stack.pop()).append(" ");
        }

        return result.toString().trim();
    }
}
