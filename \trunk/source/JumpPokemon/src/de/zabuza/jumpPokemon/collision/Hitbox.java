package de.zabuza.jumpPokemon.collision;

import java.awt.Image;

public interface Hitbox {
	
	public float getX();
	public float getY();
	public float getAnimX();
	public float getAnimY();
	public float getXA();
	public float getYA();
	public int getWidth();
	public int getImageWidth();
	public Image getImage();
}
