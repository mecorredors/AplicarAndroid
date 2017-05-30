package car.gov.co.carserviciociudadano.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import car.gov.co.carserviciociudadano.AppCar;


/**
 * Created by Olger on 14/01/2017.
 */
public class ImageUtil {

    public static  class Image{
        String path;
        String newName;
        String name;
        long size;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public String getNewName() {
            return newName;
        }

        public void setNewName(String newName) {
            this.newName = newName;
        }
    }
    public static Image scaledBitmap(String path,String nameDirectory) {
       return scaledBitmap(path,nameDirectory,false);
    }
    public static Image scaledBitmap(String path,String nameDirectory, boolean modePrivate) {
        float maxWidth = 832;
        float maxHeight = 624;
        String filePath = path;
        Image image = new Image();
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

        //by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
        //you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
        if (bmp == null) {

            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(filePath);
                BitmapFactory.decodeStream(inputStream, null, options);
                inputStream.close();
            } catch (FileNotFoundException exception) {
                exception.printStackTrace();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        float imgRatio = (float)actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        //width and height values are set maintaining the aspect ratio of the image
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight; actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

        //setting inSampleSize value allows to load a scaled down version of the original image
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

        //inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

        //this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            //load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
            if (bmp == null) {

                InputStream inputStream = null;
                try {
                    inputStream = new FileInputStream(filePath);
                    BitmapFactory.decodeStream(inputStream, null, options);
                    inputStream.close();
                } catch (FileNotFoundException exception) {
                    exception.printStackTrace(); return  null;
                } catch (IOException exception) {
                    exception.printStackTrace();  return  null;
                }
            }
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();  return  null;
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();  return  null;
        }catch (IllegalArgumentException ex){
            ex.printStackTrace(); return null;
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, 0, 0);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));

        //check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(),
                    matrix, true);
        } catch (IOException e) {
            e.printStackTrace();  return  null;
        }

        // String dir =   Environment.getExternalStorageDirectory() + "/FincaRaizPublish/";
        String dir = "";
        if (modePrivate)
             dir = AppCar.getContext().getFilesDir() + "/" +nameDirectory +"/";
        else
            dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" +nameDirectory +"/";

        File newdir = new File(dir);
        if (newdir.mkdirs() || newdir.isDirectory()) {

            String imageFileName = "Imagen_" + Utils.getTimeInMillis() + "_.jpg";
            String fileName = dir + imageFileName;

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

            image.setSize(bytes.size());

            File f = new File(fileName);

            try {
                f.createNewFile();
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());
                fo.close();

            } catch (IOException e) {
                Log.d("Utils.ScaledBitmap", e.toString());
                return null;
            }

            if(scaledBitmap!=null) scaledBitmap.recycle();
            if(scaledBitmap!=null)	scaledBitmap.recycle();

            image.setPath(fileName);
            image.setNewName(imageFileName);
            image.setName(imageFileName);
            int index = filePath.lastIndexOf("/");
            if (index > 0){
                filePath = filePath.substring(index+1);
                index = filePath.lastIndexOf(".");
                if (index > 0){
                    image.setName(filePath.substring(0,index));
                }
            }
            return image;
        } else {
            Log.d("Utils.ScaledBitmap", "No fue posible crear el directorio" + dir);
            return null;
        }
    }


    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
}
