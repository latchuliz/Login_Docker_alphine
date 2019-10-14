// Copyright 2017 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package com.neopharma.datavault.utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageResizer {
    private final File externalFilesDirectory;
    private final ExifDataCopier exifDataCopier;

    public ImageResizer(File externalFilesDirectory, ExifDataCopier exifDataCopier) {
        this.externalFilesDirectory = externalFilesDirectory;
        this.exifDataCopier = exifDataCopier;
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        return rotatedImg;
    }

    /**
     * If necessary, resizes the image located in imagePath and then returns the path for the scaled
     * image.
     *
     * <p>If no resizing is needed, returns the path for the original image.
     */
    public String resizeImageIfNeeded(String imagePath, Double maxWidth, Double maxHeight) {
        boolean shouldScale = maxWidth != null || maxHeight != null;

        if (!shouldScale) {
            return imagePath;
        }

        try {
            File scaledImage = resizedImage(imagePath, maxWidth, maxHeight);
            exifDataCopier.copyExif(imagePath, scaledImage.getPath());

            return scaledImage.getPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File resizedImage(String path, Double maxWidth, Double maxHeight) throws IOException {
        Bitmap bmp = BitmapFactory.decodeFile(path);
        double originalWidth = bmp.getWidth() * 1.0;
        double originalHeight = bmp.getHeight() * 1.0;

        boolean hasMaxWidth = maxWidth != null;
        boolean hasMaxHeight = maxHeight != null;

        Double width = hasMaxWidth ? Math.min(originalWidth, maxWidth) : originalWidth;
        Double height = hasMaxHeight ? Math.min(originalHeight, maxHeight) : originalHeight;

        boolean shouldDownscaleWidth = hasMaxWidth && maxWidth < originalWidth;
        boolean shouldDownscaleHeight = hasMaxHeight && maxHeight < originalHeight;
        boolean shouldDownscale = shouldDownscaleWidth || shouldDownscaleHeight;

        if (shouldDownscale) {
            double downscaledWidth = (height / originalHeight) * originalWidth;
            double downscaledHeight = (width / originalWidth) * originalHeight;

            if (width < height) {
                if (!hasMaxWidth) {
                    width = downscaledWidth;
                } else {
                    height = downscaledHeight;
                }
            } else if (height < width) {
                if (!hasMaxHeight) {
                    height = downscaledHeight;
                } else {
                    width = downscaledWidth;
                }
            } else {
                if (originalWidth < originalHeight) {
                    width = downscaledWidth;
                } else if (originalHeight < originalWidth) {
                    height = downscaledHeight;
                }
            }
        }

        Bitmap scaledBmp = Bitmap.createScaledBitmap(bmp, width.intValue(), height.intValue(), false);
        scaledBmp = ImageRotate(scaledBmp, path);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        scaledBmp.compress(Bitmap.CompressFormat.JPEG, 75, outputStream);

        FileOutputStream fileOutput = new FileOutputStream(externalFilesDirectory);
        fileOutput.write(outputStream.toByteArray());
        fileOutput.close();

        return externalFilesDirectory;
    }

    private Bitmap ImageRotate(Bitmap img, String path) {
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(img, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(img, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(img, 270);
                default:
                    return img;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    public String convertToBase64(String imagePath) {
        Bitmap bm = BitmapFactory.decodeFile(imagePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] byteArrayImage = baos.toByteArray();
        return Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
    }
}
