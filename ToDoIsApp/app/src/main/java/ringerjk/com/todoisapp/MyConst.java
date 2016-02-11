package ringerjk.com.todoisapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MyConst {
    public final static String HOUR = "HOUR";
    public final static String MINUTE = "MINUTE";
    public final static String YEAR = "YEAR";
    public final static String MONTH = "MONTH";
    public final static String DAY = "DAY";

    public final static String KEY_NODE_ID = "NOTE_ID";

    public final static String LOCATE_NOTIFICATIONS = "ringerjk.com.todoisapp.activity.activityUtils";

    public final static String IMAGE_FOLDER = "ToDoListFolder";

    public final static int DATE = 4;
    public final static int TIME = 9;

    public final static String KEY_TITLE_AM = "keyTitle";
    public final static String KEY_S_PREF = "sPrefKey";

    public final static int TYPE_PHOTO = 1110;
    public final static int REQUEST_CODE_PHOTO = 1111;

    public static final DateFormat dfFull = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public static final DateFormat dfHrAndMi = new SimpleDateFormat("HH:mm");
    public static final DateFormat dfDMY = new SimpleDateFormat("dd/MM/yyyy");
}
