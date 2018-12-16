package in.oriange.dailydiary.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.oriange.dailydiary.R;
import in.oriange.dailydiary.models.PackagesModel;

public class GetPackageListAdapter extends RecyclerView.Adapter<GetPackageListAdapter.MyViewHolder> {

    private List<PackagesModel> resultArrayList;
    private Context context;

    public GetPackageListAdapter(Context context, List<PackagesModel> resultArrayList) {
        this.context = context;
        this.resultArrayList = resultArrayList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_row_package, parent, false);
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
        holder.tv_addressandfullname.setText(finalPackageDetails.getFull_name() + ", " + finalPackageDetails.getAddress_name());
        holder.tv_startdate.setText("Start Date - " + finalPackageDetails.getStartDate().substring(0, 10));
        holder.tv_status.setText(finalPackageDetails.getStatus_name());


    }

    @Override
    public int getItemCount() {
        return resultArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_packagename, tv_addressandfullname, tv_startdate, tv_status;

        public MyViewHolder(View view) {
            super(view);

            tv_packagename = view.findViewById(R.id.tv_packagename);
            tv_addressandfullname = view.findViewById(R.id.tv_addressandfullname);
            tv_startdate = view.findViewById(R.id.tv_startdate);
            tv_status = view.findViewById(R.id.tv_status);
        }
    }

}
