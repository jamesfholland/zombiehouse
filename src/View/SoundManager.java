package View;

import Model.Settings;
import javafx.scene.media.AudioClip;

import java.awt.*;
import java.net.URISyntaxException;

/**
 * This class handles all zounds made in the game.
 * Currently sound files are coded here.
 * <p>
 * Each sound type gets its own thread and has a corresponding play method for it.
 * This is not ideal, but time was limited.
 */
public class SoundManager
{

  private static final AudioClip FOOT_STEP;
  private static final SoundPlayer WALK_THREAD;

  private static final AudioClip ZOMBIE_STEP1;
  private static final AudioClip ZOMBIE_STEP2;
  private static final SoundPlayer ZOMBIE_STEP_THREAD1;
  private static final SoundPlayer ZOMBIE_STEP_THREAD2;

  private static final AudioClip ZOMBIE_THUD;
  private static final SoundPlayer ZOMBIE_THUD_THREAD;

  private static final AudioClip FIRE;
  private static final SoundPlayer FIRE_THREAD;


  static
  {
    AudioClip footstep = null;
    AudioClip zombieStep1 = null;
    AudioClip zombieStep2 = null;
    AudioClip zombieThud = null;
    AudioClip fire = null;
    try
    {
      footstep = new AudioClip(View.SoundManager.class.getResource("sounds/footstep.wav").toURI().toASCIIString());
      zombieStep1 = new AudioClip(View.SoundManager.class.getResource("sounds/creak5.wav").toURI().toASCIIString());
      zombieStep2 = new AudioClip(View.SoundManager.class.getResource("sounds/creak7.wav").toURI().toASCIIString());
      zombieThud = new AudioClip(View.SoundManager.class.getResource("sounds/thudGroan.wav").toURI().toASCIIString());
      fire = new AudioClip(View.SoundManager.class.getResource("sounds/crackle.wav").toURI().toASCIIString());
    }
    catch (URISyntaxException e)
    {
      e.printStackTrace();
    }


    FOOT_STEP = footstep;
    ZOMBIE_STEP1 = zombieStep1;
    ZOMBIE_STEP2 = zombieStep2;
    ZOMBIE_THUD = zombieThud;
    FIRE = fire;

    WALK_THREAD = new SoundPlayer(FOOT_STEP, Settings.REFRESH_RATE, 1.0, .2, 1, 1);
    WALK_THREAD.start();

    ZOMBIE_STEP_THREAD1 = new SoundPlayer(ZOMBIE_STEP1, Settings.REFRESH_RATE, 1.0, .2, 1, 1);
    ZOMBIE_STEP_THREAD1.start();

    ZOMBIE_STEP_THREAD2 = new SoundPlayer(ZOMBIE_STEP2, Settings.REFRESH_RATE, 1.0, .2, 1, 1);
    ZOMBIE_STEP_THREAD2.start();

    ZOMBIE_THUD_THREAD = new SoundPlayer(ZOMBIE_THUD, Settings.REFRESH_RATE, 1.0, .2, 1, 2);
    ZOMBIE_THUD_THREAD.start();

    FIRE_THREAD = new SoundPlayer(FIRE, Settings.REFRESH_RATE, 1.0, .2, 1, 1);
    FIRE_THREAD.start();
  }

  /**
   * Plays a walking sound with alternating balances to simulate right and left steps.
   *
   * @param isRunning doubles the rate if running.
   */
  public static void playWalk(boolean isRunning)
  {
    if (isRunning)
    {
      WALK_THREAD.playAlternateFast();
    }
    else
    {
      WALK_THREAD.playAlternate();
    }
  }

  /**
   * Plays a fire crackling sound sourced at the fire Point.
   *
   * @param fire   point on map where fire is located.
   * @param player location of player at time of sound.
   */
  public static void playFire(Point fire, Point player)
  {
    FIRE_THREAD.play(fire, player);
  }

  /**
   * Plays a zombie sound.
   *
   * @param zombie the source point of the sound.
   * @param player the location of the player.
   */
  public static void playZombieWalk(Point zombie, Point player)
  {
    if (Settings.RANDOM.nextBoolean())
    {
      ZOMBIE_STEP_THREAD1.play(zombie, player);
    }
    else
    {
      ZOMBIE_STEP_THREAD2.play(zombie, player);
    }
  }

  /**
   * Play zombie thud and groan.
   *
   * @param zombie location of sound.
   * @param player location of player.
   */
  public static void playZombieThud(Point zombie, Point player)
  {
    ZOMBIE_THUD_THREAD.play(zombie, player);
  }

  /**
   * This is called to have crisp sounds when the player quits moving.
   * Other sounds don't need this, just the player since his movement is not continous.
   */
  public static void stopWalk()
  {
    WALK_THREAD.stopSound();
  }

  /**
   * The SoundPlayer class handles threading and synchronizing sounds handed to it.
   * There is a running instance for each type of sound.
   */
  private static class SoundPlayer extends Thread
  {
    private Point source;
    private Point listener;
    private final AudioClip CLIP;
    private Boolean play;
    private final long MAX_TIME;
    private double volume;
    private Double balance;
    private final int PRIORITY;
    private boolean alternator;
    private double rate;
    private double distanceMultiplier;

    SoundPlayer(AudioClip sound, long maxTime, double baseVolume, double baseBalance, int priority, double distanceMultiplier)
    {
      this.CLIP = sound;
      this.PRIORITY = priority;
      this.play = false;
      this.volume = baseVolume;
      this.balance = baseBalance;
      this.MAX_TIME = maxTime;
      rate = 1.0;
      this.distanceMultiplier = distanceMultiplier;

    }

    private void updateBalance()
    {
      synchronized (balance)
      {
        if (source != null && listener != null)
        {
          balance = Math.cos(Math.atan2(listener.x - source.x, listener.y - source.y) + Math.PI / 2);
        }
        listener = null;
        source = null;
      }
    }

    public void play(Point source, Point listener)
    {
      synchronized (play)
      {

        double tempVolume = 1.0 - Math.abs(source.distance(listener) / (Settings.playerHearing * distanceMultiplier));

        if (tempVolume > 0.0)
        {
          this.source = source;
          this.listener = listener;
          volume = tempVolume;

          play = true;
          updateBalance();
        }

      }
    }

    public void playAlternate()
    {
      alternator = true;
      source = null;
      listener = null;
      rate = 1.0;
      synchronized (play)
      {
        play = true;

      }
    }

    public void playAlternateFast()
    {
      alternator = true;
      source = null;
      listener = null;
      rate = 2.0;
      synchronized (play)
      {
        play = true;

      }
    }

    public void stopSound()
    {
      synchronized (play)
      {
        play = false;
      }
    }

    @Override
    public void run()
    {
      long startTime = 0;
      while (true)
      {
        synchronized (play)
        {
          if (play && !CLIP.isPlaying())
          {
            startTime = System.currentTimeMillis();

            if (alternator)
            {
              balance = -1 * balance;
            }
            CLIP.play(volume, balance, rate, 0, PRIORITY);
            play = false;
          }
          else if (CLIP.isPlaying() && (startTime - System.currentTimeMillis()) > MAX_TIME)
          {
            CLIP.stop();
          }
        }
      }
    }

  }
}
