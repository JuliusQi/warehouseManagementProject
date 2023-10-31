import java.awt.Color;
import java.awt.Graphics;

/**
 * [Box.java]
 * A box
 * @author Julius Qi
 * @version October 2nd, 2023
 */

public class Box implements Comparable<Box>{
    private final int boxID;
    private final int weight;
    private int length;
    private int width;
    private final int height;
    private int positionXInTruck;
    private int positionYInTruck;
    private int positionZInTruck;
    private final Color color;

    public Box(int boxID, int weight, int length, int width, int height, Color color) {
        this.positionXInTruck = -1;
        this.positionYInTruck = -1;
        this.positionZInTruck = -1;
        this.boxID = boxID;
        this.weight = weight;
        this.height = height;
        this.length = length;
        this.width = width;
        this.color = color;
    }

    public Box(int boxID, int weight, int length, int width, int height, int positionXInTruck, int positionYInTruck, int positionZInTruck, java.awt.Color color) {
        this.positionXInTruck = positionXInTruck;
        this.positionYInTruck = positionYInTruck;
        this.positionZInTruck = positionZInTruck;
        this.boxID = boxID;
        this.weight = weight;
        this.height = height;
        this.length = length;
        this.width = width;
        this.color = color;
    }

    /**
     * rotate
     * rotates the Box
     */
    public void rotate() {
        int length = this.length;
        this.length = this.width;
        this.width = length;
    }

    /**
     * draw
     * Draws the box
     * @param g Graphics object from GraphicsPanel
     * @param x int, the x coordinate where the box will be drawn
     * @param y int, the y coordinate where the box will be drawn
     * @param scaleFactor double, the factor by which the size of the boxes will be scaled
     */
    public void draw(Graphics g, int x, int y, double scaleFactor, int borderWidth) {
        g.setColor(color.darker());
        g.fillRect(x, y, (int)Math.round(width * scaleFactor), (int)Math.round(length * scaleFactor));
        g.setColor(color);
        g.fillRect(x + borderWidth, y + borderWidth, ((int)Math.round(width * scaleFactor)) - (2 * borderWidth), ((int)Math.round(length * scaleFactor)) - (2 * borderWidth));
    }

    /**
     * setPositionXInTruck
     * sets the x position of the box in the truck
     * @param positionXInTruck int, the x position of the box within the truck
     */
    public void setPositionXInTruck(int positionXInTruck) {
        this.positionXInTruck = positionXInTruck;
    }

    /**
     * setPositionYInTruck
     * sets the y position of the box in the truck
     * @param positionYInTruck int, the y position of the box within the truck
     */
    public void setPositionYInTruck(int positionYInTruck) {
        this.positionYInTruck = positionYInTruck;
    }

    /**
     * getBoxID
     * gets the ID of the box
     * @return int boxID
     */
    public int getBoxID() {
        return boxID;
    }

    /**
     * getWeight
     * gets the weight of the box
     * @return int weight
     */
    public int getWeight() {
        return weight;
    }

    /**
     * getHeight
     * gets the height of the box
     * @return int height
     */
    public int getHeight() {
        return height;
    }

    /**
     * getLength
     * gets the length of the box
     * @return int length
     */
    public int getLength() {
        return length;
    }

    /**
     * getWidth
     * gets the width of the box
     * @return int width
     */
    public int getWidth() {
        return width;
    }

    /**
     * getPositionXInTruck
     * gets the x position of the box in the truck
     * @return int positionXInTruck
     */
    public int getPositionXInTruck() {
        return positionXInTruck;
    }

    /**
     * getPositionYInTruck
     * gets the y position of the box in the truck
     * @return int positionYInTruck
     */
    public int getPositionYInTruck() {
        return positionYInTruck;
    }

    //Receiving System methods------------------------------------------------------------------------
    public String toFileFormat() {
        return this.boxID + "\n" + this.weight + "\n" + this.length + "\n" + this.width + "\n" + this.height + "\n" + this.positionXInTruck + "\n" + this.positionYInTruck + "\n" + this.positionZInTruck + "\n" + this.color.getRed() + "," + this.color.getGreen() + "," + this.color.getBlue();
    }
    
    /**
     * toString
     * converts all data in the box instance into a string
     */
    public String toString() {
        return "Box{boxID=" + this.boxID + ", weight=" + this.weight + ", length=" + this.length + ", width=" + this.width + ", height=" + this.height + ", positionXInTruck=" + this.positionXInTruck + ", positionYInTruck=" + this.positionYInTruck + ", positionZInTruck=" + this.positionZInTruck + ", color=" + this.color + '}';
    }

    //Placement System methods------------------------------------------------------------------------
    public void setCords(int x, int y) {
        positionXInTruck = x;
        positionYInTruck = y;
    }
    
    public int compareTo(Box other) {
        if (this.width < this.length) {
            this.rotate();
        }
        if (other.getWidth() < other.getLength()) {
            other.rotate();
        }
        return other.getLength() - this.length;
    }

    public int getId() {
        return boxID;
    }//-----------------------------------------------------------------------------------------------
}