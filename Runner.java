/**  
* Runner.java 
* Contains the program driver.
* Calls to other classes to:
*     Read and formulate the user input
*     Access the database
*     Calculate the best deal
*     Update the database
*     Create an output file the details of the transaction
* @author         Tony Vo
* @author         Chace Nielson
* @author         Jared Lundy
* @author         Jordan Lundy
* @version        3.3
* @since          3.0
*/

import java.util.Arrays;
import java.util.ArrayList;

public class Runner {
    private ReadInput userInput;
    private String item;
    private int quantity;
    private ReadDatabase sqlInfo;
    private Calculator bestDeal;
    private String outputFileName = "orderform.txt";
    private OutputFile theFile;

    public void runSCM() {
        System.out.println("Welcome to the University of Calgary Supply Chain Management Ordering System!");
        System.out.println("==============================================================");
        System.out.println("Furniture item: Furniture types");
        System.out.println("Chair: Ergonomic, Executive, Kneeling, Mesh, Task");
        System.out.println("Desk: Adjustable, Standing, Traditional");
        System.out.println("Filing: Small, Medium, Large");
        System.out.println("Lamp: Desk, Swing Arm, Study");
        System.out.println("==============================================================");
        
        // Gets user input for desired furniture type
        userInput = new ReadInput(); 
        item = userInput.getFurnitureItem();
        quantity = userInput.getFurnitureQuantity(); 
        String type = userInput.getFurnitureType();
        
        // Database connector
        sqlInfo = new ReadDatabase(userInput); 
        
        try {
            bestDeal = new Calculator(sqlInfo.getResultArray(), item, String.valueOf(quantity));
            // Set bestDeal to correspond to the best of deals possible
            bestDeal.fillChosenItemsArray();
           
            if (bestDeal.getMatchFound()) {
                System.out.println("Purchase " +  bestDeal.getFormatedChosenItems() + " for $" + bestDeal.getPrice() + ".");
            } else {
                System.out.println(bestDeal.errorMessage(item, type)); // 
            }
            
            // Prepare the output file for the combination of deals
            theFile = new OutputFile(outputFileName, userInput.getRequest(), bestDeal.getChosenItems(), bestDeal.getPrice(), "", "");
            
            // Update database with items taken out of system
            sqlInfo.deleteSoldItems(bestDeal.getChosenItems());
            
            // Create the output file
            theFile.makeFile(); 
                
        } catch (Exception e) {
            System.exit(1);
        }
        
        // Close connection to the database
        sqlInfo.close();
    }
    
    public static void main(String[] args) {
        Runner runner = new Runner();
        runner.runSCM();
    }
}

