package com.tedu.controller;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;

import com.tedu.element.Billing;
import com.tedu.element.ElementObj;
import com.tedu.element.Enemy;
import com.tedu.element.Fire;
import com.tedu.element.Play;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoader;

/**
 * @说明 游戏的主线程，用于控制游戏加载，游戏关卡，游戏运行时自动化
 * 		游戏判定；游戏地图切换 资源释放和重新读取。。。
 * @author renjj
 * @继承 使用继承的方式实现多线程(一般建议使用接口实现)
 */
public class GameThread extends Thread{
	private ElementManager em;
	private String[] pKS= {"play,fire","enemy,fire","boss,fire","play,missile","play,prop"};
	public static int level=1;
	public static int score=0;
	public boolean isOver=false;
	public GameThread() {
		em=ElementManager.getManager();
	}
	@Override
	public void run() {//游戏的run方法  主线程
		while(true) { //扩展,可以讲true变为一个变量用于控制结束
//		游戏开始前   读进度条，加载游戏资源(场景资源)
			gameLoad(level);
//		游戏进行时   游戏过程中
			gameRun();
//		游戏场景结束  游戏资源回收(场景资源)
			gameOver();
			try {
				sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 游戏的加载
	 */
	private void gameLoad(int level) {
		if (level==1) {
			GameLoader.loadHighBodyImg(); //加载上半身图片
			GameLoader.loadLowBodyImg();
			GameLoader.loadData();
		}
		//		加载主角
		GameLoader.loadPlay();
		GameLoader.backgrondLoad(level);//可以变为 变量，每一关重新加载  加载地图
		GameLoader.enemyLoad(level);
		GameLoader.hostageLoad(level);
		GameLoader.planeLoad(level);
		GameLoader.bossLoad(level);
//		加载敌人NPC等
		
//		全部加载完成，游戏启动
	}
	/**
	 * @说明  游戏进行时
	 * @任务说明  游戏过程中需要做的事情：1.自动化玩家的移动，碰撞，死亡
	 *                                 2.新元素的增加(NPC死亡后出现道具)
	 *                                 3.暂停等等。。。。。
	 * 先实现主角的移动
	 * */
	
	private void gameRun() {
		long gameTime=0L;//给int类型就可以啦
		while(true) {// 预留扩展   true可以变为变量，用于控制管关卡结束等
			Map<GameElement, List<ElementObj>> all = em.getGameElements();
			moveAndUpdate(all,gameTime);//	游戏元素自动化方法
			ElementPK();
			if (isPlayDead()) {//下个关卡
				break;
			}
			if (isTimeForNext()) {//下个关卡
				break;
			}
			gameTime++;//唯一的时间控制
			try {
				sleep(10);//默认理解为 1秒刷新100次 
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void ElementPK() {
		for (String string : pKS) {
			String[] strings=string.split(",");
			List<ElementObj> listA=em.getElementsByKey(GameElement.valueOf(strings[0].toUpperCase()));
			List<ElementObj> listB=em.getElementsByKey(GameElement.valueOf(strings[1].toUpperCase()));
			for(int i=0;i<listA.size();i++) {
				ElementObj obj1 = listA.get(i);
				for(int j=0;j<listB.size();j++) {
					ElementObj obj2 = listB.get(j);
					if(obj1.pk(obj2)) {
						obj1.getAttacked(obj2.getAttack());
						obj2.getAttacked(obj1.getAttack());
						break;
					}
				}
			}
		}
	}
	
	
//	自动化
	public void moveAndUpdate(Map<GameElement, List<ElementObj>> all,long gameTime) {
		for(GameElement ge:GameElement.values()) {
			List<ElementObj> list = all.get(ge);
			for(int i=list.size()-1;i>=0;i--){	
				ElementObj obj=list.get(i);//读取为基类
				obj.model();
			}
		}	
	}
	
	public boolean isPlayDead() {
		Play play =(Play)em.getElementsByKey(GameElement.PLAY).get(0);
		if (!play.isLive()) {
			this.isOver=true;
			return true;
		}
		return false;
	}
	
	public boolean isTimeForNext() {
		if (em.getElementsByKey(GameElement.BOSS).size()==0) {
			return true;
		}
		return false;
	}
	
	/**游戏切换关卡*/
	private void gameOver() {
		level++;
		GameElement[] set = GameElement.values() ;
		for (GameElement gameElement : set) {
			List<ElementObj> list=em.getElementsByKey(gameElement);
			for (int i = 0; i < list.size(); i++) {
				list.remove(i);
			}
		}//清除所有元素
		if (level>=3) {
			this.isOver=true;
		}
		if (this.isOver) {
			em.addElement(new Billing(), GameElement.Billing);
			try {
				sleep(10000);
				System.exit(0);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			
		}
	}
	
}





