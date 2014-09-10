package Traces;

import Engine.RedG;
import Engine.RedGame;
import Engine.RedSound;

/**
 *
 * @author hara
 */
public class FlashingSound extends RedSound {

	public double flashLevel = 1;
	public double niceLevel = 1;

	public FlashingSound(String SoundPath) {
		super(SoundPath);
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public byte[] updateSound() {
		flashLevel = 0;
		byte[] result = new byte[RedGame.sound.BUFFER_SIZE];
		for (int i = 0; i < Math.min(RedGame.sound.BUFFER_SIZE, soundData.length - position); i++) {
			result[i] = (byte) (soundData[i + position] * volume);
			flashLevel += Math.abs(soundData[i + position] * volume);
		}
		niceLevel = Math.max(flashLevel / RedGame.sound.BUFFER_SIZE, niceLevel - RedG.elapsed*4);
		position += RedGame.sound.BUFFER_SIZE;
		if (position >= soundData.length) {
			if (loopsLeft > 0) {
				loopsLeft--;
				position = 0;
			} else if (loopsLeft == -1) {
				position = 0;
			} else {
				destroy();
			}
		}
		return result;
	}
}
