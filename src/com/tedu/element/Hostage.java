package com.tedu.element;

import java.awt.Graphics;
import java.util.List;

import javax.security.auth.x500.X500Principal;
import javax.swing.ImageIcon;

import com.tedu.controller.GameThread;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoader;
import com.tedu.manager.StateType;
import com.tedu.manager.WeaponType;

public class Hostage extends ElementObj{
	private String fx="left";
	private StateType stateType;
	private int runStep=0;
	private long runTime=0;
	@Override
	public ElementObj createElement(String string) {
		// TODO 自动生成的方法存根
		String[] split = string.split(",");
		this.setX(Integer.parseInt(split[0]));
		this.setY(Integer.parseInt(split[1]));
		ImageIcon imageIcon=GameLoader.imgMap.get("hostage_"+fx);
		this.setIcon(imageIcon);
		this.setW(imageIcon.getIconWidth());
		this.setH(imageIcon.getIconHeight());
		this.stateType=StateType.TRAPPED;
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
		if (this.stateType==StateType.RESECUDED) {
			run();
		}else {
			sos();
		}
	}
	
	public void run() {
		if (this.stateType!=StateType.RESECUDED) {
			return;
		}
		List<ImageIcon> imageIcons=GameLoader.imgMaps.get("hostage_"+fx+"_resecured");
		if (this.runStep>=imageIcons.size()-1) {
				this.runStep=0;
		}
		if (this.getLivingTime()-this.getRunTime()>20) {
			this.setX(getX()-10);
			this.setRunTime(this.getLivingTime());
			this.runStep++;
		}
		this.setIcon(imageIcons.get(this.runStep));
	}
	
	public void sos() {
		if (this.stateType!=StateType.TRAPPED) {
			return;
		}
		List<ImageIcon> imageIcons=GameLoader.imgMaps.get("hostage_"+fx+"_trapped");
		if (this.runStep>=imageIcons.size()-1) {
				this.runStep=0;
		}
		if (this.getLivingTime()-this.getRunTime()>20) {
			this.setRunTime(this.getLivingTime());
			this.runStep++;
		}
		this.setIcon(imageIcons.get(this.runStep));
	}
	
	public void changeState() {
		if (this.getStateType()==StateType.TRAPPED) {
			this.setStateType(StateType.RESECUDED);
			int x=this.getX()+this.getW()/2;
			int y=this.getY()+this.getH();
			ElementManager eManager=ElementManager.getManager();
			GameThread.score+=50;
			eManager.addElement(new Prop().createElement(x+","+y), GameElement.PROP);	
		}
	}
	@Override
	public void die() {
		// TODO 自动生成的方法存根
		if (this.getX()<0-this.getW()&&this.stateType==StateType.RESECUDED) {
			ElementManager elementManager=ElementManager.getManager();
			elementManager.getElementsByKey(GameElement.HOSTAGE).remove(this);
		}
	}
	
	public StateType getStateType() {
		return stateType;
	}
	public void setStateType(StateType stateType) {
		this.stateType = stateType;
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
