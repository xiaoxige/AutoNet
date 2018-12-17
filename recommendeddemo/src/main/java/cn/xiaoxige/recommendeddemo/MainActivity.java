package cn.xiaoxige.recommendeddemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import cn.xiaoxige.emptylayout.EmptyLayout;

public class MainActivity extends Activity {

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    private EmptyLayout mEmptyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        registerListener();
        bindData();
    }

    private void initViews() {
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mEmptyLayout = new EmptyLayout(this, recyclerView, EmptyLayout.RELATIVESELF);
        mEmptyLayout.setIsLoadingTransparent(false);


    }

    private void registerListener() {

        // 加载数据为空， 点击重新加载按钮回调
        mEmptyLayout.setmEmptyListener(new EmptyLayout.onEmptyListener() {
            @Override
            public void onClickEmpty(View v) {

            }
        });

        // 加载失败， 点击重新加载按钮回调
        mEmptyLayout.setmErrorListener(new EmptyLayout.onErrorListener() {
            @Override
            public void onClickError(View v) {

            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
    }


    private void bindData() {

    }

}
