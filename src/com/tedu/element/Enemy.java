package com.tedu.element;

import java.awt.Graphics;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;

import com.tedu.controller.GameThread;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoader;
import com.tedu.manager.StateType;
import com.tedu.manager.WeaponType;

public class Enemy extends ElementObj{
	private String fx;
	private WeaponType weaponType;
	private StateType stateType=StateType.RUN;
	private long shootTime=0;
	private int runStep=0;
	private long runTime=0;
	private int hp;
	@Override
	public void showElement(Graphics g) {
		g.drawImage(this.getIcon().getImage(), 
				this.getX(), this.getY(), 
				this.getW(), this.getH(), null);
	}
	@Override
	public ElementObj createElement(String str) {
		String[] split = str.split(",");
		this.setX(Integer.parseInt(split[0]));
		this.setY(Integer.parseInt(split[1]));
		this.fx=split[2];
		this.weaponType=WeaponType.valueOf(split[3].toUpperCase());
		ImageIcon icon = GameLoader.imgMap.get("enemy_"+this.fx+"_"+this.weaponType.toString().toLowerCase());
		this.setW(icon.getIconWidth());
		this.setH(icon.getIconHeight());
		this.setIcon(icon);
		switch (this.weaponType.toString()) {
		case "RIFLE": this.hp=100;break;
		case "BAZOOKA":this.hp=50;break;
		}
		return this;
	}
	
	@Override
	protected void move() {
		// TODO 自动生成的方法存根
		if (this.stateType==StateType.STAND) {
			this.stateType=StateType.RUN;
		}else if (this.stateType==StateType.RUN) {
			run();
		}
		
	}
	public void run() {
		if (this.stateType!=StateType.RUN) {
			return;
		}
		List<ImageIcon> imageIcons=GameLoader.imgMaps.get("enemy_"+fx+"_"+this.weaponType.toString().toLowerCase()+"_run");
		if (this.runStep>=imageIcons.size()-1) {
			this.stateType=StateType.STAND;
			this.setIcon(GameLoader.imgMap.get("enemy_"+this.fx+"_"+this.weaponType.toString().toLowerCase()));
			this.runStep=0;
			}
		if (this.getLivingTime()-this.getRunTime()>20) {
			this.setX(getX()-3);
			this.setRunTime(this.getLivingTime());
			this.runStep++;
		}
		this.setIcon(imageIcons.get(this.runStep));
	}
	@Override
	protected void shoot() {
		// TODO 自动生成的方法存根
		//射速控制
		if (this.getLivingTime()-this.shootTime<=50&&this.getWeaponType()==WeaponType.RIFLE) {
			return;
		}
		if (this.getLivingTime()-this.shootTime<=100&&this.getWeaponType()==WeaponType.BAZOOKA) {
			return;
		}
		ElementManager em = ElementManager.getManager();
		if (this.isLive()) {
					this.shootTime=this.getLivingTime();
		em.addElement(new Fire().createElement(this.toString()), GameElement.FIRE);
		}
	}
	
	@Override
	public void getAttacked(int attack) {
		// TODO 自动生成的方法存根
		if (this.hp>0) {
			this.hp-=attack;
		}
		if (hp<=0) {
			this.setLive(false);
		}
	}
	
	@Override
	public String toString() {
		int x=this.getX();
		int y=this.getY();
		switch(this.fx) { 
		case "left": x-=20;y+=20;break;
		case "right": x+=55;y+=20;break;
		}
		return "x:"+(x+10)+",y:"+y+",f:"+this.fx+",weapon:"+this.weaponType;
	}
	public long getShootTime() {
		return shootTime;
	}
	public void setShootTime(long shootTime) {
		this.shootTime = shootTime;
	}
	public long getRunTime() {
		return runTime;
	}
	public void setRunTime(long runTime) {
		this.runTime = runTime;
	}
	
	public WeaponType getWeaponType() {
		return weaponType;
	}
	public void setWeaponType(WeaponType weaponType) {
		this.weaponType = weaponType;
	}
	@Override
	public void die() {
		// TODO 自动生成的方法存根
		if(this.isLive()) {
			return;
		}
		List<ImageIcon> imageIcons=GameLoader.imgMaps.get("enemy_"+fx+"_"+this.weaponType.toString().toLowerCase()+"_die");
		if (this.runStep!=0&&this.stateType!=StateType.DIE) {
			this.stateType=StateType.DIE;
			this.runStep=0;
		}
		if (this.getLivingTime()-this.getRunTime()>20) {
			this.setRunTime(this.getLivingTime());
			this.runStep++;
		}
		if (this.runStep>=imageIcons.size()-1) {
			ElementManager eManager=ElementManager.getManager();
			Play play=(Play)eManager.getElementsByKey(GameElement.PLAY).get(0);
			GameThread.score+=30;
			eManager.getElementsByKey(GameElement.ENEMY).remove(this);
		}
		ImageIcon imageIcon=imageIcons.get(this.runStep);
		this.setY(this.getY()+this.getH()-imageIcon.getIconHeight());
		this.setW(imageIcon.getIconWidth());
		this.setH(imageIcon.getIconHeight());
		this.setIcon(imageIcon);
	}
}
