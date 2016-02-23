package de.zabuza.soundEngine;

import de.zabuza.soundEngine.sample.SoundSample;

/**
 * FakeSoundEngine. Should be used if {@link SoundEngine} can not be created.
 * 
 * @author Zabuza
 * 
 */
public class FakeSoundEngine extends SoundEngine {
	/**
	 * Normaly it would update the sounds and play the next bytes but in
	 * FakeSoundEngine it has no affect.
	 */
	@Override
	public void clientTick() {
	}

	@Override
	public final SoundSample loadSample(final String resourceName) {
		return null;
	}

	/**
	 * Normaly it would play a {@link SoundSample} but in FakeSoundEngine it has
	 * no affect.
	 * 
	 * @param sample
	 *            SoundSample to play
	 * @param soundSource
	 *            SoundSource of the sample
	 * @param priority
	 *            Priority of the sample
	 * @param rate
	 *            Rate of the sample
	 */
	@Override
	public void play(final SoundSample sample, final SoundSource soundSource,
			final float priority, final float rate) {
	}

	@Override
	public void run() {
	}

	@Override
	public void setListener(final SoundListener soundListener) {
	}

	@Override
	public void shutDown() {
	}

	@Override
	public void tick() {
	}
}