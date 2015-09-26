import Model.HouseGeneration.HouseGeneration;
import Model.Level;
import Model.Settings;
import Model.Unit.Fire;
import Model.Unit.Firetrap;
import Model.Unit.Player;
import Model.GameObject;
import Model.Unit.Zombie.Zombie;
import View.KeyboardInput;
import View.SoundManager;
import View.ViewManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;

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
  //private LinkedList<Zombie> zombieList;

  public Controller()
  {
    view = new ViewManager();
    //Ask for defaults

    hero = new Player(new Point(0,0), currentLevel);

    heroDirection = new Point(0,0);
    // Run house generator
    houseGenerator = new HouseGeneration(hero);

    currentLevel = houseGenerator.getCurrentLevel();
    //zombieList = currentLevel.zombieList;

    view.setLevel(currentLevel);
    hero.setDoubleLocation();

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
    if (view.keyboard.keyDown(KeyEvent.VK_P))
    {
      //int heroCenterTileX;
      //int heroCenterTileY;

      //for (Firetrap firetrap : )


    }

    if (view.keyboard.keyDown(KeyEvent.VK_R))
    {
      hero.setSpeedRun();
    }
    else
    {
      hero.setSpeedWalk();
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
    long start;
    long secondsFromStart;
    public void run()
    {
      lastTime = System.currentTimeMillis();
      start = lastTime;
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
          secondsFromStart = (thisTime - start)/1000;
          deltaTime = thisTime - lastTime;
        }
        while(deltaTime < Settings.REFRESH_RATE);

        view.keyboard.poll();
        processInput();
        hero.setInputVector(heroDirection);
        hero.update(deltaTime, secondsFromStart);

        for (Zombie zombie : currentLevel.zombieList)
        {
          zombie.update(deltaTime,secondsFromStart);
        }


        for (Zombie zombie : currentLevel.zombieList)
        {
          if (hero.checkCollision(zombie.getHitbox()))
          {
            //GAME OVER
          }

          /*
          for (Fire fire : currentLevel.fireList)
          {
            if (zombie.checkCollision(fire.getHitbox()))
            {
              currentLevel.zombieList.remove(zombie);
            }
          }*/
        }


        view.repaint();
        lastTime = thisTime;
      }
    }
  }


}
