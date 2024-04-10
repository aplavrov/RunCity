package com.example.anton.runcity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private EditText editnum;
    private EditText editname;
    private TextView txtShow;
    protected static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editnum = (EditText)findViewById(R.id.editText);
        editname = (EditText)findViewById(R.id.editText2);
        txtShow = (TextView)findViewById(R.id.textView);
        txtShow.setTextColor(0xAA333333);
        try {
            FileInputStream fileInput = openFileInput("example.txt");
            InputStreamReader reader = new InputStreamReader(fileInput);
            BufferedReader buffer = new BufferedReader(reader);
            StringBuffer strBuffer = new StringBuffer();
            String lines;
            while ((lines = buffer.readLine()) != null) {
                strBuffer.append(lines + "\n");
            }
            txtShow.setText(strBuffer.toString());
            txtShow.setMovementMethod(new ScrollingMovementMethod());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createBus (View view) {
        write("Автобус");
    }

    public void createTram (View view) {
        write("Трамвай");
    }

    public void createTroll (View view) {
        write("Троллейбус");
    }

    public void createMetro (View view) {
        write("Метро");
    }

    public void createCP (View view) {
        write("КП");
    }

    public void write (String trType) {
        Calendar date = Calendar.getInstance();
        int hour = date.get(Calendar.HOUR_OF_DAY);
        String sHour = Integer.toString(hour);
        if (hour < 10) sHour = "0" + sHour;
        int minute = date.get(Calendar.MINUTE);
        String sMinute = Integer.toString(minute);
        if (minute < 10) sMinute = "0" + sMinute;
        String time = sHour + ":" + sMinute;
        String number = editnum.getText().toString();
        String name = editname.getText().toString();
        try {
            FileOutputStream fileOutput = openFileOutput("example.txt", MODE_APPEND);
            fileOutput.write((time + " ").getBytes());
            fileOutput.write((trType + " ").getBytes());
            if (number.length() > 0) fileOutput.write((number + " ").getBytes());
            fileOutput.write(("\"" + name + "\"\n").getBytes());
            fileOutput.close();
            editnum.setText("");
            editname.setText("");
            Toast.makeText(MainActivity.this, "Добавлено", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        read(findViewById(android.R.id.content));
    }

    public void erase (View view) {
        try {
            Integer cnt = 0;
            FileInputStream fileInput = openFileInput("example.txt");
            InputStreamReader reader = new InputStreamReader(fileInput);
            BufferedReader buffer = new BufferedReader(reader);
            StringBuffer strBuffer = new StringBuffer();
            String lines;
            while ((lines = buffer.readLine()) != null) {
                strBuffer.append(lines + "\n");
                ++cnt;
            }
            fileInput.close();

            Integer cnt1 = 0;
            FileInputStream fileInput1 = openFileInput("example.txt");
            InputStreamReader reader1 = new InputStreamReader(fileInput1);
            BufferedReader buffer1 = new BufferedReader(reader1);
            StringBuffer strBuffer1 = new StringBuffer();
            String lines1;
            while ((lines1 = buffer1.readLine()) != null) {
                if (cnt1 < cnt - 1) strBuffer1.append(lines1 + "\n");
                ++cnt1;
            }
            fileInput1.close();
            Toast.makeText(MainActivity.this, "Событие удалено", Toast.LENGTH_SHORT).show();
            FileOutputStream fileOutput = openFileOutput("example.txt", MODE_PRIVATE);
            fileOutput.write((strBuffer1.toString()).getBytes());
            fileOutput.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        read(findViewById(android.R.id.content));
    }

    public void read (View view) {
        try {
            FileInputStream fileInput = openFileInput("example.txt");
            InputStreamReader reader = new InputStreamReader(fileInput);
            BufferedReader buffer = new BufferedReader(reader);
            StringBuffer strBuffer = new StringBuffer();
            String lines;
            while ((lines = buffer.readLine()) != null) {
                strBuffer.append(lines + "\n");
            }
            txtShow.setText(strBuffer.toString());
            txtShow.setMovementMethod(new ScrollingMovementMethod());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClickPhoto (View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"fname_" +
                String.valueOf(System.currentTimeMillis()) + ".jpg")).toString());
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                Bundle extras = data.getExtras();
                Log.e("URI",Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"fname_" +
                        String.valueOf(System.currentTimeMillis()) + ".jpg")).toString());
                Bitmap bmp = (Bitmap) extras.get("data");
            }
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

