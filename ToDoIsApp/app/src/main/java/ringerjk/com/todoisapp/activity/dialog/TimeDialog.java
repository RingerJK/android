package ringerjk.com.todoisapp.activity.dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import ringerjk.com.todoisapp.MyConst;


public class TimeDialog extends DialogFragment {
    private int timeHour;
    private int timeMinute;
    private Handler handler;

    public TimeDialog(Handler handler){
        this.handler = handler;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        timeHour = bundle.getInt(MyConst.HOUR);
        timeMinute = bundle.getInt(MyConst.MINUTE);
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeHour = hourOfDay;
                timeMinute = minute;
                Bundle b = new Bundle();
                b.putInt(MyConst.HOUR, timeHour);
                b.putInt(MyConst.MINUTE, timeMinute);
                Message msg = new Message();
                msg.setData(b);
                handler.sendMessage(msg);
            }
        };
        return new TimePickerDialog(getActivity(), listener, timeHour, timeMinute, true);
    }

}
