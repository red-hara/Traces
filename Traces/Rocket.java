package Traces;

import Engine.RedCanvas;
import Engine.RedG;
import Engine.RedObject;
import Engine.RedPoint;
import Engine.RedPolygon;

/**
 *
 * @author hara
 */
public class Rocket extends RedPolygon {

    private RedObject _target;
    private double _maxAngular = Math.PI / 2;
    private double _speed = 32;

    public RedPolygon center;
    public Trace trace;
    public Trace whiteTrace;

    public Rocket(double X, double Y, RedObject Target) {
        super(X, Y, 2, 2, new RedPoint[]{
            new RedPoint(0, 0),
            new RedPoint(2, 0),
            new RedPoint(2, 2),
            new RedPoint(0, 2)
        }, 0xff000000
        );

        center = new RedPolygon(X+1, Y+1, 1, 1, new RedPoint[]{
            new RedPoint(0, 0),
            new RedPoint(1, 0),
            new RedPoint(1, 1),
            new RedPoint(0, 1)
        }, 0xffffffff
        );

        trace = new Trace(this, 2, 2, 0xff000000);
        whiteTrace = new Trace(center, 1, 2, 0xffffffff);

        _target = Target;
        double dx = (_target.x + ((int) _target.width >> 1)) - (x + ((int) width >> 1));
        double dy = (_target.y + ((int) _target.height >> 1)) - (y + ((int) height >> 1));
        angle = Math.atan2(dy, -dx);
        immovable = true;
    }

    @Override
    public void update() {
        if (_target != null && _target.exist) {

            if( angle < -Math.PI) {
                angle += Math.PI * 2;
            }
            if( angle > Math.PI ) {
                angle -= Math.PI * 2;
            }
            
            double dx = (_target.x + ((int) _target.width >> 1)) - (x + ((int) width >> 1)); // update angle
            double dy = (_target.y) - (y + ((int) height >> 1));
            double angleRad = Math.atan2(dx, dy);
            double targetAngle = angleRad + angle;
            double _elap = RedG.elapsed;
            double delta = _maxAngular * _elap;
            if (targetAngle < -Math.PI) {
                targetAngle += Math.PI * 2;
            }
            if (targetAngle > Math.PI) {
                targetAngle -= Math.PI * 2;
            }
            if (targetAngle > delta) {
                targetAngle = delta;
            } else if (targetAngle < -delta) {
                targetAngle = -delta;
            }
            angle += targetAngle;
            double cosa = Math.cos(angle);
            double sina = Math.sin(angle);
            velocity.x = sina * _speed;
            velocity.y = -cosa * _speed;
            x += velocity.x * _elap;
            y += velocity.y * _elap;
            
            trace.color =  color = Traces.mainColor + 0xff000000;
            whiteTrace.color = center.color = Traces.secondColor + 0xff000000;
        }
    }

    @Override
    public void postUpdate() {
        super.postUpdate();
        center.x = x+0.5;
        center.y = y+0.5;
        center.angle = angle;

        trace.update();
        whiteTrace.update();
    }

    @Override
    public void draw(RedCanvas Canvas) {
        trace.draw(Canvas);
        whiteTrace.draw(Canvas);
        super.draw(Canvas);
        center.draw(Canvas);

    }
}
