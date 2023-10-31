import java.util.ArrayList;
import java.awt.Color;
import java.io.IOException;
import java.io.File;

/**
 * Software Engineering Project - Display
 * A software solution for a courier company, which plans cargo layout
 * @author Taliesin Yip Hoi-Lee, Julius Qi
 * @version October 2, 2023
 */
public class Main {
    public static void main(String[] args) throws IOException{
        final int MAX_BOXES = 100;
        final int MAX_TRUCKS = 10;

        Warehouse warehouse = new Warehouse(1, MAX_BOXES, MAX_TRUCKS);
        ReceivingSystem receivingSystem = new ReceivingSystem(warehouse, new OnExit());
        receivingSystem.start();
    }

    static class OnExit implements SystemExitListener {
        @Override
        public void onExit(Warehouse warehouse){
            PlacementSystem placementSystem = new PlacementSystem();
            DisplaySystem displaySystem = new DisplaySystem(warehouse, placementSystem);
            displaySystem.runLoop();
        }
    }
}