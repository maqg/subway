package com.zz.henry.subwaytool;

import android.graphics.Canvas;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.RadioGroup;


public class MainActivity extends FragmentActivity {

    private RadioGroup myTabRg;
    private FragmentMap map;
    private FragmentPiaojia piaojia;
    private FragmentShoumoche shoumoche;
    private FragmentConfig config;
    //private final static int CWJ_HEAP_SIZE = 40 * 1024 * 1024;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int widthPixels= dm.widthPixels;
        int heightPixels= dm.heightPixels;
        float density = dm.density;

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        Log.d("MAIN", "device display, height is " + heightPixels + ", width is " + widthPixels + "density is " + density);

        Canvas canvas = new Canvas();

        Log.d("MAIN", "max height" + canvas.getMaximumBitmapHeight() + ", max width is " + canvas.getMaximumBitmapWidth());

        setContentView(R.layout.activity_main);

        initView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void initView() {

        piaojia = new FragmentPiaojia();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, piaojia).commit();
        myTabRg = (RadioGroup) findViewById(R.id.tab_menu);
        myTabRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.rbPiaojia:
                        piaojia = new FragmentPiaojia();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, piaojia).commit();
                        break;

                    case R.id.rbShoumoche:
                        if (shoumoche == null) {
                            shoumoche = new FragmentShoumoche();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, shoumoche).commit();
                        break;

                    case R.id.rbMap:
                        map = new FragmentMap();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, map)
                                .commit();
                        break;

                    case R.id.rbConfig:
                        config = new FragmentConfig();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, config)
                                .commit();
                        break;

                    default:
                        break;
                }

            }
        });

    }
}
