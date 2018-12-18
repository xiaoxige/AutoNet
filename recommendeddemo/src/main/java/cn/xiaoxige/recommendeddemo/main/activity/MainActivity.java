package cn.xiaoxige.recommendeddemo.main.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import cn.xiaoxige.autonet_api.AutoNet;
import cn.xiaoxige.autonet_api.abstracts.AbsAutoNetCallback;
import cn.xiaoxige.autonet_api.error.EmptyError;
import cn.xiaoxige.emptylayout.EmptyLayout;
import cn.xiaoxige.recommendeddemo.R;
import cn.xiaoxige.recommendeddemo.main.adapter.MainAdapter;
import cn.xiaoxige.recommendeddemo.main.response.MainEntity;
import cn.xiaoxige.recommendeddemo.main.response.MainResponse;
import cn.xiaoxige.recommendeddemo.util.HandlerError;
import io.reactivex.FlowableEmitter;

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

        mAdapter.setCallback(new MainAdapter.OptCallback() {
            @Override
            public void opt(int opt, MainEntity entity, int position) {
                Toast.makeText(MainActivity.this, entity.getName() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void bindData() {
        refreshData();
    }

    private void refreshData() {
        if (mAdapter.getDataSize() <= 0) {
            mEmptyLayout.showLoading();
        }

        /****
         * 把AutoNet可以再次封装成一个简易的门面Util，这样添加头部等都很方便。 eg: AutoNetUtil.java
         *
         *     public static void executePost(String method, Map request, IAutoNetCallBack callBack) {
         *          BaseRequest baseRequest = initCommonParams(method);
         *          baseRequest.setMethod(method);
         *          baseRequest.setParams(request);
         *          //noinspection unchecked
         *          AutoNet.getInstance()
         *                  .createNet()
         *                  .setSuffixUrl(ApiConstant.SUFFIX_URL)
         *                  .doPost(baseRequest)
         *                  .start(callBack);
         *    }

         *    private static BaseRequest initCommonParams(String method) {
         *          BaseRequest requestEntity = new BaseRequest();
         *          requestEntity.setVer(AppUtils.getVersionName(OverAllsituationConstants.sAppContext));
         *          String deviceId = GetDeviceid.id(OverAllsituationConstants.sAppContext);
         *          requestEntity.setDevice_id(deviceId);
         *          requestEntity.setTimestamp(System.currentTimeMillis());
         *          requestEntity.setToken(CommonConstants.token);
         *          String auth = MD5Util.MD5Encode(method + deviceId + "^8u2@BcxUn!$");
         *          AutoNet.getInstance().updateOrInsertHead("Auth", auth);
         *          return requestEntity;
         *    }
         */
        AutoNet.getInstance().createNet()
//                .doGet()
                .setSuffixUrl("/wxarticle/chapters/json")
                .start(new RefreshCallback());
    }


    private class RefreshCallback extends AbsAutoNetCallback<MainResponse, List<MainEntity>> {

        @Override
        public boolean handlerBefore(MainResponse mainResponse, FlowableEmitter emitter) {
            List<MainEntity> data = mainResponse.getData();
            if (data == null || data.isEmpty()) {
                emitter.onError(new EmptyError());
                return true;
            }
            //noinspection unchecked
            emitter.onNext(data);
            return true;
        }

        @Override
        public void onSuccess(List<MainEntity> entity) {
            super.onSuccess(entity);
            mAdapter.updateData(entity);
            refreshLayout.setRefreshing(false);
            mEmptyLayout.showContent();
        }

        @Override
        public void onFailed(Throwable throwable) {
            super.onFailed(throwable);
            if (mAdapter.getDataSize() <= 0) {
                mEmptyLayout.showError();
            }
            HandlerError.handlerError(throwable);
        }

        @Override
        public void onEmpty() {
            super.onEmpty();
            if (mAdapter.getDataSize() <= 0) {
                mEmptyLayout.showEmpty();
            }
            HandlerError.handlerEmpty();
        }
    }

}
