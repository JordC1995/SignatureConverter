package com.collier.jc.SignatureConverter.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
/**
 * @author Jordan
 *
 * Utility class contaiing methods which are used to execute image processing algorithms
 *
 * */
public class ImageHandler {

    private BufferedImage bi;

    public ImageHandler(byte[] bytes) {
        try {
            this.bi = imageConverter(bytes);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage imageConverter(byte[] bytes) throws IOException {
            // convert byte array back to BufferedImage
            InputStream in = new ByteArrayInputStream(bytes);

            return ImageIO.read(in);
    }

    public File imageConverter(BufferedImage bufferedImage, String type) throws IOException {

        if(type.equals("image/jpeg")) {
            bufferedImage = threshold(contrastStretch(bufferedImage, 125), 200);
            //bufferedImage = grayscale(bufferedImage);
            //bufferedImage = brighten(bufferedImage, 150);

            File outFile = new File("image.jpg");
            ImageIO.write(bufferedImage, "jpg", outFile);

            return outFile;
        } else {
            throw new IOException("Incompatible type");
        }
    }

    private BufferedImage grayscale(BufferedImage bi) {
        int x = bi.getWidth(), y = bi.getHeight();

        for(int y1 = 1 ; y1 < y; y1++) {
            for(int x1 = 1; x1 < x; x1++) {
                int r,g,b;
                Color c = new Color(bi.getRGB(x1,y1), true);
                r = c.getRed();
                g = c.getGreen();
                b = c.getBlue();

                int gs = Math.round((r + g + b) / 3);
                bi.setRGB(x1, y1, new Color(gs, gs, gs).getRGB());
            }
        }

        return bi;
    }

    //contrasts image con ranges between 1-255
    private BufferedImage contrastStretch(BufferedImage bi, int con) {
        brighten(bi, 125);
        float f = (259 * (con + 255)) / (255 * (259 - con));

        int x = bi.getWidth(), y = bi.getHeight();

        for(int y1 = 0 ; y1 < y; y1++) {
            for(int x1 = 0; x1 < x; x1++) {
                int r = 0,g = 0,b = 0;

                Color c = new Color(bi.getRGB(x1, y1), true);
                r = truncate(Math.round(f * (c.getRed() - 128) + 128));
                g = truncate(Math.round(f * (c.getGreen() - 128) + 128));
                b = truncate(Math.round(f * (c.getBlue() - 128) + 128));

                bi.setRGB(x1, y1, new Color(r, g, b).getRGB());
            }
            System.out.println( y1 + "rows processed");
        }

        return bi;
    }

    private BufferedImage brighten(BufferedImage bi, int brightness) {
        int x = bi.getWidth(), y = bi.getHeight();
        int r = 0, g = 0, b = 0;

        for(int y1 = 1; y1 < y; y1++) {
            for(int x1 = 1; x1 < x; x1++) {

                Color c = new Color(bi.getRGB(x1,y1), true);

                r = truncate(c.getRed() + brightness);
                g = truncate(c.getGreen() + brightness);
                b = truncate(c.getBlue() + brightness);

                bi.setRGB(x1, y1, new Color(r, g, b).getRGB());
            }
            System.out.println(y1 + " rows processed");
        }

        return bi;
    }

    private BufferedImage threshold(BufferedImage bi, int th) {
        int x = bi.getWidth(), y = bi.getHeight();

        for(int y1 = 1; y1 < y; y1++) {
            for(int x1 = 1; x1 < x; x1++) {

                Color c = new Color(bi.getRGB(x1,y1), true);

                if(((c.getRed() + c.getGreen() + c.getBlue()) / 3) < th) {
                    bi.setRGB(x1, y1, new Color(0, 0, 0).getRGB());
                } else {
                    bi.setRGB(x1, y1, new Color(255, 255, 255).getRGB());
                }
            }
            System.out.println(y1 + " rows processed");
        }

        return bi;
    }

    //private BufferedImage gaussianBlur(BufferedImage bi) {
      //  BufferedImage tmp = new BufferedImage();
        //int[][] kernel = {
         //       {1, 2, 1},
          //      {2, 4, 2},
          //      {1, 2, 1}
        //};


    //}


    private int truncate(int v) {

        if(v < 0) {
            return 0;
        } else if(v > 255) {
            return 255;
        }

        return v;
    }

    public BufferedImage getBi() {
        return bi;
    }
}
