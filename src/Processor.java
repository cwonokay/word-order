import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Processor {
    private Map<Status, Set<WorkOrder>> workOrders = new HashMap<>();

    public Processor() {
        for (Status status: Status.getAllStatus()) {
            workOrders.put(status, new HashSet<WorkOrder>());

        }
    }

    public static void main(String args[]) {
        Processor processor = new Processor();
        processor.processWorkOrders();
    }

    public void processWorkOrders() {

        try {
            while(true) {
                readIt();
                moveIt();
                writeIt();
                Thread.sleep(5000l);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
    private void moveIt() {
        // move work orders in map from one state to another
        System.out.println("These are the work orders: " + workOrders);

        Set<WorkOrder> inProgressOrders = workOrders.get(Status.IN_PROGRESS);
        System.out.println("Looking for Work Orders In Progress... ");
        if (inProgressOrders.size() > 0) {
            WorkOrder firstInProgress = inProgressOrders.iterator().next();
            inProgressOrders.remove(firstInProgress);
            firstInProgress.setStatus(Status.DONE);
            workOrders.get(Status.DONE).add(firstInProgress);
            System.out.println("****** Moved " + firstInProgress + " to Done.******");
            updateWorkOrder(firstInProgress);

        }

        Set<WorkOrder> assignedOrders = workOrders.get(Status.ASSIGNED);
        System.out.println("Looking for Assigned Work Orders... ");
        if (assignedOrders.size() > 0) {
            WorkOrder firstAssigned = assignedOrders.iterator().next();
            assignedOrders.remove(firstAssigned);
            firstAssigned.setStatus(Status.IN_PROGRESS);
            workOrders.get(Status.IN_PROGRESS).add(firstAssigned);
            System.out.println("****** Moved " + firstAssigned + " to In Progress.******");
            updateWorkOrder(firstAssigned);
        }

        Set<WorkOrder> initialOrders = workOrders.get(Status.INITIAL);
        System.out.println("Looking for new Work Orders... ");
        if (initialOrders.size() > 0) {
            WorkOrder firstInitial = initialOrders.iterator().next();
            initialOrders.remove(firstInitial);
            firstInitial.setStatus(Status.ASSIGNED);
            workOrders.get(Status.ASSIGNED).add(firstInitial);
            System.out.println("****** Moved " + firstInitial + " to Assigned.******");
            updateWorkOrder(firstInitial);
        }

        System.out.println("inital: " + initialOrders.toString());
        System.out.println("assigned: " + assignedOrders.toString());
        System.out.println("in progress: " + inProgressOrders.toString());
        System.out.println("done: " + workOrders.toString());

    }
    private void readIt() {
        // read the json files into WorkOrders and put in map
        File currentDirectory = new File(".");

        for (File f : currentDirectory.listFiles()) {
            if (f.getName().endsWith(".json")) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    WorkOrder workOrder = mapper.readValue(f, WorkOrder.class);
                    Status stat = workOrder.getStatus();
                    Set<WorkOrder> newset = workOrders.get(stat);
                    newset.add(workOrder);
                    workOrders.put(stat, newset);
                    System.out.println("This is the new workOrder: " + workOrder);

                } catch (IOException x) {

                }
            }
        }
    }

    private void writeIt(){
        for (Set<WorkOrder> order : workOrders.values()) {


        }
    }

    private void updateWorkOrder(WorkOrder workOrder) {
        try{
            File fileForJson = new File(workOrder.getId() + ".json");
            FileWriter fileWriter = new FileWriter(fileForJson);

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(workOrder);

            fileWriter.write(json);
            fileWriter.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}
