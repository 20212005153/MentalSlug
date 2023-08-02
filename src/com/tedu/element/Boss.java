package com.tedu.element;

import java.awt.Graphics;
import java.util.List;

import javax.swing.ImageIcon;

import com.tedu.controller.GameThread;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoader;
import com.tedu.manager.StateType;

public class Boss extends ElementObj{
	private int hp;
	private int runStep=0;
	private long runTime=0;
	
	@Override
	public ElementObj createElement(String string) {
		// TODO 自动生成的方法存根
		String[] split = string.split(",");
		this.setX(Integer.parseInt(split[0]));
		this.setY(Integer.parseInt(split[1]));
		ImageIcon imageIcon=GameLoader.imgMap.get("boss_1");
		this.setIcon(imageIcon);
		this.setW(imageIcon.getIconWidth());
		this.setH(imageIcon.getIconHeight());
		System.out.println(this.getW());
		this.setAttack(0);
		this.hp=200;
		return this;
	}
	@Override
	public void showElement(Graphics g) {
		// TODO 自动生成的方法存根
		g.drawImage(this.getIcon().getImage(), 
				this.getX(), this.getY(), 
				this.getW(), this.getH(), null);
	}
	
	@Override
	protected void move() {
		// TODO 自动生成的方法存根
		run();
	}
	
	public void run() {
		List<ImageIcon> imageIcons=GameLoader.imgMaps.get("boss_run");
		if (this.runStep>=imageIcons.size()-1) {
			this.runStep=0;
			}
		if (this.getLivingTime()-this.getRunTime()>20) {
			this.setX(getX()-3);
			this.setRunTime(this.getLivingTime());
			this.runStep++;
		}
		if (this.runStep==2||this.runStep==8) {
			this.setAttack(50);
		}else {
			this.setAttack(0);
		}
		ImageIcon imageIcon=imageIcons.get(this.runStep);
		this.setY(this.getY()+this.getH()-imageIcon.getIconHeight());
		this.setW(imageIcon.getIconWidth());
		this.setH(imageIcon.getIconHeight());
		this.setIcon(imageIcon);;
	}
	
	@Override
	public void getAttacked(int attack) {
		// TODO 自动生成的方法存根
		if (this.isLive()) {
			this.hp-=attack;
		}
		if (this.hp<=0) {
			this.setLive(false);//此处应该结束游戏
		}
	}
	
	@Override
	public void die() {
		// TODO 自动生成的方法存根
		if (this.getHp()<=0) {
			ElementManager eManager=ElementManager.getManager();
			GameThread.score+=100;
			eManager.getElementsByKey(GameElement.BOSS).remove(this);
		}

	}
	
	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
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
