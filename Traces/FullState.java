package Traces;

import Engine.RedCanvas;
import Engine.RedGroup;

/**
 *
 * @author hara
 */
public class FullState extends RedGroup{
    public static RedGroup subState;
    public static FlashingSound music;
    
    @Override
    public void create() {
        music = Traces.music;
        music.start(-1);
    }
    
    public static void switchToState( RedGroup SubState ) {
        SubState.create();
        subState = SubState;
    }
    
    @Override
    public void update() {
        music.update();
        subState.update();
    }
    
    @Override
    public void draw(RedCanvas Canvas) {
        subState.draw(Canvas);
    }
}
