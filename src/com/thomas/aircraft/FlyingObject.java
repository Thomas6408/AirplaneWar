package com.thomas.aircraft;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * @author Thomas_LittleTrain
 * @date 2020/6/28
 * 该类为飞行物类
 */
public abstract class FlyingObject implements Serializable {

    protected BufferedImage image;  //图片

    protected int width;  //宽

    protected int height; //高

    protected int x;  //x坐标

    protected int y;  //y坐标

    /**飞行物走步**/
    public abstract void step();

    /**检查是否出界**/
    public abstract boolean outOfBounds();

    /**敌人被子弹射击 this:敌人 bullet:子弹**/
    public boolean shootBy(Bullet bullet) {
        int x = bullet.x; //x:子弹的x
        int y = bullet.y;

        //x再x1和x2之间，并且，y在y1和y2之间，即为撞上了
        return x>this.x && x<this.x+this.width
                &&
                y>this.y && y<this.y+this.height;
    }
}
