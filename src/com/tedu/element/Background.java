package com.tedu.element;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import javax.swing.ImageIcon;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoader;
import com.tedu.manager.StateType;

public class Background extends ElementObj{

	@Override
	public void showElement(Graphics g) {
		// TODO 自动生成的方法存根
		g.drawImage(this.getIcon().getImage(), this.getX(), 0,this.getW()*3,(int)(this.getH()*(2.55)) ,null);
	}
	 public ElementObj createElement(String string) {
		 this.setX(0);
		 this.setY(0);
		 ImageIcon imageIcon=GameLoader.imgMap.get("background_"+string);
		 this.setIcon(imageIcon);
		 this.setH(imageIcon.getIconHeight());
		 this.setW(imageIcon.getIconWidth());
		 return this;
	}
		@Override
		protected void move() {
			ElementManager eManager=ElementManager.getManager();
			List<ElementObj> list=eManager.getElementsByKey(GameElement.PLAY);
			Play play=(Play)list.get(0);
			this.setX(-play.getX()-50);
		}
}
