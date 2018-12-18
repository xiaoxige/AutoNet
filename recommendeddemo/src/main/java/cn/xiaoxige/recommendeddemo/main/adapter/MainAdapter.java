package cn.xiaoxige.recommendeddemo.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.xiaoxige.recommendeddemo.R;
import cn.xiaoxige.recommendeddemo.main.response.MainEntity;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<MainEntity> mData;

    private OptCallback mCallback;

    public MainAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(this.mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.list_item_public_address, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MainEntity mainEntity = mData.get(position);
        holder.bindListener(mainEntity, position);
        holder.bindData(mainEntity, position);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public int getDataSize() {
        return mData == null ? 0 : mData.size();
    }

    public void updateData(List<MainEntity> entitys) {
        this.mData = entitys;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
        }

        public void bindListener(MainEntity entity, int position) {
            String name = entity.getName();
            name = TextUtils.isEmpty(name) ? "未知" : name;
            tvName.setText(name);
        }

        public void bindData(final MainEntity entity, final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallback != null) {
                        mCallback.opt(0, entity, position);
                    }
                }
            });
        }
    }

    public void setCallback(OptCallback callback) {
        this.mCallback = callback;
    }

    public interface OptCallback {

        void opt(int opt, MainEntity entity, int position);

    }

}
