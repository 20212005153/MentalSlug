package com.tedu.element;

import java.awt.Graphics;

import javax.swing.ImageIcon;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoader;


public class Plane extends ElementObj{
	private long runTime=0;
	private long shootTime=0;
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
		ImageIcon imageIcon=GameLoader.imgMap.get("plane_1");
		this.setIcon(imageIcon);
		this.setW(imageIcon.getIconWidth());
		this.setH(imageIcon.getIconHeight());
		return this;
	}
	
	@Override
	protected void move() {
		// TODO 自动生成的方法存根
		if (this.getLivingTime()-this.getRunTime()>20) {
			this.setRunTime(this.getLivingTime());
			this.setX(this.getX()-10);
		}
	}
	
	@Override
	protected void shoot() {
		// TODO 自动生成的方法存根
		//射速控制
		if (this.getLivingTime()-this.shootTime<=300) {
			return;
		}
		ElementManager em = ElementManager.getManager();
		if (this.isLive()) {
				this.shootTime=this.getLivingTime();
				em.addElement(new Missile().createElement(this.toString()), GameElement.MISSILE);
		}
	}
	
	@Override
	public void die() {
		// TODO 自动生成的方法存根
		if (this.getX()+this.getW()<=0&&this.isLive()) {
			this.setLive(false);
		}
		if (!this.isLive()) {
			ElementManager eManager=ElementManager.getManager();
			eManager.getElementsByKey(GameElement.PLANE).remove(this);
		}
	}

	public long getRunTime() {
		return runTime;
	}

	public void setRunTime(long runTime) {
		this.runTime = runTime;
	}
	
	@Override
	public String toString() {
		// TODO 自动生成的方法存根
		int x=this.getX()+105-8;
		int y=this.getY()+107;
		return ""+x+","+y;
	}
}
