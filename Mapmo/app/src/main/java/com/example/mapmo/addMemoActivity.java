package com.example.mapmo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.mapmo.MainActivity.addMarkerAddress;

public class addMemoActivity extends AppCompatActivity implements View.OnClickListener {

    public Button startDayBt;
    public Button finishDayBt;
    public TextView addressTv;
    public TextView latitudeTv;
    public TextView longitudeTv;
    public Dialog calendarDl;
    public static String selectDate;

    //정혜원
    public EditText contentEdit;
    public EditText titleEdit;
    public Button addBtn;

   // private String startDate;
    //private String finishDate;

    private int memo_id;
    private String memo_title;
    private String memo_start;
    private String memo_finish;

    private ListAdapter adapter;
    //private Button add;
    //private Button remove;
    private ListView listView;
    private int num;
    private int selectedPos = -1;

    //public void addFooterView(View v){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memo);

        //리스트뷰
        num = 0;
        //add =(Button)findViewById(R.id.add);
        //remove=(Button)findViewById(R.id.remove);
        listView=(ListView)findViewById(R.id.listview);
        View footer = getLayoutInflater().inflate(R.layout.listview_footer, null, false);

        listView.addFooterView(footer);

        adapter = new ListAdapter();
        listView.setAdapter(adapter);


        Button addButton = (Button) footer.findViewById(R.id.addMemo);
        addButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                adapter.addItem("",num);
                adapter.notifyDataSetChanged();
            }
        });

        listView.setOnItemLongClickListener(new ListViewItemLongClickListener());

/*
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(addMemoActivity.this, "추가되었습니다.", Toast.LENGTH_SHORT).show();
                adapter.addItem("",num);
                adapter.notifyDataSetChanged();
                num ++;
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(addMemoActivity.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                adapter.delItem();
                adapter.notifyDataSetChanged();
                num --;
            }
        });

*/


        startDayBt = (Button) findViewById(R.id.startDayBt);
        finishDayBt = (Button) findViewById(R.id.finishDayBt);
        addressTv = (TextView) findViewById(R.id.addressTv);
        latitudeTv = (TextView) findViewById(R.id.latitudeTv);
        longitudeTv = (TextView) findViewById(R.id.longitudeTv);

        //정혜원
        titleEdit = (EditText)findViewById(R.id.titlePt);
       //addBtn = (Button)findViewById(R.id.addMemoBt);

        /*
        DBHandler dbHandler = DBHandler.open(this);

        //MainActivitiy에서 데이터 받기 (memo_id)
        Intent intent = getIntent();
        int now_id = intent.getExtras().getInt("now_id");
        Log.d("now_id", String.valueOf(now_id));

        Cursor cursor = dbHandler.select_memo();
       // int count = cursor.getCount();

        cursor.moveToFirst();
        //커서가 끝나지 않을 때 까지 받아온 id에 해당하는 memo 레코드 받기
        while(cursor.isAfterLast()==false){
            memo_id = cursor.getInt(0);

            if(memo_id == now_id){
                Log.d("memo_id", String.valueOf(memo_id));

                memo_title = cursor.getString(1);
                memo_start = cursor.getString(2);
                memo_finish = cursor.getString(3);

                break;
            }
            cursor.moveToNext();
        }

        titleEdit.setText(memo_title);
        startDayBt.setText(memo_start);
        finishDayBt.setText(memo_finish);*/


        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        memo_start = sdf.format(date);
        memo_finish = sdf.format(date);

        startDayBt.setText(memo_start);
        finishDayBt.setText(memo_finish);

        addressTv.setText(addMarkerAddress);
        latitudeTv.setText(MainActivity.addMarkerLatitude);
        longitudeTv.setText(MainActivity.addMarkerLongitude);

        ((Button)findViewById(R.id.startDayBt)).setOnClickListener(this);
        ((Button)findViewById(R.id.finishDayBt)).setOnClickListener(this);

    }

    //상단바 저장 버튼 추가 (res/menu/menu 연결)

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.saveBtn){
            DBHandler dbHandler = DBHandler.open(this);

            String title = titleEdit.getText().toString();

            long cnt=0;

            if(TextUtils.isEmpty(title)) {
                Toast.makeText(this,"제목을 입력하세요.",Toast.LENGTH_SHORT).show();

            }else {
                cnt = dbHandler.insert_memo(title, memo_start, memo_finish, addMarkerAddress, MainActivity.addMarkerLatitude, MainActivity.addMarkerLongitude);

                if (cnt == -1)
                    Toast.makeText(this, "저장 오류", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "저장 성공", Toast.LENGTH_SHORT).show();

            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.startDayBt){
            calendarDl = new Dialog(v.getContext());
            calendarDl.requestWindowFeature(Window.FEATURE_NO_TITLE);
            calendarDl.setContentView(R.layout.custom_dialog_calendar);
            CalendarView calendarView = (CalendarView) calendarDl.findViewById(R.id.calVw);
            Button finishBt = (Button) calendarDl.findViewById(R.id.finishBt);

            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                    memo_start = year+"-"+(month+1)+"-"+dayOfMonth;
                    try {
                        Date dateTest = new SimpleDateFormat("yyyy-MM-dd").parse(memo_start);
                        memo_start = new SimpleDateFormat("yyyy-MM-dd").format(dateTest);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
            finishBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    calendarDl.dismiss(); // 누르면 바로 닫히는 형태
                    startDayBt.setText(memo_start);
                }
            });
            calendarDl.show();

        }else if(v.getId()==R.id.finishDayBt){
            calendarDl = new Dialog(v.getContext());
            calendarDl.requestWindowFeature(Window.FEATURE_NO_TITLE);
            calendarDl.setContentView(R.layout.custom_dialog_calendar);
            CalendarView calendarView = (CalendarView) calendarDl.findViewById(R.id.calVw);
            Button finishBt = (Button) calendarDl.findViewById(R.id.finishBt);

            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                    memo_finish = year+"-"+(month+1)+"-"+dayOfMonth;
                    try {
                        Date dateTest = new SimpleDateFormat("yyyy-MM-dd").parse(memo_finish);
                        memo_finish = new SimpleDateFormat("yyyy-MM-dd").format(dateTest);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
            finishBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    calendarDl.dismiss(); // 누르면 바로 닫히는 형태
                    finishDayBt.setText(memo_finish);
                }
            });
            calendarDl.show();
        }
    }

    class ListViewItemLongClickListener implements AdapterView.OnItemLongClickListener{

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            selectedPos = position;
            AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
            alert.setTitle("삭제?");
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.show();
            return false;
        }
    }
}
