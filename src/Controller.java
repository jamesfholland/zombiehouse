import Model.HouseGeneration.HouseGeneration;
import Model.Level;
import Model.Settings;
import Model.Unit.Player;
import Model.GameObject;
import View.KeyboardInput;
import View.ViewManager;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * This class controls the running of the program. It contains the main game loop and signals the gui to refresh.
 */
public class Controller
{
  private ViewManager view;

  private HouseGeneration houseGenerator;
  private Level currentLevel;
  private Player hero;
  private Thread gameLoop;
  private Point heroDirection;

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

    hero = new Player(new Point(0,0), currentLevel);

    heroDirection = new Point(0,0);
    //Run house generator
    houseGenerator = new HouseGeneration(hero);



    // First (default) house level
    currentLevel = houseGenerator.getCurrentLevel();


    System.out.println(currentLevel.toString());

    view.setLevel(currentLevel);

    gameLoop = new GameLoop();

    gameLoop.start();

  }

  protected void processInput()
  {
    int x = 0;
    int y = 0;
    if (view.keyboard.keyDown(KeyEvent.VK_DOWN))
    {
      y+=1;
    }
    if (view.keyboard.keyDown(KeyEvent.VK_UP))
    {
      y-=1;
    }
    if (view.keyboard.keyDown(KeyEvent.VK_LEFT))
    {
      x-=1;
    }
    if (view.keyboard.keyDown(KeyEvent.VK_RIGHT))
    {
      x+=1;
    }
    heroDirection.setLocation(x,y);
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

        view.keyboard.poll();
        processInput();
        hero.move(heroDirection, deltaTime);

        view.repaint();
        lastTime = thisTime;
      }
    }
  }


}
