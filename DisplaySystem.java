import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import java.awt.Toolkit;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Frame;
import java.awt.Dimension;

/**
 * DisplaySystem
 * The system which displays the given WAREHOUSE
 */
public class DisplaySystem {
    private final JFrame GRAPHICS_FRAME;
    private JFrame currentTruckFrame;
    private final Warehouse WAREHOUSE;
    private Truck currentTruck;
    private JPanel buttonPanel;
    private JScrollPane boxPlacementScrollPane;
    private JScrollPane boxInfoScrollPane;
    private Box currentBox;
    private int borderWidth;
    private double scaleFactor;
    private int pageIndex = 0;
    private PlacementSystem placementSystem;
    
    private final int TITLE_FONT_SIZE;
    private final int TRUCKS_PER_PAGE;
    private final double BOTTOM_OFFSET_RATIO;
    private final double BORDER_WIDTH_RATIO;
    private final Color TRUCK_BORDER_COLOR;
    private final Color TRUCK_INTERIOR_COLOR;
    private final double BOX_LIST_WIDTH;
    private final int BOX_INFO_WIDTH;
    private final int BOX_INFO_HEIGHT;
    
    DisplaySystem(Warehouse WAREHOUSE, PlacementSystem placementSystem) {
        this.WAREHOUSE = WAREHOUSE;
        this.placementSystem = placementSystem;
        GRAPHICS_FRAME = new JFrame();
        GRAPHICS_FRAME.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        GRAPHICS_FRAME.setLayout(new BorderLayout());
        GRAPHICS_FRAME.setBackground(Color.DARK_GRAY);
        GRAPHICS_FRAME.setState(JFrame.MAXIMIZED_BOTH);
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout());
        GRAPHICS_FRAME.add(buttonPanel, BorderLayout.CENTER);
        
        TITLE_FONT_SIZE = 39;
        TRUCKS_PER_PAGE = 5;
        BOTTOM_OFFSET_RATIO = 10;
        BORDER_WIDTH_RATIO = 100;
        TRUCK_BORDER_COLOR = new Color(212, 210, 210);
        TRUCK_INTERIOR_COLOR = new Color(191, 188, 187);
        BOX_LIST_WIDTH = 12;
        BOX_INFO_WIDTH = 350;
        BOX_INFO_HEIGHT = 150;
    }

    /**
     * runLoop
     * starts the display of the truck
     */
    public void runLoop() {
        JLabel titleLabel = new JLabel("Warehouse Display", SwingConstants.CENTER);
        titleLabel.setFont(new Font("serif", Font.PLAIN, TITLE_FONT_SIZE));
        GRAPHICS_FRAME.add(titleLabel, BorderLayout.NORTH);
        createBoxInfoButtonList();
        JPanel pagesPanel = new JPanel();
        JButton lastPageButton = new JButton("back");
        lastPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pageIndex > 0){
                    for (int i2 = pageIndex * TRUCKS_PER_PAGE; i2 < (pageIndex + 1) * TRUCKS_PER_PAGE;  i2++){
                        if (i2 <= WAREHOUSE.getTrucks().size() - 1) {
                            WAREHOUSE.getTrucks().get(i2).removeButton(buttonPanel);
                        }
                    }
                    if (pageIndex - 1  >= 0) {
                        pageIndex--;
                        for (int i2 = pageIndex * TRUCKS_PER_PAGE; i2 < (pageIndex + 1) * TRUCKS_PER_PAGE; i2++) {
                            if (i2 > -1 && i2 <= WAREHOUSE.getTrucks().size() - 1) {
                                createTruckGUI(i2);
                                GRAPHICS_FRAME.revalidate();
                                GRAPHICS_FRAME.repaint();
                            }
                        }
                    }
                }
            }
        });

        pagesPanel.add(lastPageButton);
        JButton nextPageButton = new JButton("next");
        nextPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!((pageIndex + 1) * TRUCKS_PER_PAGE >= WAREHOUSE.getTrucks().size())) {
                    for (int i2 = pageIndex; i2 < (pageIndex + 1) * TRUCKS_PER_PAGE; i2++) {
                        if (i2 <= WAREHOUSE.getTrucks().size() - 1) {
                            WAREHOUSE.getTrucks().get(i2).removeButton(buttonPanel);
                        }
                    }
                    pageIndex++;
                    for (int i2 = (pageIndex) * TRUCKS_PER_PAGE; i2 < (pageIndex + 1) * TRUCKS_PER_PAGE; i2++) {
                        if (i2 <= WAREHOUSE.getTrucks().size() - 1) {
                            createTruckGUI(i2);
                            GRAPHICS_FRAME.revalidate();
                            GRAPHICS_FRAME.repaint();
                        }
                    }
                }
                GRAPHICS_FRAME.revalidate();
                GRAPHICS_FRAME.repaint();
            }
        });
        pagesPanel.add(nextPageButton);
        GRAPHICS_FRAME.add(pagesPanel, BorderLayout.SOUTH);
        if (WAREHOUSE.getTrucks().size() <= TRUCKS_PER_PAGE) {
            for (int i = 0; i < WAREHOUSE.getTrucks().size(); i++) {
                createTruckGUI(i);
            }
        } else {
            for (int i = 0; i < TRUCKS_PER_PAGE; i ++){
                createTruckGUI(i);
            }
        }
        GRAPHICS_FRAME.setVisible(true);
    }

    class GraphicsPanel extends JPanel {
        private JButton backButton;
        private JButton autoPlaceButton;
        GraphicsPanel() {
            this.setLayout(new BorderLayout());
            backButton = new JButton("Back");
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    currentTruckFrame.setFocusable(true);
                    currentTruckFrame.setVisible(false);
                    GRAPHICS_FRAME.setVisible(true);
                }
            });
            this.add(backButton, BorderLayout.SOUTH);
            this.setVisible(true);
            autoPlaceButton = new JButton("Auto place boxes");
            autoPlaceButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //Calls method from placement system
                    placementSystem.loadBoxesToTruck(WAREHOUSE.getInventory(), currentTruck);
                    updateBoxInfoButtonList();
                    updateBoxPlaceList();
                }
            });
            this.add(autoPlaceButton, BorderLayout.SOUTH);
        }

        /**
         * paintComponent
         * updates the display
         * @param g Graphics object
         */
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            //Drawing background for chosen truck
            int screenHeight = ((int) Math.round(getSize().getHeight()));
            int screenWidth = ((int) Math.round(getSize().getWidth()));
            int verticalOffset = ((int) Math.round(screenHeight / BOTTOM_OFFSET_RATIO));
            //truck length == screen height
            int scaledTruckLength;
            int scaledTruckWidth;
            borderWidth = ((int) Math.round(screenHeight / BORDER_WIDTH_RATIO));

            //ratio: width/height
            double screenRatio = screenWidth / ((double) (screenHeight - verticalOffset));

            //ratio: width/length
            double truckRatio = currentTruck.getWidth() / ((double) currentTruck.getLength());

            if (screenRatio > truckRatio) {
                scaledTruckLength = screenHeight - verticalOffset - (2 * borderWidth);
                scaleFactor = scaledTruckLength / ((double) currentTruck.getLength());
                scaledTruckWidth = ((int) Math.round(currentTruck.getWidth() * scaleFactor));
            } else {
                scaledTruckWidth = screenWidth - (2 * borderWidth);
                scaleFactor = scaledTruckWidth / ((double) currentTruck.getWidth());
                scaledTruckLength = ((int) Math.round(currentTruck.getLength() * scaleFactor));
                verticalOffset = screenHeight - scaledTruckLength - (2 * borderWidth);
            }

            backButton.setBounds(0, screenHeight - verticalOffset, ((int)Math.round(screenWidth/2.0)), verticalOffset);
            autoPlaceButton.setBounds(((int)Math.round(screenWidth/2.0)), screenHeight - verticalOffset, ((int)Math.round(screenWidth/2.0)), verticalOffset);

            g.setColor(TRUCK_BORDER_COLOR);
            g.fillRect(0, 0, scaledTruckWidth + (2 * borderWidth), scaledTruckLength + (2 * borderWidth));
            g.setColor(TRUCK_INTERIOR_COLOR);
            g.fillRect(borderWidth, borderWidth, scaledTruckWidth, scaledTruckLength);

            drawBoxes(g, borderWidth, scaleFactor, currentTruck.getBoxes());

            //Draw the box being moved at the moment
            if (currentBox != null) {
                //Draws box right on cursor location
                currentBox.draw(g, MouseInfo.getPointerInfo().getLocation().x - this.getLocationOnScreen().x, MouseInfo.getPointerInfo().getLocation().y - this.getLocationOnScreen().y, scaleFactor, borderWidth);
            }
            repaint();
        }

        /**
         * drawBoxes
         * Draws all the boxes in the given arraylist
         * @param g                Graphics component from GraphicsPanel
         * @param borderWidth      int, the width of the line border around truck
         * @param scaleFactor      double, the scale factor for the dimensions of the truck and boxes to pixels on the screen
         * @param boxes            ArrayList, list of boxes
         */
        private void drawBoxes(Graphics g, int borderWidth, double scaleFactor, ArrayList<Box> boxes) {
            Box currentBox;
            for (int i = 0; i < boxes.size(); i++) {
                currentBox = boxes.get(i);
                currentBox.draw(g, borderWidth + ((int)Math.round(currentBox.getPositionXInTruck() * scaleFactor)), borderWidth + ((int) Math.round(currentBox.getPositionYInTruck() * scaleFactor)), scaleFactor, borderWidth);
            }
        }
    }

    /**
     * updateBoxPlaceList
     * updates the box place list by deleting and recreating it
     */
    private void updateBoxPlaceList() {
        currentTruckFrame.remove(boxPlacementScrollPane);
        createBoxPlacementButtonList();
        currentTruckFrame.revalidate();
        currentTruckFrame.repaint();
    }

    private void createBoxPlacementButtonList() {
        int listWidth = ((int)Math.round(currentTruckFrame.getWidth() / BOX_LIST_WIDTH));

        JPanel tempButtonPanel = new JPanel();
        tempButtonPanel.setLayout(new BoxLayout(tempButtonPanel,BoxLayout.Y_AXIS));
        tempButtonPanel.setBorder(BorderFactory.createTitledBorder("Unplaced boxes:"));
        tempButtonPanel.setPreferredSize(new Dimension(listWidth , currentTruckFrame.getHeight()));

        for (int i = 0; i < WAREHOUSE.getInventory().size(); i++){
            Box box = WAREHOUSE.getInventory().get(i);
            JButton button = new JButton("Box " + box.getBoxID());
            button.addActionListener(new BoxPlacementButtonListener(box));
            tempButtonPanel.add(button);
        }

        boxPlacementScrollPane = new JScrollPane(tempButtonPanel);
        boxPlacementScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        boxPlacementScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        currentTruckFrame.add(boxPlacementScrollPane, BorderLayout.EAST);
    }

    private void updateBoxInfoButtonList() {
        GRAPHICS_FRAME.remove(boxInfoScrollPane);
        createBoxInfoButtonList();
        GRAPHICS_FRAME.revalidate();
        GRAPHICS_FRAME.repaint();
    }

    private void createBoxInfoButtonList() {
        int listWidth = ((int)Math.round(GRAPHICS_FRAME.getWidth() / BOX_LIST_WIDTH));

        JPanel tempButtonPanel = new JPanel();
        tempButtonPanel.setLayout(new BoxLayout(tempButtonPanel,BoxLayout.Y_AXIS));
        tempButtonPanel.setBorder(BorderFactory.createTitledBorder("Unplaced boxes:"));
        tempButtonPanel.setPreferredSize(new Dimension(listWidth , GRAPHICS_FRAME.getHeight()));

        for (int i = 0; i < WAREHOUSE.getInventory().size(); i++){
            Box box = WAREHOUSE.getInventory().get(i);
            JButton button = new JButton("Box " + box.getBoxID());
            button.addActionListener(new BoxInfoButtonListener(box));
            tempButtonPanel.add(button);
        }

        boxInfoScrollPane = new JScrollPane(tempButtonPanel);
        boxInfoScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        boxInfoScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        GRAPHICS_FRAME.add(boxInfoScrollPane, BorderLayout.EAST);
    }

    /**
     * createTruckGUI
     * creates the GUI Components of each seperate Truck, and adds the gui to the main Frame
     * @param index int index
     */
    private void createTruckGUI(int index1) {
      final int INDEX = index1;
        WAREHOUSE.getTrucks().get(INDEX).drawList(buttonPanel).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentTruck = WAREHOUSE.getTrucks().get(INDEX);
                currentTruckFrame = new JFrame("Truck " + currentTruck.getTruckID());
                currentTruckFrame.setUndecorated(false);
                currentTruckFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
                currentTruckFrame.setState(Frame.MAXIMIZED_BOTH);//ideally sets the frame to full screen
                final GraphicsPanel graphicsPanel = new GraphicsPanel();

                graphicsPanel.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (currentBox != null) {
                            int unscaledX = ((int)Math.round((e.getLocationOnScreen().x - graphicsPanel.getLocationOnScreen().x - borderWidth) / scaleFactor));
                            int unscaledY = ((int)Math.round((e.getLocationOnScreen().y - graphicsPanel.getLocationOnScreen().y - borderWidth) / scaleFactor));

                            if (((currentTruck.getLength() - unscaledY) - currentBox.getLength() >= 0) && ((currentTruck.getLength() - unscaledY) - currentBox.getLength() < currentTruck.getLength()) && (unscaledX >= 0) && (unscaledX < currentTruck.getWidth())) {
                                if (placementSystem.loadBoxToTruck(currentBox, currentTruck, (currentTruck.getLength() - unscaledY) - currentBox.getLength(), unscaledX)) {
                                    WAREHOUSE.getInventory().remove(WAREHOUSE.findBox(currentBox.getBoxID()));
                                    updateBoxPlaceList();
                                    updateBoxInfoButtonList();
                                }
                            }
                            currentBox = null;
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                    }
                });

                currentTruckFrame.add(graphicsPanel);
                currentTruckFrame.setVisible(true);
                createBoxPlacementButtonList();
            }
        });
        buttonPanel.repaint();
    }

    class BoxInfoButtonListener implements ActionListener {
        private Box box;
        BoxInfoButtonListener(Box box) {
            this.box = box;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame boxInfoFrame = new JFrame("Box Info");
            String boxInfo = "<html>Box ID: " + box.getBoxID() + "<br>weight: " + box.getWeight() + "<br>height: " + box.getHeight() + "<br>length: " + box.getLength() + "<br>width:" + box.getWeight() + "</html>";
            JLabel boxInfoLabel = new JLabel(boxInfo);
            boxInfoFrame.add(boxInfoLabel);
            boxInfoFrame.setSize(BOX_INFO_WIDTH, BOX_INFO_HEIGHT);
            boxInfoFrame.setVisible(true);
        }
    }

    class BoxPlacementButtonListener implements ActionListener {
        private Box box;
        BoxPlacementButtonListener(Box box) {
            this.box = box;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            currentBox = box;
        }
    }
}