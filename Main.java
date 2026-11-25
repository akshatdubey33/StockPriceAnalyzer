
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            StockAnalyzerGUI gui = new StockAnalyzerGUI();

            gui.setVisible(true);

        });
    }
}
