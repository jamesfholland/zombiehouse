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
    BufferedImage tempImage;
    for (int i = 0; i < spriteCount; i++)
    {
      tempImage = sheet.getSubimage(i*spriteWidth, spriteRow*spriteHeight, spriteWidth, spriteHeight);

      //-2*xOffset to get both sides trimmed
      returnArray[i] = tempImage.getSubimage(xOffset,yOffset, spriteWidth - 2*xOffset, spriteHeight-yOffset);
    }

    return returnArray;
  }

  public static BufferedImage[] parseMultiRowSprites(BufferedImage sheet, int numberRows, int spriteHeight, int spriteWidth, int xOffset, int yOffset, int spriteCount)
  {

    BufferedImage[] returnArray = new BufferedImage[spriteCount*numberRows];
    BufferedImage tempImage;
    for(int spriteRow = 0; spriteRow < numberRows; spriteRow++)
    {
      for (int i = 0; i < spriteCount; i++)
      {
        tempImage = sheet.getSubimage(i * spriteWidth, spriteRow * spriteHeight, spriteWidth, spriteHeight);

        //-2*xOffset to get both sides trimmed
        returnArray[spriteRow*spriteCount + i] = tempImage.getSubimage(xOffset, yOffset, spriteWidth - 2 * xOffset, spriteHeight - yOffset);
      }
    }

    return returnArray;
  }
}
