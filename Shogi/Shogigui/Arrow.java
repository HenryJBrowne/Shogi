package Shogigui;

import java.io.File;

public class Arrow {

    private final String board_images_file_path = "images" + File.separator + "board" + File.separator; // ++
    private final String arrows_file_path = board_images_file_path + File.separator + "arrows" + File.separator;
    private final String gold_arrows_file_path = arrows_file_path + "gold_arrows" + File.separator;
    private final String green_arrows_file_path = arrows_file_path + "green_arrows" + File.separator;

    private String Direction;
    private int x;
    private int y;
    private String col;

    /**
     * The Arrow constuctor method is used to intilize an arrow object instance
     * 
     * @param Direction The compass direction where the arrow points
     * @param x         The location of the arrow position on the grids x axis
     * @param y         The location of the arrow position on the grids y axis
     * @param col       The colour of the arrow (ie black, green, gold)
     */
    public Arrow(String Direction, int x, int y, String col) {

        this.Direction = Direction;
        this.x = x;
        this.y = y;
        this.col = col;
    }

    /**
     * getFilePath accessor method, retrieves arrow file path corresponding to arrow
     * direction
     * 
     * @return the file path used to locate the image of the arrow
     */
    public String getFilePath() {

        String directionFilePath = "";

        if (Direction == "N") {
            directionFilePath = "arrow_up.png";
        }
        if (Direction == "NE") {
            directionFilePath = "arrow_up_right.png";
        }
        if (Direction == "NW") {
            directionFilePath = "arrow_up_left.png";
        }
        if (Direction == "E") {
            directionFilePath = "arrow_right.png";
        }
        if (Direction == "S") {
            directionFilePath = "arrow_down.png";
        }
        if (Direction == "SE") {
            directionFilePath = "arrow_down_right.png";
        }
        if (Direction == "SW") {
            directionFilePath = "arrow_down_left.png";
        }
        if (Direction == "W") {
            directionFilePath = "arrow_left.png";
        }

        if (col.equals("gold")) {
            return gold_arrows_file_path + directionFilePath;
        }
        if (col.equals("green")) {
            return green_arrows_file_path + directionFilePath;
        } else {
            return arrows_file_path + directionFilePath;
        }
    }

    /**
     * getDirection accessor method
     * 
     * @return The compas direction the arrow is pointing
     */
    public String getDirection() {
        return Direction;
    }

    /**
     * getX accessor method
     * 
     * @return location of an arrow position in the board grid on the x axis
     */
    public int getX() {
        return x;
    }

    /**
     * getY accessor method
     * 
     * @return location of an arrow position in the board grid on the y axis
     */
    public int getY() {
        return y;
    }

    /**
     * getCol accessor method
     * 
     * @return the colour of the arrow
     */
    public String getCol() {
        return col;
    }

    /**
     * setCol set method
     * 
     * @param colour The new colour of the arrow
     */
    public void setCol(String colour) {
        this.col = colour;
    }
}