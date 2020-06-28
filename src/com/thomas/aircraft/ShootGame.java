package com.thomas.aircraft;

import test.ImageTest;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Thomas_LittleTrain
 * @date 2020/6/28
 */
public class ShootGame extends JPanel {
    public static final int WIDTH = 400;  //窗口的宽

    public static final int HEIGHT = 654; //窗口的高

    public static BufferedImage background;
    public static BufferedImage start;
    public static BufferedImage pause;
    public static BufferedImage gameover;
    public static BufferedImage airplane;
    public static BufferedImage bee;
    public static BufferedImage bullet;
    public static BufferedImage hero0;
    public static BufferedImage hero1;

    public static final int START = 0; //启动状态
    public static final int RUNNING = 1; //运行状态
    public static final int PAUSE = 2; //游戏暂停
    public static final int GAME_OVER = 3; //游戏结束
    private int state = START; //当前状态

    //英雄机对象
    private Hero hero = new Hero();

    //敌人数组(敌人+小蜜蜂)对象
    private FlyingObject[] flyings = {};

    //子弹数组对象
    private Bullet[] bullets = {};

    /**生成敌人对象**/
    public FlyingObject nextOne() {
        Random rand = new Random();  //创建随机数对象
        int type = rand.nextInt(20);  //生成0-19之间的随机数
        if(type==0) {
            return new Bee();
        }else {
            return new Airplane();
        }
    }

    /**实现敌人入场**/
    int flyEnteredIndex = 0;
    public void enterAction() {  //每10毫秒走一次
        //生成敌人对象，将对象添加到slyings数组中
        flyEnteredIndex++;
        if(flyEnteredIndex%40==0) {
            FlyingObject obj = nextOne(); //获取敌人
            flyings = Arrays.copyOf(flyings,flyings.length+1); //扩容
            flyings[flyings.length-1] = obj;  //将敌人添加到最后一个元素
        }
    }

    /**飞行物走步**/
    public void stepAction() {  //每10毫秒走一次
        hero.step();  //英雄机走步
        for (int i=0;i<flyings.length;i++) {
            flyings[i].step();  //敌人走步
        }
        for (int i=0;i<bullets.length;i++) {
            bullets[i].step();  //子弹走步
        }
    }

    /**子弹入场**/
    int shootIndex = 0;
    public void shootAction() {
        shootIndex++;
        if(shootIndex%30==0) {
            //创建子弹对象，添加到bullets数组中
            Bullet[] bs = hero.shoot(); //获取子弹对象
            bullets = Arrays.copyOf(bullets,bullets.length+bs.length);
            //数组的追加
            //(从bs中的第一个元素开始，复制到bullets数组，bullets.length-bs.length个开始，复制bs.length个)
            System.arraycopy(bs,0,bullets,bullets.length-bs.length,bs.length);

        }
    }

    /**删除越界敌人**/
    public void outOfBoundsAction() {
        int index = 0; //1.不越界敌人数组下标
        FlyingObject[] flyingLives = new FlyingObject[flyings.length];
        for(int i=0;i<flyings.length;i++) {
            FlyingObject f = flyings[i]; //获取每一个敌人
            if(!f.outOfBounds()){ //不越界
                flyingLives[index] = f;
                index++;
            }
        }
        flyings = Arrays.copyOf(flyingLives,index); //把flyingLives装进flyings

    }

    /**子弹与敌人的碰撞**/
    public void bangAction() {
        //多个子弹与多个敌人碰撞
        for(int i=0;i<bullets.length;i++) {
            Bullet b = bullets[i]; //把遍历的子弹存入数组
            bang(b);
        }
    }

    /**一个子弹与所有敌人的碰撞**/
    int score = 0; //得分
    public void bang(Bullet b) {
        int index = -1; //储存被撞敌人的下标

        for(int i=0;i<flyings.length;i++) { //遍历敌人
            FlyingObject f = flyings[i];
            if(f.shootBy(b)) { //撞上了
                index = i;
                break;
            }
        }
        if(index!=-1) {
            FlyingObject one = flyings[index]; //获得被撞敌人
            if(one instanceof Enemy) { //是敌人
                //是敌人，则玩家加分
                Enemy e = (Enemy)one;
                score += e.getScore();
            }
            if(one instanceof Award) { //是奖励
                //判断加命还是火力值
                Award a = (Award)one;
                int type = a.getType();
                switch(type) {
                    case Award.DOUBLE_FIRE:
                        hero.addDoubleFire();
                        break;
                    case Award.LIFE:
                        hero.addLife();
                        break;
                }
            }
            //删除被撞的敌人\
            //将被撞敌人与数组最后一个元素交换
            FlyingObject t = flyings[index];
            flyings[index] = flyings[flyings.length-1];
            flyings[flyings.length-1] = t;
            //缩容(去掉最后一个元素，即被撞的敌人对象)
            flyings = Arrays.copyOf(flyings,flyings.length-1);
        }
    }

    /**检查游戏结束**/
    public void checkGameOverAction() {
        if(isGameOver()) { //游戏结束
            state = GAME_OVER; //当前状态改为游戏结束
        }
    }

    /**判断游戏是否结束**/
    public boolean isGameOver() {
        for(int i=0;i<flyings.length;i++) { //遍历所有敌人
            FlyingObject f = flyings[i]; //
            if(hero.hit(f)) { //英雄机撞上了
                hero.subtractLife(); //英雄机减命
                hero.clearDoubleFire(); //英雄机清空火力值
                //将被撞敌人与数组最后一个交换
                FlyingObject t = flyings[i];
                flyings[i] = flyings[flyings.length-1];
                flyings[flyings.length-1] = t;
                //缩容(去掉被撞的敌人对象)
                flyings = Arrays.copyOf(flyings,flyings.length-1);
            }
        }
        return hero.getLife()<=0; //英雄机命数<=0,即为游戏结束
    }

    /**启动程序的执行**/
    public void action() {
        //创建监听器对象
        MouseAdapter l = new MouseAdapter() {
            /**重写鼠标移动事件**/
            public void mouseMoved(MouseEvent e) {
                if(state==RUNNING) {
                    int x = e.getX(); //获取鼠标的x坐标
                    int y = e.getY(); //获取鼠标的y坐标
                    hero.moveTo(x,y); //英雄机随着鼠标移动
                }
            }

            /**重写鼠标点击事件**/
            public void mouseClicked(MouseEvent e) {
                switch (state) { //根据当前状态做不同处理
                    case START: //启动状态时变为运行状态
                        state = RUNNING;
                        break;
                    case GAME_OVER: //游戏结束状态时变为启动状态
                        score = 0; //清理线程(数据归零)
                        hero = new Hero();
                        flyings = new FlyingObject[0];
                        bullets = new Bullet[0];
                        state = START; //变成启动状态
                        break;
                }
            }

            /**重写鼠标移出事件**/
            public void mouseExited(MouseEvent e) {
                if(state==RUNNING) {
                    state = PAUSE; //改为暂停状态
                }
            }

            /**重写鼠标移入事件**/
            public void mouseEntered(MouseEvent e) {
                if(state==PAUSE) {
                    state = RUNNING; //改为运行状态
                }
            }
        };
        this.addMouseListener(l);  //处理鼠标操作事件
        this.addMouseMotionListener(l); //处理鼠标滑动事件
        /**
         * timer.chedule(?,10,10)
         * 第一个10:从程序启动到第一次触发的时间间隔
         * 第二个10:第一次触发到第二次触发的时间间隔
         */
        java.util.Timer timer = new Timer();  //创建定时器对象
        int intervel = 10;  //时间间隔10毫秒
        timer.schedule(new TimerTask() {
            public void run() {
                if(state==RUNNING) {
                    enterAction();  //敌人
                    stepAction();   //飞行物
                    shootAction();  //子弹入场
                    outOfBoundsAction();  //删除越界的敌人
                    bangAction();  //子弹与敌人（敌人+小蜜蜂)碰撞
                    checkGameOverAction(); //检查游戏是否结束
                }
                repaint();  //重画--调用paint()方法
            }
        }, intervel, intervel);
    }

    //初始化静态资源
    static {
        try {
//            background = ImageIO.read(ShootGame.class.getResource("resources/background.png"));
            background = ImageIO.read(ShootGame.class.getClassLoader().getResourceAsStream("images/background.png"));
            airplane = ImageIO.read(ShootGame.class.getClassLoader().getResourceAsStream("images/airplane.png"));
            bee = ImageIO.read(ShootGame.class.getClassLoader().getResourceAsStream("images/bee.png"));
            bullet = ImageIO.read(ShootGame.class.getClassLoader().getResourceAsStream("images/bullet.png"));
            gameover = ImageIO.read(ShootGame.class.getClassLoader().getResourceAsStream("images/gameover.png"));
            hero0 = ImageIO.read(ShootGame.class.getClassLoader().getResourceAsStream("images/hero0.png"));
            hero1 = ImageIO.read(ShootGame.class.getClassLoader().getResourceAsStream("images/hero1.png"));
            pause = ImageIO.read(ShootGame.class.getClassLoader().getResourceAsStream("images/pause.png"));
            start = ImageIO.read(ShootGame.class.getClassLoader().getResourceAsStream("images/start.png"));
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("图片接收异常");
        }
    }

    /**重写paint() g:画笔 **/
    public void paint(Graphics g) {
        g.drawImage(background,0,0,null); //画背景图 (null)
        paintHero(g); //画英雄机
        paintFlyingObjects(g); //画敌人
        paintBullets(g); //画子弹对象
        paintScoreAndLife(g); //画分和画命
        paintState(g); //画状态
    }

    /**画英雄机对象**/
    public void paintHero(Graphics g) {
        g.drawImage(hero.image,hero.x,hero.y,null);
    }

    /**画敌人(敌机+小蜜蜂)对象**/
    public void paintFlyingObjects(Graphics g) {
        //遍历敌人
        for (int i=0;i<flyings.length;i++) {
            FlyingObject f = flyings[i]; //获取每一个敌人
            g.drawImage(f.image,f.x,f.y,null);
        }
    }

    /**画子弹对象**/
    public void paintBullets(Graphics g) {
        //遍历子弹
        for (int i=0;i<bullets.length;i++) {
            Bullet b = bullets[i]; //获取每一个子弹
            g.drawImage(b.image,b.x,b.y,null);
        }
    }

    /**画分和画命**/
    public void paintScoreAndLife(Graphics g) {
        g.setColor(new Color(0xFF0000)); //设置颜色
        g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24)); //设置字体(字体,加粗,字号)
        g.drawString("SCORE:"+score,10,25); //画分
        g.drawString("LIFE:"+hero.getLife(),10,45); //画命

    }

    /**画状态**/
    public void paintState(Graphics g) {
        switch(state) { //根据不同状态来画不同的图片
            case START:
                g.drawImage(start,0,0,null);
                break;
            case PAUSE:
                g.drawImage(pause,0,0,null);
                break;
            case GAME_OVER:
                g.drawImage(gameover,0,0,null);
                break;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Fly"); //窗口
        ShootGame game = new ShootGame();  //面板
        frame.add(game);  //把面板添加到窗口
        frame.setSize(WIDTH,HEIGHT); //设置窗口宽高
        frame.setAlwaysOnTop(true);  //设置一直在最上面
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //设置默认关闭操作(关闭窗口时退出程序)
        frame.setLocationRelativeTo(null);  //设置窗口居中显示
        frame.setVisible(true);  //1、设置窗口可见  2、尽快调用paint()方法
        game.action();  //启动程序的执行
    }
}
