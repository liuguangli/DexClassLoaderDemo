package com.dexclassdemo.liuguangli.dexclassloaderdemo;


import android.os.Bundle;
import android.os.Environment;

import android.support.v7.app.AppCompatActivity;

import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;

import java.io.File;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String apkPath = Environment.getExternalStorageDirectory() + "/bundle.apk";
        loadApk(apkPath);
    }

    public  void loadApk(String apkPath) {


        Log.v("loadDexClasses", "Dex Preparing to loadDexClasses!");

        File dexOpt = this.getDir("dexOpt", MODE_PRIVATE);
            final DexClassLoader classloader = new DexClassLoader(
                    apkPath,
                    dexOpt.getAbsolutePath(),
                    null,
                    this.getClassLoader());

            Log.v("loadDexClasses", "Searching for class : "
                    + "com.registry.Registry");
            try {
                Class<?> classToLoad = (Class<?>) classloader.loadClass("com.dexclassdemo.liuguangli.apkbeloaded.ClassToBeLoad");
                Object instance = classToLoad.newInstance();
                Method method = classToLoad.getMethod("method");
                method.invoke(instance);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
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
