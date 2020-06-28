package com.thomas.aircraft;

import java.io.Serializable;
import java.util.Random;

/**
 * @author Thomas_LittleTrain
 * @date 2020/6/28
 * 该类为小蜜蜂类
 * 小蜜蜂即是飞行物，也是奖励
 */
public class Bee extends FlyingObject implements Award, Serializable {

    private int xSpeed = 1;  //x坐标的走步步数

    private int ySpeed = 2;  //y坐标的走步步数

    private int awardType;   //奖励类型

    /**构造方法**/
    public Bee() {
        image = ShootGame.bee;
        width = image.getWidth();
        height = image.getHeight();
        Random rand = new Random();  //调用随机数对象
        x = rand.nextInt(ShootGame.WIDTH-this.width);
        y = -this.height;
        awardType = rand.nextInt(2);  //0-1之间的随机数
    }

    @Override
    public int getType() {
        return awardType;  //返回奖励值类型
    }

    /**重写飞行物 step()**/
    public void step() {
        x+=xSpeed; //向左或向右
        y+=ySpeed; //向下
        if(x>=ShootGame.WIDTH-this.width) {
            xSpeed = -1;
        }
        if(x<=0) {
            xSpeed = 1;
        }
    }

    @Override
    /**检查是否越界**/
    public boolean outOfBounds() {
        return this.y>= ShootGame.HEIGHT;
    }
}
