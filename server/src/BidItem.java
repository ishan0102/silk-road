/*
 * EE422C Final Project submission by
 * Ishan Shah
 * irs428
 * 16165
 * Fall 2020
 */

/**
 * Bidding item for a particular auction item
 */
public class BidItem {
    private Integer itemId;
    private String name;
    private String description;
    private Double bidPrice;
    private Double buyPrice;
    private Integer bidderId;
    private Integer sellerId;
    private Boolean buyable;
    private Boolean valid;

    public BidItem(String name, String description, Double bidPrice, Double buyPrice, Integer bidderId) {
        this.itemId = null;
        this.name = name;
        this.description = description;
        this.bidPrice = bidPrice;
        this.buyPrice = buyPrice;
        this.bidderId = bidderId;
        this.sellerId = bidderId;
        this.buyable = true;
        this.valid = true;
    }

    public BidItem(int itemId, String name, String description, double bidPrice, double buyPrice, int bidderId, int sellerId, boolean buyable, boolean valid) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.bidPrice = bidPrice;
        this.buyPrice = buyPrice;
        this.bidderId = bidderId;
        this.sellerId = sellerId;
        this.buyable = buyable;
        this.valid = valid;
    }

    public Item toSimpleItem(String email) {
        return new Item(name, description, bidPrice.toString(), buyPrice.toString(), email);
    }

    public Integer getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getBidPrice() {
        return bidPrice;
    }

    public Double getBuyPrice() {
        return buyPrice;
    }

    public Integer getBidderId() {
        return bidderId;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public Boolean getBuyable() {
        return buyable;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setBidPrice(Double bidPrice) {
        this.bidPrice = bidPrice;
    }

    public void setBuyPrice(Double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public void setBidderId(Integer bidderId) {
        this.bidderId = bidderId;
    }

    public void setBuyable(Boolean buyable) {
        this.buyable = buyable;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    @Override
    public String toString() {
        return "id: " + itemId + ", name: " + name + ", description: " + description + ", bid price: " + bidPrice
                + ", buy price: " + buyPrice + ", bidder id: " + bidderId + ", seller id: " + sellerId + ", buyable: "
                + buyable + ", valid: " + valid;
    }
}
