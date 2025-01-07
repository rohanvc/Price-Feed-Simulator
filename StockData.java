import java.time.*;

public class StockData implements Comparable<StockData> {
    private String ticker;
    private double bidPrice,askPrice,lastPrice, priceChange, originalPrice;
    private Instant quoteTime;
    private volatile boolean flag;
    /**
     * StockData: 
     * Constructor for the tockData class. This initializes all the class variables. 
     * This class holds all the essential data needed for each stock.   
     * 
     * @param String tic: ticker symbol
     * @param double bid: bid price
     * @param double ask: ask price
     * @param double last: last price
     * @param double firstPrice: Original price of stock.
     * @param Instant time: quote time
     *  
     * 
     */
    public StockData(String tic, double bid, double ask, double last, double firstPrice, Instant time) {
        this.ticker = tic;
        this.bidPrice = bid;
        this.askPrice = ask;
        this.lastPrice = last;
        this.quoteTime = time;
        this.priceChange = 0;
        this.originalPrice = firstPrice;
        this.flag = false;
    }

    /**
     * getTicker: 
     * Returns the ticker symbol.
     * 
     * @return String: Returns the ticker symbol.
     */
    public String getTicker(){
        return this.ticker;
    }

    /**
     * getBidPrice: 
     * Returns the bid price.
     * 
     * @return double: Returns the bid price.
     */
    public double getBidPrice() {
        return this.bidPrice;
    }

    /**
     * getAskPrice: 
     * Returns the ask price.
     * 
     * @return double: Returns the ask price.
     */
    public double getAskPrice() {
        return this.askPrice;
    }

    /**
     * getLastPrice: 
     * Returns the last price.
     * 
     * @return double: Returns the last price.
     */
    public double getLastPrice() {
        return this.lastPrice;
    }

    /**
     * getQuoteTime: 
     * Returns the quote time.
     * 
     * @return Instant: Returns the quote time.
     */
    public Instant getQuoteTime() {
        return this.quoteTime;
    }

    /**
     * getPriceChange: 
     * Returns the difference in the original price and the current price.
     * 
     * @return double: Returns the difference in the original price and the current price.
     */
    public double getPriceChange() {
        return this.priceChange;
    }

    /**
     * getOriginalPrice: 
     * Returns the original price.
     * 
     * @return double: Returns the original price.
     */
    public double getOriginalPrice() {
        return this.originalPrice;
    }

    /**
     * getBidPrice: 
     * Returns the value of the flag. The flag is used to check if there has been a change in price 
     * since the last time we displayed this price.
     * 
     * @return double: Returns the value of the flag.
     */
    public boolean getFlag() {
        return this.flag;
    }
     /**
     * setBidPrice: 
     * Sets the bid price for this ticker.
     * 
     * @param double bid: New bid price.
     */
    public void setBidPrice(double bid) {
        this.bidPrice = bid;
    }

     /**
     * setAskPrice: 
     * Sets the ask price for this ticker.
     * 
     * @param double ask: New ask price.
     */
    public void setAskPrice(double ask) {
        this.askPrice = ask;
    }

     /**
     * setLastPrice: 
     * Sets the last price for this ticker.
     * 
     * @param double last: New last price.
     */
    public void setLastPrice(double last) {
        this.lastPrice = last;
    }

     /**
     * setQuoteTime: 
     * Sets the Quote time (UTC) for this ticker.
     * 
     * @param Instant Quote: New Quote time.
     */
    public void setQuoteTime(Instant cur) {
        this.quoteTime = cur;
    }

     /**
     * setPriceChange: 
     * Sets the difference in price between the original price and the current price for this ticker.
     * 
     * @param double change: New price difference.
     */
    public void setPriceChange(double change) {
        this.priceChange = change;
    }

     /**
     * setOriginalPrice: 
     * Sets the original price for this ticker.
     * 
     * @param double original: New original price.
     */
    public void setOriginalPrice(double price) {
        this.originalPrice = price;
    }

     /**
     * setFlag: 
     * Sets the update flag for this ticker.
     * 
     * @param boolean bool: New flag value.
     */
    public void setFlag(boolean bool) {
        this.flag = bool;
    }

     /**
     * compareTo: 
     * Returns -1, 0, and 1 if the current object is less, equal to, and greater than the other object, respectively.
     * 
     * @param StockData other: A different stock to compareTo.
     * 
     * @return int: Returns -1, 0, and 1 if the current object is less, equal to, and greater than the other object, respectively.
     */
    @Override
    public int compareTo(StockData other) {
        return Double.compare(other.priceChange, this.priceChange);
    }
}
