package Model.Unit;

import Model.GameObject;

import javax.sound.sampled.Clip;

public abstract class Unit extends GameObject
{
  /**
   * This updates the game object's state as determined by its child class.
   * @param deltaTime
   */
  public abstract void update(long deltaTime);


  /**
   * Returns the current sound associated with the unit;
   * @return Clip containing sound to be played. Null if no sound needs to be played.
   */
  public abstract Clip getSound();
}
