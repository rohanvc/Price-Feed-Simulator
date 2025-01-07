import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.time.*;
import java.io.*; 
import java.time.format.DateTimeFormatter;

public class DataGen {
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Hashtable<String, StockData> data;
    private Hashtable<String,TrackUpdates> updateTracking;
    private Hashtable<String,MovingAverage> movingAverage;
    private Set<StockData> movers;
    private int numberTickers, refreshInterval, periods;
    private String[] arrayTickers;
    private Set<String> setTickers;

    /**
     * DataGen: 
     * Constructor for the DataGen class. This generates all the initial information on 
     * each ticker based on the input number of tickers and refresh interval. It also initializes 
     * all the class variables and begins the maintainence on them.  
     * 
     * @param int numTickers: Number of tickers we want to create. 
     * @param int updateInt: Refresh interval of the class.
     * 
     */
    public DataGen(int numTickers, int updateInt) {
        this.numberTickers = numTickers;
        this.refreshInterval = updateInt;
        this.updateTracking = new Hashtable<String,TrackUpdates>();
        this.data = new Hashtable<String,StockData>();
        this.setTickers = new HashSet<String>();
        this.movingAverage = new Hashtable<String,MovingAverage>();
        this.movers = new TreeSet<StockData>();
        this.arrayTickers = new String[this.numberTickers];
        this.periods = 300/this.refreshInterval;
        String ticker;
        double bidPrice, askPrice, lastPrice;
        int count = 0;
        outerloop:
        for(char tOne = 'a'; tOne <='z'; tOne++) {
            for(char tTwo = 'a'; tTwo <= 'z'; tTwo++) {
                for(char tThree = 'a'; tThree <= 'z'; tThree ++) {
                    if(count >= this.numberTickers) { break outerloop; }
                    ticker = "" + tOne + tTwo + tThree;
                    this.setTickers.add(ticker); 
                    this.arrayTickers[count] = ticker;
                    bidPrice = Math.random() * 100;
                    while(bidPrice <1) {
                        bidPrice = Math.random() * 100;
                    }
                    askPrice = bidPrice*1.1;
                    lastPrice = (bidPrice+askPrice)/2;
                    MovingAverage ma = new MovingAverage();
                    ma.addLastPriceData(lastPrice, this.periods);
                    this.movingAverage.put(ticker,ma);
                    StockData curStockData = new StockData(ticker, bidPrice, askPrice, lastPrice, lastPrice, Instant.now());
                    this.movers.add(curStockData);
                    this.data.put(ticker, curStockData);
                    TrackUpdates curUpdates = new TrackUpdates();
                    curUpdates.update(curStockData);
                    this.updateTracking.put(ticker,curUpdates);
                    count++;
                }
            }
        }
    }
    /**
     * getRefreshInterval: 
     * Returns the refresh interval
     * 
     * @return int: returns the refresh interval.
     * 
     */
    public int getRefreshInterval() {
        return this.refreshInterval;
    }
    /**
     * refresh: 
     * Updates data for at least 1 ticker, and up to 25% of the total number of tickers with
     * reasonable and realistic data. It chooses which ticker to update at random, so that each 
     * ticker gets updated roughly the same number of times. 
     * 
     */
    public void refresh() {
        Set<Integer> tickersRefreshed = new HashSet<Integer>();
        int index, sign;
        double changePercent, newBid, newAsk, newLast, lastPrice, originalPrice;
        double volatility = 0.04;
        StockData curStockData;
        int numRefresh = (int) ((Math.random() * this.numberTickers) * 0.25);
        if( numRefresh < 1 ) { numRefresh = 1; }
        while(numRefresh>0) {
            index = (int) (Math.random() * numberTickers);
            while(tickersRefreshed.contains(index)) {
                index = (int) (Math.random() * numberTickers);
            }
            tickersRefreshed.add(index);
            if(Math.random() >= .5) { 
                sign = 1;
            } else { 
                sign = -1;
            }
            changePercent = 2 * volatility * Math.random();
            if (changePercent > volatility) {
                changePercent -= (2 * volatility);
            }
            lock.readLock().lock();
            try {
                curStockData = this.data.get(this.arrayTickers[index]);
                newBid = curStockData.getBidPrice() + (curStockData.getBidPrice() * changePercent * sign);
                newAsk = curStockData.getAskPrice() + (curStockData.getAskPrice() * changePercent * sign);
                newLast = curStockData.getLastPrice() + (curStockData.getLastPrice() * changePercent * sign);
                originalPrice = curStockData.getOriginalPrice();
            } finally {
                lock.readLock().unlock();
            }
            Instant curTime = Instant.now();
            StockData newUpdate = new StockData(this.arrayTickers[index], newBid, newAsk, newLast, originalPrice, curTime);
            // All Writes
            lock.writeLock().lock();
            try {
                this.updateTracking.get(this.arrayTickers[index]).update(newUpdate);
                this.movers.remove(curStockData);
                curStockData.setBidPrice(newBid);
                curStockData.setAskPrice(newAsk);
                curStockData.setLastPrice(newLast);
                curStockData.setQuoteTime(curTime);
                curStockData.setPriceChange(Math.abs(curStockData.getOriginalPrice() - newLast));
                curStockData.setFlag(true);
                this.movers.add(curStockData);
                this.movingAverage.get(this.arrayTickers[index]).addLastPriceData(newLast, this.periods);
            } finally {
                lock.writeLock().unlock();
            }
            numRefresh -=1;
        }
        lock.writeLock().lock();
        try {
            for(int i = 0; i<this.arrayTickers.length; i++) {
                if(!tickersRefreshed.contains(i)) {
                    lastPrice = this.data.get(this.arrayTickers[i]).getLastPrice();
                    this.movingAverage.get(this.arrayTickers[i]).addLastPriceData(lastPrice, this.periods);
                }
            }
        } finally {
            lock.writeLock().unlock();
        }

    }

    /**
     * displayAllStockInfo: 
     * Sends the information stored for each indivdual stock to the client. 
     * 
     * @param PrintWriter out: allows us to send information to the client. 
     *       
     */
    public void displayAllStockInfo(PrintWriter out) {
        String tic = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        StockData curTickerData;
        lock.readLock().lock();
        try {
            for(int i = 0; i< this.arrayTickers.length; i++) {
                tic = this.arrayTickers[i];
                curTickerData = this.data.get(tic);
                ZonedDateTime pstTime = curTickerData.getQuoteTime().atZone(ZoneId.of("America/Los_Angeles"));
                String formattedDate = pstTime.format(formatter);
                out.println("\nTicker: " + tic + ":\n\tBid Price: " + String.format("%.2f", curTickerData.getBidPrice()) + "\n\tAsk Price: " + 
                    String.format("%.2f", curTickerData.getAskPrice()) + "\n\tLast Price: " + String.format("%.2f", curTickerData.getLastPrice()) + "\n\tQuote Time: " + formattedDate + 
                    "\n\tOriginal Price: " + String.format("%.2f", curTickerData.getOriginalPrice()) + "\n\tPrice Difference: " + 
                    String.format("%.2f", curTickerData.getPriceChange()) + "\n");
            }
        } finally {
            lock.readLock().unlock();
        }
        out.println("done");
    }

    /**
     * displaySingleStockInfo: 
     * Sends the information stored for a specific stock requested by the user. 
     * 
     * @param PrintWriter out: allows us to send information to the client. 
     * @param BufferedReader in: allows us to receive information from the client.
     * @param String ticker: the specific stock the user has requested information on.
     *       
     * @throws IOException: thrown to indicate that an I/O error occurred when an
     *                      application attempts to read or write data.
     */

    public void displaySingleStockInfo(BufferedReader in, PrintWriter out, String tic) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        while(!this.setTickers.contains(tic)) {
            out.println("-1");
            tic = in.readLine();
        }
        lock.readLock().lock();
        try {
            StockData curTickerData;
            curTickerData = this.data.get(tic);
            ZonedDateTime pstTime = curTickerData.getQuoteTime().atZone(ZoneId.of("America/Los_Angeles"));
            String formattedDate = pstTime.format(formatter);
            out.println("\nTicker: " + tic + ":\n\tBid Price: " + String.format("%.2f", curTickerData.getBidPrice()) + "\n\tAsk Price: " + 
                    String.format("%.2f", curTickerData.getAskPrice()) + "\n\tLast Price: " + String.format("%.2f", curTickerData.getLastPrice()) + "\n\tQuote Time: " + formattedDate + 
                    "\n\tOriginal Price: " + String.format("%.2f", curTickerData.getOriginalPrice()) + "\n\tPrice Difference: " + 
                    String.format("%.2f", curTickerData.getPriceChange()) + "\n");
            
        } finally {
            lock.readLock().unlock();
        }
        out.println("done");
    }
    /**
     * sendUpdates: 
     * Sends the last 10 updates for a specific ticker that was requested by the user. 
     * 
     * @param PrintWriter out: allows us to send information to the client. 
     * @param BufferedReader in: allows us to receive information from the client.
     * @param String ticker: the specific stock the user has requested information on.
     * 
     * @throws IOException: thrown to indicate that an I/O error occurred when an
     *                      application attempts to read or write data.
     */
    public void sendUpdates(BufferedReader in, PrintWriter out, String tic) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        while(!this.setTickers.contains(tic)) {
            out.println("-1");
            tic = in.readLine();
        }
        lock.readLock().lock();
        try {
            TrackUpdates tickUpdates = this.updateTracking.get(tic);
            int start = 0;
            int end = 10;
            if(tickUpdates.getNumElements() == 10){
                start = tickUpdates.getPointer();
            } else {
                end = tickUpdates.getNumElements();
            }
            int count = 0;
            StockData[] ticHistory = tickUpdates.getUpdateArray();
            while(count<end) {
                ZonedDateTime pstTime = ticHistory[start].getQuoteTime().atZone(ZoneId.of("America/Los_Angeles"));
                String formattedDate = pstTime.format(formatter);
                out.println("Ticker: " + tic + ":\n\tBid Price: " + String.format("%.2f", ticHistory[start].getBidPrice()) + "\n\tAsk Price: " + 
                String.format("%.2f", ticHistory[start].getAskPrice()) + "\n\tLast Price: " + String.format("%.2f", ticHistory[start].getLastPrice()) + "\n\tQuote Time: " + formattedDate + 
                    "\n\tOriginal Price: " + String.format("%.2f", ticHistory[start].getOriginalPrice()) + "\n\tPrice Difference: " + String.format("%.2f", ticHistory[start].getPriceChange()) + "\n");
                count++;
                start = (start+1)%10;
            }
        } finally {
            lock.readLock().unlock();
        }
        out.println("done");
    }
    /**
     * sendMovingAvg: 
     * Sends the 5-minute moving average for a specific ticker that was requested by the user.
     * 
     * @param PrintWriter out: allows us to send information to the client. 
     * @param BufferedReader in: allows us to receive information from the client.
     * @param String ticker: the specific stock the user has requested information on.
     *       
     * @throws IOException: thrown to indicate that an I/O error occurred when an
     *                      application attempts to read or write data.
     */ 
    public void sendMovingAvg(BufferedReader in, PrintWriter out, String tic) throws IOException {
        
        while(!this.setTickers.contains(tic)) {
            out.println("-1");
            tic = in.readLine();
        }
        lock.readLock().lock();
        try {
            out.println(String.format("%.2f", this.movingAverage.get(tic).getMovingAverage()));
        } finally {
            lock.readLock().unlock();
        }
        out.println("done");
    }
    /**
     * sendMovers: 
     * Sends the top 10 biggest movers, in terms of price change, to the client.
     * 
     * @param PrintWriter out: allows us to send information to the client. 
     */ 
    public void sendMovers(PrintWriter out) {
        int count = 0; 
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        Set<String> tickerSet = new HashSet<String>();
        lock.readLock().lock();
        try {
            for(StockData s: this.movers) {
                if(count>=10){
                    break;
                }
                tickerSet.add(s.getTicker());
                ZonedDateTime pstTime = s.getQuoteTime().atZone(ZoneId.of("America/Los_Angeles"));
                String formattedDate = pstTime.format(formatter);
                out.println("\nTicker: " + s.getTicker() + ":\n\tBid Price: " + String.format("%.2f", s.getBidPrice()) + "\n\tAsk Price: " + 
                    String.format("%.2f", s.getAskPrice()) + "\n\tLast Price: " + String.format("%.2f", s.getLastPrice()) + "\n\tQuote Time: " + formattedDate + 
                    "\n\tOriginal Price: " + String.format("%.2f", s.getOriginalPrice()) + "\n\tPrice Difference: " + String.format("%.2f", s.getPriceChange()) + "\n");
                count++;
            }
            StockData s;
            if(count<10 && this.numberTickers > count) {
                for(String t: this.arrayTickers) {
                    if(count <this.numberTickers && count <10) {
                        if(!tickerSet.contains(t)) {
                            tickerSet.add(t);
                            s = this.data.get(t);
                            ZonedDateTime pstTime = s.getQuoteTime().atZone(ZoneId.of("America/Los_Angeles"));
                            String formattedDate = pstTime.format(formatter);
                            out.println("\nTicker: " + s.getTicker() + ":\n\tBid Price: " + String.format("%.2f", s.getBidPrice()) + "\n\tAsk Price: " + 
                                String.format("%.2f", s.getAskPrice()) + "\n\tLast Price: " + String.format("%.2f", s.getLastPrice()) + "\n\tQuote Time: " + formattedDate + 
                                "\n\tOriginal Price: " + String.format("%.2f", s.getOriginalPrice()) + "\n\tPrice Difference: " + String.format("%.2f", s.getPriceChange()) + "\n");
                        }
                    }
                }
                
            }
        } finally {
            lock.readLock().unlock();
        }
        out.println("done");
    }

    /**
     * displaySingleStockWithUpdates: 
     * Sends the current information on a user-specified stock as well as updates as
     * they come in. These updates last for a certain period time that is also specified by the user.
     * 
     * @param PrintWriter out: allows us to send information to the client. 
     * @param BufferedReader in: allows us to receive information from the client.
     * @param String ticker: the specific stock the user has requested information on.
     * @param String seconds: the number of seconds the user would like to subscribe to updates.
     *                        Note this is in the form of a string, because that's how the server received it.
     *       
     * @throws IOException: thrown to indicate that an I/O error occurred when an
     *                      application attempts to read or write data.
     */
    public void displaySingleStockWithUpdates(BufferedReader in, PrintWriter out, String tic, String sec) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        
        int seconds;
        while(!this.setTickers.contains(tic)) {
            out.println("-1");
            tic = in.readLine();
        }
        while(true) {
            try {
                seconds = Integer.parseInt(sec);
                break;
            } catch (Exception e) {
                out.println("-2");
                sec = in.readLine();
            }
        }
        long startTime = System.currentTimeMillis();
        this.displaySingleStockInfo(in, out, tic);
        lock.writeLock().lock();
        try {
            data.get(tic).setFlag(false);
        } finally {
            lock.writeLock().unlock();
        }
        while(System.currentTimeMillis() < (startTime + (seconds * 1000))) {
            lock.writeLock().lock();
            try {
                if(data.get(tic).getFlag()) {
                    data.get(tic).setFlag(false);
                    ZonedDateTime pstTime = data.get(tic).getQuoteTime().atZone(ZoneId.of("America/Los_Angeles"));
                    String formattedDate = pstTime.format(formatter);
                    out.println("\nTicker: " + tic + ":\n\tBid Price: " + String.format("%.2f", data.get(tic).getBidPrice()) + "\n\tAsk Price: " + 
                    String.format("%.2f", data.get(tic).getAskPrice()) + "\n\tLast Price: " + String.format("%.2f", data.get(tic).getLastPrice()) + "\n\tQuote Time: " + formattedDate + 
                        "\n\tOriginal Price: " + String.format("%.2f", data.get(tic).getOriginalPrice()) + "\n\tPrice Difference: " + String.format("%.2f", data.get(tic).getPriceChange()) + "\n");
                }
            } finally {
                lock.writeLock().unlock();
            }
        }
        out.println("finish");
    }
}
