package com.tedu.element;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.ImageIcon;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoader;
import com.tedu.manager.WeaponType;

/**
 * @说明 玩家子弹类，本类的实体对象是由玩家对象调用和创建
 * @author renjj
 * @子类的开发步骤
 *   1.继承与元素基类;重写show方法
 *   2.按照需求选择性重写其他方法例如：move等
 *   3.思考并定义子类特有的属性
 */
public class Fire extends ElementObj{
	private int moveNum;//移动速度值
	private String fx;
	private WeaponType weaponType;
	private long deathTime;
//	剩下的大家扩展; 可以扩展出多种子弹： 激光，导弹等等。(玩家类就需要有子弹类型)
	public Fire() {}//一个空的构造方法
//	对创建这个对象的过程进行封装，外界只需要传输必要的约定参数，返回值就是对象实体
	@Override   //{X:3,y:5,f:up}
	public  ElementObj createElement(String str) {//定义字符串的规则
		String[] split = str.split(",");
		for(String str1 : split) {
			String[] split2 = str1.split(":");// 0下标 是 x,y,f   1下标是值
			switch(split2[0]) {
			case "x": this.setX(Integer.parseInt(split2[1]));break;
			case "y":this.setY(Integer.parseInt(split2[1]));break;
			case "f":this.fx=split2[1];break;
			case "weapon":this.weaponType=WeaponType.valueOf(split2[1].toUpperCase());break;
			}
		}
		if (this.weaponType==WeaponType.PISTOL) {
			this.moveNum=10;
			this.setAttack(20);
		}else if (this.weaponType==WeaponType.RIFLE) {
			this.moveNum=5;
			this.setAttack(10);
		}else if (this.weaponType==WeaponType.BAZOOKA) {
			this.moveNum=5;
			this.setAttack(30);
		}else if (this.weaponType==WeaponType.SHOTGUN) {
			this.moveNum=7;
			this.setAttack(50);
		}
		ImageIcon imageIcon =GameLoader.imgMap.get("fire_"+this.fx+"_"+this.weaponType.toString().toLowerCase()+"_0");
		this.setW(imageIcon.getIconWidth());
		this.setH(imageIcon.getIconHeight());
		this.setIcon(imageIcon);
		this.deathTime=0;
		return this;
	}
	@Override
	public void showElement(Graphics g) {	
		g.drawImage(this.getIcon().getImage(), this.getX(), this.getY(), this.getW(), this.getH(), null);
	}	
	@Override
	protected void move() {
		if(this.getX()<0 || this.getX() >900 || 
				this.getY() <0 || this.getY()>600) {
			this.setLive(false);
			return;
		}
		switch(this.fx) {
		case "left": this.setX(this.getX()-this.moveNum);break;
		case "right": this.setX(this.getX()+this.moveNum);break;
		}
	}
	/**
	 * 对于子弹来说：1.出边界  2.碰撞  3.玩家放保险
	 * 处理方式就是，当达到死亡的条件时，只进行 修改死亡状态的操作。
	 */
	@Override
	public void getAttacked(int attack) {
		// TODO 自动生成的方法存根
			this.setLive(false);
	}
	@Override
	public void die() {//+this.weaponType.toString().toLowerCase()+
		if (this.isLive()) {
			return;
		}
		this.setIcon(GameLoader.imgMap.get("fire_"+this.fx+"_"+"pistol_1"));
		if (this.deathTime==0) {
			this.deathTime=this.getLivingTime();
			this.setAttack(0);
		}
		if (this.getLivingTime()-this.deathTime>1) {
			ElementManager em =ElementManager.getManager();
			em.getElementsByKey(GameElement.FIRE).remove(this);
		}
	}
	
}





