
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import javax.swing.*;

public class StockChartPanel extends JPanel {

    private ArrayList<Stock> stocks;
    private String companyName;

    private final Color BACKGROUND =
        new Color(12, 20, 35);

    private final Color GRID =
        new Color(45, 60, 82);

    private final Color TEXT =
        new Color(220, 230, 245);

    private final Color GREEN =
        new Color(40, 210, 145);

    private final Color RED =
        new Color(245, 90, 95);

    public StockChartPanel(
            ArrayList<Stock> stocks,
            String companyName) {

        this.stocks = new ArrayList<>(stocks);
        this.companyName = companyName;

        this.stocks.sort(
            Comparator.comparing(
                s -> LocalDate.parse(s.getDate())
            )
        );

        setBackground(BACKGROUND);
        setPreferredSize(new Dimension(850, 550));
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        int width = getWidth();
        int height = getHeight();

        int left = 95;
        int right = width - 45;
        int top = 100;
        int bottom = height - 115;

        // Chart heading
        g2.setColor(TEXT);
        g2.setFont(
            new Font("Segoe UI", Font.BOLD, 24)
        );

        g2.drawString(
            companyName.toUpperCase() + " PRICE HISTORY",
            left, 40
        );

        g2.setFont(
            new Font("Segoe UI", Font.PLAIN, 13)
        );

        g2.setColor(new Color(145, 165, 190));

        g2.drawString(
            "Historical stock price analysis",
            left, 65
        );

        if (stocks.isEmpty()) {

            g2.setColor(TEXT);
            g2.drawString(
                "No stock data available",
                left, 140
            );

            g2.dispose();
            return;
        }

        // Find minimum and maximum prices
        double min = stocks.get(0).getPrice();
        double max = min;

        for (Stock s : stocks) {

            min = Math.min(min, s.getPrice());
            max = Math.max(max, s.getPrice());
        }

        // Handle equal prices
        double range = max - min;

        if (range == 0) {
            range = Math.max(1, max * 0.1);
        }

        double padding = range * 0.15;

        min -= padding;
        max += padding;

        range = max - min;

        // Draw grid and price labels
        g2.setFont(
            new Font("Segoe UI", Font.PLAIN, 11)
        );

        for (int i = 0; i <= 5; i++) {

            int y = bottom -
                i * (bottom - top) / 5;

            double value =
                min + i * range / 5;

            g2.setColor(GRID);

            g2.drawLine(
                left, y, right, y
            );

            g2.setColor(TEXT);

            g2.drawString(
                String.format("₹%.2f", value),
                15, y + 5
            );
        }

        // Draw chart axes
        g2.setColor(TEXT);

        g2.drawLine(left, top, left, bottom);
        g2.drawLine(left, bottom, right, bottom);

        int n = stocks.size();

        int[] xPoints = new int[n];
        int[] yPoints = new int[n];

        // Calculate graph coordinates
        for (int i = 0; i < n; i++) {

            Stock stock = stocks.get(i);

            xPoints[i] = n == 1
                ? (left + right) / 2
                : left + i * (right - left) / (n - 1);

            yPoints[i] = bottom - (int)(
                (stock.getPrice() - min)
                / range * (bottom - top)
            );
        }

        // Draw stock price line
        for (int i = 0; i < n - 1; i++) {

            Color lineColor =
                stocks.get(i + 1).getPrice()
                >= stocks.get(i).getPrice()
                ? GREEN : RED;

            g2.setColor(lineColor);

            g2.setStroke(
                new BasicStroke(
                    3,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND
                )
            );

            g2.drawLine(
                xPoints[i],
                yPoints[i],
                xPoints[i + 1],
                yPoints[i + 1]
            );
        }

        // Draw points, prices and dates
        for (int i = 0; i < n; i++) {

            Stock stock = stocks.get(i);

            Color pointColor = GREEN;

            if (i > 0 &&
                stock.getPrice()
                < stocks.get(i - 1).getPrice()) {

                pointColor = RED;
            }

            g2.setColor(pointColor);

            g2.fillOval(
                xPoints[i] - 6,
                yPoints[i] - 6,
                12, 12
            );

            // Display price
            g2.setFont(
                new Font("Segoe UI", Font.BOLD, 11)
            );

            String price =
                String.format("₹%.2f", stock.getPrice());

            g2.drawString(
                price,
                xPoints[i] - 28,
                yPoints[i] - 15
            );

            // Display complete date
            g2.setColor(TEXT);

            g2.setFont(
                new Font("Segoe UI", Font.PLAIN, 10)
            );

            String date = LocalDate.parse(
                stock.getDate()
            ).format(
                DateTimeFormatter.ofPattern("dd MMM yy")
            );

            g2.drawString(
                date,
                xPoints[i] - 27,
                bottom + 25
            );
        }

        // Show minimum and maximum
        g2.setFont(
            new Font("Segoe UI", Font.BOLD, 12)
        );

        g2.setColor(GREEN);

        g2.drawString(
            "Highest: ₹" + String.format("%.2f",
                stocks.stream()
                    .mapToDouble(Stock::getPrice)
                    .max().orElse(0)),
            left,
            height - 35
        );

        g2.setColor(RED);

        g2.drawString(
            "Lowest: ₹" + String.format("%.2f",
                stocks.stream()
                    .mapToDouble(Stock::getPrice)
                    .min().orElse(0)),
            left + 220,
            height - 35
        );

        g2.dispose();
    }
}
