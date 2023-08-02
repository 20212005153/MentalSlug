package com.tedu.element;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

import javax.swing.ImageIcon;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoader;

public class Missile extends ElementObj{
	private int runStep=0;
	private long runTime=0;
	@Override
	public ElementObj createElement(String string) {
		// TODO 自动生成的方法存根
		String[] split = string.split(",");
		this.setX(Integer.parseInt(split[0]));
		this.setY(Integer.parseInt(split[1]));
		ImageIcon imageIcon=GameLoader.imgMap.get("missile_1");
		this.setIcon(imageIcon);
		this.setW(imageIcon.getIconWidth());
		this.setH(imageIcon.getIconHeight());
		this.setAttack(30);
		return this;
	}
	@Override
	public void showElement(Graphics g) {
		// TODO 自动生成的方法存根
		g.drawImage(this.getIcon().getImage(), this.getX(), this.getY(), this.getW(), this.getH(), null);
	}
	
	@Override
	protected void move() {
		// TODO 自动生成的方法存根
		if (this.getX()<0||this.getX()>800||this.getY()>550-this.getH()) {
			this.setLive(false);
			return;
		}
		if (this.isLive()) {
			this.setY(this.getY()+5);
		}
			
	}
	@Override
	public void getAttacked(int attack) {
		// TODO 自动生成的方法存根
			this.setLive(false);//此处应该结束游戏
	}
	@Override
	public void die() {
		// TODO 自动生成的方法存根
		if (this.isLive()) {
			return;
		}
		List<ImageIcon> imageIcons=GameLoader.imgMaps.get("missile_bang");
		if (this.runStep==0) {
			this.setH(imageIcons.get(this.runStep).getIconHeight());
			this.setW(imageIcons.get(this.runStep).getIconWidth());
		}
		if (this.getLivingTime()-this.getRunTime()>20) {
			this.setRunTime(this.getLivingTime());
			this.runStep++;
		}
		if (this.runStep>=imageIcons.size()-1) {
			ElementManager eManager=ElementManager.getManager();
			eManager.getElementsByKey(GameElement.MISSILE).remove(this);
		}
		this.setIcon(imageIcons.get(this.runStep));
	}
	
	public int getRunStep() {
		return runStep;
	}
	public void setRunStep(int runStep) {
		this.runStep = runStep;
	}
	public long getRunTime() {
		return runTime;
	}
	public void setRunTime(long runTime) {
		this.runTime = runTime;
	}
	
	
}
