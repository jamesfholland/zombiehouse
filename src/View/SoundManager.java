package View;

import Model.Settings;
import javafx.scene.media.AudioClip;

import java.awt.*;
import java.net.URISyntaxException;

/**
 * This class handles all zounds made in the game.
 */
public class SoundManager
{

  private static final AudioClip FOOT_STEP;
  private static final SoundPlayer WALK_THREAD;

  private static final AudioClip ZOMBIE_STEP1;
  private static final AudioClip ZOMBIE_STEP2;
  private static final SoundPlayer ZOMBIE_STEP_THREAD1;
  private static final SoundPlayer ZOMBIE_STEP_THREAD2;

  private static final AudioClip FIRE_START;
  private static final AudioClip FIRE_CONTINOUS;


  static
  {
    AudioClip footstep = null;
    AudioClip zombieStep1 = null;
    AudioClip zombieStep2 = null;
    AudioClip FireStart = null;
    AudioClip FireContinous = null;
    try
    {
      footstep = new AudioClip(View.SoundManager.class.getResource("sounds/footstep.wav").toURI().toASCIIString());
      zombieStep1 = new AudioClip(View.SoundManager.class.getResource("sounds/creak5.wav").toURI().toASCIIString());
      zombieStep2 = new AudioClip(View.SoundManager.class.getResource("sounds/creak7.wav").toURI().toASCIIString());
    }
    catch (URISyntaxException e)
    {
      e.printStackTrace();
    }


    FOOT_STEP = footstep;
    ZOMBIE_STEP1 = zombieStep1;
    ZOMBIE_STEP2 = zombieStep2;
    FIRE_START = FireStart;
    FIRE_CONTINOUS = FireContinous;

    WALK_THREAD = new SoundPlayer(FOOT_STEP, Settings.REFRESH_RATE, 1.0, .2, 1);
    WALK_THREAD.start();

    ZOMBIE_STEP_THREAD1 = new SoundPlayer(ZOMBIE_STEP1, Settings.REFRESH_RATE, 1.0, .2, 1);
    ZOMBIE_STEP_THREAD1.start();

    ZOMBIE_STEP_THREAD2 = new SoundPlayer(ZOMBIE_STEP2, Settings.REFRESH_RATE, 1.0, .2, 1);
    ZOMBIE_STEP_THREAD2.start();
  }

  public static void playWalk(boolean isRunning)
  {
    if(isRunning) WALK_THREAD.playAlternateFast();
    else WALK_THREAD.playAlternate();
  }

  public static void playZombieWalk(Point zombie, Point player)
  {
    if(Settings.RANDOM.nextBoolean())
    {
      ZOMBIE_STEP_THREAD1.play(zombie, player);
    }
    else
    {
      ZOMBIE_STEP_THREAD2.play(zombie, player);
    }

  }

  public static void stopWalk()
  {
    WALK_THREAD.stopSound();
  }

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

    SoundPlayer(AudioClip sound, long maxTime, double baseVolume, double baseBalance, int priority)
    {
      this.CLIP = sound;
      this.PRIORITY = priority;
      this.play = false;
      this.volume = baseVolume;
      this.balance = baseBalance;
      this.MAX_TIME = maxTime;
      rate = 1.0;
    }

    private void updateBalance()
    {
      synchronized (balance)
      {
        if(source != null && listener != null)
        {
          balance = Math.cos(Math.atan2(listener.x - source.x ,  listener.y - source.y) + Math.PI/2);
        }
        listener = null;
        source = null;
      }
    }

    public void play(Point source, Point listener)
    {
      synchronized (play)
      {

        double tempVolume = 1.0 - Math.abs(source.distance(listener)/Settings.playerHearing);

        if(tempVolume > 0.0)
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
          } else if (CLIP.isPlaying() && (startTime - System.currentTimeMillis()) > MAX_TIME)
          {
            CLIP.stop();
          }
        }
      }
    }

  }
}
