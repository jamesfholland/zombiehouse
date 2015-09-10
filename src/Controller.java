import Model.Tile.Floor;
import Model.Tile.Tile;
import Model.Unit.Player;
import Model.Unit.Unit;
import View.ViewManager;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * This class controls the running of the program. It contains the main game loop and signals the gui to refresh.
 */
public class Controller
{
  private ViewManager view;

  //60hz loop calls
  /*
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

    //Run house generator


    //Temporary until we build a better static map and then generated map.
    ArrayList<Tile> tiles = new ArrayList<>();
    tiles.add(new Floor(new Point(0, 0)));

    Unit player = new Player(new Point(0, 0));

    view.setUnitsAndTiles(tiles, null, player);

  }




  private void gameLoop()
  {

  }
}
