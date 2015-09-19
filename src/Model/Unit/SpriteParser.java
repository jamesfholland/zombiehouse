package Model.Unit;

import java.awt.image.BufferedImage;

/**
 *
 * SpriteParser handles splitting sprite sheets into individual animation cells
 */
public class SpriteParser
{
  public static BufferedImage[] parseSprites(BufferedImage sheet, int spriteRow, int spriteHeight, int spriteWidth, int xOffset, int yOffset, int spriteCount)
  {

    BufferedImage[] returnArray = new BufferedImage[spriteCount];

    for (int i = 0; i < spriteCount; i++)
    {
      returnArray[i] = sheet.getSubimage(i*spriteWidth, spriteRow*spriteHeight, spriteWidth, spriteHeight);
    }

    return returnArray;
  }
}
