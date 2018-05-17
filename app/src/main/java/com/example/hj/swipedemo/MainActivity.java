package com.example.hj.swipedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private List<String> list = new ArrayList<>();
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_view);

        for (int i = 1; i <= 30; i++) {
            list.add("name-" + i);
        }
        myAdapter = new MyAdapter();

        listView.setAdapter(myAdapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    //如果需要滑动，则需要关闭已经打开的layout
                    SwipeLayoutManager.getInstance().closeCurrentLayout();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private class MyAdapter extends BaseAdapter implements SwipeLayout.OnSwipeStateChangeListener {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(MainActivity.this, R.layout.adapter_list, null);
            }
            final ViewHolder holder = ViewHolder.getHolder(convertView);
            holder.tv_name.setText(list.get(position));
            holder.swipeLayout.setTag(position);
            holder.swipeLayout.setOnSwipeStateChangeListener(this);
            holder.tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SwipeLayoutManager.getInstance().closeCurrentLayout();
                    list.remove(position);
                    myAdapter.notifyDataSetChanged();
                }
            });
            return convertView;
        }

        @Override
        public void onOpen(Object tag) {
            Toast.makeText(MainActivity.this, "第" + tag + "个打开", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onClose(Object tag) {
            Toast.makeText(MainActivity.this, "第" + tag + "个关闭", Toast.LENGTH_SHORT).show();
        }

    }

    static class ViewHolder {
        TextView tv_name, tv_delete;
        SwipeLayout swipeLayout;

        private ViewHolder(View convertView) {
            tv_name = convertView.findViewById(R.id.tv_name);
            tv_delete = convertView.findViewById(R.id.tv_delete);
            swipeLayout = convertView.findViewById(R.id.swipe_layout);
        }

        public static ViewHolder getHolder(View convertView) {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            return holder;
        }
    }
}
