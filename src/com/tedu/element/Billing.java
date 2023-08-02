package com.tedu.element;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.tedu.controller.GameThread;

public class Billing extends ElementObj{

	@Override
	public void showElement(Graphics g) {
		// TODO 自动生成的方法存根
		g.setColor(Color.RED);
		g.fillRect(0, 0, 800, 600);
		g.setColor(Color.WHITE);
		g.setFont(new Font("宋体",Font.BOLD,16));
		g.drawString("游戏结束！", 200, 200);
		g.drawString("你的得分为："+GameThread.score, 400, 400);
	}

}
