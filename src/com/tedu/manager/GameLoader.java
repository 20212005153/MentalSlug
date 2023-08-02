package com.tedu.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.swing.ImageIcon;

import com.tedu.element.Background;
import com.tedu.element.Boss;
import com.tedu.element.ElementObj;
import com.tedu.element.Enemy;
import com.tedu.element.Hostage;
import com.tedu.element.Plane;
import com.tedu.element.Play;

/**
 * @说明  加载器(工具：用户读取配置文件的工具)工具类,大多提供的是 static方法
 * @author renjj
 * @使用pro文件来完成简单的加载 敌人的位置 boss的位置
 */
public class GameLoader {
//	得到资源管理器
	private static ElementManager em=ElementManager.getManager();
//	图片集合  使用map来进行存储     枚举类型配合移动(扩展)
	public static Map<String,ImageIcon> imgMap = new HashMap<>();
	public static Map<String,List<ImageIcon>> imgMaps=new HashMap<>();
	public static Map<String, List<String>> dataMap=new HashMap<>();
//	用户读取文件的类
	private static Properties pro =new Properties();	
	
	/**
	 *@说明 加载图片代码
	 *加载图片 代码和图片之间差 一个 路径问题 
	 */
	public static void loadHighBodyImg() {
		String texturl="com/tedu/text/GameData.pro";//文件的命名可以更加有规律
		ClassLoader classLoader = GameLoader.class.getClassLoader();
		InputStream texts = classLoader.getResourceAsStream(texturl);
		pro.clear();
		try {
			pro.load(texts);
			Set<Object> set = pro.keySet();//是一个set集合
			for(Object o:set) {
				String url=pro.getProperty(o.toString());
				imgMap.put(o.toString(), new ImageIcon(url));
			}	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void loadLowBodyImg() {
		String fileName="com/tedu/text/LowBodyImageData.pro";
		ClassLoader classLoader = GameLoader.class.getClassLoader();
		InputStream lowBodyImageData = classLoader.getResourceAsStream(fileName);
		if(lowBodyImageData == null) {
			System.out.println("配置文件读取异常,请重新安装");
			return;
		}
		
		try {
			pro.clear();
			pro.load(lowBodyImageData);
//			可以直接动态的获取所有的key，有key就可以获取 value
			Enumeration<?> names = pro.propertyNames();
			while(names.hasMoreElements()) {//获取是无序的
//				这样的迭代都有一个问题：一次迭代一个元素。
				String key=names.nextElement().toString();
				List<ImageIcon> list=new ArrayList<>();
//				就可以自动的创建和加载动作切片 
				String [] arrs=pro.getProperty(key).split(";");
				for(int i=0;i<arrs.length;i++) {
					ImageIcon imageIcon = new ImageIcon(""+arrs[i]);
					list.add(imageIcon);
				}
				imgMaps.put(key, list);
			}	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void loadData() {
		String fileName="com/tedu/text/ElementData.pro";
		ClassLoader classLoader = GameLoader.class.getClassLoader();
		InputStream elementData = classLoader.getResourceAsStream(fileName);
		if(elementData == null) {
			System.out.println("配置文件读取异常,请重新安装");
			return;
		}
		try {
			pro.clear();
			pro.load(elementData);
			Enumeration<?> names = pro.propertyNames();
			while(names.hasMoreElements()) {
				String key=names.nextElement().toString();
				List<String> list=new ArrayList<>();
				String [] arrs=pro.getProperty(key).split(";");
				for(int i=0;i<arrs.length;i++) {
					String string =arrs[i];
					list.add(string);
				}
				dataMap.put(key, list);
			}	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void backgrondLoad(int level) {
		String bgStr=""+level;
		ElementObj bg = new Background().createElement(bgStr);
		em.addElement(bg, GameElement.BACKGROUND);
	}
	
	public static void loadPlay() {
		String playStr=dataMap.get("play").get(0);
		ElementObj play = new Play().createElement(playStr);
		em.addElement(play, GameElement.PLAY);
	}
	
	 public static void enemyLoad(int level) {
		List<String> list=dataMap.get("enemy_"+level);
		if (list==null) {
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			String enemyString=list.get(i);
			em.addElement(new Enemy().createElement(enemyString), GameElement.ENEMY);
		}
	}
	 
	 public static void hostageLoad(int level) {
		 List<String> list=dataMap.get("hostage_"+level);
		 if (list==null) {
				return;
			}
			for (int i = 0; i < list.size(); i++) {
				String string=list.get(i);
				em.addElement(new Hostage().createElement(string), GameElement.HOSTAGE);
			}
	 }
	
	 public static void planeLoad(int level) {
		 List<String> list=dataMap.get("plane_"+level);
		 if (list==null) {
				return;
			}
			for (int i = 0; i < list.size(); i++) {
				String planeString=list.get(i);
				em.addElement(new Plane().createElement(planeString), GameElement.PLANE);
			}
	}
	 
	 public static void bossLoad(int level) {
		 List<String> list=dataMap.get("boss_"+level);
		 //boss和play不做检验 因为是必有的
			for (int i = 0; i < list.size(); i++) {
				String bossString=list.get(i);
				em.addElement(new Boss().createElement(bossString), GameElement.BOSS);
				}
	}
	 
}
	
