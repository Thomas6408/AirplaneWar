package com.thomas.aircraft;

import java.io.Serializable;

/**
 * @author Thomas_LittleTrain
 * @date 2020/6/28
 * 该类为子弹类
 * 子弹是飞行物
 */
public class Bullet extends FlyingObject implements Serializable {

    private int speed = 3;  //走步的步数

    /**构造方法**/
    public Bullet(int x,int y) {
        image = ShootGame.bullet;
        width = image.getWidth();
        height = image.getHeight();
        this.x = x;
        this.y = y;
    }

    /**重写step飞行物 ()**/
    public void step() {
        y-=speed;  //向上
    }

    @Override
    /**检查是否越界**/
    public boolean outOfBounds() {
        return this.y<=-this.height;
    }


}
