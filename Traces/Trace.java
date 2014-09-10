package Traces;

import Engine.RedBasic;
import Engine.RedCanvas;
import Engine.RedG;
import Engine.RedMethod;
import Engine.RedObject;
import Engine.RedPoint;
import Engine.RedPolygon;
import Engine.RedTimer;
import java.awt.Graphics;

/**
 *
 * @author hara
 */
public class Trace extends RedBasic {

	public RedObject parent;
	public double liveDuration;
	public TraceBit firstBit;
	public int amountOfBits = 0;
	public int color;
	public RedPoint[] points;
	public double width;
	public RedTimer traceBitTimer;
	public RedBasic selfPointer;

	public Trace(RedObject Parent, double Width, double LiveDuration, int Color) {
		super();
		selfPointer = this;
		this.traceBitTimer = new RedTimer(
				new RedMethod() {
					@Override
					public void execute() {
						addTraceBit(new TraceBit(parent, (Trace) selfPointer));
					}
				}
		);
		parent = Parent;
		width = Width;
		liveDuration = LiveDuration;
		firstBit = new TraceBit(Parent, this);
		color = Color;
		traceBitTimer.start(0.01, -1);
	}

	@Override
	public void update() {

		traceBitTimer.update();
		TraceBit bit = firstBit;
		while (bit.nextBit != null) {
			bit.update();
			bit = bit.nextBit;
		}
	}

	@Override
	public void draw(RedCanvas Canvas) {
		Graphics graphics = Canvas.getGraphics();
		points = new RedPoint[amountOfBits * 2];
		TraceBit bit = firstBit;
		RedPolygon polygon;
		for (int i = 0; i < amountOfBits - 1; i++) {
			polygon = new RedPolygon(0, 0, 0, 0,
					new RedPoint[]{
						bit.leftPoint,
						bit.rightPoint,
						bit.nextBit.rightPoint,
						bit.nextBit.leftPoint
					}, color);
			polygon.draw(Canvas);
			bit = bit.nextBit;
		}
	}

	public void addTraceBit(TraceBit Bit) {
		firstBit.getLastBit().nextBit = Bit;
		amountOfBits++;
	}

	public void removeTraceBit() {
		firstBit = firstBit.nextBit;
		amountOfBits--;
	}
}

class TraceBit {

	public double x, y;
	public Trace parent;
	public double timer;
	public TraceBit previousBit, nextBit;
	public RedPoint leftPoint, rightPoint;
	public double angle;

	public TraceBit(RedObject Tracer, Trace Parent) {
		parent = Parent;
		angle = Tracer.angle;
		timer = 0;
		x = Tracer.x + Tracer.width / 2;
		y = Tracer.y + Tracer.height / 2;
		leftPoint = new RedPoint(x - parent.width / 2 * Math.cos(angle), y - parent.width / 2 * Math.sin(angle));
		rightPoint = new RedPoint(x + parent.width / 2 * Math.cos(angle), y + parent.width / 2 * Math.sin(angle));
	}

	public TraceBit getLastBit() {
		return (nextBit == null) ? this : nextBit.getLastBit();
	}

	public int getAmount() {
		if (nextBit != null) {
			return 1 + nextBit.getAmount();
		} else {
			return 1;
		}
	}

	public void update() {
		timer += RedG.elapsed;
		if (timer < parent.liveDuration) {
			leftPoint = new RedPoint(x - parent.width / 2 * Math.cos(angle) * ((parent.liveDuration - timer) / parent.liveDuration), y - parent.width / 2 * Math.sin(angle) * ((parent.liveDuration - timer) / parent.liveDuration));
			rightPoint = new RedPoint(x + parent.width / 2 * Math.cos(angle) * ((parent.liveDuration - timer) / parent.liveDuration), y + parent.width / 2 * Math.sin(angle) * ((parent.liveDuration - timer) / parent.liveDuration));
		} else {
			parent.removeTraceBit();
		}
	}
}
