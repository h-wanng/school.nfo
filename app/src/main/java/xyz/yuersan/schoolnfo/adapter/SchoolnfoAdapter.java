package xyz.yuersan.schoolnfo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import xyz.yuersan.schoolnfo.R;
import xyz.yuersan.schoolnfo.model.InfoModel;

public class SchoolnfoAdapter extends RecyclerView.Adapter<SchoolnfoAdapter.SchoolnfoViewHolder> {
    private List<InfoModel> mInfoDatas;
    private Context mContext;

    public SchoolnfoAdapter(List<InfoModel> data, Context context) {
        this.mContext = context;
        this.mInfoDatas = data;
        Log.d("INFOSIZE","size"+mInfoDatas.size());
    }

    @NonNull
    @Override
    public SchoolnfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.schoolnfo_item, parent, false);
        return new SchoolnfoViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull SchoolnfoViewHolder holder, int position) {
        InfoModel infoModel = mInfoDatas.get(position);
        holder.infoTitle.setText(infoModel.getInfoTitle());
        holder.infoAuthor.setText(infoModel.getInfoAuthorName());
        holder.infoDate.setText(infoModel.getInfoDate());

    }


    @Override
    public int getItemCount() {
        return mInfoDatas.size();
    }

    static class SchoolnfoViewHolder extends RecyclerView.ViewHolder {

        private TextView infoAuthor;
        private TextView infoDate;
        private TextView infoTitle;

        SchoolnfoViewHolder(@NonNull View itemView) {
            super(itemView);
            infoTitle = (TextView) itemView.findViewById(R.id.text_infotitle);
            infoAuthor = (TextView) itemView.findViewById(R.id.text_infoauthor);
            infoDate = (TextView) itemView.findViewById(R.id.text_infodate);
        }
    }
}
