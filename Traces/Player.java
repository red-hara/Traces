package Traces;

import Engine.RedCanvas;
import Engine.RedG;
import Engine.RedPoint;
import Engine.RedPolygon;

/**
 *
 * @author hara
 */
public class Player extends RedPolygon {

    public Trace trace = new Trace(this, 8, 4, 0xffffffff);

    public RedPolygon center;
    public Trace blackTrace;

    public Player(double X, double Y) {
        super(X, Y, 8, 8, new RedPoint[]{
            new RedPoint(4 - 2 * Math.sqrt(2), 4 - 2 * Math.sqrt(2)),
            new RedPoint(4, 0),
            new RedPoint(4 + 2 * Math.sqrt(2), 4 - 2 * Math.sqrt(2)),
            new RedPoint(8, 4),
            new RedPoint(4 + 2 * Math.sqrt(2), 4 + 2 * Math.sqrt(2)),
            new RedPoint(4, 8),
            new RedPoint(4 - 2 * Math.sqrt(2), 4 + 2 * Math.sqrt(2)),
            new RedPoint(0, 4)
        },
                0xffffffff);
        center = new RedPolygon(X, Y, 8, 8, new RedPoint[]{
            new RedPoint(4 - 2 * Math.sqrt(2), 4 - 2 * Math.sqrt(2)),
            new RedPoint(4, 0),
            new RedPoint(4 + 2 * Math.sqrt(2), 4 - 2 * Math.sqrt(2)),
            new RedPoint(8, 4),
            new RedPoint(4 + 2 * Math.sqrt(2), 4 + 2 * Math.sqrt(2)),
            new RedPoint(4, 8),
            new RedPoint(4 - 2 * Math.sqrt(2), 4 + 2 * Math.sqrt(2)),
            new RedPoint(0, 4)
        },
                0xff000000);
        blackTrace = new Trace(center, 4, 4, 0xff000000);
        drag.x = 128;
        drag.y = 128;
        elasticity = 1;
    }

    @Override
    public void update() {
        maxVelocity.x = Math.abs(Math.sin(angle) * Math.min(FullState.music.niceLevel,128));
        maxVelocity.y = Math.abs(Math.cos(angle) * Math.min(FullState.music.niceLevel,128));
        acceleration.x = Math.sin(angle) * 512;
        acceleration.y = -Math.cos(angle) * 512;
        if (RedG.keys.isPressed(RedG.keys.RIGHT)) {
            angle += RedG.elapsed * Math.PI * 2;
        }
        if (RedG.keys.isPressed(RedG.keys.LEFT)) {
            angle -= RedG.elapsed * Math.PI * 2;
        }
        center.angle = angle;
        center.displayZoom = Math.min((FullState.music.niceLevel) / 80, 1);
        trace.color =  color = Traces.secondColor + 0xff000000;
        blackTrace.color = center.color = Traces.mainColor + 0xff000000;
        
    }

    @Override
    public void postUpdate() {
        super.postUpdate();
        trace.update();
        blackTrace.update();
        center.x = x;
        center.y = y;
    }

    @Override
    public void draw(RedCanvas Canvas) {
//        trace.draw(Canvas);
//        blackTrace.draw(Canvas);
        super.draw(Canvas);
        center.draw(Canvas);

    }

    public void drawTrace(RedCanvas Canvas) {
        trace.draw(Canvas);
        blackTrace.draw(Canvas);
    }
}
