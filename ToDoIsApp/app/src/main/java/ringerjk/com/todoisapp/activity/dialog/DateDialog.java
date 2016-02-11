package ringerjk.com.todoisapp.activity.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import ringerjk.com.todoisapp.MyConst;

public class DateDialog extends DialogFragment {

    private int timeDay;
    private int timeMonth;
    private int timeYear;

    private Handler h;

    public DateDialog(Handler h) {
        this.h = h;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        timeDay = bundle.getInt(MyConst.DAY);
        timeMonth = bundle.getInt(MyConst.MONTH);
        timeYear = bundle.getInt(MyConst.YEAR);

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                timeDay = dayOfMonth;
                timeMonth = monthOfYear;
                timeYear = year;
                Bundle b = new Bundle();
                b.putInt(MyConst.DAY, timeDay);
                b.putInt(MyConst.MONTH, timeMonth);
                b.putInt(MyConst.YEAR, timeYear);
                Message msg = new Message();
                msg.setData(b);
                h.sendMessage(msg);
            }
        };
        return new DatePickerDialog(getActivity(), listener, timeYear, timeMonth, timeDay);
    }
}
