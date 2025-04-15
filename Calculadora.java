import java.util.regex.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class Calculadora {
    
    public static String operador;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->createWindow());
    }
    private static void createWindow(){
        JFrame f = new JFrame("Calculadora");
        f.getContentPane().setBackground(Color.DARK_GRAY);
        JTextArea t = new JTextArea("0");
        t.setPreferredSize(new Dimension(400, 100));
        t.setFont(new Font("Arial", Font.PLAIN, 48)); 
        t.setBackground(Color.DARK_GRAY);  
        t.setForeground(Color.WHITE);  
        t.setEditable(false);  
        t.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2)); 
        f.add(t, BorderLayout.NORTH);
        JPanel p = new JPanel(new GridLayout(5, 4)); 
        createButton(p, t, "%");
        createButton(p, t,"CE");
        createButton(p, t, "x²");
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
        f.setSize(400, 400);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        f.setVisible(true);
    }
    private static void createButton(JPanel panel, JTextArea display, String bName){
        JButton b = new JButton(bName);
        b.addActionListener(e -> {
            if (bName.equals("=")) {
                String expression = display.getText();
                float resultado = identificarPadrao(expression); // Process calculation
                display.setText(String.valueOf(resultado)); // Show result
            }
            else if(bName.equals("CE")){
                display.setText("0");
            } else {
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
    private static float identificarPadrao(String line) {
        Pattern p = Pattern.compile("(-?\\d+(\\.\\d+)?)([+\\-*/])(-?\\d+(\\.\\d+)?)");
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
            default:{
                System.out.println("Operador inválido!");
                return Float.NaN;
        }
    }
    }
}
