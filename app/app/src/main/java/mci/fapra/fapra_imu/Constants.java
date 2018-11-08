package mci.fapra.fapra_imu;

import android.content.res.Resources;
import android.os.Build;


public class Constants {

    public static final String m_androidId = Build.MODEL;
    public static final int SCREEN_WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels;
    public static final int SCREEN_HEIGHT = Resources.getSystem().getDisplayMetrics().heightPixels;

    // Target sizes
    public static final int CIRCLE_RADIUS = 50;

    //Pixel Sizes
    public static final double PX_S3_MINI = 0.1089;
    public static final double PX_S4 = 0.0577;
    public static final double PX_NEXUS = 0.0515;
    public static final double PX_ONEPLUSONE = 0.0634;

    // CSV Delimiter
    public static final String CSV_DELIMITER = ";";

    public static final int AMOUNT_REPETITIONS = 12;

    public static int getTargetPixelsForPhone(){
        int pixels = -1;
        double constant = 7.194599999999999;
        switch (m_androidId){
            case "Nexus 6":
                pixels = (int)(constant/PX_NEXUS);
                break;
            case "A0001":
                pixels = (int)(constant/PX_ONEPLUSONE);
                break;
            case "GT-I9505":
                pixels = (int)(constant/PX_S4);
                break;
            case "GT-I8190":
                pixels = (int)(constant/PX_S3_MINI);
                break;
            default:
                pixels = (int)(constant/PX_S4);
        }
        return pixels;
    }
    public static int calcPixels(int px){
        double constant = 0.0571;
        switch (m_androidId){
            case "Nexus 6":
                px = (int) ((px*constant)/PX_NEXUS);
                break;
            case "A0001":
                px = (int) ((px*constant)/PX_ONEPLUSONE);
                break;
            case "GT-I9505":
                px = (int) ((px*constant)/PX_S4);
                break;
            case "GT-I8190":
                px = (int) ((px*constant)/PX_S3_MINI);
                break;
        }
        return px;
    }

    public static int getScreenHeight(){
        switch (Build.MODEL){
            case "Nexus 6":
                return 2560;
            case "A0001":
                return 1920;
            case "GT-I9505":
                return 1920;
            case "GT-I8190":
                return 800;
            default:
                return SCREEN_HEIGHT;
        }
    }

    public static String getNameForModel(){
        switch (Build.MODEL){
            case "Nexus 6":
                return "Nexus 6";
            case "A0001":
                return "OnePlus One";
            case "GT-I9505":
                return "Samsung S4";
            case "GT-I8190":
                return "Samsung S3 Mini";
            default:
                return Build.MODEL;
        }
    }
}
