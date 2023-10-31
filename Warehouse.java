import java.util.ArrayList;
import java.util.Iterator;

/**
 * [Warehouse.java]
 * A Warehouse class, which holds boxes and trucks
 * @author Taliesin Yip Hoi-Lee
 * @version October 2, 2023
 */

public class Warehouse {
    private final int warehouseID;
    private final ArrayList<Box> inventory = new ArrayList<Box>();
    private final ArrayList<Truck> trucks = new ArrayList<Truck>();
    private final int MAX_BOXES;
    private final int MAX_TRUCKS;

    public Warehouse(int warehouseID, int MAX_BOXES, int MAX_TRUCKS) {
        this.warehouseID = warehouseID;
        this.MAX_BOXES = MAX_BOXES;
        this.MAX_TRUCKS = MAX_TRUCKS;
    }


    /**
     * getWarehouseID
     * gets the ID of the warehouse
     * @return warehouseID
     */
    public int getWarehouseID() {
        return warehouseID;
    }

    public int getMaxBoxes() {
        return this.MAX_BOXES;
    }

    public int getMaxTrucks() {
        return this.MAX_TRUCKS;
    }

    /**
     * getInventory
     * gets the ArrayList of the warehouses inventory
     * @return ArrayList of boxes
     */
    public ArrayList<Box> getInventory() {
        return inventory;
    }

    /**
     * getTrucks
     * gets the ArrayList of trucks
     * @return ArrayList of trucks
     */
    public ArrayList<Truck> getTrucks() {
        return trucks;
    }
    
    //Receiving System Methods------------------------------------------------------------------------------------------------------------------------
    public Box getBoxById(int boxID) {
        Iterator var2 = this.inventory.iterator();

        Box box;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            box = (Box)var2.next();
        } while(box.getBoxID() != boxID);

        return box;
    }

    public Truck getTruckById(int truckID) {
        Iterator var2 = this.trucks.iterator();

        Truck truck;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            truck = (Truck)var2.next();
        } while(truck.getTruckID() != truckID);

        return truck;
    }
    
    @Override
    public String toString() {
        String data = "warehouseID: " + this.warehouseID + "\nMAX_BOXES: " + this.MAX_BOXES + "\n MAX_TRUCKS" + this.MAX_TRUCKS + "\n";

        Iterator var2;
        Box box;
        for(var2 = this.inventory.iterator(); var2.hasNext(); data = data + box.toString() + "\n") {
            box = (Box)var2.next();
        }

        data = data + "\n";

        Truck truck;
        for(var2 = this.trucks.iterator(); var2.hasNext(); data = data + truck.toString() + "\n") {
            truck = (Truck)var2.next();
        }

        return data;
    }//------------------------------------------------------------------------------------------------------------------------------------

    /**
     * addBox
     * adds a box to the warehouse storage
     * @param box Box
     */
    public void addBox(Box box){
        inventory.add(box);
    }

    /**
     * removeBox
     * removes a box from the inventory ArrayList, given that a valid box ID is given
     * @param boxID int id of box to be removed
     */
    public void removeBox(int boxID){
        for (int i = 0; i < inventory.size(); i++){
            if (inventory.get(i).getBoxID() == boxID){
                inventory.remove(inventory.get(i));
            }
        }
    }

    /**
     * adds a Truck to the trucks ArrayList
     * @param t Truck
     */
    public void addTruck(Truck t){
        trucks.add(t);
    }

    /**
     * removeTruck
     * removes a truck from the trucks ArrayList, given a valid truck ID
     * @param truckID int
     */
    public void removeTruck(int truckID){
        for (int i = 0; i < trucks.size(); i++){
            if (trucks.get(i).getTruckID() == truckID){
                trucks.remove(trucks.get(i));
            }
        }
    }

    /**
     * findTruck
     * finds a certain truck, given a truck ID
     * @param id int, the id of the truck to be found
     * @return int, index
     */
    public int findTruck(int id) {
        for (int i = 0; i < trucks.size(); i++) {
            if (trucks.get(i).getTruckID() == id) {
                return i;
            }
        }
        return -1;
    }

    /**
     * findBox
     * finds the index of a box matching the id
     * @param id
     * @return int
     */
    public int findBox(int id) {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).getBoxID() == id) {
                return i;
            }
        }
        return -1;
    }
}