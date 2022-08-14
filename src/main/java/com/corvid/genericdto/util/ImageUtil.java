package com.corvid.genericdto.util;

import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.logging.Logger;


public class ImageUtil {
    private static Logger log = Logger.getLogger(ImageUtil.class.getName());

    // Extension of bmp image file
    public static final String BMP_IMAGE_EXTENSION = "bmp";

    // Extension of gif image file
    public static final String GIF_IMAGE_EXTENSION = "gif";

    // Extension of jpg image file
    public static final String JPG_IMAGE_EXTENSION = "jpg";

    // Extension of jepg image file
    public static final String JPEG_IMAGE_EXTENSION = "jpeg";

    // Extension of pcd image file
    public static final String PCD_IMAGE_EXTENSION = "pcd";

    // Extension of png image file
    public static final String PNG_IMAGE_EXTENSION = "png";

    // Extension of psd document/image file
    public static final String PSD_IMAGE_EXTENSION = "psd";

    // Extension of tga image file
    public static final String TGA_IMAGE_EXTENSION = "tga";

    // Extension of tif image file
    public static final String TIF_IMAGE_EXTENSION = "tif";

    public static final String[] IMAGE_EXTENTIONS = new String[]{BMP_IMAGE_EXTENSION,
            GIF_IMAGE_EXTENSION, JPG_IMAGE_EXTENSION, JPEG_IMAGE_EXTENSION, PCD_IMAGE_EXTENSION,
            PNG_IMAGE_EXTENSION, TGA_IMAGE_EXTENSION, TIF_IMAGE_EXTENSION};

    public static String[] getWidthAndHeightByWidth(File image, int widthParam) {
        String[] result = new String[2];
        if (image != null && image.isFile()) {
            InputStream in = null;
            try {
                in = new BufferedInputStream(new FileInputStream(image));
                return getWidthAndHeightByWidth(in, widthParam);
            } catch (Exception e) {
                if (image != null) {
                    log.info("ImageUtil.getWidthAndHeightByWidth,root.MSG_GEN_ERROR" +
                            "File not found : " + image.getAbsolutePath());
                }
            } finally {
                IOUtils.closeQuietly(in);
            }
        }
        result[0] = "";
        result[1] = "";
        return result;
    }

    public static String[] getWidthAndHeightByWidth(InputStream image, int widthParam) {
        String[] result = new String[2];
        try {
            BufferedImage inputBuf = ImageIO.read(image);
            // calcul de la taille de la sortie
            double inputBufWidth;
            double inputBufHeight;
            double width;
            double ratio;
            double height;
            if (inputBuf.getWidth() > widthParam) {
                inputBufWidth = inputBuf.getWidth();
                inputBufHeight = inputBuf.getHeight();
                width = widthParam;
                ratio = inputBufWidth / width;
                height = inputBufHeight / ratio;
            } else {
                width = inputBuf.getWidth();
                height = inputBuf.getHeight();
            }
            String sWidth = Double.toString(width);
            String sHeight = Double.toString(height);

            result[0] = sWidth.substring(0, sWidth.indexOf('.'));
            result[1] = sHeight.substring(0, sHeight.indexOf('.'));

            return result;
        } catch (Exception e) {
            if (image != null) {
                e.printStackTrace();
                log.info("ImageUtil.getWidthAndHeightByWidth");
            }
        }
        result[0] = "";
        result[1] = "";
        return result;
    }

    public static String[] getWidthAndHeightByHeight(File image, int heightParam) {
        String[] result = new String[2];
        if (image != null && image.isFile()) {
            InputStream in = null;
            try {
                in = new BufferedInputStream(new FileInputStream(image));
                return getWidthAndHeightByHeight(in, heightParam);
            } catch (Exception e) {
                if (image != null) {
                    log.info("ImageUtil.getWidthAndHeightByHeight" +
                            "File not found : " + image.getAbsolutePath());
                }
            } finally {
                IOUtils.closeQuietly(in);
            }
        }
        result[0] = "";
        result[1] = "";
        return result;
    }

    public static String[] getWidthAndHeightByHeight(InputStream image, int heightParam) {
        String[] result = new String[2];
        try {
            BufferedImage inputBuf = ImageIO.read(image);
            // calcul de la taille de la sortie
            double inputBufWidth;
            double inputBufHeight;
            double height;
            double ratio;
            double width;
            if (inputBuf.getHeight() > heightParam) {
                inputBufHeight = inputBuf.getHeight();
                inputBufWidth = inputBuf.getWidth();
                height = heightParam;
                ratio = inputBufHeight / height;
                width = inputBufWidth / ratio;
            } else {
                height = inputBuf.getHeight();
                width = inputBuf.getWidth();
            }
            String sWidth = Double.toString(width);
            String sHeight = Double.toString(height);

            result[0] = sWidth.substring(0, sWidth.indexOf('.'));
            result[1] = sHeight.substring(0, sHeight.indexOf('.'));

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ImageUtil.getWidthAndHeightByHeight");
        }
        return result;
    }

    public static String[] getWidthAndHeight(InputStream image) {
        String[] result = new String[2];
        try {
            BufferedImage inputBuf = ImageIO.read(image);
            // calcul de la taille de la sortie
            double inputBufWidth = inputBuf.getWidth();
            double inputBufHeight = inputBuf.getHeight();
            String sWidth = Double.toString(inputBufWidth);
            String sHeight = Double.toString(inputBufHeight);

            result[0] = sWidth.substring(0, sWidth.indexOf('.'));
            result[1] = sHeight.substring(0, sHeight.indexOf('.'));

            return result;
        } catch (Exception e) {
            if (image != null) {
                e.printStackTrace();
                log.info("ImageUtil.getWidthAndHeightByHeight");
            }
        }
        result[0] = "";
        result[1] = "";
        return result;
    }

    public static String[] getWidthAndHeight(File image) {
        String[] result = new String[2];
        if (image != null && image.isFile()) {
            InputStream in = null;
            try {
                in = new BufferedInputStream(new FileInputStream(image));
                return getWidthAndHeight(in);
            } catch (Exception e) {
                if (image != null) {
                    log.info("ImageUtil.getWidthAndHeight" +
                            "File not found : " + image.getAbsolutePath());
                }
            } finally {
                IOUtils.closeQuietly(in);
            }
        }
        result[0] = "";
        result[1] = "";
        return result;
    }
}