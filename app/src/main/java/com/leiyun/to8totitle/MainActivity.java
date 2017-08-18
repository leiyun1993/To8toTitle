package com.leiyun.to8totitle;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ColorTrackView trackView;
    private ColorTrackImageView trackImageView;
    private ColorTrackImageView trackImageView2;
    private RecyclerView recyclerView;
    private RelativeLayout titleLayout;
    //    private NestedScrollView scrollView;

    private int startColor = Color.parseColor("#30000000");
    private int endColor = Color.WHITE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trackView = (ColorTrackView) findViewById(R.id.track_view);
        trackImageView = (ColorTrackImageView) findViewById(R.id.track_image_view);
        trackImageView2 = (ColorTrackImageView) findViewById(R.id.track_image_view2);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
//        scrollView = (NestedScrollView) findViewById(R.id.scroll_view);
        titleLayout = (RelativeLayout) findViewById(R.id.title_layout);

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(0, 0, 0, 1);
            }
        });
        recyclerView.setAdapter(new MyAdapter());

//        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//
//                float progress = (scrollY - 150) / 100f;
//                if (progress <= 0) progress = 0;
//                else if (progress >= 1) progress = 1;
//                trackView.setProgress(progress);
//                trackImageView.setProgress(progress);
//                trackImageView2.setProgress(progress);
//                float ratio = (scrollY - 250) / 250f;
//                if (ratio <= 0) ratio = 0;
//                else if (ratio >= 1) ratio = 1;
//                titleLayout.setBackgroundColor(blendColors(endColor, startColor, ratio));
//            }
//        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalY = getScrollYDistance();
                float progress = (totalY - 150) / 300f;
                if (progress <= 0) progress = 0;
                else if (progress >= 1) progress = 1;
                trackView.setProgress(progress);
                trackImageView.setProgress(progress);
                trackImageView2.setProgress(progress);
                float ratio = (totalY - 250) / 250f;
                if (ratio <= 0) ratio = 0;
                else if (ratio >= 1) ratio = 1;
                titleLayout.setBackgroundColor(blendColors(endColor, startColor, ratio));
            }
        });
    }

    /**
     * recyclerView 向下滑动的距离
     *
     * @return
     */
    public int getScrollYDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisibleChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisibleChildView.getHeight();
        return (position) * itemHeight - firstVisibleChildView.getTop();
    }


    private static class MyAdapter extends RecyclerView.Adapter<ViewHolder> {


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView itemView = new TextView(parent.getContext());
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            itemView.setLayoutParams(params);
            itemView.setPadding(0, 50, 0, 50);
            itemView.setTextColor(Color.BLACK);
            itemView.setTextSize(16);
            itemView.setGravity(Gravity.CENTER);
            itemView.setBackgroundColor(Color.WHITE);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bindData(position);
        }

        @Override
        public int getItemCount() {
            return 80;
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void bindData(int position) {
            if (itemView instanceof TextView) {
                ((TextView) itemView).setText(String.format("第%s个子项", position));
            }
        }
    }

    /**
     * 颜色渐变效果
     *
     * @param endColor   最终颜色
     * @param startColor 开始颜色
     * @param ratio
     * @return
     */
    public int blendColors(int endColor, int startColor, float ratio) {
        final float inverseRation = 1f - ratio;
        float r = (Color.red(endColor) * ratio)
                + (Color.red(startColor) * inverseRation);
        float g = (Color.green(endColor) * ratio)
                + (Color.green(startColor) * inverseRation);
        float b = (Color.blue(endColor) * ratio)
                + (Color.blue(startColor) * inverseRation);
        float a = (Color.alpha(endColor) * ratio)
                + (Color.alpha(startColor) * inverseRation);
        return Color.argb((int) a, (int) r, (int) g, (int) b);
    }
}
