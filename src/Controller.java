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
import java.util.Set;

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
  private Thread gameLoop;

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

    gameLoop = new GameLoop();

    gameLoop.start();

  }

  /*

  How the the game processes keyboard input and moves the hero around
  need to talk to you guys about how to get model data here
*/
  protected void processInput()
  {
    if (keyboard.keyDown(KeyEvent.VK_DOWN))
    {
      hero.moveUnit(0,2);
    }
    if (keyboard.keyDown(KeyEvent.VK_UP))
    {
      hero.moveUnit(0, -2);
    }
    if (keyboard.keyDown(KeyEvent.VK_LEFT))
    {
      hero.moveUnit(-2, 0);
    }
    if (keyboard.keyDown(KeyEvent.VK_RIGHT))
    {
      hero.moveUnit(2, 0);
    }
  }

  /**
   * This is a private class for handling the game loop.
   */
  private class GameLoop extends Thread
  {
    long lastTime;
    long thisTime;
    long deltaTime;
    public void run()
    {
      lastTime = System.currentTimeMillis();
      while(true)
      {
        do
        {
          try
          {
            Thread.sleep(1L);
          }
          catch (InterruptedException e)
          {
            break; //Something interrupted us probably application closing, break to kill refreshing.
          }
          thisTime = System.currentTimeMillis();
          deltaTime = thisTime - lastTime;

        }
        while(deltaTime < Settings.REFRESH_RATE);


        processInput();

        view.repaint();

      }
    }

  }

}
