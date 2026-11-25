
import java.io.*;
import java.util.*;

public class StockManager {

    private ArrayList<Stock> stocks = new ArrayList<>();

    private Stack<Stock> undoStack = new Stack<>();

    private Queue<Stock> recentStocks = new LinkedList<>();

    private static final String FILE_NAME = "stocks.dat";

    // Add stock
public boolean addStock(Stock stock) {

    stocks.add(stock);
    undoStack.push(stock);
    recentStocks.offer(stock);

    if (recentStocks.size() > 5) {
        recentStocks.poll();
    }

    if (saveStocks()) {
        return true;
    }

    // If saving fails, undo the changes
    stocks.remove(stock);
    undoStack.pop();
    recentStocks.remove(stock);

    return false;
}

    // Undo last stock
    public boolean undoLastStock() {

        if (undoStack.isEmpty()) {
            return false;
        }

        Stock last = undoStack.pop();

        stocks.remove(last);
        recentStocks.remove(last);

        saveStocks();

        return true;
    }

    // Save stock records to file
    public boolean saveStocks() {
    try (ObjectOutputStream out =
             new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

        out.writeObject(stocks);
        return true;

    } catch (IOException e) {
        System.out.println("Error saving stock data: " + e.getMessage());
        return false;
    }
}

    // Load stock records from file
    @SuppressWarnings("unchecked")
    public void loadStocks() {

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return;
        }

        try (ObjectInputStream in =
             new ObjectInputStream(
                 new FileInputStream(file))) {

            ArrayList<Stock> loaded =
                (ArrayList<Stock>) in.readObject();

            stocks.clear();
            undoStack.clear();
            recentStocks.clear();

            stocks.addAll(loaded);

            for (Stock stock : stocks) {
                undoStack.push(stock);
            }

            for (int i = Math.max(0, stocks.size() - 5);
                 i < stocks.size(); i++) {

                recentStocks.offer(stocks.get(i));
            }

        } catch (IOException | ClassNotFoundException e) {

            System.out.println("Error loading stock data: "
                + e.getMessage());
        }
    }

    public ArrayList<Stock> getStocks() {
        return new ArrayList<>(stocks);
    }

    public Queue<Stock> getRecentStocks() {
        return new LinkedList<>(recentStocks);
    }

    public double getHighestPrice() {
        return StockAlgorithms.findMaximum(stocks);
    }

    public double getLowestPrice() {
        return StockAlgorithms.findMinimum(stocks);
    }

    public double getAveragePrice() {
        return StockAlgorithms.findAverage(stocks);
    }

    public ArrayList<Stock> getSortedStocks() {
        return StockAlgorithms.mergeSort(stocks);
    }

    public ArrayList<Stock> searchCompany(String name) {
        return StockAlgorithms.searchCompany(stocks, name);
    }

    public Stock searchPrice(double price) {

        ArrayList<Stock> sorted = getSortedStocks();

        return StockAlgorithms.binarySearch(sorted, price);
    }
}
