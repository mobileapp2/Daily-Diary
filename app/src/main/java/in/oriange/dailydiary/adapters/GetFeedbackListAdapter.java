package in.oriange.dailydiary.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.oriange.dailydiary.R;
import in.oriange.dailydiary.models.PackagesModel;

public class GetFeedbackListAdapter extends RecyclerView.Adapter<GetFeedbackListAdapter.MyViewHolder> {

    private List<PackagesModel> resultArrayList;
    private Context context;

    public GetFeedbackListAdapter(Context context, List<PackagesModel> resultArrayList) {
        this.context = context;
        this.resultArrayList = resultArrayList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_row_feedback, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        PackagesModel packageDetails = new PackagesModel();
        packageDetails = resultArrayList.get(position);
        final PackagesModel finalPackageDetails = packageDetails;

        holder.tv_packagename.setText(finalPackageDetails.getPackageName());

        ArrayList<PackagesModel.Feedback> feedback = new ArrayList<>();
        feedback = packageDetails.getFeedback();

        if (feedback != null) {
            holder.tv_feedback.setVisibility(View.VISIBLE);
            holder.rb_feedbackstars.setVisibility(View.VISIBLE);
            holder.tv_nofeedback.setVisibility(View.GONE);

            holder.tv_feedback.setText(feedback.get(0).getFeedback());
            holder.rb_feedbackstars.setRating(Float.parseFloat(feedback.get(0).getStarRating()));

        } else {
            holder.tv_feedback.setVisibility(View.GONE);
            holder.rb_feedbackstars.setVisibility(View.GONE);
            holder.tv_nofeedback.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return resultArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_packagename, tv_feedback, tv_nofeedback;
        private RatingBar rb_feedbackstars;

        public MyViewHolder(View view) {
            super(view);

            tv_packagename = view.findViewById(R.id.tv_packagename);
            tv_feedback = view.findViewById(R.id.tv_feedback);
            tv_nofeedback = view.findViewById(R.id.tv_nofeedback);
            rb_feedbackstars = view.findViewById(R.id.rb_feedbackstars);
        }
    }

}
