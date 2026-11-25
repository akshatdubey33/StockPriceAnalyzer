
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.*;

public class StockAnalyzerGUI extends JFrame {

    private final StockManager manager = new StockManager();

    private JTextField companyField;
    private JTextField dateField;
    private JTextField priceField;
    private JTextField searchField;

    private JTable stockTable;
    private DefaultTableModel tableModel;

    private JLabel highestLabel;
    private JLabel lowestLabel;
    private JLabel averageLabel;
    private JLabel countLabel;

    // Theme colors
    private final Color BACKGROUND = new Color(12, 20, 35);
    private final Color CARD = new Color(23, 35, 55);
    private final Color BLUE = new Color(48, 125, 246);
    private final Color TEXT = new Color(235, 242, 252);
    private final Color MUTED = new Color(155, 172, 195);
    private final Color GREEN = new Color(39, 190, 135);

    public StockAnalyzerGUI() {

        setTitle("Stock Price Analyzer");
        setSize(1150, 780);
        setMinimumSize(new Dimension(950, 680));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(0, 20));
        root.setBackground(BACKGROUND);
        root.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // Header
        root.add(createHeader(), BorderLayout.NORTH);

        // Main content
        JPanel content = new JPanel();
        content.setBackground(BACKGROUND);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(createInputPanel());
        content.add(Box.createVerticalStrut(18));

        content.add(createStatisticsPanel());
        content.add(Box.createVerticalStrut(18));

        content.add(createTablePanel());
        content.add(Box.createVerticalStrut(15));

        content.add(createActionPanel());

        root.add(content, BorderLayout.CENTER);

        add(root);

        // Load saved records
        manager.loadStocks();
        refreshTable(manager.getStocks());
        updateStatistics();
    }

    // HEADER
    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(
            BorderFactory.createEmptyBorder(5, 0, 5, 0)
        );

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("STOCK PRICE ANALYZER");
        title.setFont(new Font("Segoe UI", Font.BOLD, 27));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
            "Market insights  |  Stock tracking  |  Price analysis"
        );
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(MUTED);

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitle);

        JLabel status = new JLabel("●  SYSTEM READY");
        status.setFont(new Font("Segoe UI", Font.BOLD, 12));
        status.setForeground(GREEN);

        header.add(titlePanel, BorderLayout.WEST);
        header.add(status, BorderLayout.EAST);

        return header;
    }

    // INPUT PANEL
    private JPanel createInputPanel() {

        JPanel card = createCard();
        card.setLayout(new BorderLayout(10, 12));
        card.setBorder(BorderFactory.createEmptyBorder(17, 20, 17, 20));

        JLabel heading = createHeading("ADD STOCK RECORD");

        JPanel fields = new JPanel(new GridLayout(2, 4, 12, 8));
        fields.setOpaque(false);

        companyField = new JTextField();
        dateField = new JTextField();
        priceField = new JTextField();

        styleInput(companyField);
        styleInput(dateField);
        styleInput(priceField);

        fields.add(createLabel("COMPANY NAME"));
        fields.add(createLabel("DATE (YYYY-MM-DD)"));
        fields.add(createLabel("STOCK PRICE (₹)"));
        fields.add(new JLabel(""));

        fields.add(companyField);
        fields.add(dateField);
        fields.add(priceField);

        JButton addButton = createButton("＋  ADD STOCK", BLUE);
        addButton.addActionListener(e -> addStock());

        fields.add(addButton);

        card.add(heading, BorderLayout.NORTH);
        card.add(fields, BorderLayout.CENTER);

        return card;
    }

    // STATISTICS PANEL
    private JPanel createStatisticsPanel() {

        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 0));
        panel.setOpaque(false);

        highestLabel = new JLabel("₹ 0.00");
        lowestLabel = new JLabel("₹ 0.00");
        averageLabel = new JLabel("₹ 0.00");
        countLabel = new JLabel("0");

        panel.add(createStatCard(
            "HIGHEST PRICE", highestLabel, GREEN
        ));

        panel.add(createStatCard(
            "LOWEST PRICE", lowestLabel, new Color(245, 170, 80)
        ));

        panel.add(createStatCard(
            "AVERAGE PRICE", averageLabel, BLUE
        ));

        panel.add(createStatCard(
            "TOTAL RECORDS", countLabel, new Color(180, 135, 245)
        ));

        return panel;
    }

    private JPanel createStatCard(
        String heading, JLabel value, Color accent) {

        JPanel card = createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(
            BorderFactory.createEmptyBorder(15, 17, 15, 10)
        );

        JLabel title = createLabel(heading);
        value.setFont(new Font("Segoe UI", Font.BOLD, 23));
        value.setForeground(accent);

        card.add(title);
        card.add(Box.createVerticalStrut(12));
        card.add(value);

        return card;
    }

    // TABLE PANEL
    private JPanel createTablePanel() {

        JPanel card = createCard();
        card.setLayout(new BorderLayout(10, 12));
        card.setBorder(BorderFactory.createEmptyBorder(17, 18, 17, 18));

        JLabel heading = createHeading("STOCK RECORDS");

        String[] columns = {
            "COMPANY NAME", "DATE", "STOCK PRICE (₹)"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        stockTable = new JTable(tableModel);

        stockTable.setRowHeight(34);
        stockTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        stockTable.setForeground(TEXT);
        stockTable.setBackground(CARD);
        stockTable.setGridColor(new Color(44, 59, 80));
        stockTable.setSelectionBackground(new Color(45, 85, 145));
        stockTable.setSelectionForeground(Color.WHITE);
        stockTable.setShowVerticalLines(false);
        stockTable.setFillsViewportHeight(true);

        JTableHeader tableHeader = stockTable.getTableHeader();
        tableHeader.setPreferredSize(new Dimension(0, 38));
        tableHeader.setFont(
            new Font("Segoe UI", Font.BOLD, 12)
        );
        tableHeader.setBackground(new Color(35, 55, 82));
        tableHeader.setForeground(TEXT);
        tableHeader.setReorderingAllowed(false);

        DefaultTableCellRenderer renderer =
            new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(
                JTable table, Object value, boolean selected,
                boolean focused, int row, int column) {

                Component c = super.getTableCellRendererComponent(
                    table, value, selected, focused, row, column
                );

                if (!selected) {
                    c.setBackground(
                        row % 2 == 0
                        ? CARD
                        : new Color(27, 41, 62)
                    );
                    c.setForeground(TEXT);
                }

                setBorder(
                    BorderFactory.createEmptyBorder(0, 12, 0, 8)
                );

                return c;
            }
        };

        for (int i = 0; i < stockTable.getColumnCount(); i++) {
            stockTable.getColumnModel()
                .getColumn(i).setCellRenderer(renderer);
        }

        JScrollPane scrollPane = new JScrollPane(stockTable);
        scrollPane.getViewport().setBackground(CARD);
        scrollPane.setBorder(
            BorderFactory.createLineBorder(
                new Color(44, 59, 80)
            )
        );

        card.add(heading, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);

        return card;
    }

    // SEARCH AND ACTION PANEL
    private JPanel createActionPanel() {

        JPanel card = createCard();
        card.setLayout(new GridLayout(2, 1, 5, 5));
        card.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        JPanel searchRow = new JPanel(
            new FlowLayout(FlowLayout.LEFT, 9, 2)
        );
        searchRow.setOpaque(false);

        searchField = new JTextField(17);
        styleInput(searchField);

        JButton companySearch = createButton(
            "SEARCH COMPANY", BLUE
        );

        JButton priceSearch = createButton(
            "SEARCH PRICE", BLUE
        );

        searchRow.add(createLabel("SEARCH"));
        searchRow.add(searchField);
        searchRow.add(companySearch);
        searchRow.add(priceSearch);

        JPanel actionRow = new JPanel(
            new FlowLayout(FlowLayout.LEFT, 9, 2)
        );
        actionRow.setOpaque(false);

        JButton sortButton = createButton(
            "SORT PRICES", new Color(75, 95, 125)
        );

        JButton showAllButton = createButton(
            "SHOW ALL", new Color(75, 95, 125)
        );

        JButton undoButton = createButton(
            "UNDO", new Color(180, 90, 80)
        );

        JButton recentButton = createButton(
            "RECENT RECORDS", new Color(75, 95, 125)
        );

        JButton trendButton = createButton(
            "ANALYZE TREND", new Color(30, 145, 115)
        );

        JButton chartButton = createButton(
            "SHOW CHART", new Color(110, 90, 190)
        );

        actionRow.add(sortButton);
        actionRow.add(showAllButton);
        actionRow.add(undoButton);
        actionRow.add(recentButton);
        actionRow.add(trendButton);
        actionRow.add(chartButton);

        companySearch.addActionListener(e -> searchCompany());
        priceSearch.addActionListener(e -> searchPrice());
        sortButton.addActionListener(
            e -> refreshTable(manager.getSortedStocks())
        );
        showAllButton.addActionListener(
            e -> refreshTable(manager.getStocks())
        );
        undoButton.addActionListener(e -> undoStock());
        recentButton.addActionListener(e -> showRecentStocks());
        trendButton.addActionListener(e -> analyzeTrend());
        chartButton.addActionListener(e -> showChart());

        card.add(searchRow);
        card.add(actionRow);

        return card;
    }

    // ADD STOCK
    private void addStock() {

        try {

            String company = companyField.getText().trim();
            String date = dateField.getText().trim();
            String priceText = priceField.getText().trim();

            if (company.isEmpty() || date.isEmpty()
                    || priceText.isEmpty()) {
                throw new IllegalArgumentException(
                    "Please fill in all fields."
                );
            }

            LocalDate.parse(date);

            double price = Double.parseDouble(priceText);

            if (!Double.isFinite(price) || price <= 0) {
                throw new IllegalArgumentException(
                    "Enter a valid positive stock price."
                );
            }

            Stock stock = new Stock(company, date, price);

            if (!manager.addStock(stock)) {
                JOptionPane.showMessageDialog(
                this,
                "Could not save stock record. Please check file permissions.",
                "Save Error",
                JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            refreshTable(manager.getStocks());
            updateStatistics();

            companyField.setText("");
            dateField.setText("");
            priceField.setText("");

            JOptionPane.showMessageDialog(
                this,
                "Stock record added successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                this,
                "Invalid input: " + ex.getMessage(),
                "Input Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // SEARCH COMPANY
    private void searchCompany() {

        String name = searchField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "Enter a company name."
            );
            return;
        }

        ArrayList<Stock> results = manager.searchCompany(name);

        refreshTable(results);

        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "No matching company found."
            );
        }
    }

    // SEARCH PRICE
    private void searchPrice() {

        try {

            double price = Double.parseDouble(
                searchField.getText().trim()
            );

            Stock result = manager.searchPrice(price);

            ArrayList<Stock> results = new ArrayList<>();

            if (result != null) {
                results.add(result);
            }

            refreshTable(results);

            if (result == null) {
                JOptionPane.showMessageDialog(
                    this, "Stock price not found."
                );
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                this, "Enter a valid price."
            );
        }
    }

    // UNDO
    private void undoStock() {

        if (manager.undoLastStock()) {

            refreshTable(manager.getStocks());
            updateStatistics();

            JOptionPane.showMessageDialog(
                this, "Last stock record removed."
            );

        } else {

            JOptionPane.showMessageDialog(
                this, "No record available to undo."
            );
        }
    }

    // RECENT RECORDS
    private void showRecentStocks() {

        ArrayList<Stock> recent =
            new ArrayList<>(manager.getRecentStocks());

        refreshTable(recent);
    }

    // TREND ANALYSIS
    private void analyzeTrend() {

        String company = searchField.getText().trim();

        ArrayList<Stock> records =
            manager.searchCompany(company);

        records.sort(
            (a, b) -> a.getDate().compareTo(b.getDate())
        );

        if (records.size() < 2) {

            JOptionPane.showMessageDialog(
                this,
                "Enter a company with at least two records."
            );
            return;
        }

        Stock previous = records.get(records.size() - 2);
        Stock latest = records.get(records.size() - 1);

        double change = latest.getPrice() - previous.getPrice();

        double percentage =
            (change / previous.getPrice()) * 100;

        String trend = change > 0
            ? "PRICE INCREASING"
            : change < 0
            ? "PRICE DECREASING"
            : "PRICE STABLE";

        JOptionPane.showMessageDialog(
            this,
            trend +
            "\n\nPrevious: ₹" + previous.getPrice() +
            "\nLatest: ₹" + latest.getPrice() +
            "\nChange: ₹" + String.format("%.2f", change) +
            "\nPercentage: " +
            String.format("%.2f", percentage) + "%",
            "Stock Trend Analysis",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    // SHOW CHART
    private void showChart() {

        String company = searchField.getText().trim();

        if (company.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "Enter a company name first."
            );
            return;
        }

        ArrayList<Stock> records =
            manager.searchCompany(company);

        if (records.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "No records found for this company."
            );
            return;
        }

        JFrame chartFrame = new JFrame(
            company + " - Stock Price Chart"
        );

        chartFrame.setSize(850, 550);
        chartFrame.setLocationRelativeTo(this);

        chartFrame.add(
            new StockChartPanel(records, company)
        );

        chartFrame.setVisible(true);
    }

    // REFRESH TABLE
    private void refreshTable(ArrayList<Stock> stocks) {

        tableModel.setRowCount(0);

        for (Stock s : stocks) {

            tableModel.addRow(new Object[]{
                s.getCompanyName(),
                s.getDate(),
                String.format("%.2f", s.getPrice())
            });
        }
    }

    // UPDATE STATISTICS
    private void updateStatistics() {

        highestLabel.setText(
            String.format("₹ %.2f", manager.getHighestPrice())
        );

        lowestLabel.setText(
            String.format("₹ %.2f", manager.getLowestPrice())
        );

        averageLabel.setText(
            String.format("₹ %.2f", manager.getAveragePrice())
        );

        countLabel.setText(
            String.valueOf(manager.getStocks().size())
        );
    }

    // REUSABLE DESIGN METHODS
    private JPanel createCard() {

        JPanel panel = new JPanel();
        panel.setBackground(CARD);
        panel.setBorder(
            BorderFactory.createLineBorder(
                new Color(42, 57, 78)
            )
        );

        return panel;
    }

    private JLabel createHeading(String text) {

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(TEXT);

        return label;
    }

    private JLabel createLabel(String text) {

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(MUTED);

        return label;
    }

    private void styleInput(JTextField field) {

        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setForeground(TEXT);
        field.setBackground(new Color(15, 27, 44));
        field.setCaretColor(Color.WHITE);
        field.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(58, 77, 105)
                ),
                BorderFactory.createEmptyBorder(7, 9, 7, 9)
            )
        );
    }

    private JButton createButton(String text, Color color) {

        JButton button = new JButton(text);

        button.setFont(new Font("Segoe UI", Font.BOLD, 11));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(
            Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );
        button.setMargin(new Insets(9, 13, 9, 13));

        return button;
    }
}