package com.tedu.element;

import java.awt.Graphics;

import javax.swing.ImageIcon;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoader;
import com.tedu.manager.StateType;

public class Prop extends ElementObj{

	@Override
	public void showElement(Graphics g) {
		// TODO 自动生成的方法存根
		g.drawImage(this.getIcon().getImage(), this.getX(), this.getY(), this.getW(), this.getH(), null);
	}
	
	@Override
	public ElementObj createElement(String string) {
		// TODO 自动生成的方法存根
		String[] split = string.split(",");
		this.setX(Integer.parseInt(split[0]));
		this.setY(Integer.parseInt(split[1]));
		ImageIcon imageIcon=GameLoader.imgMap.get("prop_1");
		this.setIcon(imageIcon);
		this.setW(imageIcon.getIconWidth());
		this.setH(imageIcon.getIconHeight());
		this.setX(this.getX()-this.getW());
		this.setY(this.getY()-this.getH());
		return this;
	}

	@Override
	public void getAttacked(int attack) {
		// TODO 自动生成的方法存根
		ElementManager elementManager=ElementManager.getManager();
		Play play=(Play)elementManager.getElementsByKey(GameElement.PLAY).get(0);
		play.setHp(play.getHp()+50);
		this.setLive(false);
	}
	
	@Override
	public void die() {
		// TODO 自动生成的方法存根
		if (this.isLive()) {
			return;
		}
		ElementManager elementManager=ElementManager.getManager();
		elementManager.getElementsByKey(GameElement.PROP).remove(this);
	}
}
