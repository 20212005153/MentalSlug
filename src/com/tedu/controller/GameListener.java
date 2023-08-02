package com.tedu.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tedu.element.ElementObj;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;

/**
 * @说明 监听类，用于监听用户的操作 KeyListener
 * @author renjj
 *
 */
public class GameListener implements KeyListener,MouseListener{
	private ElementManager em=ElementManager.getManager();
	
	/*能否通过一个集合来记录所有按下的键，如果重复触发，就直接结束
	 * 同时，第1次按下，记录到集合中，第2次判定集合中否有。
	 *       松开就直接删除集合中的记录。
	 * set集合
	 * */
	private Set<Integer> set=new HashSet<Integer>();
	
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
//		拿到玩家集合
		int key=e.getKeyCode();
		if(set.contains(key)) { //判定集合中是否已经存在,包含这个对象
			return;
		}
		set.add(key);
		List<ElementObj> play = em.getElementsByKey(GameElement.PLAY);
		for(ElementObj obj:play) {
			obj.keyClick(true, e.getKeyCode());
		}
	}
	/**松开*/
	@Override
	public void keyReleased(KeyEvent e) {
		if(!set.contains(e.getKeyCode())) {//如果这个不存在，就停止
			return;
		}//存在(已经按过这个案件)
		set.remove(e.getKeyCode());//移除数据
		List<ElementObj> play = em.getElementsByKey(GameElement.PLAY);
		for(ElementObj obj:play) {
			obj.keyClick(false, e.getKeyCode());
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO 自动生成的方法存根
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO 自动生成的方法存根
		List<ElementObj> play = em.getElementsByKey(GameElement.PLAY);
		for(ElementObj obj:play) {
			obj.mouseClick(true);
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO 自动生成的方法存根
		List<ElementObj> play = em.getElementsByKey(GameElement.PLAY);
		for(ElementObj obj:play) {
			obj.mouseClick(false);
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO 自动生成的方法存根
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO 自动生成的方法存根
		
	}

}
