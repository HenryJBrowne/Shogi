package Shogigui;

/**
 * The Shogigui class start the program and generate the program GUI
 * 
 * @Author Henry Browne – 37733273
 */
public class Shogigui {

    private MenuFrame menuframe;

    public static void main(String[] args) {
        Shogigui gui = new Shogigui();

        gui.menuframe = new MenuFrame();
        gui.menuframe.setVisible(true);

    }
}
