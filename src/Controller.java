import Model.HouseGeneration.HouseGeneration;
import Model.Level;
import Model.Settings;
import Model.Unit.Fire;
import Model.Unit.Firetrap;
import Model.Unit.Zombie.Zombie;
import View.ViewManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Iterator;

/**
 * This class controls the running of the program. It contains the main game loop and signals the gui to refresh.
 */
public class Controller
{
  private ViewManager view;

  private Level currentLevel;
  private Point heroDirection;

  //private LinkedList<Zombie> zombieList;

  public Controller()
  {
    view = new ViewManager();
    //Ask for defaults

    heroDirection = new Point(0, 0);
    // Run house generator
    HouseGeneration houseGenerator = new HouseGeneration();

    currentLevel = houseGenerator.getCurrentLevel();
    //zombieList = currentLevel.zombieList;


    view.setLevel(currentLevel);
    currentLevel.player.setDoubleLocation();

    Thread gameLoop = new GameLoop();

    gameLoop.start();

  }

  protected void processInput()
  {
    int x = 0;
    int y = 0;
    if (view.keyboard.keyDown(KeyEvent.VK_DOWN))
    {
      y += 1;
    }
    if (view.keyboard.keyDown(KeyEvent.VK_UP))
    {
      y -= 1;
    }
    if (view.keyboard.keyDown(KeyEvent.VK_LEFT))
    {
      x -= 1;
    }
    if (view.keyboard.keyDown(KeyEvent.VK_RIGHT))
    {
      x += 1;
    }
    if (view.keyboard.keyDown(KeyEvent.VK_P))
    {
      if (currentLevel.player.pickingOrPlacing())
      {
        return;
      }
      boolean onFireTrap = false;
      for (Firetrap firetrap : currentLevel.firetrapList)
      {
        if (firetrap.getHitbox().contains(currentLevel.player.getCenterLocation()))
        {
          onFireTrap = true;
          currentLevel.player.pickUpFireTrap(firetrap);
        }
      }

      onFireTrap = false;

      if (!onFireTrap && currentLevel.fireTrapCount > 0)
      {
        currentLevel.player.placeFireTrap();
      }
    }

    if (view.keyboard.keyDown(KeyEvent.VK_R))
    {
      currentLevel.player.setSpeedRun();
    } else
    {
      currentLevel.player.setSpeedWalk();
    }
    heroDirection.setLocation(x, y);
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
      while (true)
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
          secondsFromStart = (thisTime - start) / 1000;
          deltaTime = thisTime - lastTime;
        }
        while (deltaTime < Settings.REFRESH_RATE);

        synchronized (currentLevel)
        {
          view.keyboard.poll();
          processInput();
          currentLevel.player.setInputVector(heroDirection);
          currentLevel.player.update(deltaTime, secondsFromStart);

          Iterator<Zombie> zlIterator = currentLevel.zombieList.iterator();
          while (zlIterator.hasNext())
          {
            Zombie zombie = zlIterator.next();
            zombie.update(deltaTime, secondsFromStart);
            if (currentLevel.player.checkCollision(zombie.getHitbox()))
            {
              //GAME OVER
              //hero = new Player(new Point(0, 0), null);
              //houseGenerator.respawnSameMap();
              //currentLevel = houseGenerator.getCurrentLevel();

              //currentLevel.player.setDoubleLocation();
              //view.setLevel(currentLevel);
              //break;
            }

            Iterator<Firetrap> ftIterator = currentLevel.firetrapList.iterator();
            while (ftIterator.hasNext())
            {
              Firetrap ft = ftIterator.next();
              if (ft.checkCollision(zombie.getHitbox()))
              {
                ft.spawnFire();
                ftIterator.remove();
              }
              if (currentLevel.player.isRunning() && currentLevel.player.checkCollision(ft.getHitbox()))
              {
                ft.spawnFire();
                ftIterator.remove();
              }
            }

            Iterator<Fire> fireIterator = currentLevel.fireList.iterator();
            while (fireIterator.hasNext())
            {
              Fire fire = fireIterator.next();

              if (fire.checkCollision(zombie.getHitbox()))
              {
                zlIterator.remove();
                break;
              }
            }
          }

          Iterator<Fire> fireIterator = currentLevel.fireList.iterator();
          while (fireIterator.hasNext())
          {
            Fire fire = fireIterator.next();
            fire.update(deltaTime, secondsFromStart);

            if (!fire.isBurning())
            {
              Point tilePoint = fire.getTileLocation();
              currentLevel.houseTiles[tilePoint.x][tilePoint.y].burn();
              fireIterator.remove();
              break;
            }

            if (fire.checkCollision(currentLevel.player.getHitbox()))
            {
              //GAME OVER
              //hero = new Player(new Point(0, 0), null);
              //houseGenerator.respawnSameMap();
              //currentLevel = houseGenerator.getCurrentLevel();

              //currentLevel.player.setDoubleLocation();
              //view.setLevel(currentLevel);
              //break;
            }
          }
        }
        view.repaint();
        lastTime = thisTime;
      }
    }
  }


}
