package Traces;

import Engine.RedG;
import Engine.RedGame;
import Engine.RedGroup;
import Engine.RedLoad;
import Engine.RedText;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hara
 */
public class MainMenuState extends RedGroup {

    public RedText recordText = new RedText(RedGame.width / 2, RedGame.height / 6-4, "");
    public RedLoad loader;
    public Blackout blackout = new Blackout();
    public double timer;

    public MainMenuState() {
		try {
			this.loader = new RedLoad("./.save");
		} catch (IOException ex) {
			Logger.getLogger(MainMenuState.class.getName()).log(Level.SEVERE, null, ex);
			loader = null;
		}
        if (loader != null) {
			try {
				byte[] readed = new byte[8];
				loader.loadFile.read(readed);
				double last = ByteBuffer.wrap(readed).getDouble();
				loader.loadFile.read(readed);
				Traces.record = ByteBuffer.wrap(readed).getDouble();
				recordText.text = "Best time is: " + String.valueOf(((double) Math.round(Traces.record * 100)) / 100);
				recordText.text += "\nLast time is: " + String.valueOf(((double) Math.round(last * 100)) / 100);
				loader.close();
			} catch (IOException ex) {
				Logger.getLogger(MainMenuState.class.getName()).log(Level.SEVERE, null, ex);
			}
        } else {
            recordText.text = "No best time yet.";
            recordText.text += "\n";
        }
        recordText.centered = true;
        recordText.color = Traces.mainColor;
        recordText.shadow = Traces.secondColor;
        add(new RotatingStar(RedGame.width, 0, 128));
        add(new RotatingStar(0, RedGame.height, 128));
        add(recordText);
        recordText.text += "\n\n"
                + "Arrows to move\n"
                + "Space to start\n\n"
                + "Made by red__hara (press H)\n"
                + "Msuci by Kyleadam98 (press K)\n"
                + "and by F4LL0UT (press F)\n\n"
                + "We also have fullscreen (F11)\n"
                + "and music switch (press S)" ;
        add(blackout);
    }

    @Override
    public void create() {
        blackout.startBecomingBrighter();
    }

    @Override
    public void update() {
        super.update();
        timer += RedG.elapsed;
        recordText.y = RedGame.height / 6 + Math.sin(timer) * 4;
        recordText.color = 0xff000000 + Traces.mainColor;
        recordText.shadow = 0xff000000 + Traces.secondColor;
        if (!blackout.exist && RedG.keys.justPressed(' ')) {
            blackout.startBecomingDarker();
        }
        if (blackout.becomeDarker && blackout.done) {
            FullState.switchToState(new MainGameState());
        }

        if (RedG.keys.justPressed(RedG.keys.ESC)) {
            System.exit(0);
        }

        if (RedG.keys.justPressed('H')) {
			try {
				Desktop.getDesktop().browse( new URI("http://red-hara.newgrounds.com/"));
			} catch (IOException | URISyntaxException ex) {
				Logger.getLogger(MainMenuState.class.getName()).log(Level.SEVERE, null, ex);
			}
        }
        if (RedG.keys.justPressed('F')) {
			try {
				Desktop.getDesktop().browse( new URI("http://f4ll0ut.newgrounds.com/"));
			} catch (URISyntaxException | IOException ex) {
				Logger.getLogger(MainMenuState.class.getName()).log(Level.SEVERE, null, ex);
			}
        }
        if (RedG.keys.justPressed('K')) {
			try {
				Desktop.getDesktop().browse( new URI("http://kyleadam89.newgrounds.com/"));
			} catch (IOException | URISyntaxException ex) {
				Logger.getLogger(MainMenuState.class.getName()).log(Level.SEVERE, null, ex);
			}
        }
        if( RedG.keys.justPressed(122)) {
            RedG.setFullscreen(!Traces.fullscreen);
            Traces.fullscreen = !Traces.fullscreen;
        }
        if( RedG.keys.justPressed('S')) {
            FullState.music.destroy();
            FullState.music = new FlashingSound((!Traces.musicByF4LL0UT)?"/Traces/data/Neon (MatchTwo).wav":"/Traces/data/Sinnister loop(WIP).wav");
            FullState.music.start(-1);
            Traces.musicByF4LL0UT = !Traces.musicByF4LL0UT;
        }
    }

}
