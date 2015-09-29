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
 * The Controller class is responsible for holding and updating the game loop. It tries to update every 1/60th of a second.
 *
 * On the program level, Controller is responsible for dictating when things move.
 *
 * On the class level, the Controller has touches both the model and the view. It has a pointer to the current level which is essentially the model
 * and a pointer to the view. It tells both to update. It goes through the data in the model and tells it to update.
 * It checks for death conditions for the zombies and the player and updates the model accordingly. It also tells the view to repaint
 * after every update cycle.
 *
 * only the Main class has an instance of the controller as that is where zombiehouse is entered from
 */

public class Controller
{
  private final ViewManager VIEW;

  private HouseGeneration houseGenerator;
  private  Level currentLevel;
  private Point heroDirection;

  public Controller()
  {
    //Ask for defaults
    VIEW = new ViewManager();

    heroDirection = new Point(0, 0);

    // Run house generator
    houseGenerator = new HouseGeneration();

    currentLevel = houseGenerator.getCurrentLevel();

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

  /**
   * A simple function which spawns and starts the GameLoop
   */
  public void startGameLoop()
  {
    Thread gameLoop = new GameLoop();
    gameLoop.start();
  }

  private void processInput()
  {
    int x = 0;
    int y = 0;
    //movement checks
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

    //picking up firetrap checks
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

    //running checks
    if (VIEW.KEYBOARD.keyDown(KeyEvent.VK_R))
    {
      currentLevel.PLAYER.setSpeedRun();
    } else
    {
      currentLevel.PLAYER.setSpeedWalk();
    }

    //set the hero direction to the user's input e.g 1,0 for holding the right/D key
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
          VIEW.KEYBOARD.poll(); //find what keys are pressed this update
          processInput(); //get the direciton the player wants to move
          currentLevel.PLAYER.setInputVector(heroDirection);
          currentLevel.PLAYER.update(deltaTime); //update the player

          if( currentLevel.PLAYER.checkCollision(currentLevel.EXIT.getHitbox()))
          {
            //if the player finds the exit, win the live
            playerWinLevel();
          }

          currentLevel.MASTER.update(deltaTime); //update the master zombie

          if (currentLevel.MASTER.checkCollision(currentLevel.PLAYER.getHitbox())) //check if the master zombie has killed the player
          {
            playerDeath();
            continue;
          }

          Iterator<Zombie> zlIterator = currentLevel.ZOMBIES.iterator(); //loop through the existing zombies on the level
          while (zlIterator.hasNext())
          {
            Zombie zombie = zlIterator.next();
            zombie.update(deltaTime); //update zombie
            //if zombie knows where the player is, let the master zombie know that he also knows where the player is
            if (zombie.knowsPlayerLocation())
            {
              currentLevel.MASTER.setAnOtherZombieKnowsTrue();
            }
            else
            {
              currentLevel.MASTER.setAnOtherZombieKnowsFalse();
            }

            //if a zombie hits a player
            if (currentLevel.PLAYER.checkCollision(zombie.getHitbox()))
            {
              //GAME OVER
              playerDeath();
              break;
            }

            //iterate over all the firetraps in the level
            Iterator<Firetrap> ftIterator = currentLevel.FIRETRAPS.iterator();
            while (ftIterator.hasNext())
            {
              Firetrap ft = ftIterator.next();
              if (ft.checkCollision(zombie.getHitbox())) //if zombie hits fire trap, make sure it explodes
              {
                ft.spawnFire();
                ftIterator.remove();
              }
              //if a player is running and hits a fire trap, explode it
              if (currentLevel.PLAYER.isRunning() && currentLevel.PLAYER.checkCollision(ft.getHitbox()))
              {
                ft.spawnFire();
                ftIterator.remove();
              }
            }

            //remove zombies who hit fires
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

            //if player intersects with fire, reset level
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
