package Traces;

import Engine.RedBasic;
import Engine.RedCanvas;
import Engine.RedG;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author hara
 */
public class Blackout extends RedBasic{
    public double timer=0;
    public double delay=1;
    public boolean becomeDarker;
    public double darkness;
    public boolean done = false;
    public int color;
            
    public Blackout() {
        super();
        color = Traces.secondColor;
    }
    
    @Override
    public void update() {
        timer += RedG.elapsed;
        if( timer >= delay ) {
            exist = visible = false;
            done = true;
        } else if(becomeDarker ) {
            darkness = timer/delay;
        } else {
            darkness = 1-timer/delay;
        }
        color = Traces.secondColor;
    }
    
    @Override
    public void draw(RedCanvas Canvas) {
        if( visible ) {
            Graphics g = Canvas.getGraphics();
            g.setColor(new Color( (((int)(0xff * Math.min(darkness,1))) * 0x1000000 + color), true));
            g.fillRect(0, 0, Canvas.width, Canvas.height);
        }
    }
    
    public void startBecomingDarker() {
        becomeDarker = true;
        exist = true;
        visible = true;
        timer = 0;
        done = false;
        darkness = 0;
    }
    
    public void startBecomingBrighter() {
        becomeDarker = false;
        exist = true;
        visible = true;
        timer = 0;
        done = false;
        darkness = 1;
    }
}
