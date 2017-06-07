package com.devtonix.amerricard.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devtonix.amerricard.R;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

public class CreateBirthdayActivity extends BaseActivity {

    private TextView timerText;
    private ViewGroup timerContainer;
    private RelativeLayout rlSaveBirthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_birthday);
        setTitle(getString(R.string.add_birthday));

        rlSaveBirthday = (RelativeLayout) findViewById(R.id.rl_save);
        timerText = (TextView) findViewById(R.id.tv_timer_value);
        timerContainer = (ViewGroup) findViewById(R.id.ll_timer_container);
        timerContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTimeClick();
            }
        });

        rlSaveBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CreateBirthdayActivity.this, "save birthday", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void onTimeClick() {
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                final String time = hourOfDay + ":" + (minute < 10 ? "0" + minute : minute);
                timerText.setText(time);
            }
        }, 8, 0, true);
        timePickerDialog.setAccentColor(getResources().getColor(R.color.colorPrimaryDark));
        timePickerDialog.show(getFragmentManager(), "timer");
    }
}
