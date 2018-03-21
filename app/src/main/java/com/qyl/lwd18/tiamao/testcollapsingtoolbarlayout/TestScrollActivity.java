package com.qyl.lwd18.tiamao.testcollapsingtoolbarlayout;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.qyl.lwd18.tiamao.R;

public class TestScrollActivity extends AppCompatActivity {

  private float totalHeight;      //总高度
  private float toolBarHeight;    //toolBar高度
  private float offSetHeight;     //总高度 -  toolBar高度  布局位移值
  private float llHeight;         //搜索框高度
  private float llWidth;          //搜索框宽度

  private float llHeightOffScale;     //高度差比值
  private float llOffDistance;        //距离差
  private float llOffDistanceScale;   //距离差比值
  private FrameLayout.LayoutParams params;

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.toolbar_layout) CollapsingToolbarLayout toolbarLayout;
  @BindView(R.id.app_bar) AppBarLayout appBar;
  @BindView(R.id.fab) EditText fab;
  @BindView(R.id.ll) RelativeLayout ll;
  @BindView(R.id.bac) TextView bac;
  @BindView(R.id.fl) FrameLayout fl;
  //@BindView(R.id.image)
  //ImageView image;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scrolling2);
    ButterKnife.bind(this);
    bac.setAlpha(0f);
    totalHeight = getResources().getDimension(R.dimen.app_bar_height);
    toolBarHeight = getResources().getDimension(R.dimen.tool_bar_height);
    offSetHeight = totalHeight - toolBarHeight;

    fab.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        startActivity(new Intent(TestScrollActivity.this, TestActivity.class));
      }
    });

    /**
     *   移动效果值／最终效果值 =  移动距离／ 能移动总距离（确定）
     */


    appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
      @Override public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        ScaleAnimation animation = new ScaleAnimation(1.0f,1.0f,1.0f,0.7f);
        animation.setDuration(500);
        if (verticalOffset<0){
          toolbar.setAnimation(animation);
        }

        //第一次进入获取高度，以及差值 高度差比值
        if (llHeight == 0) {
          llHeight = ll.getMeasuredHeight();
          llWidth = ll.getMeasuredWidth();
          params = (FrameLayout.LayoutParams) ll.getLayoutParams();

          //算出高度偏移量比值  相对与llHeight
          llHeightOffScale = 1.0f - (toolBarHeight / llHeight);

          //得到滑动差值 就是布局marginTop
          llOffDistance = params.topMargin;
          //得到滑动比值
          llOffDistanceScale = llOffDistance / offSetHeight;
        }

        //滑动一次 得到渐变缩放值
        float alphaScale = (-verticalOffset) / offSetHeight;

        //获取高度缩放值
        float llHeightScale = 1.0f - (llHeightOffScale * ((-verticalOffset) / offSetHeight));
        //计算maigintop值
        float distance = llOffDistance - (-verticalOffset) * llOffDistanceScale;

        // image.setAlpha(1.0f-alphaScale);
        bac.setAlpha(alphaScale);
        // params.height = (int)(llHeight* llHeightScale);
        params.width = (int) (llWidth * llHeightScale);
        params.setMargins(0, (int) distance, 0, 0);

        fl.requestLayout();
      }
    });
  }
}
