package View;

import Model.Settings;
import Model.Unit.Player;

import javax.sound.sampled.*;
import java.awt.*;
import java.io.IOException;

/**
 * This class handles all zounds made in the game.
 */
public class SoundManager
{

  private static final Clip STEP_1;
  private static SoundPlayer step1Thread;

  private static final AudioInputStream STEP_2;

  private static final AudioInputStream FIRE_START;
  private static final AudioInputStream FIRE_CONTINOUS;


  static
  {
    Clip tempStep1 = null;
    AudioInputStream tempStep2 = null;
    AudioInputStream tempFireStart = null;
    AudioInputStream tempFireContinous = null;
    try
    {
      tempStep1 = AudioSystem.getClip();
          tempStep1.open(AudioSystem.getAudioInputStream(SoundManager.class.getResourceAsStream("sounds/stepwood_1.wav")));

      tempStep2 = AudioSystem.getAudioInputStream(SoundManager.class.getResourceAsStream("sounds/stepwood_1.wav"));

      tempFireStart = AudioSystem.getAudioInputStream(SoundManager.class.getResourceAsStream("sounds/stepwood_1.wav"));

      tempFireContinous = AudioSystem.getAudioInputStream(SoundManager.class.getResourceAsStream("sounds/stepwood_1.wav"));
    }
    catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
    {
      e.printStackTrace();
    }

    STEP_1 = tempStep1;
    STEP_2 = tempStep2;
    FIRE_START = tempFireStart;
    FIRE_CONTINOUS = tempFireContinous;

    step1Thread = new SoundPlayer(STEP_1, Settings.REFRESH_RATE);
    step1Thread.start();
    //playWalk(null, null);
  }

  public static void playWalk(Point source, Player listener)
  {
    step1Thread.play(source, listener);
  }

  private static class SoundPlayer extends Thread
  {
    private Point source;
    private Player listener;
    private Clip clip;
    private long timePerLoop;
    private Integer loops = 0;

    private static final long PLAY_ONCE = Long.MIN_VALUE;

    SoundPlayer( Clip sound, long timePerLoop)
    {
      this.timePerLoop = timePerLoop;
      clip = sound;
    }

    public void play(Point source, Player listener)
    {
      this.source = source;
      this.listener = listener;
      synchronized (loops)
      {
        loops++;
      }
    }

    @Override
    public void run()
    {
      try
      {
        //Adjust volume
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.setFramePosition(0);

        long timeLast = System.currentTimeMillis();
        long timeNow;
        long timeLeft = 0;
        while (true)
        {
          if(timeLeft>0)
          {
              clip.start();
          }
          else
          {
            //clip.stop();
            //clip.setFramePosition(0);
          }
          Thread.sleep(Settings.REFRESH_RATE);

          //Adjust Volume again

          synchronized (loops)
          {
            timeNow =System.currentTimeMillis();
            timeLeft -= timeNow - timeLast;

            timeLast = timeNow;
            if(timeLeft < 0) timeLeft = 0;

            timeLeft += loops * timePerLoop;
            loops = 0;
          }
        }
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
    }
  }
}
