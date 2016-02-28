package com.dexclassdemo.liuguangli.dexclassloaderdemo;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity {

    private static final String ASSETS_PLUGINS_DIR = "des";
    private static final String OPT_DIR = "opt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadDexClassses();
    }
    public  void loadDexClassses() {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            Log.v("loadDexClassses", "LoadDexClasses is only available for ICS or up");
        }
        String paths[] = null;
        try {
            paths = getAssets().list("plugins");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (paths == null) {
            Log.v("loadlDexClasses", "There was no " + paths);
            return;
        }

        Log.v("loadDexClasses", "Dex Preparing to loadDexClasses!");

        for (String file : paths) {
            File pluginDir  = Environment.getExternalStorageDirectory();
            pluginDir.mkdirs();
            String desDir = pluginDir.getAbsolutePath();
            String des = desDir + "/" + "apkbeloaded-debug.apk";
            File desFile = new File(des);
            File optimizedDirectory = this.getDir(OPT_DIR, Context.MODE_PRIVATE);
            if (!desFile.exists()){
                copyAssetsApkToFile(this, "plugins/"+file, des);
            }
            final DexClassLoader classloader = new DexClassLoader(
                    des, optimizedDirectory.getAbsolutePath(),
                    "data/local/tmp/natives/",
                    ClassLoader.getSystemClassLoader());

            Log.v("loadDexClasses", "Searching for class : "
                    + "com.registry.Registry");
            try {
                Class<?> classToLoad = (Class<?>) classloader.loadClass("com.dexclassdemo.liuguangli.apkbeloaded.Registry");

                Field classesField = classToLoad.getDeclaredField("_classes");

                ArrayList<Class<?>> classes = null;

                classes = (ArrayList<Class<?>>) classesField.get(null);
                for (Class<?> cls : classes) {
                    Log.v("loadDexClasses", "Class loaded " + cls.getName());
                    if (cls.getName().contains("ClassToBeImported")) {
                        Method m = cls.getMethod("method");
                        ClassLoader xb = (ClassLoader) m.invoke(null);
                        if (xb.equals(ClassLoader.getSystemClassLoader()))
                            Log.v("loadDexClasses", "Same ClassLoader");
                        else
                            Log.v("loadDexClasses", "Different ClassLoader");
                        Log.v("loadDexClasses", xb.toString());
                    }

                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }


        }
    }
    public void copyAssetsApkToFile(Context context, String src, String des) {
        try {
            InputStream is = context.getAssets().open(src);
            FileOutputStream fos = new FileOutputStream(new File(des));
            byte[] buffer = new byte[1024];
            while (true) {
                int len = is.read(buffer);
                if (len == -1) {
                    break;
                }
                fos.write(buffer, 0, len);
            }
            is.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
