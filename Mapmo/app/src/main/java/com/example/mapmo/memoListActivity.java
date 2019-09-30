package com.example.mapmo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class memoListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private MemoListAdapter adapter;
    private ListView listView;
    private ArrayList<memoListItem> items;



    private ImageButton backButton;

    DBHandler dbHandler = DBHandler.open(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        list_item_set();

        ((ListView)findViewById(R.id.memo_list)).setOnItemClickListener(this);

        //뒤로가기 버튼
        backButton = (ImageButton) findViewById(R.id.backBtn) ;
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){
            switch(requestCode){
                case 200:
                    list_item_set();
                    break;
            }
        }
    }

    public void list_item_set(){
        items = new ArrayList<memoListItem>();
        adapter = new MemoListAdapter(getApplicationContext(), items);
        listView=(ListView)findViewById(R.id.memo_list);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener( new ListViewItemLongClickListener() );

        final Cursor cursor_memo = dbHandler.select_memo();
        cursor_memo.moveToFirst();

        int count = cursor_memo.getCount();

        for(int i=0; i<count; i++){
            memoListItem item = new memoListItem();

            item.setId(cursor_memo.getInt(0));
            item.setTitle(cursor_memo.getString(1));
            item.setStart_date(cursor_memo.getString(2));
            item.setFinish_date(cursor_memo.getString(3));
            item.setAddress(cursor_memo.getString(4));

            items.add(item);
            cursor_memo.moveToNext();
        }
    }

    //팝업 메뉴
    public void mOnClick(View v){

        //팝업 메뉴 객체 만듬

        PopupMenu popup = new PopupMenu(this, v);

        //xml파일에 메뉴 정의한것을 가져오기위해서 전개자 선언

        MenuInflater inflater = popup.getMenuInflater();

        final Menu menu = popup.getMenu();


        //실제 메뉴 정의한것을 가져오는 부분 menu 객체에 넣어줌

        inflater.inflate(R.menu.menu_list, menu);


        //메뉴가 클릭했을때 처리하는 부분

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override

            public boolean onMenuItemClick(MenuItem item) {

                // TODO Auto-generated method stub

                //각 메뉴별 아이디를 조사한후 할일을 적어줌

                switch(item.getItemId()){
                    case R.id.order:
                        AlertDialog.Builder builder = new AlertDialog.Builder(memoListActivity.this);

                        builder.setItems(R.array.order, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        Comparator<memoListItem> idAsc = new Comparator<memoListItem>() {
                                            @Override
                                            public int compare(memoListItem item1, memoListItem item2) {
                                                int ret;

                                                if(item1.getId() < item2.getId())
                                                    ret = -1;
                                                else if(item1.getId() == item2.getId())
                                                    ret = 0;
                                                else
                                                    ret = 1;

                                                return ret;
                                            }
                                        };
                                        Collections.sort(items, idAsc);
                                        adapter.notifyDataSetChanged();

                                        break;
                                    case 1:

                                        Comparator<memoListItem> startAsc = new Comparator<memoListItem>() {
                                            @Override
                                            public int compare(memoListItem item1, memoListItem item2) {
                                                return item1.getStart_date().compareTo(item2.getStart_date());
                                            }
                                        };
                                        Collections.sort(items, startAsc);
                                        adapter.notifyDataSetChanged();

                                        break;
                                    case 2:
                                        Comparator<memoListItem> finishAsc = new Comparator<memoListItem>() {
                                            @Override
                                            public int compare(memoListItem item1, memoListItem item2) {
                                                return item1.getFinish_date().compareTo(item2.getFinish_date());
                                            }
                                        };
                                        Collections.sort(items, finishAsc);
                                        adapter.notifyDataSetChanged();

                                        break;
                                }
                            }
                        }).show();

                        break;
                }
                return false;
            }

        });

        popup.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(memoListActivity.this, viewMemoActivity.class);

        int select_id = items.get(position).getId();
        intent.putExtra("select_id", select_id);

        startActivityForResult(intent, 200);
    }


    //꾹눌러서 삭제
    class ListViewItemLongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            AlertDialog.Builder alertDlg = new AlertDialog.Builder(view.getContext());

            //OK버튼
            alertDlg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int count;
                    count = adapter.getCount();
                    if (count > 0) {
                        Log.d("position_check", String.valueOf(position));
                        if (position > -1 && position < count) {
                            //Log.d("check", String.valueOf(position));

                            deleteMemo(items.get(position).getId());

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
            return true;
        }
    }

    public void deleteMemo(int selectId){
        //선택 레코드 데이터 삭제
        int select_id = selectId;

        dbHandler.delete_memo(select_id);

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
    }
}
