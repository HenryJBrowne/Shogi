package Shogigui;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

class ImageFactory { //add interface?     ++ check with factory class design pattern

    private static Image NULL_IMAGE = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
   
    private String image_file_path;
    private Image image;
    private Rectangle2D rect;
    
    public ImageFactory(String image_file_path, double x, double y) {

        this.image_file_path=image_file_path;
        this.image = loadImage(image_file_path);
        this.rect = new Rectangle2D.Double(x, y, this.image.getWidth(null), this.image.getHeight(null));
    }
    
    public void drawImage(Graphics2D g2) {
        Rectangle2D bounds = rect.getBounds2D();
        g2.drawImage(image, (int) bounds.getMinX(), (int) bounds.getMinY(), (int) bounds.getMaxX(),
                (int) bounds.getMaxY(),
                0, 0, image.getWidth(null), image.getHeight(null), null);
    }

    public Image loadImage(String image_file_path) {
        try {
            return ImageIO.read(new File(image_file_path));
        } catch (IOException e) {
            return NULL_IMAGE;
        }
    }

    public Image getImage()
    {
        return this.image;
    }
    public Rectangle2D getRect()
    {
        return this.rect;
    }
    public String getFilePath()
    {
        return this.image_file_path;
    }
}