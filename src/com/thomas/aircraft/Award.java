package com.thomas.aircraft;

/**
 * @author Thomas_LittleTrain
 * @date 2020/6/28
 * 该类为奖励接口
 */
public interface Award {
    public int DOUBLE_FIRE = 0;  //火力值

    public int LIFE = 1;  //命

    /**获取奖励类型**/
    public int getType();
}
