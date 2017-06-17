package com.devtonix.amerricard.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.network.request.CreateEventRequest;
import com.devtonix.amerricard.repository.CardRepository;
import com.devtonix.amerricard.repository.EventRepository;
import com.devtonix.amerricard.ui.callback.EventCreateCallback;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

public class CreateBirthdayActivity extends BaseActivity {

    @Inject
    EventRepository eventRepository;
    @Inject
    CardRepository cardRepository;

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

        ACApplication.getMainComponent().inject(this);

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

//                final List<CategoryItemFirstLevel> cards = cardRepository.getCardsFromStorage();
//                final List<Long> cardIds = new ArrayList<Long>();
//                for (CategoryItemFirstLevel first : cards) {
//
//                    for (CategoryItemSecondLevel second : first.getData()){
//                        for (CardItem card : second.getData()){
//                            cardIds.add(Long.valueOf(card.getId()));
//                        }
//                    }
//                }

                CreateEventRequest request = new CreateEventRequest();
                request.setName(etUsername.getText().toString().trim());
                request.setDates(birthdays);
                request.setCards(Arrays.asList(36L, 10L, 26L));

                progressDialog.show();

                eventRepository.createEvent(request, new MyEventCreateCallback());

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

    private class MyEventCreateCallback implements EventCreateCallback {
        @Override
        public void onSuccess() {
            progressDialog.dismiss();
            finish();
        }

        @Override
        public void onError() {
            progressDialog.dismiss();
            finish();
        }

        @Override
        public void onRetrofitError(String message) {
            progressDialog.dismiss();
            finish();
        }
    }
}
