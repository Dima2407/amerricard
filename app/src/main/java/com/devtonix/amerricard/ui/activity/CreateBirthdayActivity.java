package com.devtonix.amerricard.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devtonix.amerricard.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateBirthdayActivity extends BaseActivity {

    private TextView tvBirthdayDate;
    private ViewGroup timerContainer;
    private RelativeLayout rlSaveBirthday;
    private EditText etUsername;

    private List<Long> birthdays = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_birthday);
        setTitle(getString(R.string.add_birthday));

        rlSaveBirthday = (RelativeLayout) findViewById(R.id.rl_save);
        tvBirthdayDate = (TextView) findViewById(R.id.tv_date);
        etUsername = (EditText) findViewById(R.id.etUsername);
        timerContainer = (ViewGroup) findViewById(R.id.ll_timer_container);
        timerContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDate();
            }
        });

        rlSaveBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isValid()) return;

//                final List<Item> cards = SharedHelper.getInstance().getCards();
//                final List<Long> cardIds = new ArrayList<Long>();
//                for (Item item : cards) {
//                    cardIds.add(item.id);
//                }
//
//                CreateEventRequest request = new CreateEventRequest();
//                request.setName(etUsername.getText().toString().trim());
//                request.setDates(birthdays);
//                request.setCards(cardIds);
//
//                NetworkService.createEvent(request, CreateBirthdayActivity.this);
//
//                Toast.makeText(CreateBirthdayActivity.this, "save birthday", Toast.LENGTH_SHORT).show();
//                finish();
            }
        });
    }

    private boolean isValid() {

        if (etUsername.getText().toString().trim().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private void pickDate() {
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                final String date = monthOfYear + "." + dayOfMonth + "." + year;
                tvBirthdayDate.setText(date);

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, dayOfMonth, dayOfMonth);

                birthdays.add(calendar.getTimeInMillis());

                for (int i = 0; i < 5; i++) {
                    calendar.add(Calendar.YEAR, 1);
                    birthdays.add(calendar.getTimeInMillis());
                }


            }
        }, 1995, 1, 1);
        datePickerDialog.setAccentColor(getResources().getColor(R.color.colorPrimaryDark));
        datePickerDialog.show(getFragmentManager(), "datepicker");
    }
}
