package de.zabuza.jumpPokemon.collision;

import de.zabuza.jumpPokemon.level.Level;

public class Collision {

	private Collision() {
		
	}
	
	public static final int NO_COLLIDE = 0;
	public static final int TOP = 1;
	public static final int RIGHT = 2;
	public static final int BOTTOM = 3;
	public static final int LEFT = 4;
	
	public static int spriteCollidesLevel(final Hitbox hitbox,
										final Level level) {
		return NO_COLLIDE;
	}
	
	public static int spriteCollideSprite(final Hitbox hitbox1,
										final Hitbox hitbox2) {
		return NO_COLLIDE;
	}
}
