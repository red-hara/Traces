package Traces;

import Engine.RedG;
import Engine.RedPoint;
import Engine.RedPolygon;

/**
 *
 * @author hara
 */
public class RotatingStar extends RedPolygon {

    public RotatingStar(double X, double Y, double Size) {
        super(X, Y, 0, 0, null, 0xffff8000);
        points = new RedPoint[48];
        for (int i = 0; i < 48; i++) {
            points[i++] = new RedPoint(0, 0);
            points[i] = new RedPoint(Size * Math.cos(i * Math.PI / 24), Size * Math.sin(i * Math.PI / 24));
            i++;
            points[i] = new RedPoint(Size * Math.cos(i * Math.PI / 24), Size * Math.sin(i * Math.PI / 24));
        }
    }

    public void setSize(double Size) {
        points = new RedPoint[48];
        for (int i = 0; i < 48; i++) {
            points[i++] = new RedPoint(0, 0);
            points[i] = new RedPoint(Size * Math.cos(i * Math.PI / 24), Size * Math.sin(i * Math.PI / 24));
            i++;
            points[i] = new RedPoint(Size * Math.cos(i * Math.PI / 24), Size * Math.sin(i * Math.PI / 24));

        }
    }

    @Override
    public void update() {
        super.update();
        color = Traces.mainColor + 0xff000000;
        angle += RedG.elapsed * Math.PI / 4;
    }
}
