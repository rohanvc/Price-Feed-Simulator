import java.util.*;

public class MovingAverage {
    private Queue<Double> movingAvg = new LinkedList<>();
    private double sum = 0;
    
    /**
     * addLastPriceData: 
     * Adds a new data point to the 5 minute moving average.
     * 
     * @param double newData: the value to add to the moving average. 
     * @param int numPeriods: The number of periods that would be needed for a complete 
     *                        5 minute moving average, based on the refresh interval in DataGen.
     */
    public void addLastPriceData(double newData, int numPeriods) {
        this.sum += newData;
        this.movingAvg.add(newData);
        if (this.movingAvg.size() > numPeriods) {
            this.sum -= this.movingAvg.remove();  
        }
    }

    /**
     * getMovingAverage: 
     * Returns the 5 minute moving average.
     * 
     * @return double: Returns the 5 minute moving average.
     */
    public double getMovingAverage() {
        return this.movingAvg.isEmpty() ? 0 : sum/this.movingAvg.size();
    }
    
}
