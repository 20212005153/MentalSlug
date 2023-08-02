package com.tedu.manager;
/**
 * @说明 玩家的状态类型
 * @author 32708
 * @解释 STAND 站立 RUN 跑步 SQUAT 蹲下 JUMPUP 跳起上升 JUMPDOWN 跳起下降 CHANGE_AMMUNITION 换弹
 */
public enum StateType {
	CHANGE_AMMUNITION,OFF_FIRE,ON_FIRE,//上半身的可能状态
	STAND,RUN,SQUAT,JUMPUP,JUMPDOWN,//下半身的可能状态
	DIE,
	TRAPPED,RESECUDED;
}
