package com.thomas.aircraft;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * @author Thomas_LittleTrain
 * @date 2020/6/28
 * 该类为英雄机类
 * 飞行物
 */
public class Hero extends FlyingObject implements Serializable {

    private int life;  //命

    private int doubleFire;  //火力值

    private BufferedImage[] images;  //图片数组

    private int index;  //协助图片切换

    /**构造方法**/
    public Hero() {
        image = ShootGame.hero0;
        width = image.getWidth();
        height = image.getHeight();
        x = 150;
        y = 400;
        life = 3;  //默认3条命
        doubleFire = 0;  //默认火力值0
        images = new BufferedImage[] { //两张图片切换
                ShootGame.hero0,ShootGame.hero1
        };
        index = 0;  //协助切换
    }

    /**重写飞行物 step()**/
    public void step() {
//        index++;
//        int a = index/10;
//        int b = a%2;
//        image = images[b];
        image = images[index++/10%images.length];
    }

    @Override
    public boolean outOfBounds() {
        return false; //永不越界
    }

    /**英雄机发射子弹**/
    public Bullet[] shoot() {
        int xStep = this.width/4;
        int yStep = 20; //y坐标向上20

        if(doubleFire>0) {  //双倍火力
            Bullet[] bs = new Bullet[2];
            bs[0] = new Bullet(this.x+xStep,this.y-yStep);
            bs[1] = new Bullet(this.x+3*xStep,this.y-yStep);
            doubleFire-=2;
            return bs;
        }else {  //单倍火力
            Bullet[] bs = new Bullet[1];
            bs[0] = new Bullet(this.x+2*xStep,this.y-yStep);
            return bs;
        }
    }

    /**英雄机移动**/
    public void moveTo(int x,int y) {
        this.x = x-this.width/2;
        this.y = y-this.height/2;
    }

    /**英雄机曾命**/
    public void addLife() {
        life++;
    }

    /**获取命**/
    public int getLife() {
        return life;
    }

    /**减命**/
    public void subtractLife() {
        life--; //火力值减1
    }

    /**清空火力**/
    public void clearDoubleFire() {
        doubleFire = 0; //火力值清零
    }

    /**英雄机增火力**/
    public void addDoubleFire() {
        doubleFire+=40; //火力值增40
    }

    /**英雄机撞敌人 this:英雄机 obj:敌人**/
    public boolean hit(FlyingObject obj) {
        int x1 = obj.x-this.width/2; //x1:敌人的x-1/2英雄机的宽
        int x2 = obj.x+obj.width+this.width/2;
        int y1 = obj.y-this.height/2; //y1:敌人的y-1/2英雄机的高
        int y2 = obj.y+obj.height+this.height/2;
        int x = this.x+this.width/2;
        int y = this.y+this.height/2;

        //x在x1和x2之间，并且，在y在y1和y2之间，即为撞上
        return x>x1 && x<x2
                &&
                y>y1 && y<y2;
    }
}
