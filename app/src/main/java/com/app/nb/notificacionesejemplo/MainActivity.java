package com.app.nb.notificacionesejemplo;

import android.app.Notification;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.editTextTitle)
    EditText editTextTitle;
    @BindView(R.id.editTextMessage)
    EditText editTextMessage;
    @BindView(R.id.switchImportance)
    Switch switchImportance;
    @BindString(R.string.switch_notifications_on)
    String switchTextOn;
    @BindString(R.string.switch_notifications_off)
    String switchTextOff;

    private boolean isHighImportance = false;

    private NotificationHandler notificationHandler;

    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        notificationHandler = new NotificationHandler(this);
    }

    @OnClick(R.id.button)
    public void onClick() {
        sendNotification();
    }

    @OnCheckedChanged(R.id.switchImportance)
    public void change(CompoundButton button, boolean isChecked) {
        isHighImportance = isChecked;
        // Operador ternario para cambiar el texto del switch
        switchImportance.setText((isChecked) ? switchTextOn : switchTextOff);
    }

    private void sendNotification() {
        String title = editTextTitle.getText().toString();
        String message = editTextMessage.getText().toString();

        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(message)) {
            Notification.Builder builder = notificationHandler.createNotification(title, message, isHighImportance);
            notificationHandler.getManager().notify(++counter, builder.build());
            notificationHandler.publishNotificationSumaryGroup(isHighImportance);
        }
    }

}
