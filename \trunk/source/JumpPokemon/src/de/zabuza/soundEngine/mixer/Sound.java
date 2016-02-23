package de.zabuza.soundEngine.mixer;

import de.zabuza.soundEngine.SoundListener;
import de.zabuza.soundEngine.SoundProducer;
import de.zabuza.soundEngine.SoundSource;

/**
 * Sound class Combines all what is needed for one sound line.
 * 
 * @author Zabuza
 * 
 */
public class Sound implements Comparable<Sound> {

	/**
	 * Logarithmus of 10.
	 */
	private static final double L10 = Math.log(10);
	/**
	 * Distance of sounds reference.
	 */
	private static final float REFERENCE_DISTANCE = 1;
	/**
	 * Sounds rolloff factor.
	 */
	private static final float ROLLOFF_FACTOR = 2;
	/**
	 * Sounds decibel factor.
	 */
	private static final int DECIBEL_FACTOR = 20;
	/**
	 * Decibel minimum of the sound.
	 */
	private static final int DECIBEL_MINIMUM = 6;
	/**
	 * A Divider for sounds pan calculation.
	 */
	private static final float PAN_DIVIDER_1 = 320f;
	/**
	 * A Divider for sounds pan calculation.
	 */
	private static final int PAN_DIVIDER_2 = 16;

	/**
	 * Sounds producer.
	 */
	private SoundProducer producer;
	/**
	 * Sounds source.
	 */
	private SoundSource source;
	/**
	 * Volume of the sound.
	 */
	private float volume;
	/**
	 * Priority of the sound.
	 */
	private float priority;

	/**
	 * Coordinates of the sound amplitude.
	 */
	private float x, y, z;
	/**
	 * Sounds score, used with sounds decibel for comparison.
	 */
	private float score = 0;

	/**
	 * Sounds pan or spread.
	 */
	private float pan;
	/**
	 * Sounds current amplitude.
	 */
	private float amplitude;

	/**
	 * Creates a new Sound with a SoundProducer, its SoundSource, volume and
	 * priority.
	 * 
	 * @param thatProducer
	 *            SoundProducer of the sound
	 * @param thatSource
	 *            SoundSource of the producer
	 * @param thatVolume
	 *            Volume of the sound
	 * @param thatPriority
	 *            Priority of the sound
	 */
	public Sound(final SoundProducer thatProducer,
			final SoundSource thatSource, final float thatVolume,
			final float thatPriority) {
		this.producer = thatProducer;
		this.source = thatSource;
		this.volume = thatVolume;
		this.priority = thatPriority;
	}

	@Override
	public final int compareTo(final Sound o) {
		Sound s = o;
		if (s.score > score) {
			return 1;
		}
		if (s.score < score) {
			return -1;
		}
		return 0;
	}

	/**
	 * Returns if the sample is still alive.
	 * 
	 * @return True if sample is alive
	 */
	public final boolean isLive() {
		return producer.isLive();
	}

	/**
	 * Reads the next sounds in the buffer with a reading rate.
	 * 
	 * @param buf
	 *            Sound buffer of the sound
	 * @param readRate
	 *            Reading rate
	 */
	public final void read(final float[] buf, final int readRate) {
		producer.read(buf, readRate);
	}

	/**
	 * Skips samples with a reading rate.
	 * 
	 * @param samplesToSkip
	 *            Samples to skip
	 * @param readRate
	 *            Reading rate
	 */
	public final void skip(final int samplesToSkip, final int readRate) {
		producer.skip(samplesToSkip, readRate);
	}

	/**
	 * Updates the sound.
	 * 
	 * @param listener
	 *            SoundListener of the sound
	 */
	public final void update(final SoundListener listener) {
		x = source.getX() - listener.getX();
		y = source.getY() - listener.getY();

		float distSqr = x * x + y * y + z * z;
		float dist = (float) Math.sqrt(distSqr);

		// float dB = (float)(volume + (20 * (Math.log(1.0 / distSqr) / l10)));
		float dB = (float) (volume - DECIBEL_FACTOR
				* Math.log(1 + ROLLOFF_FACTOR * (dist - REFERENCE_DISTANCE)
						/ REFERENCE_DISTANCE) / L10);
		dB = Math.min(dB, DECIBEL_MINIMUM);
		// dB = Math.max(dB, MIN_GAIN);

		score = dB * priority;

		// double angle = WMath.atan2(y, x);

		float p = -x / PAN_DIVIDER_1;
		if (p < -1) {
			p = -1;
		}
		if (p > 1) {
			p = 1;
		}
		float dd = distSqr / PAN_DIVIDER_2;
		if (dd > 1) {
			dd = 1;
		}
		pan = (p * dd);
		amplitude = volume * 1f;
	}

	/**
	 * Gets sounds amplitude.
	 * 
	 * @return Sounds amplitude
	 */
	protected final float getAmplitude() {
		return amplitude;
	}

	/**
	 * Gets sounds pan or spread.
	 * 
	 * @return Sounds pan or spread
	 */
	protected final float getPan() {
		return pan;
	}
}