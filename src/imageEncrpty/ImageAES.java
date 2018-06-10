package imageEncrpty;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author CassiniHuy
 * @version 1.0
 * encrypt image in aes
 * mix the pixels
 * */

public class ImageAES {

    /**
     * @param source Integer need to be split into four parts
     * @return the byte array contains 4 bytes
     * */
    public static byte[] intToByteArray(int source){
        byte[] portions = new byte[4];

        int mode = -1;  //means 11111111 11111111 11111111 11111111
        portions[0] = (byte)((source>>24) & mode);
        portions[1] = (byte)((source>>16) & mode);
        portions[2] = (byte)((source>>8) & mode);
        portions[3] = (byte)((source) & mode);

        return portions;
    }

    /**
     * @param parts the bytes made up of a integer
     * @return the integer the bytes make
     * */
    public static int byteArrayToInt(byte[] parts){
        if (parts.length != 4)
            try {
                throw new Exception("Need 4 bytes");
            } catch (Exception e) {
                e.printStackTrace();
            }
        int pixel = 0; //means 00000000 0000000 0000000 0000000

        pixel |= ((int) parts[3]) << 24;
        pixel >>>= 8;

        pixel |= ((int) parts[2]) << 24;
        pixel >>>= 8;

        pixel |= ((int) parts[1]) << 24;
        pixel >>>= 8;

        pixel |= ((int) parts[0]) << 24;

        return pixel;
    }

    /**
     * one pixel is an int (4 byte) don't need padding
     *
     * @param plainImage the image needed be encrypted
     * @param key the key
     * @return the cipher image, if go wrong null
     * */
    public static BufferedImage aesEcbNoPaddingImage(BufferedImage plainImage, SecretKeySpec key)
            throws InvalidKeyException {

        int width = plainImage.getWidth();
        int height = plainImage.getHeight();
        int miniX = plainImage.getMinTileX();
        int miniY = plainImage.getMinY();
        try {
            Cipher enCipher = Cipher.getInstance("AES/ECB/NoPadding");

            enCipher.init(Cipher.ENCRYPT_MODE, key);

            int[] pixels = plainImage.getRGB(miniX, miniY, width, height, null, 0, width);

            ByteArrayOutputStream bArrayOutStream = new ByteArrayOutputStream();
            for (int temp : pixels)
                bArrayOutStream.write(intToByteArray(temp));
            byte[] temp = bArrayOutStream.toByteArray();
            byte[] cipherBytes = enCipher.doFinal(temp);

            for (int i = 0; i < cipherBytes.length; i += 4)
                pixels[i / 4] = byteArrayToInt(new byte[]{
                            cipherBytes[i],
                            cipherBytes[i+1],
                            cipherBytes[i+2],
                            cipherBytes[i+3], } );

            BufferedImage cipherBufImg = plainImage.getSubimage(miniX, miniY, width, height);
            cipherBufImg.setRGB(miniX, miniY, width, height, pixels, 0, width);
            return cipherBufImg;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return null;
    }
}
