package de.zabuza.soundEngine;

/**
 * Interface for SoundListeners. Can use {@link SoundEngine}s
 * setListener(SoundListener) to listen the sounds. Can use {@link SoundEngine}s
 * play(...) to play sounds with itself as {@link SoundSource}
 * 
 * @author Zabuza
 * 
 */
public interface SoundListener extends SoundSource {
}