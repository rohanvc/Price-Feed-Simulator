import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
 
public class Server {
    private Thread refreshThread;
    /**
     * initializeStockInfo: 
     * This function is called when the server is starting. It receives the number of tickers and
     * the refresh interval from the client. After validating the inputs, it creates data type of DataGen 
     * and returns it. 
     * 
     * @param PrintWriter out: allows us to send information to the server. 
     * @param BufferedReader in: allows us to receive information from the server.
     * 
     * @return DataGen: holds all the information relating to all the stocks that have been intialized
     *                  as well as any updates that may occur. 
     *       
     * @throws IOException: thrown to indicate that an I/O error occurred when an
     *                      application attempts to read or write data.
     */
    public static DataGen initializeStockInfo(BufferedReader in, PrintWriter out) throws IOException {
        String numTickers = in.readLine();
        String refreshInt = in.readLine();
        int numOfTickers = -1;
        int refreshInterval = -1;
        while(true) {
            try {
                numOfTickers = Integer.parseInt(numTickers);
                if(numOfTickers<1 || numOfTickers > 10000) {
                    out.println("-1");
                    numTickers = in.readLine();
                } else {
                    break;
                }
            } catch (Exception e) {
                out.println("-1");
                numTickers = in.readLine();
            }            
        }
        while(true) {
            try {
                refreshInterval = Integer.parseInt(refreshInt);
                if(refreshInterval<1 || refreshInterval > 30) {
                    out.println("-2");
                    refreshInt = in.readLine();
                } else {
                    break;
                }
            } catch (Exception e) {
                out.println("-2");
                refreshInt = in.readLine();
            }
        }
        out.println("done");
        return new DataGen(numOfTickers, refreshInterval);
    }
    /**
     * startRefreshThread: 
     * This function initializes the refresh thread and updates the data in DataGen. 
     * This runs continuously and calls the refresh function in the DataGen class. 
     * The frequency at which it calls the function depends on the refresh interval. 
     * 
     * @param DataGen stockInfo: holds all the information relating to all the stocks that have been intialized
     *                           as well as any updates that may occur.  
     */
    public void startRefreshThread(DataGen stockInfo) {
        this.refreshThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    stockInfo.refresh();
                    Thread.sleep(stockInfo.getRefreshInterval() * 1000);
                }
            } catch (InterruptedException e) {
                System.out.println("Stock info refresh thread interrupted.");
            }
        });
        refreshThread.start();
    }
    /**
     * stopThread: 
     * Interrupts the refresh thread. 
     * 
     */
    public void stopThread() {
        if (refreshThread != null) {
            refreshThread.interrupt();
        }
    }

    /**
     * main: 
     * This is the main function. It continuously receives information from the client and calls different 
     * functions depending on the situation. It sets up the server end of the socket as well.  
     * 
     * @param String[] args: array of arguments given from the command line. 
     *       
     * @throws IOException: thrown to indicate that an I/O error occurred when an
     *                      application attempts to read or write data.
     */
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        System.out.println("Server is starting...");
 
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
 
        try {
            serverSocket = new ServerSocket(8081);
            System.out.println("Server is ready!");
        } catch (Exception e) {
            System.out.println("Initializing error. Try changing port number!" + e);
        }
 
        clientSocket = serverSocket.accept();
 
        //use sending message to client
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        //use receiving message from client
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        DataGen stockInfo = initializeStockInfo(in, out);
        server.startRefreshThread(stockInfo);
        String message = null;
        String ticker;
        while (true) {
            //receive message
            message = in.readLine();
            message = message.toLowerCase();
            switch (message) {
                case "0":
                    break;
                case "1":
                    stockInfo.displayAllStockInfo(out);
                    break;
                case "2":
                    ticker = in.readLine();
                    stockInfo.displaySingleStockInfo(in,out,ticker);
                    break;
                case "3":
                    ticker = in.readLine();
                    stockInfo.sendUpdates(in, out, ticker);
                    break;
                case "4":
                    ticker = in.readLine();
                    stockInfo.sendMovingAvg(in, out, ticker);
                    break;
                case "5":
                    stockInfo.sendMovers(out);
                    break;
                case "6":
                    ticker = in.readLine();
                    String seconds = in.readLine();
                    stockInfo.displaySingleStockWithUpdates(in, out, ticker, seconds);
                    break;
                case "exit":
                    break;
                default:
                    System.out.println("Invalid Option, please try again.");
            }
 
            //close socket when receive "exit"
            if (message.equalsIgnoreCase("exit") || message.equals("0")) {
                System.out.println("Session closed!");
                out.close();
                in.close();
                clientSocket.close();
                serverSocket.close();
                server.stopThread();
                break;
            }
        }
 
    }
}