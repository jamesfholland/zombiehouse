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
  private static SoundPlayer walkThread;
  private static boolean rightStep = true;

  private static final AudioClip FIRE_START;
  private static final AudioClip FIRE_CONTINOUS;


  static
  {
    AudioClip footstep = null;
    AudioClip FireStart = null;
    AudioClip FireContinous = null;
    try
    {
      footstep = new AudioClip(View.SoundManager.class.getResource("sounds/footstep.wav").toURI().toASCIIString());
    }
    catch (URISyntaxException e)
    {
      e.printStackTrace();
    }


    FOOT_STEP = footstep;
    FIRE_START = FireStart;
    FIRE_CONTINOUS = FireContinous;

    walkThread = new SoundPlayer(FOOT_STEP, Settings.REFRESH_RATE, 1.0, .2, 1);
    walkThread.start();
  }

  public static void playWalk()
  {
    walkThread.playAlternate();
  }

  public static void stopWalk()
  {
    walkThread.stopSound();
  }

  private static class SoundPlayer extends Thread
  {
    private Point source;
    private Point listener;
    private AudioClip clip;
    private Boolean play;
    private long maxTime;
    private double volume;
    private Double balance;
    private int priority;
    private boolean alternator;

    SoundPlayer(AudioClip sound, long maxTime, double baseVolume, double baseBalance, int priority)
    {
      this.clip = sound;
      this.priority = priority;
      this.play = false;
      this.volume = baseVolume;
      this.balance = baseBalance;
      this.maxTime = maxTime;
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
          if (play && !clip.isPlaying())
          {
            startTime = System.currentTimeMillis();

            if (alternator)
            {
              balance = -1 * balance;
            }
            clip.play(volume, balance, 1, 0, priority);
            play = false;
          } else if (clip.isPlaying() && (startTime - System.currentTimeMillis()) > maxTime)
          {
            clip.stop();
          } else if (clip.isPlaying() && listener != null && source != null)
          {
            updateBalance();
            clip.setBalance(balance);
          }
        }
      }
    }

  }
}
