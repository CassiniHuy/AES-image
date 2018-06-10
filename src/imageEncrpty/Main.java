package imageEncrpty;

import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;

public class Main {
    public static void main(String[] args){
        try {
            File testImage = new File("./src/test.jpg");
            BufferedImage plainBufImg = ImageIO.read(testImage);

            SecretKeySpec key = new SecretKeySpec("aaaabbbbccccdddd".getBytes(), "AES");

            BufferedImage cipherBufImg = ImageAES.aesEcbNoPaddingImage(plainBufImg, key);

            File outImagePath = new File("./src/out.jpg");
            ImageIO.write(cipherBufImg, "JPEG", outImagePath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }
}
