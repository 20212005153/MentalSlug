package com.tedu.element;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.random.RandomGenerator.JumpableGenerator;

import javax.swing.ImageIcon;

import com.tedu.controller.GameThread;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoader;
import com.tedu.manager.StateType;
import com.tedu.manager.WeaponType;
/**
 * @说明 play 是玩家操控的元素 只有一个 
 * 		主要功能有 通过awsd来进行移动 通过点击鼠标来开火，即发射子弹 通过123来切换武器类型 通过空格来跳跃
 * 		自动会进行的有借助键盘监听器来修改位置，以及武器类型的修改，人物状态的修改 
 * 				 借助鼠标监听器来创建fire对象,
 * 				 根据武器类型和人物状态来变更形态
 * 				受攻击时的血量变化 
 * 				起跳时自动屏蔽加速
 * @author 32708
 * @param left左状态 right右状态
 * 必须有两个状态 上半身和下半身的状态
 */
public class Play extends ElementObj{

	private boolean left=false; //左
	private boolean up=false;   //上
	private boolean right=false;//右
	private boolean down=false; //下
	private String fx;
	private boolean ATK;//攻击状态 true 攻击  false停止
	private StateType highstateType;
	private StateType lowstateType;
	private WeaponType weaponType;
	private int hp;
	private ImageIcon highBody;
	private ImageIcon lowBody;//下半身
	private int runStep;
	private long runTime;
	private long shootTime=0L;
	private boolean squatType=false;
	
	//初始化三件套
	public Play() {}
	@Override
	public ElementObj createElement() {
		// TODO 自动生成的方法存根
		return createElement("");//玩家的特殊性 所以直接编程实现
	}
	@Override
	public ElementObj createElement(String str) {//初始化
		String[] split = str.split(",");
		this.setX(Integer.parseInt(split[0]));
		this.setY(Integer.parseInt(split[1]));
		this.fx="right";
		this.ATK=false;
		this.highstateType=StateType.OFF_FIRE;
		this.lowstateType=StateType.STAND;
		this.weaponType=WeaponType.PISTOL;
		this.hp=100;
		this.runStep=0;
		this.runTime=0;
		ImageIcon icon = GameLoader.imgMap.get("play_"+split[2]+"_"+getHighstateType().toString().toLowerCase()+"_"+
												getWeaponType().toString().toLowerCase()+"_"+0);//有待改进
		ImageIcon icon1=GameLoader.imgMap.get("play_low_"+split[2]+"_"+getLowstateType().toString().toLowerCase()+"_"+0);
		this.setW(icon.getIconWidth());
		this.setH(icon.getIconHeight()+icon1.getIconHeight());
		this.setHighBody(icon);this.setLowBody(icon1);
		
		return this;
	}
	
	@Override
	public void showElement(Graphics g) {
//		绘画图片
		if (fx.equals("left")) {
			g.drawImage(this.getLowBody().getImage(), 
					this.getX()+this.getHighBody().getIconWidth()-this.getLowBody().getIconWidth()-4, 
					this.getY()+this.getHighBody().getIconHeight()-7, 
					this.getLowBody().getIconWidth(), this.getLowBody().getIconHeight(), null);//微调		
		}else if (fx.equals("right")) {
		g.drawImage(this.getLowBody().getImage(), 
				this.getX()+4,
				this.getY()+this.getHighBody().getIconHeight()-7, 
				this.getLowBody().getIconWidth(), this.getLowBody().getIconHeight(), null);//微调			
		}

		g.drawImage(this.getHighBody().getImage(), 
				this.getX(), this.getY(), 
				this.getHighBody().getIconWidth(), this.getHighBody().getIconHeight(), null);
		
		g.setColor(Color.YELLOW);
		g.drawString("你的血量：", 10, 20);
		g.setColor(Color.GREEN);
		g.drawRect(75, 10, 100, 20);
		g.fillRect(75, 10, this.hp, 20);
		g.setColor(Color.RED);
		g.drawString(""+this.hp, 120, 25);
		
		g.setColor(Color.YELLOW);
		g.drawString("你的得分：", 200, 20);
		g.setColor(Color.WHITE);
		g.drawString(""+GameThread.score,265, 20);

	}

	@Override   // 注解 通过反射机制，为类或者方法或者属性 添加的注释(相当于身份证判定)
	public void keyClick(boolean bl,int key) { //只有按下或者鬆開才會 调用这个方法
		if(bl) {
			switch (key) {
			case 49:this.setWeaponType(WeaponType.PISTOL);break;
			case 50:this.setWeaponType(WeaponType.SHOTGUN);break;
			case 69:
				ElementManager eManager=ElementManager.getManager();
				List<ElementObj> list=eManager.getElementsByKey(GameElement.HOSTAGE);
				for (ElementObj elementObj : list) {
					Hostage hostage=(Hostage)elementObj;
					if (this.pk(hostage)) {
						hostage.changeState();
					}
				}
				break;
			case 65: 
				if(this.getLowstateType()==StateType.STAND) {
					this.setLowstateType(StateType.RUN);
					this.left=true;this.right=false;this.fx="left";
				}
				break;
			case 68: 
				if(this.getLowstateType()==StateType.STAND) {
					this.setLowstateType(StateType.RUN);this.right=true;this.left=false;this.fx="right";
				}
				break;
			case 82: this.lowstateType=StateType.CHANGE_AMMUNITION;break;//换弹
			case 17: //蹲下
					if (!squatType) {
						this.lowstateType=StateType.SQUAT;
						this.squatType=true;
					}else {
						this.lowstateType=StateType.STAND;
						this.squatType=false;
					}
				break;
			case 32: 
					this.lowstateType=StateType.JUMPUP;
				break;//跳跃
			}
		}
		else {
			switch (key) {
			//蹲 跑 可以随时转化为 站立 但是只有跳不行 得落地自动转化
			case 65: 
				if (this.lowstateType!=StateType.JUMPUP&&this.lowstateType!=StateType.JUMPDOWN) {
					this.lowstateType=StateType.STAND;
				}
				this.left=false;break;
			case 68: 
				if (this.lowstateType!=StateType.JUMPUP&&this.lowstateType!=StateType.JUMPDOWN) {
					this.lowstateType=StateType.STAND;
				}
				this.right=false;break;
			case 17: //蹲下
				break;
			case 32: break;
			}
		}
	}
	
	@Override
	public void mouseClick(boolean bl) {
		// TODO 自动生成的方法存根
			this.setATK(bl);
			if (bl) {
				this.setHighstateType(StateType.ON_FIRE);
			}else {
				this.setHighstateType(StateType.OFF_FIRE);
			}
			
	}
	@Override
	public void move() {//移动
		if (!this.squatType) {
			if (this.getLowstateType()==StateType.RUN) {
				if (this.left && this.getX()>0) {
				this.setX(this.getX() - 5);
				}
			if (this.right && this.getX()<900-this.getW()) {  
				this.setX(this.getX() + 5);
				}
			}
		}else {
			if (this.getLowstateType()==StateType.RUN) {
				if (this.left && this.getX()>0) {
				this.setX(this.getX() - 2);
			}
			if (this.right && this.getX()<900-this.getW()) {  
				this.setX(this.getX() + 2);
			}	
			}
		}
		
		if (this.lowstateType==StateType.JUMPUP) {
			jumpUp();
		}
		if (this.lowstateType==StateType.JUMPDOWN) {
			jumpDown();
		}
	}
	public void jumpUp() {
			this.setY(this.getY()-5);//跳起上升速度 未确定
			if (this.getY()<=200) {//到达一定高度
				this.lowstateType=StateType.JUMPDOWN;
			}
	}
	public void jumpDown() {
		this.setY(this.getY()+5);//跳起下降速度 未确定
			if (this.getY()>=450) {//下降到到一定高度
				this.lowstateType=StateType.STAND;//变回站立形态		
		}
	}
	public void run() {
		if (this.getLowstateType()!=StateType.RUN) {
			return;
		}
		List<ImageIcon> imageIcons=GameLoader.imgMaps.get("play_low_"+fx+"_run");
		if (this.runStep>=imageIcons.size()-1) {
				this.runStep=0;
			}
		if (this.getLivingTime()-this.getRunTime()>20) {
			this.setRunTime(this.getLivingTime());
			this.runStep++;
		}
		this.setLowBody(imageIcons.get(this.runStep));
	}
	public void squat() {
		if (!this.squatType) {
			return;
		}
		List<ImageIcon> imageIcons=GameLoader.imgMaps.get("play_low_"+fx+"_squat");
		if (this.runStep>=imageIcons.size()-1) {
				this.runStep=0;
			}
		if (this.getLivingTime()-this.getRunTime()>20) {
			this.setRunTime(this.getLivingTime());
			this.runStep++;
		}
		this.setLowBody(imageIcons.get(this.runStep));
	}
	@Override
	protected void changeClothes() {
		this.setHighBody(GameLoader.imgMap.get("play_"+fx+"_"+getHighstateType().toString().toLowerCase()+"_"+
				getWeaponType().toString().toLowerCase()+"_"+0));
		
		if (this.squatType) {
			squat();
		}else if (this.getLowstateType()==StateType.RUN){
			run();//跑步动作
		}
		else {
			this.setLowBody(GameLoader.imgMap.get("play_low_"+fx+"_"+this.getLowstateType().toString().toLowerCase()+"_"+0));//站立
		}
		
		
	}

	@Override   //添加子弹
	public void shoot() {//有时间就可以进行控制
		if(!this.ATK) {
			return;
		}
		//射速控制
		if (this.getLivingTime()-this.shootTime<=20&&this.getWeaponType()==WeaponType.PISTOL) {
			return;
		}
		if (this.getLivingTime()-this.shootTime<=50&&this.getWeaponType()==WeaponType.SHOTGUN) {
			return;
		}
		this.shootTime=this.getLivingTime();
		this.ATK=false; 		
		ElementObj element = new Fire().createElement(this.toString());
		ElementManager.getManager().addElement(element, GameElement.FIRE);
	}
	@Override
	public String toString() {//偷懒
		int x=this.getX();
		int y=this.getY();
		switch(this.fx) { // 子弹在发射时候就已经给予固定的轨迹。可以加上目标，修改json格式
		case "left": x-=90;y+=20;break;
		case "right": x+=90;y+=20;break;
		}
		return "x:"+x+",y:"+y+",f:"+this.fx+",weapon:"+this.weaponType;
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
		if (this.getHp()>0) {
			return;
		}
		
	}
	public WeaponType getWeaponType() {
		return weaponType;
	}
	public void setWeaponType(WeaponType weaponType) {
		this.weaponType = weaponType;
	}
	public boolean isATK() {
		return ATK;
	}
	public void setATK(boolean aTK) {
		ATK = aTK;
	}
	public ImageIcon getLowBody() {
		return lowBody;
	}
	public void setLowBody(ImageIcon lowBody) {
		this.lowBody = lowBody;
	}
	public ImageIcon getHighBody() {
		return highBody;
	}
	public void setHighBody(ImageIcon highBody) {
		this.highBody = highBody;
	}
	public StateType getHighstateType() {
		return highstateType;
	}
	public void setHighstateType(StateType highstateType) {
		this.highstateType = highstateType;
	}
	public StateType getLowstateType() {
		return lowstateType;
	}
	public void setLowstateType(StateType lowstateType) {
		this.lowstateType = lowstateType;
	}
	public long getRunTime() {
		return runTime;
	}
	public void setRunTime(long runTime) {
		this.runTime = runTime;
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		if (hp>=100) {
			this.hp = 100;
		}else {
			this.hp =hp;
		}
		
	}
	
	
	
}






