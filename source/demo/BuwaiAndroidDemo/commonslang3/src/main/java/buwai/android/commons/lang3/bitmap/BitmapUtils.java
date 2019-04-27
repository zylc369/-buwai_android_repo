package buwai.android.commons.lang3.bitmap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;

/**
 * Bitmap工具类
 *
 * @author 不歪
 * @version 创建时间：2019-04-27 12 15
 */
public class BitmapUtils {

    public static byte[] bitmapDrawable2ByteArray(BitmapDrawable bitmapDrawable) {
        Bitmap bmp = bitmapDrawable.getBitmap();
        //第二步，声明并创建一个输出字节流对象
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        //第三步，调用compress将Bitmap对象压缩为PNG格式，第二个参数为PNG图片质量，第三个参数为接收容器，即输出字节流os
        bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
        //第四步，将输出字节流转换为字节数组，并直接进行存储数据库操作，注意，所对应的列的数据类型应该是BLOB类型
        return os.toByteArray();
    }

    public static Bitmap byteArray2BitMap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    public static BitmapDrawable bitmapToDrawable(Resources resources, Bitmap bitmap) {
        return new BitmapDrawable(resources, bitmap);
    }

    /**
     * drawable 转换成 bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth(); // 取 drawable 的长宽
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = (drawable.getOpacity() != PixelFormat.OPAQUE) ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565; // 取 drawable 的颜色格式
        Bitmap bitmap = Bitmap.createBitmap(width, height, config); // 建立对应 bitmap
        Canvas canvas = new Canvas(bitmap); // 建立对应 bitmap 的画布
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas); // 把 drawable 内容画到画布中
        return bitmap;
    }

    public static BitmapDrawable zoomDrawable(Drawable drawable, Float w, Float h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable);//drawable转换成bitmap
        Matrix matrix = new Matrix();//创建操作图片用的Matrix对象
        float scaleWidth = w / width;//计算缩放比例
        float scaleHeight = h / height;
        matrix.postScale(scaleWidth, scaleHeight);//设置缩放比例
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);//建立新的bitmap，其内容是对原bitmap的缩放后的图
        return new BitmapDrawable(newbmp);//把bitmap转换成drawable并返回
    }

}
