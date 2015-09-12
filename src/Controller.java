import Model.HouseGeneration.HouseGeneration;
import Model.Level;
import Model.Settings;
import Model.Tile.*;
import Model.Unit.Player;
import Model.Unit.Unit;
import View.ViewManager;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * This class controls the running of the program. It contains the main game loop and signals the gui to refresh.
 */
public class Controller
{
  private ViewManager view;

  private HouseGeneration houseGenerator;
  private Level currentLevel;
  private Player hero;

  //60hz loop calls
  /*
  for (u:units)
  {
    u.move();
  }
  gui.repaint();
   */
  public Controller()
  {
    view = new ViewManager();
    //Ask for defaults

    hero = new Player(new Point(0,0));
    //Run house generator
    houseGenerator = new HouseGeneration(hero);



    // First (default) house level
    currentLevel = houseGenerator.getCurrentLevel();


    System.out.println(currentLevel.toString());

 view.setLevel(currentLevel);

  }




  private void gameLoop()
  {

  }
}
