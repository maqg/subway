package com.zz.henry.subwaytool;

import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.zz.henry.subwaytool.utils.ZoomImageView;


/**
 * Created by henry on 15/4/22.
 */
public class FragmentMap extends Fragment {

    private static View rootView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        /*int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        Log.d("TAG", "Max memory is " + maxMemory + "KB");*/

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.map, null);

            super.onCreate(savedInstanceState);

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(rootView.getResources(), R.drawable.subway_map_new, options);

            WindowManager wm = (WindowManager) getActivity().getSystemService(getActivity().WINDOW_SERVICE);

            Point point = new Point();
            wm.getDefaultDisplay().getSize(point);
            options.inSampleSize = calculateInSampleSize(options, point.x, point.y);

            Log.d("MAP", "got inSampleSize " + options.inSampleSize);

            // 使用获取到的inSampleSize值再次解析图片
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeResource(rootView.getResources(), R.drawable.subway_map_new, options);

            ZoomImageView zoomImageView = (ZoomImageView)rootView.findViewById(R.id.image);
            Log.d("MAP", "height is " + options.outHeight + ", width is " + options.outWidth
                    + ", devheight is " + zoomImageView.getHeight()
                    + ", devwidth is " + zoomImageView.getWidth());

            zoomImageView.setImage(bitmap);
        }

        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;
    }
}
