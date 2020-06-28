package test;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author Thomas_LittleTrain
 * @date 2020/6/28
 */
public class ImageTest {
    @Test
    public void test1() {
        BufferedImage image = null;
        try {
            image = ImageIO.read(ImageTest.class.getClassLoader().getResourceAsStream("images.bee.png"));
//            BufferedImage background = ImageIO.read(ImageTest.class.getResource("background.png"));
            System.out.println(image);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("图片接收异常");
        }
    }
}
