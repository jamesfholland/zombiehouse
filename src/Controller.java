import Model.HouseGeneration.HouseGeneration;
import Model.Level;
import Model.Settings;
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

  private HouseGeneration houseGenerator;
  private Level currentLevel;
  private Player hero;

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
    houseGenerator = new HouseGeneration();

    // First (default) house level
    currentLevel = houseGenerator.getCurrentLevel();
    hero = new Player(new Point(3 * Settings.TILE_SIZE, 2 * Settings.TILE_SIZE));

    // a call to currentLevel.getHouseTiles() will return a 2d array with the default 5x5 map


    //Temporary until we build a better static map and then generated map.
    ArrayList<Tile> tiles = new ArrayList<>();
//    tiles.add(new Floor(new Point(0, 0)));
    // loops over 2d array and adds the tiles:
//    for(int i = 0; i < 5; i++)
//    {
//      for(int j = 0; j < 5; j++)
//      {
//        tiles.add(currentLevel.getHouseTiles()[i][j]);
//      }
//    }

    // Unit player = new Player(new Point(0, 0));

    view.setUnitsAndTiles(tiles, null, hero);

  }




  private void gameLoop()
  {

  }
}
