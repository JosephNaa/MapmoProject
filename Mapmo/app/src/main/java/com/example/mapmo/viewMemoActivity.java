package com.example.mapmo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
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

public class viewMemoActivity extends AppCompatActivity implements View.OnClickListener {

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

    int select_id=0;

    DBHandler dbHandler = DBHandler.open(this);

    public String phchk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_memo);

        //리스트뷰 연결
        items = new ArrayList<ListItem>();
        adapter = new ListAdapter(this, android.R.layout.simple_list_item_multiple_choice, items);
        listView=(ListView)findViewById(R.id.listview);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener( new ListViewItemLongClickListener() );

        startDayBt = (Button) findViewById(R.id.startDayBt);
        finishDayBt = (Button) findViewById(R.id.finishDayBt);
        addressTv = (TextView) findViewById(R.id.addressTv);

        //정혜원
        titleEdit = (EditText)findViewById(R.id.titlePt);


        //메모 데이터 넣기
        String title = null;
        String address = null;

        Intent intent = getIntent();

        select_id = intent.getIntExtra("select_id",0);

        Cursor cursor_memo = dbHandler.select_memo();
        cursor_memo.moveToFirst();

        int count = cursor_memo.getCount();

        for(int i=0; i<count; i++){
            int id = cursor_memo.getInt(0);

            if(id==select_id){
                title = cursor_memo.getString(1);
                memo_start = cursor_memo.getString(2);
                memo_finish = cursor_memo.getString(3);
                address = cursor_memo.getString(4);
                phchk = cursor_memo.getString(7);

            }
            cursor_memo.moveToNext();
        }

        titleEdit.setText(title);
        startDayBt.setText(memo_start);
        finishDayBt.setText(memo_finish);
        addressTv.setText(address);

        //체크리스트 내용 넣기

        Cursor cursor_content = dbHandler.select_content();
        cursor_content.moveToFirst();

        int count_ = cursor_content.getCount();

        for(int i=0; i<count_; i++){
            int id = cursor_content.getInt(1);

            if(id==select_id){
                //Log.d("item_", cursor_content.getString(2));
                boolean check = (cursor_content.getInt(3) != 0);

                ListItem item = new ListItem();
                item.setContent(cursor_content.getString(2));
                item.setCheck(check);

                items.add(item);

            }
            cursor_content.moveToNext();
        }

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
                //Log.d("item", String.valueOf(items.size()));
                adapter.notifyDataSetChanged();
            }
        }) ;


        ((Button)findViewById(R.id.startDayBt)).setOnClickListener(this);
        ((Button)findViewById(R.id.finishDayBt)).setOnClickListener(this);
        ((Button)findViewById(R.id.delete_btn)).setOnClickListener(this);
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
            String title = titleEdit.getText().toString();

            if (TextUtils.isEmpty(title))
                title = "제목 없음";

            long cnt = 0;
            long cnt_ = 0;

            int startInt = Integer.parseInt(memo_start.replace("-",""));
            int finishInt = Integer.parseInt(memo_finish.replace("-",""));

            if (items.size() == 0) {
                Toast.makeText(this, "입력한 내용이 없어 저장하지 않았습니다.", Toast.LENGTH_SHORT).show();

                delete_memo();

            }
            else if (startInt > finishInt)
            {
                Toast.makeText(this,"종료날짜를 확인해주세요.",Toast.LENGTH_SHORT).show();
            }
            else {
                //체크 리스트 비우기
                Cursor cursor_content = dbHandler.select_content();
                cursor_content.moveToFirst();

                int count_ = cursor_content.getCount();

                for (int i = 0; i < count_; i++) {
                    int id = cursor_content.getInt(1);

                    if (id == select_id) {
                        dbHandler.delete_content(cursor_content.getInt(0));
                    }
                    cursor_content.moveToNext();
                }

                for (int i = 0; i < items.size(); i++) {
                    int check = items.get(i).isCheck() ? 1 : 0;

                    Log.d("item_content", items.get(i).getContent());

                    cnt_ = dbHandler.insert_content(select_id, items.get(i).getContent(), check);

                    if (cnt_ == -1) {
                        // Toast.makeText(this, "저장 오류", Toast.LENGTH_SHORT).show();
                    }

                }


                //모두 체크되면 알림 안울리게
                Cursor cursor = dbHandler.select_content();
                cursor.moveToFirst();

                int count = cursor.getCount();
                int all_check = 0;
                int select_count=0;

                for (int i = 0; i < count; i++) {
                    int id = cursor.getInt(1);

                    if (id == select_id) {
                        select_count++;
                        Log.d("select",select_count+"");
                        if(cursor.getInt(3)==1) {
                            all_check++;
                            Log.d("all",all_check+"");
                        }
                    }
                    cursor.moveToNext();
                }
                Log.i("allccc",all_check+"");
                Log.i("cacccc",select_count+"");

                if(all_check==select_count){

                    dbHandler.update_memo(select_id, title, memo_start, memo_finish, "true",1);
                }else
                {

                    dbHandler.update_memo(select_id, title, memo_start, memo_finish, phchk,0);
                    ////
                }


                dbHandler.close();

                Intent resultIntent = new Intent();

                setResult(RESULT_OK, resultIntent);
                finish();
            }
        }else if(v.getId()==R.id.delete_btn){
            //삭제 다이얼로그 띄우기
            AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);

            //OK버튼
            alertDlg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    delete_memo();

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
                            //Toast.makeText(viewMemoActivity.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
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

    public void delete_memo(){
        dbHandler.delete_memo(select_id);
        //체크 리스트 비우기
        Cursor cursor_content = dbHandler.select_content();
        cursor_content.moveToFirst();

        int count_ = cursor_content.getCount();

        for (int i = 0; i < count_; i++) {
            int id = cursor_content.getInt(1);

            if (id == select_id) {
                dbHandler.delete_content(cursor_content.getInt(0));
            }
            cursor_content.moveToNext();
        }
        dbHandler.close();

        Intent resultIntent = new Intent();
        setResult(RESULT_OK,resultIntent);
        finish();
    }
}