package xyz.yuersan.schoolnfo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import xyz.yuersan.schoolnfo.R;
import xyz.yuersan.schoolnfo.model.ClassModel;

public class ClassGroupAdapter extends RecyclerView.Adapter<ClassGroupAdapter.ClassGroupViewHolder> {
    private List<ClassModel> mClassDatas;
    private Context mContext;

    public ClassGroupAdapter(List<ClassModel> data, Context context){
        this.mClassDatas = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ClassGroupAdapter.ClassGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item, parent, false);
        return new ClassGroupViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassGroupAdapter.ClassGroupViewHolder holder, int position) {
        ClassModel classModel = mClassDatas.get(position);
        holder.classGroupName.setText(classModel.getClassDes());
    }

    @Override
    public int getItemCount() {
        return mClassDatas.size();
    }

    static class ClassGroupViewHolder extends RecyclerView.ViewHolder{

        private TextView classGroupName;
        private TextView classGroupLast;
        private TextView classGroupLastTime;

        ClassGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            classGroupName = (TextView) itemView.findViewById(R.id.text_classgroup_name);
            classGroupLast = (TextView) itemView.findViewById(R.id.text_classgroup_last);
            classGroupLastTime = (TextView) itemView.findViewById(R.id.text_classgroup_lasttime);
        }
    }
}
