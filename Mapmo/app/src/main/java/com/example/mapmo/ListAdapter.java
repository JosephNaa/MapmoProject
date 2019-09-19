package com.example.mapmo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter {
    private ArrayList<ListItem> filteredItemList = new ArrayList<ListItem>();
    private Context mContext;
    private String content;

    public ListAdapter(Context context, int resource, ArrayList<ListItem> items) {
        super(context, resource, items);
        this.mContext = context;
        this.filteredItemList = items;
    }

    @Override
    public int getCount() {
        return filteredItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        final ViewHolder holder;

        // "listview_memo" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_memo, parent, false);
            holder.editText1 = (EditText)convertView.findViewById(R.id.editText1);
            holder.checkBox1 = (CheckBox)convertView.findViewById(R.id.checkBox1);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.ref = position;

        holder.editText1.requestFocus();

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        final EditText editText1 = (EditText)convertView.findViewById(R.id.editText1);

        // Data Set(filteredItemList)에서 position에 위치한 데이터 참조 획득
        final ListItem listViewItem = filteredItemList.get(position);

        holder.editText1.setText(listViewItem.getContent());
        holder.checkBox1.setChecked(listViewItem.isCheck());

        content+=holder.editText1.getText()+"#";

        //상태 업데이트
        if(holder.checkBox1.isChecked()){
            filteredItemList.get(position).setCheck(true);
            holder.editText1.setTextColor(Color.parseColor("#808080"));
            holder.editText1.setPaintFlags(editText1.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            //holder.editText1.setEnabled(false);
        }else{
            filteredItemList.get(position).setCheck(false);
            holder.editText1.setTextColor(Color.parseColor("#000000"));
            holder.editText1.setPaintFlags(editText1.getPaintFlags()& ~Paint.STRIKE_THRU_TEXT_FLAG);
            //holder.editText1.setEnabled(true);
        }

        holder.editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) { filteredItemList.get(holder.ref).setContent(s.toString()); }
        });

        //체크박스 눌렀을 시
        holder.checkBox1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(holder.checkBox1.isChecked()){
                    filteredItemList.get(position).setCheck(true);
                    holder.editText1.setTextColor(Color.parseColor("#808080"));
                    holder.editText1.setPaintFlags(editText1.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                    //holder.editText1.setEnabled(false);
                }else{
                    filteredItemList.get(position).setCheck(false);
                    holder.editText1.setTextColor(Color.parseColor("#000000"));
                    holder.editText1.setPaintFlags(editText1.getPaintFlags()& ~Paint.STRIKE_THRU_TEXT_FLAG);
                    //holder.editText1.setEnabled(true);
                }
            }
        });

        return convertView;
    }

    public class ViewHolder {
        EditText editText1;
        CheckBox checkBox1;
        int ref;
    }
}
