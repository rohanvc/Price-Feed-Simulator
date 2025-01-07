import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
 
public class Client {
    /**
     * mainDisplay: 
     * This function is the main display of the UI. It gives the users
     * all its potential options. This is where the user can input an option 
     * and gather information on the tickers. 
     * 
     */
    public static void mainDisplay(){
        System.out.println("\nPlease select an option:\n");
        System.out.println("\t[1] Current data for a all tickers.");
        System.out.println("\t[2] Current data for a specific ticker.");
        System.out.println("\t[3] Last 10 updates for a specific ticker.");
        System.out.println("\t[4] 5 minute moving average for a specific ticker.");
        System.out.println("\t[5] Top 10 biggest movers in terms of price change.");
        System.out.println("\t[6] Subscribe to updates for a specific ticker.");
        System.out.println("\t[0] Exit.\n");
        System.out.print("Enter choice: ");
    }

    /**
     * startUp: 
     * This function is called when the client is first starting. 
     * This is where the user inputs the number of tickers and 
     * the refresh interval requested. This then goes to the server for 
     * generation of the data. 
     * 
     * @param PrintWriter out: allows us to send information to the server. 
     * @param BufferedReader in: allows us to receive information from the server.
     *       
     * @throws IOException: thrown to indicate that an I/O error occurred when an
     *                      application attempts to read or write data.
     */
    public static void startUp(PrintWriter out, BufferedReader in) throws IOException {
        BufferedReader data = new BufferedReader(new InputStreamReader(System.in));
        System.out.println();
        System.out.println("Welcome to the Price Feed Simulator! Follow the instructions below to get started.");
        System.out.print("\nEnter the Requested Number of Tickers (1-10,000): ");
        String strNumTickers = data.readLine();
        System.out.println();
        System.out.print("Enter the Requested Update Interval (1-30 seconds): ");
        String strRefreshInterval = data.readLine();
        System.out.println();
        out.println(strNumTickers);
        out.println(strRefreshInterval);
        System.out.println();
        while(true) {
            String message = in.readLine();
            if(message.equalsIgnoreCase("done")) {
                return;
            }else if(message.equalsIgnoreCase("-1")) {
                System.out.print("\nError! Please Enter a valid Number of Tickers: ");
                out.println(data.readLine());
            } else if(message.equalsIgnoreCase("-2")) {
                System.out.print("\nError! Please Enter a valid Refresh Interval: ");
                out.println(data.readLine());
            }
        }
        
    }
    /**
     * displayCompleteTickerInformation: 
     * Receives and displays all the information stored for each indivdual stock generated. 
     * 
     * @param PrintWriter out: allows us to send information to the server. 
     * @param BufferedReader in: allows us to receive information from the server.
     *       
     * @throws IOException: thrown to indicate that an I/O error occurred when an
     *                      application attempts to read or write data.
     */
    public static void displayCompleteTickerInformation(PrintWriter out, BufferedReader in) throws IOException {
        //Tell Server to perform action 1 (display all ticker info)
        out.println("1");
        while(true) {
            String message = in.readLine();
            //If the server tells us there are no more tickers, then return to main
            if(message.equalsIgnoreCase("done")) {
                return;
            }
            System.out.println(message);
        }
    }
    /**
     * displaySpecificTickerInfo: 
     * Receives and displays all the information stored for a specific stock requested by the user. 
     * 
     * @param PrintWriter out: allows us to send information to the server. 
     * @param BufferedReader in: allows us to receive information from the server.
     * @param String ticker: the specific stock the user has requested information on.
     *       
     * @throws IOException: thrown to indicate that an I/O error occurred when an
     *                      application attempts to read or write data.
     */
    public static void displaySpecificTickerInfo(PrintWriter out, BufferedReader in, String ticker) throws IOException {
        out.println("2");
        out.println(ticker.toLowerCase());
        while(true) {
            String message = in.readLine();
            //If the server tells us we are done, then return to main
            if(message.equalsIgnoreCase("done")) {
                return;
            }else if(message.equalsIgnoreCase("-1")) {
                System.out.print("\nError, this ticker is not found. Please Enter a valid ticker: ");
                BufferedReader data = new BufferedReader(new InputStreamReader(System.in));
                String inputTicker = data.readLine();
                out.println(inputTicker);
            } else {
                System.out.println(message);
            }
        }
    }
    /**
     * receiveTicHistory: 
     * Receives and displays the last 10 updates for a specific ticker that was requested by the user. 
     * 
     * @param PrintWriter out: allows us to send information to the server. 
     * @param BufferedReader in: allows us to receive information from the server.
     * @param String ticker: the specific stock the user has requested information on.
     *       
     * @throws IOException: thrown to indicate that an I/O error occurred when an
     *                      application attempts to read or write data.
     */
    public static void receiveTicHistory(PrintWriter out, BufferedReader in, String ticker) throws IOException {
        out.println("3");
        out.println(ticker.toLowerCase());
        while(true) {
            String message = in.readLine();
            //If the server tells us we are done, then return to main
            if(message.equalsIgnoreCase("done")) {
                return;
            }else if(message.equalsIgnoreCase("-1")) {
                System.out.print("\nError, this ticker is not found. Please Enter a valid ticker: ");
                BufferedReader data = new BufferedReader(new InputStreamReader(System.in));
                String inputTicker = data.readLine();
                out.println(inputTicker);
            } else {
                System.out.println(message);
            }
        }
    }
    /**
     * receiveMovingAvg: 
     * Receives and displays the 5-minute moving average for a specific ticker 
     * that was requested by the user. 
     * 
     * @param PrintWriter out: allows us to send information to the server. 
     * @param BufferedReader in: allows us to receive information from the server.
     * @param String ticker: the specific stock the user has requested information on.
     *       
     * @throws IOException: thrown to indicate that an I/O error occurred when an
     *                      application attempts to read or write data.
     */
    public static void receiveMovingAvg(PrintWriter out, BufferedReader in, String ticker) throws IOException {
        out.println("4");
        out.println(ticker.toLowerCase());
        while(true) {
            String message = in.readLine();
            //If the server tells us we are done, then return to main
            if(message.equalsIgnoreCase("done")) {
                return;
            }else if(message.equalsIgnoreCase("-1")) {
                System.out.print("\nError, this ticker is not found. Please Enter a valid ticker: ");
                BufferedReader data = new BufferedReader(new InputStreamReader(System.in));
                String inputTicker = data.readLine();
                out.println(inputTicker);
            } else {
                System.out.println("\nThe 5 minute moving average for " + ticker + " is: " + message);
            }
        }
    }
    /**
     * receiveMovers: 
     * Receives and displays the top 10 biggest movers, in terms of price change, 
     * of all the stocks that were generated. 
     * 
     * @param PrintWriter out: allows us to send information to the server. 
     * @param BufferedReader in: allows us to receive information from the server.
     *       
     * @throws IOException: thrown to indicate that an I/O error occurred when an
     *                      application attempts to read or write data.
     */
    public static void receiveMovers(PrintWriter out, BufferedReader in) throws IOException {
        out.println("5");
        while(true) {
            String message = in.readLine();
            //If the server tells us there are no more tickers, then return to main
            if(message.equalsIgnoreCase("done")) {
                return;
            }
            System.out.println(message);
        }
    }
    /**
     * receiveMovingAvg: 
     * Receives and displays current information on a user-specified stock as well as updates as
     * they come in. These updates last for a certain period time that is also specified by the user.
     * 
     * @param PrintWriter out: allows us to send information to the server. 
     * @param BufferedReader in: allows us to receive information from the server.
     * @param String ticker: the specific stock the user has requested information on.
     * @param String seconds: the number of seconds the user would like to subscribe to updates.
     *                        Note this is in the form of a string, so that it can be passed to the server. 
     *       
     * @throws IOException: thrown to indicate that an I/O error occurred when an
     *                      application attempts to read or write data.
     */
    public static void subscriptionUpdates(PrintWriter out, BufferedReader in, String ticker, String seconds) throws IOException {
        out.println("6");
        out.println(ticker.toLowerCase());
        out.println(seconds);
        BufferedReader data = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            String message = in.readLine();
            if(message.equalsIgnoreCase("done")) {
                continue;
            }else if(message.equalsIgnoreCase("-1")) {
                System.out.print("\nError! Please Enter a Valid Ticker: ");
                out.println(data.readLine());
            } else if(message.equalsIgnoreCase("-2")) {
                System.out.print("\nError! Please Enter a Valid, Integer Number of Seconds: ");
                out.println(data.readLine());
            } else if(message.equalsIgnoreCase("finish")) {
                return;
            }else {
                System.out.println(message);
            } 
        }
    }
    /**
     * main: 
     * This is the main function. It continuously receives information and calls different 
     * functions depending on the situation. It directly receives information from the command line, as well as 
     * sets up the socket to communicate with the server. 
     * 
     * @param String[] args: array of arguments given from the command line. 
     *       
     * @throws IOException: thrown to indicate that an I/O error occurred when an
     *                      application attempts to read or write data.
     */
    public static void main(String[] args) throws IOException {
 
        System.out.println("Client is starting...");
 
        Socket socket = null;
 
        try {
            //localhost because server running on my machine otherwise IP address of Server machine
            socket = new Socket("localhost", 8081);
        } catch (Exception e) {
            System.out.println("Initializing error. Make sure that server is alive!\n" + e);
        }
 
        //use sending message to server
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        //use receiving message from server
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        startUp(out,in);
        displayCompleteTickerInformation(out, in);
        String ticker;
        while (true) {
            mainDisplay();
            //get text from user
            BufferedReader data = new BufferedReader(new InputStreamReader(System.in));
            String message = data.readLine();
            switch (message) {
                case "0":
                    break;
                case "1":
                    displayCompleteTickerInformation(out,in);
                    break;
                case "2":
                    System.out.print("\nEnter Ticker: ");
                    ticker = data.readLine();
                    displaySpecificTickerInfo(out, in, ticker);
                    break;
                case "3":
                    System.out.print("\nEnter Ticker: ");
                    ticker = data.readLine();
                    receiveTicHistory(out, in, ticker);
                    break;
                case "4":
                    System.out.print("\nEnter Ticker: ");
                    ticker = data.readLine();
                    receiveMovingAvg(out, in, ticker);
                    break;
                case "5":
                    receiveMovers(out, in);
                    break;
                case "6":
                    System.out.print("\nEnter Ticker: ");
                    ticker = data.readLine();
                    System.out.print("\nEnter an Integer Number of Seconds: ");
                    String seconds = data.readLine();
                    subscriptionUpdates(out, in, ticker, seconds);
                    break;
                case "exit":
                    break;
                default:
                    System.out.println("Invalid option, please try again.");
            }
            //close socket when receive exit
            if (message.equalsIgnoreCase("exit") || message.equals("0")) {
                out.println(message);
                out.close();
                in.close();
                data.close();
                socket.close();
                break;
            }
 
        }
 
    }
 
}