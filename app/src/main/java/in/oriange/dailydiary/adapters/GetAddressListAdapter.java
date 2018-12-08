package in.oriange.dailydiary.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.oriange.dailydiary.R;
import in.oriange.dailydiary.activities.EditAddress_Activity;
import in.oriange.dailydiary.models.AddressesModel;

public class GetAddressListAdapter extends RecyclerView.Adapter<GetAddressListAdapter.MyViewHolder> {

    private List<AddressesModel> resultArrayList;
    private Context context;

    public GetAddressListAdapter(Context context, List<AddressesModel> resultArrayList) {
        this.context = context;
        this.resultArrayList = resultArrayList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_row_address, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        AddressesModel addressDetails = new AddressesModel();
        addressDetails = resultArrayList.get(position);
        final AddressesModel finalAddressDetails = addressDetails;

        holder.tv_addressandfullname.setText(addressDetails.getFull_name() + ", " + addressDetails.getAddress_name());

        holder.tv_addressdetails.setText(addressDetails.getAddressline_one() + ", " + addressDetails.getAddressline_two() + ", " +
                addressDetails.getState_name() + ", " + addressDetails.getCity_name() + ", " + addressDetails.getPincode());

        holder.tv_emailandmobileno.setText(addressDetails.getMobile_number() + ", " + addressDetails.getEmail());


        holder.cv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, EditAddress_Activity.class).putExtra("addressDetails", finalAddressDetails));
            }
        });


    }

    @Override
    public int getItemCount() {
        return resultArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_addressandfullname, tv_addressdetails, tv_emailandmobileno;
        private CardView cv_address;

        public MyViewHolder(View view) {
            super(view);
            tv_addressandfullname = view.findViewById(R.id.tv_addressandfullname);
            tv_addressdetails = view.findViewById(R.id.tv_addressdetails);
            tv_emailandmobileno = view.findViewById(R.id.tv_emailandmobileno);

            cv_address = view.findViewById(R.id.cv_address);
        }
    }

}
