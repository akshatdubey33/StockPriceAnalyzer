
import java.util.ArrayList;

public class StockAlgorithms {

    // 1. Find maximum stock price
    public static double findMaximum(ArrayList<Stock> stocks) {

        if (stocks == null || stocks.isEmpty()) return 0;

        double max = stocks.get(0).getPrice();

        for (Stock s : stocks) {
            if (s.getPrice() > max) {
                max = s.getPrice();
            }
        }

        return max;
    }

    // 2. Find minimum stock price
    public static double findMinimum(ArrayList<Stock> stocks) {

        if (stocks == null || stocks.isEmpty()) return 0;

        double min = stocks.get(0).getPrice();

        for (Stock s : stocks) {
            if (s.getPrice() < min) {
                min = s.getPrice();
            }
        }

        return min;
    }

    // 3. Calculate average price
    public static double findAverage(ArrayList<Stock> stocks) {

        if (stocks == null || stocks.isEmpty()) return 0;

        double sum = 0;

        for (Stock s : stocks) {
            sum += s.getPrice();
        }

        return sum / stocks.size();
    }

    // 4. Merge Sort
    public static ArrayList<Stock> mergeSort(
            ArrayList<Stock> stocks) {

        if (stocks == null || stocks.size() <= 1) {
            return stocks == null
                    ? new ArrayList<>()
                    : new ArrayList<>(stocks);
        }

        int mid = stocks.size() / 2;

        ArrayList<Stock> left =
                new ArrayList<>(stocks.subList(0, mid));

        ArrayList<Stock> right =
                new ArrayList<>(stocks.subList(mid, stocks.size()));

        return merge(mergeSort(left), mergeSort(right));
    }

    // Merge two sorted lists
    private static ArrayList<Stock> merge(
            ArrayList<Stock> left,
            ArrayList<Stock> right) {

        ArrayList<Stock> result = new ArrayList<>();

        int i = 0, j = 0;

        while (i < left.size() && j < right.size()) {

            if (left.get(i).getPrice() <=
                    right.get(j).getPrice()) {

                result.add(left.get(i++));
            } else {
                result.add(right.get(j++));
            }
        }

        while (i < left.size()) {
            result.add(left.get(i++));
        }

        while (j < right.size()) {
            result.add(right.get(j++));
        }

        return result;
    }

    // 5. Binary Search
    // IMPORTANT: Input must be sorted by price
    public static Stock binarySearch(
            ArrayList<Stock> stocks, double target) {

        if (stocks == null) return null;

        int low = 0;
        int high = stocks.size() - 1;

        while (low <= high) {

            int mid = low + (high - low) / 2;

            double price = stocks.get(mid).getPrice();

            if (Double.compare(price, target) == 0) {
                return stocks.get(mid);
            }

            if (price < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return null;
    }

    // 6. Linear Search by company
    public static ArrayList<Stock> searchCompany(
            ArrayList<Stock> stocks, String company) {

        ArrayList<Stock> result = new ArrayList<>();

        if (stocks == null || company == null) {
            return result;
        }

        for (Stock s : stocks) {

            if (s.getCompanyName().equalsIgnoreCase(
                    company.trim())) {

                result.add(s);
            }
        }

        return result;
    }

    // 7. Calculate total stock records for a company
    public static int countCompanyRecords(
            ArrayList<Stock> stocks, String company) {

        return searchCompany(stocks, company).size();
    }

    // 8. Find the latest record in the given list
    public static Stock findLatestRecord(
            ArrayList<Stock> stocks) {

        if (stocks == null || stocks.isEmpty()) return null;

        Stock latest = stocks.get(0);

        for (Stock s : stocks) {

            if (s.getDate().compareTo(latest.getDate()) > 0) {
                latest = s;
            }
        }

        return latest;
    }
}
