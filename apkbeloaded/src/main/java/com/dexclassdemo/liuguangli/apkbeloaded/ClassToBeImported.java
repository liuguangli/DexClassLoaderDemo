package com.dexclassdemo.liuguangli.apkbeloaded;

import android.util.Log;

/**
 * Created by liuguangli on 16/2/13.
 */
public class ClassToBeImported {
    public static ClassLoader method(){
        Log.v("ClassToBeImported", "called method of class " + ClassToBeImported.class.getName());
        return ClassToBeImported.class.getClassLoader();
    }
}
