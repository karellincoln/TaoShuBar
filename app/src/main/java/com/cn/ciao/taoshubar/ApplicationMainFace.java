package com.cn.ciao.taoshubar;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil;

import org.json.JSONArray;
import org.w3c.dom.Attr;

import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ApplicationMainFace extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener,
        BuyFragment.OnFragmentInteractionListener, MallFragment.OnFragmentInteractionListener, SellFragment.OnFragmentInteractionListener {


    List<Book> historyLists, historyListb, HistoryBookList;
    ExpandableListView traceHistoryList;
    DrawerLayout drawerlayout;
    Button sliceOn;
    private int currentPosition;
    private float currentOffset = 0;
    AVUser avUser;
    ViewPager viewPager;
    ArrayList<Fragment> fragmentsArrayList;
    ArrayList<ChangeColorIndicatorView> indicatorsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_main_face);
        currentPosition = 0;
        avUser = getIntent().getParcelableExtra("AVuser");
        viewPager = (ViewPager) findViewById(R.id.application_main_face);
        initList();
        initViewPager();
        initDrawerLayout();
        initListView();
    }

    class HistoryHandler extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            updateHistoryList();
            return null;
        }

        private void updateHistoryList() {

            UpdateTheList();
        }

        private void UpdateTheList() {
            JSONArray bookhistory = avUser.getJSONArray("BookHistory");
            historyListb = new LinkedList<Book>();
            historyLists = new LinkedList<Book>();
            try {
                HistoryBookList = Book.fromList(bookhistory);
                String userName = avUser.getUsername();
                for (Book hb : HistoryBookList) {
                    if (hb.getCustomertName().equals(userName)) {
                        historyListb.add(hb);
                    } else {
                        historyLists.add(hb);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    }

    private void initListView() {
        HistoryHandler historyHandler = new HistoryHandler();
        historyHandler.execute(new Object());
        traceHistoryList = (ExpandableListView) findViewById(R.id.trace_history);
        traceHistoryList.setAdapter(new HistoryListAdapter());
    }

    private void initDrawerLayout() {
        drawerlayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        sliceOn = (Button) findViewById(R.id.topic_button);
        sliceOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerlayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    private void initList() {
        Bundle arguments = new Bundle();
        arguments.putParcelable("AVuser", avUser);
        MallFragment fragment1 = new MallFragment();
        fragment1.setArguments(arguments);
        BuyFragment fragment2 = new BuyFragment();
        fragment2.setArguments(arguments);
        SellFragment fragment3 = new SellFragment();
        fragment3.setArguments(arguments);
        fragmentsArrayList = new ArrayList<Fragment>(3);
        fragmentsArrayList.add(fragment1);
        fragmentsArrayList.add(fragment2);
        fragmentsArrayList.add(fragment3);
        ChangeColorIndicatorView indicator1 = (ChangeColorIndicatorView) findViewById(R.id.indicator1);
        ChangeColorIndicatorView indicator2 = (ChangeColorIndicatorView) findViewById(R.id.indicator2);
        ChangeColorIndicatorView indicator3 = (ChangeColorIndicatorView) findViewById(R.id.indicator3);
        indicator1.setOnClickListener(this);
        indicator2.setOnClickListener(this);
        indicator3.setOnClickListener(this);
        indicatorsArrayList = new ArrayList<ChangeColorIndicatorView>(3);
        indicatorsArrayList.add(indicator1);
        indicatorsArrayList.add(indicator2);
        indicatorsArrayList.add(indicator3);
        resetAllIndicators();
        indicatorsArrayList.get(0).setmAlpha(1);
    }

    private void initViewPager() {
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentsArrayList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentsArrayList.size();
            }
        });
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset > 0) {
            ChangeColorIndicatorView leftView = indicatorsArrayList.get(position);
            ChangeColorIndicatorView rightView = indicatorsArrayList.get(position + 1);
            currentOffset = positionOffset;
            leftView.setmAlpha(1 - positionOffset);
            rightView.setmAlpha(positionOffset);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (currentOffset >= 0.5) {
                    if (currentPosition < 2) {
                        currentPosition++;
                        currentOffset = 0;
                        viewPager.setCurrentItem(currentPosition);
                        resetAllIndicators();
                        indicatorsArrayList.get(currentPosition).setmAlpha(1);
                    } else if (currentPosition > 0) {
                        currentPosition--;
                        currentOffset = 0;
                        viewPager.setCurrentItem(currentPosition);
                        indicatorsArrayList.get(currentPosition).setmAlpha(1);
                    }


                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void resetAllIndicators() {
        for (ChangeColorIndicatorView temp : indicatorsArrayList) {
            temp.setmAlpha(0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.indicator1:
                viewPager.setCurrentItem(0);
                currentPosition = 0;
                resetAllIndicators();
                indicatorsArrayList.get(0).setmAlpha(1);
                break;
            case R.id.indicator2:
                viewPager.setCurrentItem(1);
                currentPosition = 1;
                resetAllIndicators();
                indicatorsArrayList.get(1).setmAlpha(1);
                break;
            case R.id.indicator3:
                viewPager.setCurrentItem(2);
                currentPosition = 2;
                resetAllIndicators();
                indicatorsArrayList.get(2).setmAlpha(1);
                break;
            default:
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public class HistoryListAdapter extends BaseExpandableListAdapter {

        String[] groups = new String[]{"已买书籍", "已售书籍"};

        @Override
        public int getGroupCount() {
            return groups.length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            if (groupPosition == 0) {
                return historyListb.size();
            } else {
                return historyLists.size();
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groups[groupPosition];
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            if (groupPosition == 0) {
                return historyListb.get(childPosition);
            } else {
                return historyLists.get(childPosition);
            }

        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                TextView tempView = new TextView(ApplicationMainFace.this);
                tempView.setText(groups[groupPosition]);
                tempView.setGravity(Gravity.CENTER);
                tempView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
                tempView.setBackgroundResource(R.drawable.three_main_fragment);
                tempView.setTextSize(30);
                return tempView;
            } else {
                TextView tempView = (TextView) convertView;
                tempView.setText(groups[groupPosition]);
                return tempView;
            }
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            if (convertView == null) {
                TextView tempView = new TextView(ApplicationMainFace.this);
                if (groupPosition == 0)
                    tempView.setText(historyListb.get(childPosition).getBookname());
                else
                    tempView.setText(historyLists.get(childPosition).getBookname());
                tempView.setGravity(Gravity.CENTER);
                tempView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
                tempView.setBackgroundResource(R.drawable.three_main_fragment);
                tempView.setTextSize(20);
                return tempView;
            } else {
                TextView tempView = (TextView) convertView;
                if (groupPosition == 0)
                    tempView.setText(historyListb.get(childPosition).getBookname());
                else
                    tempView.setText(historyLists.get(childPosition).getBookname());
                return tempView;
            }
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }

    }
}
