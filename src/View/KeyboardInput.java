package View;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * KeyboardInput is the class responsible for listening to all of the user's keyboard input
 *
 * On a program wide level, the KeyboardInput deals with view and controller. It is added to the GamePanel which displays the game
 * However, the controller does the most with the KeyboardInput as it reads in what keys are being pressed from KeyboardInput
 * and tells the model to behave accordingly.
 *
 * ViewManager holds the only instance of this class in the game and Controller is the only class to ask for information from it
 *
 * Based off of the tutorial found at this page
 * http://www.gamedev.net/page/resources/_/technical/general-programming/java-games-keyboard-and-mouse-r2439
 */
public class KeyboardInput implements KeyListener
{
  private final int KEY_COUNT = 256;

  private enum KeyState
  {
    RELEASED, PRESSED, ONCE
  }

  //how the keys are pressed
  private KeyState[] keys = null;

  //what keys are pressed
  private boolean[] currentKeys = null;

  /**
   * Constructor for the KeyboardInput class
   *
   * initializes key arrays and sets the keystate for every key to released
   */
  public KeyboardInput()
  {
    currentKeys = new boolean[KEY_COUNT];
    keys = new KeyState[KEY_COUNT];
    for (int i = 0; i < KEY_COUNT; ++i)
    {
      keys[i] = KeyState.RELEASED;
    }
  }

  /**
   * Polls KeyboardInput for what keys have been pressed
   */
  public synchronized void poll()
  {
    for (int i = 0; i < KEY_COUNT; ++i)
    {
      if (currentKeys[i]) //if the key has been pressed
      {
        if (keys[i] == KeyState.RELEASED) //if key is down now but not down last frame, set to once
        {
          keys[i] = KeyState.ONCE;
        } else
        {
          keys[i] = KeyState.PRESSED; //otherwise set key to pressed
        }
      } else
      {
        keys[i] = KeyState.RELEASED; //if key is not down set to released
      }
    }
  }

  /**
   * Asks KeyboardInput if a certain key is currently down
   * @param keyCode corresponds to which key has been pressed
   * @return true if key is down, false if key isn't
   */
  public boolean keyDown(int keyCode)
  {
    return keys[keyCode] == KeyState.ONCE || keys[keyCode] == KeyState.PRESSED;
  }

  @Override
  public void keyTyped(KeyEvent e)
  {

  }

  /**
   * If a key is pressed, sets it to true in the currentKeys array
   * @param e keyEvent from the keylistener
   */
  @Override
  public synchronized void keyPressed(KeyEvent e)
  {
    int keyCode = e.getKeyCode();

    if (keyCode >= 0 && keyCode < KEY_COUNT)
    {
      currentKeys[keyCode] = true;
    }
  }

  /**
   * If a key is not pressed, sets it to false in the currentKeys array
   * @param e keyEvent from the keylistener
   */
  @Override
  public synchronized void keyReleased(KeyEvent e)
  {
    int keyCode = e.getKeyCode();

    if (keyCode >= 0 && keyCode < KEY_COUNT)
    {
      currentKeys[keyCode] = false;
    }
  }
}