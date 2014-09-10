package Traces;

import Engine.EngineSplash;
import Engine.RedG;
import Engine.RedGame;
import Engine.RedImage;

/**
 *
 * @author hara
 */
public class Traces extends RedGame {

    public static int mainColor = 0x00ff8000;
    public static int secondColor = 0x00804000;
    public static double record;
    public static boolean musicByF4LL0UT;
    public static boolean fullscreen = false;

    public static FlashingSound music;

    public Traces() {
        super(256, 128, null, 1, 30, 60, "Traces", true, 0xff000000, 5);
        setCursor(new RedImage(1, 1, RedImage.TYPE_INT_ARGB));
        scaleType = PROPOTION;
        setWindowIcon("/Traces/data/Logo.png");
    }

    public static void main(String[] args) {
        Traces game = new Traces();
        if (RedG.random() > 0.5) {
            music = new FlashingSound("/Traces/data/Neon (MatchTwo).wav");
            musicByF4LL0UT = true;
        } else {
            music = new FlashingSound("/Traces/data/Sinnister loop(WIP).wav");
            musicByF4LL0UT = false;
        }
        Traces.state = new EngineSplash() {
            @Override
            public void stateSwitch() {
                RedG.bgColor = 0x80000000 + secondColor;
                RedG.switchState(new FullState());
                FullState.switchToState(new MainMenuState());
            }
        };
        game.start();
    }
}
