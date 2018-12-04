package in.oriange.dailydiary.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import in.oriange.dailydiary.R;
import in.oriange.dailydiary.models.TopProductsModel;
import in.oriange.dailydiary.utilities.ApplicationConstants;

public class GetTopProductsGridAdapter extends RecyclerView.Adapter<GetTopProductsGridAdapter.MyViewHolder> {

    private List<TopProductsModel> resultArrayList;
    private Context context;

    public GetTopProductsGridAdapter(Context context, List<TopProductsModel> resultArrayList) {
        this.context = context;
        this.resultArrayList = resultArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_grid_topproducts, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int pos) {
        int position = holder.getAdapterPosition();

        final int[] totalCount = {0};
        final TopProductsModel topProductsModel = resultArrayList.get(position);

        holder.tv_productprice.setText("₹ " + topProductsModel.getUnitPrice());
        holder.tv_productname.setText(topProductsModel.getItem_Name());

        Picasso.with(context)
                .load(ApplicationConstants.PRODUCTIMAGE + "" + topProductsModel.getItem_image())
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


        holder.edt_totalcount.setText("" + totalCount[0]);
        holder.tv_totalrate.setText("₹ " + 0);
        holder.imv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalCount[0] > 0) {

                    totalCount[0] = totalCount[0] - 1;
                    holder.edt_totalcount.setText("" + totalCount[0]);
                    holder.tv_totalrate.setText("₹ " + Integer.parseInt(topProductsModel.getUnitPrice()) * totalCount[0]);

                }

            }
        });

        holder.imv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalCount[0] = totalCount[0] + 1;
                holder.edt_totalcount.setText("" + totalCount[0]);
                holder.tv_totalrate.setText("₹ " + Integer.parseInt(topProductsModel.getUnitPrice()) * totalCount[0]);

            }
        });

        holder.btn_addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return resultArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_productprice, tv_productname, tv_totalrate, tv_nopreview;
        private ImageView imv_productimage, imv_remove, imv_add;
        private EditText edt_totalcount;
        private Button btn_addtocart;

        private MyViewHolder(View view) {
            super(view);
            tv_productprice = view.findViewById(R.id.tv_productprice);
            tv_productname = view.findViewById(R.id.tv_productname);
            tv_totalrate = view.findViewById(R.id.tv_totalrate);
            tv_nopreview = view.findViewById(R.id.tv_nopreview);
            imv_productimage = view.findViewById(R.id.imv_productimage);
            imv_remove = view.findViewById(R.id.imv_remove);
            imv_add = view.findViewById(R.id.imv_add);
            edt_totalcount = view.findViewById(R.id.edt_totalcount);
            btn_addtocart = view.findViewById(R.id.btn_addtocart);

        }
    }
}

