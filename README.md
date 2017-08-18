# To8toTitle
仿土巴兔首页的渐变色Title

### 预览图

![image](https://github.com/leiyun1993/To8toTitle/raw/master/screenshot/1.gif)

### 实现方式

采用鸿洋大神的[ColorTrackView](https://github.com/hongyangAndroid/ColorTrackView/blob/master/library_ColorTrackView/src/com/zhy/view/ColorTrackView.java)

图标变色也是根据同样的原理写的一个简单的实现[ColorTrackImageView](https://github.com/leiyun1993/To8toTitle/blob/master/app/src/main/java/com/leiyun/to8totitle/ColorTrackImageView.java)

### 监听滑动

1、如果仅使用RecyclerView
```java
recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int totalY = getScrollYDistance();
        float progress = (totalY - 150) / 300f;     //使得向下滑动一段距离后再变色
        if (progress <= 0) progress = 0;
        else if (progress >= 1) progress = 1;
        trackView.setProgress(progress);
        trackImageView.setProgress(progress);
        trackImageView2.setProgress(progress);
        float ratio = (totalY - 250) / 250f;        //当字体颜色变化快结束的时候再改变标题栏背景
        if (ratio <= 0) ratio = 0;
        else if (ratio >= 1) ratio = 1;
        titleLayout.setBackgroundColor(blendColors(endColor, startColor, ratio));
    }
});

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

```

2、使用ScrollView(NestedScrollView)

```java

scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

        float progress = (scrollY - 150) / 100f;        //使得向下滑动一段距离后再变色
        if (progress <= 0) progress = 0;
        else if (progress >= 1) progress = 1;
        trackView.setProgress(progress);
        trackImageView.setProgress(progress);
        trackImageView2.setProgress(progress);
        float ratio = (scrollY - 250) / 250f;           //当字体颜色变化快结束的时候再改变标题栏背景
        if (ratio <= 0) ratio = 0;
        else if (ratio >= 1) ratio = 1;
        titleLayout.setBackgroundColor(blendColors(endColor, startColor, ratio));
    }
});
```

3、计算标题栏的颜色

```java
/**
 * 颜色渐变效果(ARGB)
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
```

### 已知问题

1、ColorTrackImageView在和其他View关联时无效，所以代码中使用一下方式

```xml
<LinearLayout
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_centerVertical="true"
    android:layout_marginRight="12dp"
    android:layout_toLeftOf="@+id/msg_layout"
    android:orientation="horizontal">

    <com.leiyun.to8totitle.ColorTrackImageView
        android:id="@+id/track_image_view2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:src="@mipmap/ic_advisory"
        app:icon_change_color="#666666"
        app:icon_origin_color="@android:color/white" />
</LinearLayout>
```
