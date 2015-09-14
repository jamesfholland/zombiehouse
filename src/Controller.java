import Model.HouseGeneration.HouseGeneration;
import Model.Level;
import Model.Settings;
import Model.Tile.*;
import Model.Unit.Player;
import Model.Unit.Unit;
import View.ViewManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * This class controls the running of the program. It contains the main game loop and signals the gui to refresh.
 */
public class Controller
{
  private ViewManager view;
  private KeyboardInput keyboard = new KeyboardInput();

  private HouseGeneration houseGenerator;
  private Level currentLevel;
  private Player hero;

  //60hz loop calls
  /*
  keyboard.poll();
  processInput();
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



  /*

  How the the game processes keyboard input and moves the hero around
  need to talk to you guys about how to get model data here

  protected void processInput()
  {
    if (keyboard.keyDown(KeyEvent.VK_DOWN))
    {
      model.hero.location.setLocation(model.hero.getLocationX(),model.hero.getLocationY()+2);
    }
    if (keyboard.keyDown(KeyEvent.VK_UP))
    {
      model.hero.location.setLocation(model.hero.getLocationX(), model.hero.getLocationY() - 2);
    }
    if (keyboard.keyDown(KeyEvent.VK_LEFT))
    {
      model.hero.location.setLocation(model.hero.getLocationX()-2,model.hero.getLocationY());
    }
    if (keyboard.keyDown(KeyEvent.VK_RIGHT))
    {
      model.hero.location.setLocation(model.hero.getLocationX()+2,model.hero.getLocationY());
    }
  }*/


}
