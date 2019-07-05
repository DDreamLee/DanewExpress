package com.example.danewexpress.Util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/***    活动管理器：对活动进行管理    ***/

public class ActivityCollector {
    private static List<Activity> activities=new ArrayList<>();  /*  活动集合    */

    public static void addActivity(Activity activity){          /*  加入集合    */
        activities.add(activity);
    }

    public static void removeActivity(Activity activity){       /*  移出集合    */
        activities.remove(activity);
    }

    public static void finishAll(){                             /*  全部销毁：一次性退出程序时   */
        /*  迭代销毁集合中的所有活动    */
        for(Activity activity:activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }

        /*  为确保完全退出，杀死当前进程   */
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
