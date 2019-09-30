package com.example.mapmo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.mapmo.MainActivity.addMarkerAddress;

public class addMemoActivity extends AppCompatActivity implements View.OnClickListener {

    public Button startDayBt;
    public Button finishDayBt;
    public TextView addressTv;

    public Dialog calendarDl;

    public EditText titleEdit;


    private String memo_start;
    private String memo_finish;

    private ListAdapter adapter;
    private ListView listView;
    private ArrayList<ListItem> items;

    private ImageButton backButton;

    DBHandler dbHandler = DBHandler.open(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memo);

        //리스트뷰 연결
        items = new ArrayList<ListItem>();
        adapter = new ListAdapter(this, android.R.layout.simple_list_item_multiple_choice, items);
        listView=(ListView)findViewById(R.id.listview);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener( new ListViewItemLongClickListener() );

        //뒤로가기 버튼
        backButton = (ImageButton) findViewById(R.id.backBtn) ;
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //추가하기 버튼
        Button addButton = (Button)findViewById(R.id.add) ;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = "";
                ListItem item = new ListItem();
                item.setContent(content);

                items.add(item);

                adapter.notifyDataSetChanged();
            }
        }) ;


        startDayBt = (Button) findViewById(R.id.startDayBt);
        finishDayBt = (Button) findViewById(R.id.finishDayBt);
        addressTv = (TextView) findViewById(R.id.addressTv);
        titleEdit = (EditText)findViewById(R.id.titlePt);


        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        memo_start = sdf.format(date);
        memo_finish = sdf.format(date);

        startDayBt.setText(memo_start);
        finishDayBt.setText(memo_finish);

        addressTv.setText(addMarkerAddress);

        ((Button)findViewById(R.id.startDayBt)).setOnClickListener(this);
        ((Button)findViewById(R.id.finishDayBt)).setOnClickListener(this);
        ((Button)findViewById(R.id.confirm_btn)).setOnClickListener(this);

    }


    Button startSelectedBt;
    String startSelectedSt;
    Date startTo;

    Button finishSelectedBt;
    String finishSelectedSt;
    Date finishTo;

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.startDayBt){
            startSelectedBt = (Button) findViewById(R.id.startDayBt);
            startSelectedSt = startSelectedBt.getText().toString();

            calendarDl = new Dialog(v.getContext());
            calendarDl.requestWindowFeature(Window.FEATURE_NO_TITLE);
            calendarDl.setContentView(R.layout.custom_dialog_calendar);
            CalendarView calendarView = (CalendarView) calendarDl.findViewById(R.id.calVw);
            Button finishBt = (Button) calendarDl.findViewById(R.id.finishBt);

            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");

            try {
                startTo = transFormat.parse(startSelectedSt);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendarView.setDate(startTo.getTime());


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
            finishSelectedBt = (Button) findViewById(R.id.finishDayBt);
            finishSelectedSt = finishSelectedBt.getText().toString();

            calendarDl = new Dialog(v.getContext());
            calendarDl.requestWindowFeature(Window.FEATURE_NO_TITLE);
            calendarDl.setContentView(R.layout.custom_dialog_calendar);
            CalendarView calendarView = (CalendarView) calendarDl.findViewById(R.id.calVw);
            Button finishBt = (Button) calendarDl.findViewById(R.id.finishBt);

            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");

            try {
                finishTo = transFormat.parse(finishSelectedSt);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendarView.setDate(finishTo.getTime());

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
        }else if(v.getId()==R.id.confirm_btn){
            Log.d("btn","버튼 눌림");
            String title = titleEdit.getText().toString();

            if(TextUtils.isEmpty(title))
                title = "제목 없음";

            long cnt=0;
            long cnt_=0;

            int startInt = Integer.parseInt(memo_start.replace("-",""));
            int finishInt = Integer.parseInt(memo_finish.replace("-",""));

            Log.d("start",memo_start);

            if(items.size()==0) {
                Toast.makeText(this,"입력한 내용이 없어 저장하지 않았습니다.",Toast.LENGTH_SHORT).show();
                dbHandler.close();
                finish();

            }
            else if (startInt > finishInt)
            {
                Toast.makeText(this,"종료날짜를 확인해주세요.",Toast.LENGTH_SHORT).show();
            }
            else {
                cnt = dbHandler.insert_memo(title, memo_start, memo_finish, addMarkerAddress, MainActivity.addMarkerLatitude, MainActivity.addMarkerLongitude, "false",0);

                if (cnt == -1) {
                    //Toast.makeText(this, "저장 오류", Toast.LENGTH_SHORT).show();
                }
                else {
                    Cursor cursor = dbHandler.select_memo();
                    cursor.moveToLast();


                    //현재 생성된 메모의 id값 얻어오기
                    int new_id = cursor.getInt(0);

                    int all_check = 0;

                    for (int i = 0; i < items.size(); i++) {
                        int check = items.get(i).isCheck() ? 1 : 0;
                        cnt_ = dbHandler.insert_content(new_id, items.get(i).getContent(), check);

                        if (cnt_ == -1) {
                            //Toast.makeText(this, "저장 오류", Toast.LENGTH_SHORT).show();
                        }
                        if(check==1)
                            all_check++;
                    }

                    if (cnt_ != -1) {
                        //Toast.makeText(this, "저장 완료", Toast.LENGTH_SHORT).show();
                    }
                    //모두 체크시 알림 울리지 않게
                    if(all_check==items.size())
                        dbHandler.update_memo(new_id, title, memo_start, memo_finish, "true",1);


                    dbHandler.close();

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("new_id", new_id);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        }
    }

    //꾹눌러서 삭제
    class ListViewItemLongClickListener implements AdapterView.OnItemLongClickListener{
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            AlertDialog.Builder alertDlg = new AlertDialog.Builder(view.getContext());

            //OK버튼
            alertDlg.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick( DialogInterface dialog, int which ) {
                    int count;
                    count = adapter.getCount();
                    if(count > 0){
                        Log.d("position_check", String.valueOf(position));
                        if(position > -1 && position < count){
                            //Log.d("check", String.valueOf(position));
                            //Toast.makeText(addMemoActivity.this, "삭제..", Toast.LENGTH_SHORT).show();
                            items.remove(position);
                            //Log.d("item", String.valueOf(items.size()));
                            listView.clearChoices();
                            adapter.notifyDataSetChanged();
                        }
                    }
                    dialog.dismiss();
                }
            });

            //NO버튼
            alertDlg.setNegativeButton( "NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick( DialogInterface dialog, int which ) { dialog.dismiss(); }
            });
            alertDlg.setMessage( "삭제하시겠습니까?" );
            alertDlg.show();
            return false;
        }
    }
}