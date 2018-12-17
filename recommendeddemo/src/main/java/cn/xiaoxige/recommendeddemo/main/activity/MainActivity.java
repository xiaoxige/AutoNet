package cn.xiaoxige.recommendeddemo.main.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import cn.xiaoxige.emptylayout.EmptyLayout;
import cn.xiaoxige.recommendeddemo.R;
import cn.xiaoxige.recommendeddemo.main.adapter.MainAdapter;

public class MainActivity extends Activity {

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    private EmptyLayout mEmptyLayout;

    private MainAdapter mAdapter;

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

        mAdapter = new MainAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);

    }

    private void registerListener() {

        // 加载数据为空， 点击重新加载按钮回调
        mEmptyLayout.setmEmptyListener(new EmptyLayout.onEmptyListener() {
            @Override
            public void onClickEmpty(View v) {
                refreshData();
            }
        });

        // 加载失败， 点击重新加载按钮回调
        mEmptyLayout.setmErrorListener(new EmptyLayout.onErrorListener() {
            @Override
            public void onClickError(View v) {
                refreshData();
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
    }


    private void bindData() {
        refreshData();
    }

    private void refreshData() {

    }

}
