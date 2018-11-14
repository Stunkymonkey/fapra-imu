package mci.fapra.fapra_imu;

import android.content.res.Resources;
import android.os.Build;


public class Constants {

    public static final String m_androidId = Build.MODEL;
    public static final int SCREEN_WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels;
    public static final int SCREEN_HEIGHT = Resources.getSystem().getDisplayMetrics().heightPixels;

    // Target sizes
    public static final int CIRCLE_RADIUS = 50;

    //Pixel Sizes in mm
    public static final double PX_S3_MINI = 0.1089;
    public static final double PX_S4 = 0.0577;
    public static final double PX_NEXUS_6 = 0.05109;
    public static final double PX_NEXUS_5X = 0.06;

    public static final int AMOUNT_REPETITIONS = 12;

    /**
     * @return amount pixels on specific phone to represent constant value
     */
    public static int getTargetPixelsForPhone(){
        int px = -1;
        // arbitrary constant delimiting the original length of the target
        double constant = 7.194599999999999;
        switch (m_androidId){
            case "Nexus 6":
                px = (int)(constant/ PX_NEXUS_6);
                break;
            case "Nexus 5X":
                px = (int)(constant/ PX_NEXUS_5X);
                break;
            case "GT-I9505":
                px = (int)(constant/PX_S4);
                break;
            case "GT-I8190":
                px = (int)(constant/PX_S3_MINI);
                break;
            default:
                px = (int)(constant/PX_S4);
        }
        return px;
    }

    /**
     * @param px
     * @return the amount of pixels on the specific phone that represent the handover parameter px
     */
    public static int calcPixels(int px){
        double constant = 0.0571;
        switch (m_androidId){
            case "Nexus 6":
                px = (int) ((px*constant)/ PX_NEXUS_6);
                break;
            case "Nexus 5X":
                px = (int) ((px*constant)/ PX_NEXUS_5X);
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

    /**
     *
     * @return Screen height in px for the phone
     */
    public static int getScreenHeight(){
        switch (Build.MODEL){
            case "Nexus 6":
                return 2560;
            case "Nexus 5X":
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
                return "N6";
            case "Nexus 5X":
                return "N5X";
            case "GT-I9505":
                return "S4";
            case "GT-I8190":
                return "S3Mini";
            default:
                return Build.MODEL;
        }
    }
}
