package Model.Unit;

import java.awt.image.BufferedImage;

/**
 * SpriteParser handles splitting sprite sheets into individual animation cells
 * On a program level, the SpriteParser does not interact with any of the larger abstract classes
 * Every unit which has an animation uses SpriteParser to get which sprite should be played at the current update
 */
public class SpriteParser
{
  /**
   * method for parsing sprites on a single page
   *
   * @param sheet        - a page of images/sprites
   * @param spriteRow    - which row of sprites to take
   * @param spriteHeight - the height of each sprite in pixels
   * @param spriteWidth  - the width of each sprite in pixels
   * @param xOffset      - x offset in pixels
   * @param yOffset      - y offset in pixels
   * @param spriteCount  - how many sprites to take from row
   * @return an array of BufferedImages for the sprite
   */
  public static BufferedImage[] parseSprites(BufferedImage sheet, int spriteRow, int spriteHeight, int spriteWidth, int xOffset, int yOffset, int spriteCount)
  {

    BufferedImage[] returnArray = new BufferedImage[spriteCount];
    BufferedImage tempImage;
    for (int i = 0; i < spriteCount; i++)
    {
      tempImage = sheet.getSubimage(i * spriteWidth, spriteRow * spriteHeight, spriteWidth, spriteHeight);

      //-2*xOffset to get both sides trimmed
      returnArray[i] = tempImage.getSubimage(xOffset, yOffset, spriteWidth - 2 * xOffset, spriteHeight - yOffset);
    }

    return returnArray;
  }

  /**
   * method for parsing sprites from across multiple rows (specifically used with fire)
   *
   * @param sheet        - a page of images/sprites
   * @param numberRows   - the number of rows to process/parse
   * @param spriteHeight - the sprite height in pixels
   * @param spriteWidth  - the sprite width in pixels
   * @param xOffset      - the x offset in pixels
   * @param yOffset      - the y offset in pixels
   * @param spriteCount  - the number of sprites to take from page
   * @return an array of BufferedImages for the sprites
   */
  public static BufferedImage[] parseMultiRowSprites(BufferedImage sheet, int numberRows, int spriteHeight, int spriteWidth, int xOffset, int yOffset, int spriteCount)
  {

    BufferedImage[] returnArray = new BufferedImage[spriteCount * numberRows];
    BufferedImage tempImage;
    for (int spriteRow = 0; spriteRow < numberRows; spriteRow++)
    {
      for (int i = 0; i < spriteCount; i++)
      {
        tempImage = sheet.getSubimage(i * spriteWidth, spriteRow * spriteHeight, spriteWidth, spriteHeight);

        //-2*xOffset to get both sides trimmed
        returnArray[spriteRow * spriteCount + i] = tempImage.getSubimage(xOffset, yOffset, spriteWidth - 2 * xOffset, spriteHeight - yOffset);
      }
    }

    return returnArray;
  }
}
