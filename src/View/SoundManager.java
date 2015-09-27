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

  //private static final AudioClip ZOMBIE_STEP;
  //private static final SoundPlayer ZOMBIE_STEP_THREAD;

  private static final AudioClip FIRE_START;
  private static final AudioClip FIRE_CONTINOUS;


  static
  {
    AudioClip footstep = null;
    AudioClip zombieStep = null;
    AudioClip FireStart = null;
    AudioClip FireContinous = null;
    try
    {
      footstep = new AudioClip(View.SoundManager.class.getResource("sounds/footstep.wav").toURI().toASCIIString());
      footstep = new AudioClip(View.SoundManager.class.getResource("sounds/footstep.wav").toURI().toASCIIString());
    }
    catch (URISyntaxException e)
    {
      e.printStackTrace();
    }


    FOOT_STEP = footstep;
    FIRE_START = FireStart;
    FIRE_CONTINOUS = FireContinous;

    WALK_THREAD = new SoundPlayer(FOOT_STEP, Settings.REFRESH_RATE, 1.0, .2, 1);
    WALK_THREAD.start();
  }

  public static void playWalk(boolean isRunning)
  {
    if(isRunning) WALK_THREAD.playAlternateFast();
    else WALK_THREAD.playAlternate();
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
        if ((source.y - listener.y) == 0)
        {
          balance = 0.0;
        } else
        {
          balance = (source.x - listener.x) / (double) (source.y - listener.y);
        }
      }
    }

    public void play(Point source, Point listener)
    {
      synchronized (play)
      {
        this.source = source;
        this.listener = listener;
        play = true;
        updateBalance();
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
          } else if (CLIP.isPlaying() && listener != null && source != null)
          {
            updateBalance();
            CLIP.setBalance(balance);
          }
        }
      }
    }

  }
}
