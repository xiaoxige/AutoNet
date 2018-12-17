package cn.xiaoxige.emptylayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.StringRes;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;
import java.util.Set;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

/**
 * Created by 小稀革 on 2017/7/16.
 * 空布局
 */

public class EmptyLayout {

    public static final int RELATIVESELF = 0x00;
    public static final int RELATIVEPARENT = 0x01;

    private static final int STATE_ERROR = 0x01;
    private static final int STATE_EMPTY = 0x02;
    private static final int STATE_LOADING = 0x03;
    private static final int STATE_CONTENT = 0x00;


    private onEmptyListener mEmptyListener;
    private onErrorListener mErrorListener;

    protected Context mContext;
    protected LayoutInflater mInFlater;
    private ViewGroup mRootGroupView;

    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;
    private View mContentView;

    /**
     * 加载时是否透明
     */
    private boolean mIsLoadingTransparent;
    private boolean mIsShowLoadingAnimation;
    protected int mState;

    private Map<Integer, View> mCustomLayouts;

    public EmptyLayout(Context context, View view) {
        this(context, view, RELATIVEPARENT);
    }

    public EmptyLayout(Context context, View view, @relativeType int relativeWho) {
        this.mContext = context;
        this.mCustomLayouts = new ArrayMap<>();

        if (view == null) {
            throw new NullPointerException("view is null.");
        }
        mInFlater = LayoutInflater.from(mContext);

        mContentView = relativeWho == RELATIVEPARENT ? view : (View) view.getParent();
        if (mContentView == null) {
            throw new ExceptionInInitializerError("view's parent is null.");
        }

        if (RELATIVEPARENT == relativeWho && !(mContentView instanceof ViewGroup)) {
            throw new ExceptionInInitializerError("it's relative is parent and parent can't with view.");
        }

        this.mIsLoadingTransparent = true;
        this.mIsShowLoadingAnimation = true;
        mState = STATE_CONTENT;

        mContentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                mContentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mRootGroupView = new RelativeLayout(mContext);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                params.addRule(RelativeLayout.CENTER_VERTICAL);
                mRootGroupView.setLayoutParams(params);
                int index = getEmptyLayoutIndex();
                if (index == -1) {
                    throw new RuntimeException("this centerview is not find.");
                }
                ViewGroup parent = (ViewGroup) mContentView.getParent();
                parent.removeView(mContentView);
                addViewInRoot(mContentView);
                parent.addView(mRootGroupView, index);

                showInvalidate();
            }
        });
    }

    public void changeErrorIcon(@DrawableRes int errorIcon) {
        invalidateError();
        setIcon(mErrorView, errorIcon);
    }

    public void changeEmptyIcon(@DrawableRes int emptyIcon) {
        invilidateEmpty();
        setIcon(mEmptyView, emptyIcon);
    }

    public void changeErrorShowMsg(@StringRes int strId) {
        changeErrorShowMsg(mContext.getString(strId));
    }

    public void changeEmptyShowMsg(@StringRes int strId) {
        changeEmptyShowMsg(mContext.getString(strId));
    }

    public void changeLoadingShowMsg(@StringRes int strId) {
        changeLoadingShowMsg(mContext.getString(strId));
    }

    public void setErrorReTryText(@StringRes int strId) {
        setErrorReTryText(mContext.getString(strId));
    }

    public void setEmptyReTryText(@StringRes int strId) {
        setEmptyReTryText(mContext.getString(strId));
    }

    public void changeErrorShowMsg(String msg) {
        invalidateError();
        setMsg(mErrorView, msg);
    }

    public void changeEmptyShowMsg(String msg) {
        invilidateEmpty();
        setMsg(mEmptyView, msg);
    }

    public void changeLoadingShowMsg(String msg) {
        invilidateLoading();
        setMsg(mLoadingView, msg);
    }

    public void setErrorReTryText(String msg) {
        invalidateError();
        setReTryText(mErrorView, msg);
    }

    public void setEmptyReTryText(String msg) {
        invilidateEmpty();
        setReTryText(mEmptyView, msg);
    }

    public void setErrorRetryVisible(boolean visible) {
        invalidateError();
        setRetryVisible(mErrorView, visible);
    }

    public void setEmptyVisible(boolean visible) {
        invilidateEmpty();
        setRetryVisible(mEmptyView, visible);
    }

    public void setIsShowLoadingAnimation(boolean isShowLoadingAnimation) {
        this.mIsShowLoadingAnimation = isShowLoadingAnimation;
    }

    public void setIsLoadingTransparent(boolean isLoadingTransparent) {
        this.mIsLoadingTransparent = isLoadingTransparent;
    }

    public void addCustomLayout(Integer state, View view) {
        if (!isLegitimateState(state)) {
            throw new IllegalArgumentException("This state has already existed, please redefine the state...");
        }
        if (view == null) {
            throw new IllegalArgumentException("Custom empty layout can not be empty...");
        }

        ViewGroup customParent = (ViewGroup) view.getParent();
        if (customParent != null) {
            throw new IllegalArgumentException("Custom layout has a parent layout...");
        }

        mCustomLayouts.put(state, view);
    }


    public void showError() {
        mState = STATE_ERROR;
        showInvalidate();
    }

    public void showEmpty() {
        mState = STATE_EMPTY;
        showInvalidate();
    }

    public void showLoading() {
        mState = STATE_LOADING;
        showInvalidate();
    }

    public void showContent() {
        mState = STATE_CONTENT;
        showInvalidate();
    }

    protected void showInvalidate() {
        if (mRootGroupView == null) {
            return;
        }
        hindAll();
        switch (mState) {
            case STATE_ERROR:
                invalidateError();
                mErrorView.setVisibility(View.VISIBLE);
                break;
            case STATE_EMPTY:
                invilidateEmpty();
                mEmptyView.setVisibility(View.VISIBLE);
                break;
            case STATE_LOADING:
                invilidateLoading();
                if (mIsShowLoadingAnimation) {
                    showLoadingAnimation();
                }
                mLoadingView.setVisibility(View.VISIBLE);
                break;
            case STATE_CONTENT:
                mContentView.setVisibility(View.VISIBLE);
                break;
            default:
                handlerCustomLayout();
                break;
        }
    }


    private void showLoadingAnimation() {
        View view = mLoadingView.findViewById(R.id.iv_img);
        RotateAnimation animation = new RotateAnimation(0, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(500);
        view.setAnimation(animation);
        animation.setRepeatMode(Animation.RESTART);
        animation.setRepeatCount(-1);
        animation.start();
    }

    private void hindAll() {
        if (mErrorView != null) {
            mErrorView.setVisibility(View.GONE);
        }
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.GONE);
        }
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
        }

        Set<Integer> customStates =
                mCustomLayouts.keySet();
        for (Integer state : customStates) {
            mCustomLayouts.get(state).setVisibility(View.GONE);
        }

        if (mContentView != null) {
            mContentView.setVisibility(mState == STATE_LOADING && mIsLoadingTransparent ? View.VISIBLE : View.GONE);
        }
    }

    private void invalidateError() {
        if (mErrorView == null) {
            mErrorView = mInFlater.inflate(R.layout.empty_errorlayout, null);
            mErrorView.findViewById(R.id.btn_retry).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mErrorListener != null) {
                        mErrorListener.onClickError(v);
                    }
                }
            });
        }
        addViewInRoot(mErrorView);
    }

    private void invilidateEmpty() {
        if (mEmptyView == null) {
            mEmptyView = mInFlater.inflate(R.layout.empty_emptylayout, null);
            mEmptyView.findViewById(R.id.btn_retry).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mEmptyListener != null) {
                        mEmptyListener.onClickEmpty(v);
                    }
                }
            });
        }
        addViewInRoot(mEmptyView);
    }

    private void invilidateLoading() {
        if (mLoadingView == null) {
            mLoadingView = mInFlater.inflate(R.layout.emtpy_loadinglayout, null);
        }
        addViewInRoot(mLoadingView);
    }

    private void handlerCustomLayout() {
        View view = mCustomLayouts.get(mState);
        if (view == null) {
            return;
        }
        addViewInRoot(view);
        view.setVisibility(View.VISIBLE);
    }

    private boolean isLegitimateState(Integer state) {
        if (state == EmptyLayout.STATE_LOADING
                || state == EmptyLayout.STATE_CONTENT
                || state == EmptyLayout.STATE_EMPTY
                || state == EmptyLayout.STATE_ERROR) {
            return false;
        }
        return true;
    }

    private View getPackingView(View view) {
        RelativeLayout layout = new RelativeLayout(mContext);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        layout.setLayoutParams(params);

        layout.addView(view, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        return layout;
    }

    private void addViewInRoot(View view) {
        if (mRootGroupView == null) {
            return;
        }
        if (view.getParent() == null) {
            mRootGroupView.addView(getPackingView(view));
        }
    }

    private void setRetryVisible(View view, boolean isShow) {
        if (view == null) {
            return;
        }
        (view.findViewById(R.id.btn_retry)).setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private void setIcon(View view, @DrawableRes int iconId) {
        if (view == null) {
            return;
        }
        ((ImageView) view.findViewById(R.id.iv_img)).setImageResource(iconId);
    }


    private void setMsg(View view, String msg) {
        if (view == null) {
            return;
        }
        ((TextView) view.findViewById(R.id.tv_msg)).setText(msg);
    }

    private void setReTryText(View view, String msg) {
        if (view == null) {
            return;
        }
        ((Button) view.findViewById(R.id.btn_retry)).setText(msg);
    }

    public void setmEmptyListener(onEmptyListener mEmptyListener) {
        this.mEmptyListener = mEmptyListener;
    }

    public void setmErrorListener(onErrorListener mErrorListener) {
        this.mErrorListener = mErrorListener;
    }

    public int getEmptyLayoutIndex() {

        ViewGroup parent = (ViewGroup) mContentView.getParent();
        int childCount = parent.getChildCount();
        View tempView;
        for (int i = 0; i < childCount; i++) {
            tempView = parent.getChildAt(i);
            if (tempView == mContentView) {
                return i;
            }
        }

        return -1;
    }

    public interface onEmptyListener {
        void onClickEmpty(View v);
    }

    public interface onErrorListener {
        void onClickError(View v);
    }


    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({RELATIVESELF, RELATIVEPARENT})
    public @interface relativeType {
    }

}
