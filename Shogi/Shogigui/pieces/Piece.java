package Shogigui.pieces;

import Shogigui.Board;

public class Piece {
    private int x;
    private int y;
    final private boolean is_white;
    private String file_path;
    public Board board;

    //++
    boolean is_captured;
    boolean is_promoted;
    boolean is_protected;
    boolean is_checking;
    
    public Piece(int x, int y, boolean is_white, String file_path, Board board, boolean is_promoted)
    {
        this.is_white = is_white;
        this.x = x;
        this.y = y;
        this.file_path = file_path;
        this.board = board;
        this.is_promoted=is_promoted;
    }
    
    public String getFilePath()
    {
        return file_path;
    }
    
    public void setFilePath(String path)
    {
        this.file_path = path;
    }
    
    public boolean isWhite()
    {
        return is_white;
    }
    
    public boolean isBlack()
    {
        return !is_white;
    }
    
    public void setX(int x)
    {
        this.x = x;
    }
    
    public void setY(int y)
    {
        this.y = y;
    }
    
    public int getX()
    {
        return x;
    }
    
    public int getY()
    {
        return y;
    }
    
    public boolean canMove(int destination_x, int destination_y)
    {
        return false;
    }

    public boolean is_captured()
    {
        return is_captured;
    }

    public void captured(boolean is_captured)
    {
        this.is_captured = is_captured;
    }

    public boolean is_promoted()
    {
        return is_promoted;
    }

    public void promote(boolean is_promoted)
    {
        this.is_promoted = is_promoted;
    }

    public boolean is_protected()
    {
        return is_protected;
    }

    public void set_protected(boolean is_protected)
    {
        this.is_protected = is_protected;
    }
    //
    public boolean is_checking()
    {
        return is_checking;
    }

    public void set_checking(boolean is_checking)
    {
        this.is_checking = is_checking;
    }
}
