package Traces;

import Engine.RedCanvas;
import Engine.RedG;
import Engine.RedGame;
import Engine.RedGroup;
import Engine.RedMethod;
import Engine.RedObject;
import Engine.RedSave;
import Engine.RedText;
import Engine.RedTimer;
import java.awt.Color;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hara
 */
public class MainGameState extends RedGroup {

	public Borders borders = new Borders();
	public Player player = new Player(RedGame.width / 2 - 4, RedGame.height - 4);
	public RotatingStar starOne = new RotatingStar(0, 0, 128);
	public RotatingStar starTwo = new RotatingStar(RedGame.width, RedGame.height, 128);
	public double timer = 0;
	public RedText liveTime = new RedText(RedGame.width / 8, RedGame.height / 8 - 4, "");
	public boolean died = false;
	public Blackout blackout = new Blackout();
	public RedGroup rockets = new RedGroup();

	public RedTimer rocketTimer = new RedTimer(
			new RedMethod() {
				@Override
				public void execute() {
					rocketTimer.time *= 0.95;
					if (RedG.random() > 0.5) {
						rockets.add(new Rocket(0, 0, player));
					} else {
						rockets.add(new Rocket(RedGame.width, RedGame.height, player));
					}

					Traces.mainColor = (int) (RedG.random() * 0xffffff);
					Traces.secondColor = new Color(Traces.mainColor).darker().getRGB();
					Traces.mainColor = new Color(Traces.mainColor).brighter().getRGB();
				}
			}
	);

	public MainGameState() {
		super();
	}

	@Override
	public void create() {
		rocketTimer.start(5, -1);
		add(rocketTimer);
		add(starOne);
		add(starTwo);
		liveTime.color = Traces.mainColor;
		liveTime.shadow = Traces.secondColor;
		liveTime.zoom = 4 * RedGame.getWindow().displayZoom;
		liveTime.centered = true;
		add(liveTime);
		add(borders);
		add(player.trace);
		add(player.blackTrace);
		add(rockets);
		blackout.startBecomingBrighter();
	}

	@Override
	public void draw(RedCanvas Canvas) {
		super.draw(Canvas);
		player.draw(Canvas);
		blackout.draw(Canvas);
	}

	@Override
	public void update() {
		RedG.bgColor = 0xff000000 + Traces.secondColor;
		liveTime.color = 0xff000000 + Traces.mainColor;
		liveTime.shadow = 0xff000000 + Traces.secondColor;
		if (!died) {
			super.update();
			timer += RedG.elapsed;
			player.preUpdate();
			player.update();
			player.postUpdate();
		}
		liveTime.text = String.valueOf(((double) Math.round(timer * 100)) / 100);
		borders.doCollision(player);

		blackout.update();

		if (RedG.collide(player, rockets) && !died) {
			died = true;
			blackout.startBecomingDarker();

			if (timer > Traces.record) {
				Traces.record = timer;
			}
			RedSave saver;
			try {
				saver = new RedSave("./.save");
				
				saver.saveFile.write( ByteBuffer.wrap(new byte[8]).putDouble(timer).array());
				saver.saveFile.write(ByteBuffer.wrap(new byte[8]).putDouble(Traces.record).array());
				saver.close();
			} catch (IOException ex) {
				Logger.getLogger(MainGameState.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		if (died) {
			player.center.color = player.blackTrace.color = player.color = player.trace.color = (int) ((int) (0xff * (1 - blackout.darkness)) * 0x1000000) + (int) (Traces.mainColor);

		}
		if (died && blackout.done) {
			FullState.switchToState(new MainMenuState());
		}

		liveTime.zoom = RedGame.getWindow().displayZoom * 4 * FullState.music.niceLevel / 64;
		liveTime.x = RedGame.width / 8 / FullState.music.niceLevel * 64;
		liveTime.y = (RedGame.height / 8 - 4) / FullState.music.niceLevel * 64;

		starOne.displayZoom = starTwo.displayZoom = FullState.music.niceLevel / 64;
	}
}

class Borders extends RedGroup {

	public Borders() {
		super();
		add(new RedObject(-4, -4, RedGame.width + 8, 4));
		add(new RedObject(RedGame.width, -4, 4, RedGame.height + 8));
		add(new RedObject(-4, RedGame.height, RedGame.width + 8, 4));
		add(new RedObject(-4, -4, 4, RedGame.height + 8));
		for (Engine.RedBasic member : members) {
			((RedObject) member).immovable = true;
		}
	}

	public void doCollision(Player Player) {
		if (RedG.collide(members.get(1), Player) || RedG.collide(members.get(3), Player)) {
			Player.angle = -Player.angle;
		}
		if (RedG.collide(members.get(0), Player) || RedG.collide(members.get(2), Player)) {
			Player.angle = Math.PI - Player.angle;
		}
	}
}
