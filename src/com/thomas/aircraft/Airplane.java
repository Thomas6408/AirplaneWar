package com.thomas.aircraft;

import java.io.Serializable;
import java.util.Random;

/**
 * @author Thomas_LittleTrain
 * @date 2020/6/28
 * 该类为敌机类
 * 敌机即是飞行物也是敌人
 */
public class Airplane extends FlyingObject implements Enemy, Serializable {

    private int speed = 2;  //走步的步数

    /**构造方法**/
    public Airplane() {
        //重写五个变量
        image = ShootGame.airplane;
        width = image.getWidth();
        height = image.getHeight();
        Random rand = new Random();  //调用随机数对象
        x = rand.nextInt(ShootGame.WIDTH-this.width);
        y = -this.height;
    }

    @Override
    public int getScore() {
        return 20;
    }

    /**重写飞行物走步 step()**/
    public void step() {
        y+=speed;  //向下
    }

    /**检查是否越界**/
    public boolean outOfBounds() {
        return this.y>= ShootGame.HEIGHT;
    }
}
