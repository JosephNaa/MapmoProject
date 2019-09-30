package com.example.mapmo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MemoListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<memoListItem> listViewItemList;

    public MemoListAdapter(Context context, ArrayList<memoListItem> items) {
        this.context = context;
        this.listViewItemList = items;
    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_memolist, parent, false);
        }

        TextView memoTitle = (TextView)convertView.findViewById(R.id.memo_title);
        TextView startDate = (TextView)convertView.findViewById(R.id.st_date);
        TextView finishDate = (TextView)convertView.findViewById(R.id.fn_date);
        TextView memoAddress = (TextView)convertView.findViewById(R.id.memo_location);

        memoListItem memolistitem = listViewItemList.get(position);

        memoTitle.setText(memolistitem.getTitle());
        startDate.setText(memolistitem.getStart_date());
        finishDate.setText(memolistitem.getFinish_date());
        memoAddress.setText(memolistitem.getAddress());

        return convertView;
    }
}
