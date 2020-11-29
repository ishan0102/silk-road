public class Item {
    private String name;
    private String description;
    private String bidPrice;
    private String buyPrice;
    private String email;

    public Item(String name, String description, String bidPrice, String buyPrice, String email) {
        this.name = name;
        this.description = description;
        this.bidPrice = bidPrice;
        this.buyPrice = buyPrice;
        this.email = email;
    }

    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }

    public String getBidPrice() {
        return bidPrice;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public String getEmail() {
        return email;
    }
}
