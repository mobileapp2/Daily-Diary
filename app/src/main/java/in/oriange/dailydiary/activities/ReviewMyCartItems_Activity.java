package in.oriange.dailydiary.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.oriange.dailydiary.R;
import in.oriange.dailydiary.models.AddressesModel;
import in.oriange.dailydiary.models.PackageItemsModel;
import in.oriange.dailydiary.utilities.ApplicationConstants;
import in.oriange.dailydiary.utilities.ConstantData;
import in.oriange.dailydiary.utilities.UserSessionManager;

public class ReviewMyCartItems_Activity extends Activity {

    private Context context;
    private UserSessionManager session;
    private ConstantData constantData;
    private LinearLayout ll_mainlayout;
    private RecyclerView rv_itemlist;
    private TextView tv_addressandfullname, tv_addressdetails, tv_emailandmobileno;
    private Button btn_next;

    private String ConsumerID;
    private List<Date> selectedDates;
    private AddressesModel selectedAddress;
    private ArrayList<PackageItemsModel> selectedItemsList;

    private int totalFinalRate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviewmy_cartitems);

        init();
        getSessionData();
        setDefault();
        setEventHandler();
        setUpToolBar();
    }

    private void init() {
        context = ReviewMyCartItems_Activity.this;
        session = new UserSessionManager(context);
        constantData = ConstantData.getInstance();

        rv_itemlist = findViewById(R.id.rv_itemlist);
        rv_itemlist.setLayoutManager(new LinearLayoutManager(context));

        ll_mainlayout = findViewById(R.id.ll_mainlayout);
        tv_addressandfullname = findViewById(R.id.tv_addressandfullname);
        tv_addressdetails = findViewById(R.id.tv_addressdetails);
        tv_emailandmobileno = findViewById(R.id.tv_emailandmobileno);
        btn_next = findViewById(R.id.btn_next);
    }

    private void getSessionData() {
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            JSONObject json = user_info.getJSONObject(0);
            ConsumerID = json.getString("ConsumerID");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    private void setDefault() {
        selectedDates = (List<Date>) getIntent().getSerializableExtra("selectedDates");
        selectedAddress = (AddressesModel) getIntent().getSerializableExtra("selectedAddress");
        selectedItemsList = constantData.getPackageItemsList();


        for (PackageItemsModel packageItemsModel : selectedItemsList) {
            String rate = packageItemsModel.getTotalProductrate().substring(2);
            totalFinalRate = totalFinalRate + Integer.parseInt(rate);
        }

        tv_addressandfullname.setText(selectedAddress.getFull_name() + ", " + selectedAddress.getAddress_name());

        tv_addressdetails.setText(selectedAddress.getAddressline_one() + ", " + selectedAddress.getAddressline_two() + ", " +
                selectedAddress.getState_name() + ", " + selectedAddress.getCity_name() + ", " + selectedAddress.getPincode());

        tv_emailandmobileno.setText(selectedAddress.getMobile_number() + ", " + selectedAddress.getEmail());

        rv_itemlist.setAdapter(new SelectedItemsAdapter());

    }

    private void setEventHandler() {
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CreatePackage_Activity.class);
                intent.putExtra("selectedDates", (Serializable) selectedDates);
                intent.putExtra("selectedAddress", selectedAddress);
                startActivity(intent);
            }
        });
    }

    public class SelectedItemsAdapter extends RecyclerView.Adapter<SelectedItemsAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.list_row_mycart, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int pos) {
            int position = holder.getAdapterPosition();

            final PackageItemsModel selectedItem = selectedItemsList.get(position);
            final int[] totalCount = {Integer.parseInt(selectedItem.getTotalProductCount())};

            holder.tv_productname.setText(selectedItem.getItem_Name());
            holder.tv_productprice.setText("₹ " + selectedItem.getUnitPrice());
            holder.edt_totalcount.setText("" + totalCount[0]);
            holder.tv_unit.setText("Qty/ "+selectedItem.getUoM_Name());
            holder.tv_totalrate.setText(selectedItem.getTotalProductrate());

            Picasso.with(context)
                    .load(ApplicationConstants.PRODUCTIMAGE + "" + selectedItem.getItem_image())
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

            holder.imv_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            holder.imv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            holder.imv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }

        @Override
        public int getItemCount() {
            return selectedItemsList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private ImageView imv_productimage, imv_remove, imv_add, imv_delete;
            private TextView tv_nopreview, tv_productname, tv_productprice, edt_totalcount, tv_unit, tv_totalrate;

            private MyViewHolder(View view) {
                super(view);
                imv_productimage = view.findViewById(R.id.imv_productimage);
                imv_remove = view.findViewById(R.id.imv_remove);
                imv_add = view.findViewById(R.id.imv_add);
                imv_delete = view.findViewById(R.id.imv_delete);
                tv_nopreview = view.findViewById(R.id.tv_nopreview);
                tv_productname = view.findViewById(R.id.tv_productname);
                tv_productprice = view.findViewById(R.id.tv_productprice);
                edt_totalcount = view.findViewById(R.id.edt_totalcount);
                tv_unit = view.findViewById(R.id.tv_unit);
                tv_totalrate = view.findViewById(R.id.tv_totalrate);

            }
        }
    }

    private void setUpToolBar() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle(Html.fromHtml("<font color='#00000'>My Cart</font>"));
        mToolbar.setNavigationIcon(R.drawable.icon_backarrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
