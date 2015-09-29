import Model.HouseGeneration.HouseGeneration;
import Model.Level;
import Model.Settings;
import Model.Unit.Fire;
import Model.Unit.Firetrap;
import Model.Unit.Zombie.Zombie;
import Model.Unit.Zombie.ZombieLine;
import Model.Unit.Zombie.ZombieMaster;
import Model.Unit.Zombie.ZombieRandom;
import View.ViewManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.Set;

/**
 * This class controls the running of the program. It contains the main game loop and signals the gui to refresh.
 */
public class Controller
{
  private final ViewManager VIEW;

  private HouseGeneration houseGenerator;
  private Level currentLevel;
  private Point heroDirection;

  public Controller()
  {
    VIEW = new ViewManager();
    //Ask for defaults

    heroDirection = new Point(0, 0);
    // Run house generator
    houseGenerator = new HouseGeneration();

    currentLevel = houseGenerator.getCurrentLevel();
    //ZOMBIES = currentLevel.ZOMBIES;


    VIEW.setLevel(currentLevel);
    currentLevel.PLAYER.setDoubleLocation();




    //Setup fire's static members especially graphics
    Object dump = new Fire(0,0);

    while (VIEW.isPaused())
    {
      try
      {
        Thread.sleep(Settings.REFRESH_RATE);
      } catch (InterruptedException e)
      {
        e.printStackTrace();
      }

    }

    startGameLoop();
  }

  public void startGameLoop()
  {
    Thread gameLoop = new GameLoop();
    gameLoop.start();
  }

  private void processInput()
  {
    int x = 0;
    int y = 0;
    if (VIEW.KEYBOARD.keyDown(KeyEvent.VK_DOWN) || VIEW.KEYBOARD.keyDown((KeyEvent.VK_S)))
    {
      y += 1;
    }
    if (VIEW.KEYBOARD.keyDown(KeyEvent.VK_UP) || VIEW.KEYBOARD.keyDown((KeyEvent.VK_W)))
    {
      y -= 1;
    }
    if (VIEW.KEYBOARD.keyDown(KeyEvent.VK_LEFT) || VIEW.KEYBOARD.keyDown((KeyEvent.VK_A)))
    {
      x -= 1;
    }
    if (VIEW.KEYBOARD.keyDown(KeyEvent.VK_RIGHT) || VIEW.KEYBOARD.keyDown((KeyEvent.VK_D)))
    {
      x += 1;
    }
    if (VIEW.KEYBOARD.keyDown(KeyEvent.VK_P))
    {
      if (currentLevel.PLAYER.pickingOrPlacing())
      {
        return;
      }
      boolean onFireTrap = false;
      for (Firetrap firetrap : currentLevel.FIRETRAPS)
      {
        if (firetrap.getHitbox().contains(currentLevel.PLAYER.getCenterLocation()))
        {
          onFireTrap = true;
          currentLevel.PLAYER.pickUpFireTrap(firetrap);
        }
      }

      if (!onFireTrap && currentLevel.fireTrapCount > 0)
      {
        currentLevel.PLAYER.placeFireTrap();
      }
    }

    if (VIEW.KEYBOARD.keyDown(KeyEvent.VK_R))
    {
      currentLevel.PLAYER.setSpeedRun();
    } else
    {
      currentLevel.PLAYER.setSpeedWalk();
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
          VIEW.KEYBOARD.poll();
          processInput();
          currentLevel.PLAYER.setInputVector(heroDirection);
          currentLevel.PLAYER.update(deltaTime);

          if( currentLevel.PLAYER.checkCollision(currentLevel.EXIT.getHitbox()))
          {
            playerWinLevel();
          }

          currentLevel.MASTER.update(deltaTime);

          if (currentLevel.MASTER.checkCollision(currentLevel.PLAYER.getHitbox()))
          {
            playerDeath();
            continue;
          }

          Iterator<Zombie> zlIterator = currentLevel.ZOMBIES.iterator();
          while (zlIterator.hasNext())
          {
            Zombie zombie = zlIterator.next();
            zombie.update(deltaTime);
            if (zombie.knowsPlayerLocation())
            {
              currentLevel.MASTER.setAnOtherZombieKnowsTrue();
            }
            else
            {
              currentLevel.MASTER.setAnOtherZombieKnowsFalse();
            }

            if (currentLevel.PLAYER.checkCollision(zombie.getHitbox()))
            {
              //GAME OVER
              playerDeath();
              break;
            }

            Iterator<Firetrap> ftIterator = currentLevel.FIRETRAPS.iterator();
            while (ftIterator.hasNext())
            {
              Firetrap ft = ftIterator.next();
              if (ft.checkCollision(zombie.getHitbox()))
              {
                ft.spawnFire();
                ftIterator.remove();
              }
              if (currentLevel.PLAYER.isRunning() && currentLevel.PLAYER.checkCollision(ft.getHitbox()))
              {
                ft.spawnFire();
                ftIterator.remove();
              }
            }

            for (Fire fire : currentLevel.FIRES)
            {
              if (fire.checkCollision(zombie.getHitbox()))
              {
                zlIterator.remove();
                break;
              }
            }
          }

          Iterator<Fire> fireIterator = currentLevel.FIRES.iterator();
          while (fireIterator.hasNext())
          {
            Fire fire = fireIterator.next();
            fire.update(deltaTime);

            if (!fire.isBurning())
            {
              Point tilePoint = fire.getTileLocation();
              currentLevel.TILES[tilePoint.x][tilePoint.y].burn();
              fireIterator.remove();
              break;
            }

            if (fire.checkCollision(currentLevel.PLAYER.getHitbox()))
            {
              //GAME OVER
              playerDeath();
              break;
            }
          }
        }

        VIEW.repaint();
        lastTime = thisTime;
      }
    }

    private void playerDeath()
    {
      houseGenerator.respawnSameMap();
      currentLevel = houseGenerator.getCurrentLevel();
      currentLevel.PLAYER.setDoubleLocation();
      VIEW.setLevel(currentLevel);
    }

    private void playerWinLevel()
    {
      houseGenerator.spawnNewLevel();
      currentLevel = houseGenerator.getCurrentLevel();
      currentLevel.PLAYER.setDoubleLocation();
      VIEW.setLevel(currentLevel);
    }
  }


}
