package View;

import javax.swing.*;
import java.awt.*;

/**
 *
 */
public class MainFrame extends JFrame
{
  MainFrame()
  {
    super("Zombie House");
    GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    setSize(new Dimension(graphicsDevice.getDisplayMode().getWidth(), graphicsDevice.getDisplayMode().getHeight()));
  }
}
