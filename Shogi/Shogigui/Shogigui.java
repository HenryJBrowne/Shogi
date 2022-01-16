package Shogigui;

public class Shogigui {

    private MenuFrame menuframe;

    public static void main(String[] args) {
        Shogigui gui = new Shogigui();

        gui.menuframe = new MenuFrame();
        gui.menuframe.setVisible(true); 

    }
}
