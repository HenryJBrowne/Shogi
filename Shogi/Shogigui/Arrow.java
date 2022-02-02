package Shogigui;

import java.io.File;

public class Arrow{

    private final String board_images_file_path = "images" + File.separator + "board" + File.separator; // ++ 
    private final String arrows_file_path = board_images_file_path + File.separator + "arrows" + File.separator;
    private final String gold_arrows_file_path = arrows_file_path + "gold_arrows" + File.separator;
    private final String green_arrows_file_path = arrows_file_path + "green_arrows" + File.separator;

    private String Direction;
    private int x;
    private int y;
    private String col;

    public Arrow(String Direction, int x, int y, String col){

        this.Direction=Direction;
        this.x=x;
        this.y=y;
        this.col=col;
    }

    public String getDirection() {
        return Direction;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public String getFilePath() {

        String directionFilePath="";

        if (Direction=="N"){
            directionFilePath= "arrow_up.png";
        }
        if (Direction=="NE"){
            directionFilePath= "arrow_left.png";
        }
        if (Direction=="NW"){
            directionFilePath= "arrow_up_left.png";
        }
        if (Direction=="E"){
            directionFilePath= "arrow_right.png";
        }
        if (Direction=="S"){
            directionFilePath= "arrow_down.png";
        }
        if (Direction=="SE"){
            directionFilePath= "arrow_down_right.png";
        }
        if (Direction=="SW"){
            directionFilePath= "arrow_down_left.png";
        }
        if (Direction=="W"){
            directionFilePath= "arrow_left.png";
        }

        if (col.equals("gold")){
            return gold_arrows_file_path+directionFilePath;
        }
        if (col.equals("green")){
            return green_arrows_file_path+directionFilePath;
        }
        else{
            return arrows_file_path+directionFilePath;
        }
    }
}