import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class Creator {
    public void createWorkOrders() {
        // read input, create work orders and write as json files
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter your name ");
        String nameOfSender = scanner.nextLine();

        System.out.println("Please enter the description ");
        String description = scanner.nextLine();


        WorkOrder order = new WorkOrder(description, nameOfSender, Status.INITIAL);


        try {
            String fileName = order.getId() + ".json";
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File(fileName), order);
            String json = mapper.writeValueAsString(order);
        } catch (IOException e) {
            System.out.println("Error in processing file.");
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        Creator creator = new Creator();
            creator.createWorkOrders();

    }
 }



