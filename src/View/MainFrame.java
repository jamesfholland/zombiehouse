package View;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

/**
 * MainFrame is the JFrame where all game components are displayed in.
 */
class MainFrame extends JFrame
{
  /**
   * Sets up the Jframe, determines the default screen and displays maximized there.
   */
  MainFrame()
  {
    super("Zombie Tree House");
    GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    setSize(new Dimension(graphicsDevice.getDisplayMode().getWidth(), graphicsDevice.getDisplayMode().getHeight()));
  }
}
