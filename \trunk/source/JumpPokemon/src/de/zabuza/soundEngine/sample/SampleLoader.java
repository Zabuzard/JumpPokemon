package de.zabuza.soundEngine.sample;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Static SampleLoader utility class. Can load Samples from URLs
 * 
 * @author Zabuza
 * 
 */
public final class SampleLoader {

	/**
	 * Supported default sample size.
	 */
	private static final int SAMPLE_SIZE = 8;
	/**
	 * Size of the sample buffer.
	 */
	private static final int BUF_SIZE = 4096;

	/**
	 * Hexadezimal FF.
	 */
	private static final int HEX_FF = 0xFF;
	/**
	 * Hexadezimal 80.
	 */
	private static final int HEX_80 = 0x80;
	/**
	 * Hexadezimal FFFF.
	 */
	private static final int HEX_FFFF = 0xFFFF;
	/**
	 * Hexadezimal 8000.
	 */
	private static final int HEX_8000 = 0x8000;
	/**
	 * Hexadezimal FFFFFFFF.
	 */
	private static final long HEX_FFFFFFFF = 0xFFFFFFFFL;
	/**
	 * Hexadezimal 80000000.
	 */
	private static final long HEX_80000000 = 0x80000000L;

	/**
	 * Loads a SoundSample from an url.
	 * 
	 * @param resourceName
	 *            URL of the sample
	 * @return Loaded sample
	 * @throws UnsupportedAudioFileException
	 *             If sample is not supported, e.g. stereo samples, unsupported
	 *             sample sizes or a unsupported encoding
	 * @throws IOException
	 *             If an IO Exception occurred
	 */
	public static SoundSample loadSample(final String resourceName)
			throws UnsupportedAudioFileException, IOException {
		// Hack to prevent "mark/reset not supported" on some systems
		byte[] d = rip(SampleLoader.class.getResourceAsStream(resourceName));
		AudioInputStream ais = AudioSystem
				.getAudioInputStream(new ByteArrayInputStream(d));
		return buildSample(rip(ais), ais.getFormat());
	}

	/**
	 * Reorganizes audio sample data into the intenal sound format.
	 * 
	 * @param b
	 *            Audio buffer of the sample
	 * @param af
	 *            AudioFormat of the sample
	 * @return Compatible SoundSample
	 * @throws UnsupportedAudioFileException
	 *             If sample is not supported, e.g. stereo samples, unsupported
	 *             sample sizes or a unsupported encoding
	 */
	private static SoundSample buildSample(final byte[] b, final AudioFormat af)
			throws UnsupportedAudioFileException {
		// Rip audioformat data
		int channels = af.getChannels();
		int sampleSize = af.getSampleSizeInBits();
		float rate = af.getFrameRate();
		boolean signed = af.getEncoding() == AudioFormat.Encoding.PCM_SIGNED;

		// Sanity checking
		if (channels != 1) {
			throw new UnsupportedAudioFileException(
					"Only mono samples are supported");
		}
		if (!(sampleSize == SAMPLE_SIZE || sampleSize == SAMPLE_SIZE * 2 || sampleSize == SAMPLE_SIZE * 2 * 2)) {
			throw new UnsupportedAudioFileException("Unsupported sample size");
		}
		if (!(af.getEncoding() == AudioFormat.Encoding.PCM_UNSIGNED || af
				.getEncoding() == AudioFormat.Encoding.PCM_SIGNED)) {
			throw new UnsupportedAudioFileException("Unsupported encoding");
		}

		// Wrap the data into a bytebuffer, and set up the byte order
		ByteBuffer bb = ByteBuffer.wrap(b);

		if (af.isBigEndian()) {
			bb.order(ByteOrder.BIG_ENDIAN);
		} else {
			bb.order(ByteOrder.LITTLE_ENDIAN);
		}

		int s = b.length / (sampleSize / SAMPLE_SIZE);
		float[] buf = new float[s];

		// Six different cases for reordering the data.
		// Can this be improved without slowing it down?

		if (sampleSize == SAMPLE_SIZE) {
			if (signed) {
				for (int i = 0; i < s; i++) {
					buf[i] = bb.get() / (float) HEX_80;
				}
			} else {
				for (int i = 0; i < s; i++) {
					buf[i] = ((bb.get() & HEX_FF) - HEX_80) / (float) HEX_80;
				}
			}
		} else if (sampleSize == SAMPLE_SIZE * 2) {
			if (signed) {
				for (int i = 0; i < s; i++) {
					buf[i] = bb.getShort() / (float) HEX_8000;
				}
			} else {
				for (int i = 0; i < s; i++) {
					buf[i] = ((bb.getShort() & HEX_FFFF) - HEX_8000)
							/ (float) HEX_8000;
				}
			}
		} else if (sampleSize == SAMPLE_SIZE * 2 * 2) {
			if (signed) {
				for (int i = 0; i < s; i++) {
					buf[i] = bb.getInt() / (float) HEX_80000000;
				}
			} else {
				// Nasty.. check this.
				for (int i = 0; i < s; i++) {
					buf[i] = ((bb.getInt() & HEX_FFFFFFFF) - HEX_80000000)
							/ (float) HEX_80000000;
				}
			}
		}

		// Return the completed sample
		return new SoundSample(buf, rate);
	}

	/**
	 * Rips the entire contents of an InputStream into a byte array.
	 * 
	 * @param in
	 *            InputStream to rip
	 * @return Ripped byte array
	 * @throws IOException
	 *             If an IO Exception occured.
	 */
	private static byte[] rip(final InputStream in) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] b = new byte[BUF_SIZE];

		int read = 0;
		while ((read = in.read(b)) > 0) {
			bos.write(b, 0, read);
		}

		bos.close();
		return bos.toByteArray();
	}

	/**
	 * Utility class, constructor has no effect.
	 */
	private SampleLoader() {
	}
}