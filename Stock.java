
import java.io.Serializable;

public class Stock implements Serializable {

    private static final long serialVersionUID = 1L;
    private String companyName;
    private String date;
    private double price;

    public Stock(String companyName, String date, double price) {
        this.companyName = companyName;
        this.date = date;
        this.price = price;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getDate() {
        return date;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return companyName + " | " + date + " | " + price;
    }
}
