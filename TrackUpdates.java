public class TrackUpdates {
    private int pointer;
    private StockData [] prevTenUpdates;
    private int numElements;
    
    /**
     * StockData: 
     * Constructor for the tockData class. This initializes all the class variables. 
     * This class tracks the previous ten updates made to a certain stock.   
     *  
     */
    public TrackUpdates() {
        this.prevTenUpdates = new StockData [10];
        this.pointer = 0;
        this.numElements = 0;
    }

    /**
     * update: 
     * Adds an update to this stock. If this stock has less than ten updates, we add the stock to
     * the next available spot in the array. If it has more than ten updates, we replace the data 
     * of the oldest update with the new update.
     * 
     * @param StockData newData: Stock data of the newest update for a certain stock. 
     */
    public void update(StockData newData) {
        this.prevTenUpdates[this.pointer] = new StockData(newData.getTicker(),newData.getBidPrice(), newData.getAskPrice(), 
            newData.getLastPrice(), newData.getOriginalPrice(), newData.getQuoteTime());
        if(this.numElements <10) {
            this.numElements++;
        }
        this.pointer = (this.pointer + 1) % 10;
    }
    
    /**
     * getUpdateArray: 
     * Returns the class array prevTenUpdates.
     * 
     * @return StockData[]:  Returns the class array prevTenUpdates.
     */
    public StockData[] getUpdateArray() {
        return this.prevTenUpdates;
    }

    /**
     * getPointer: 
     * Returns the pointer that tells us which spot in the array we should update next.
     * 
     * @return int:  Returns the pointer that tells us which spot in the array we should update next.
     */
    public int getPointer() {
        return this.pointer;
    }

    /**
     * getNumElements: 
     * Returns the number of elements we are currently holding in our array.
     * 
     * @return int:  Returns the number of elements we are currently holding in our array.
     */
    public int getNumElements(){
        return this.numElements;
    }
}
