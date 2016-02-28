package com.dexclassdemo.liuguangli.apkbeloaded;
import java.util.ArrayList;
/**
 * Created by liuguangli on 16/2/13.
 */
public class Registry {
    public static ArrayList<Class<?>> _classes = new ArrayList<Class<?>>();
    static{
        _classes.add(ClassToBeImported.class);
        //more classes here
    }
}
