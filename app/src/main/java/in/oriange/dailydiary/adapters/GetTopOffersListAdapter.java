package in.oriange.dailydiary.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import in.oriange.dailydiary.R;
import in.oriange.dailydiary.models.TopOffersModel;
import in.oriange.dailydiary.utilities.ApplicationConstants;

public class GetTopOffersListAdapter extends RecyclerView.Adapter<GetTopOffersListAdapter.MyViewHolder> {

    private List<TopOffersModel> resultArrayList;
    private Context context;

    public GetTopOffersListAdapter(Context context, List<TopOffersModel> resultArrayList) {
        this.context = context;
        this.resultArrayList = resultArrayList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_row_topoffers, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int pos) {
        int position = holder.getAdapterPosition();
        TopOffersModel topProductsModel = resultArrayList.get(position);

        Picasso.with(context)
                .load(ApplicationConstants.OFFERIMAGE + "" + topProductsModel.getOfferImage())
                .into(holder.imv_productimage, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.tv_nopreview.setVisibility(View.GONE);
                        holder.imv_productimage.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {
                        holder.tv_nopreview.setVisibility(View.VISIBLE);
                        holder.imv_productimage.setVisibility(View.GONE);
                    }
                });


    }

    @Override
    public int getItemCount() {
        return resultArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_productprice, tv_productname, tv_nopreview;
        private ImageView imv_productimage;


        private MyViewHolder(View view) {
            super(view);
            tv_productprice = view.findViewById(R.id.tv_productprice);
            tv_productname = view.findViewById(R.id.tv_productname);
            tv_nopreview = view.findViewById(R.id.tv_nopreview);
            imv_productimage = view.findViewById(R.id.imv_productimage);

        }
    }


}
