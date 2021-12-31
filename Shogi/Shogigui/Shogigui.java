package Shogigui;

public class Shogigui {

    private BoardFrame boardframe;
    private MenuFrame menuframe;

    public static void main(String[] args) {
        Shogigui gui = new Shogigui();

        gui.menuframe = new MenuFrame();
        gui.menuframe.setVisible(true); 

        while (true) { //++break when menu window is closed 

            synchronized(gui){} //fix threading at the start of program...?

            if (gui.menuframe.menu.PlayPressed == true) {   

                gui.menuframe.dispose(); // visible false...?

                gui.boardframe = new BoardFrame(gui.menuframe.menu.PlayerIsWhite, gui.menuframe.menu.HintsOn, gui.menuframe.menu.TutorialOn); //use accessor methods?
                gui.boardframe.setVisible(true);
                break;

            }       
        }
    }
}
