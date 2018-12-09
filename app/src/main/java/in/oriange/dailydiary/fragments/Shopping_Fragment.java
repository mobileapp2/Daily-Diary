package in.oriange.dailydiary.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.oriange.dailydiary.R;
import in.oriange.dailydiary.activities.SelectDateForPackage_Activity;
import in.oriange.dailydiary.models.PackageItemsModel;
import in.oriange.dailydiary.models.TopProductsModel;
import in.oriange.dailydiary.models.TopProductsPojo;
import in.oriange.dailydiary.utilities.ApplicationConstants;
import in.oriange.dailydiary.utilities.ConstantData;
import in.oriange.dailydiary.utilities.CountDrawable;
import in.oriange.dailydiary.utilities.UserSessionManager;
import in.oriange.dailydiary.utilities.Utilities;
import in.oriange.dailydiary.utilities.WebServiceCalls;

public class Shopping_Fragment extends Fragment {

    private Context context;
    private UserSessionManager session;
    private RecyclerView rv_topproducts;
    private LinearLayout main_content;
    private ArrayList<TopProductsModel> topProductsList;
    private ConstantData constantData;

    private MenuItem menuItem;
    private LayerDrawable icon_cart;
    private ArrayList<PackageItemsModel> packageItemsList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shopping, container, false);
        context = getActivity();
        init(rootView);
        getSessionData();
        setDefault();
        setEventHandlers();
        return rootView;
    }

    private void init(View rootView) {
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        constantData = ConstantData.getInstance();
        session = new UserSessionManager(context);
        main_content = getActivity().findViewById(R.id.main_content);
        rv_topproducts = rootView.findViewById(R.id.rv_topproducts);
        rv_topproducts.setLayoutManager(new GridLayoutManager(context, 2));
        topProductsList = new ArrayList<>();
        packageItemsList = new ArrayList<>();
    }

    private void getSessionData() {

    }

    private void setDefault() {

        packageItemsList = constantData.getPackageItemsList();

        if (packageItemsList == null) {
            packageItemsList = new ArrayList<>();
        }

        if (constantData.getTopProductsList() == null) {
            if (Utilities.isInternetAvailable(context)) {
                new GetTopProducts().execute();
            } else {
                Utilities.showSnackBar(main_content, "Please Check Internet Connection");
            }
        } else {
            topProductsList = constantData.getTopProductsList();
            rv_topproducts.setAdapter(new GetTopProductsGridAdapter(context, constantData.getTopProductsList()));
        }

    }

    private void setEventHandlers() {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.shopping_fragment_menus, menu);

        MenuItem mSearch = menu.findItem(R.id.action_search);

        final SearchView mSearchView = (SearchView) mSearch.getActionView();

        EditText searchEditText = (EditText) mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.black));
        searchEditText.setHintTextColor(getResources().getColor(R.color.mediumGray));
        mSearchView.setQueryHint("Search here");

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchView.clearFocus();
                return filterList(query);
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return filterList(newText);
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_cart:

                if (packageItemsList.size() > 0) {
                    startActivity(new Intent(context, SelectDateForPackage_Activity.class));
                } else {
                    Utilities.showMessageString(context, "Please add items to the cart to proceed further");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (menu != null) {
            menuItem = menu.findItem(R.id.action_cart);
            icon_cart = (LayerDrawable) menuItem.getIcon();

            setCount(context, String.valueOf(packageItemsList.size()), icon_cart);

        }
    }

    public void setCount(Context context, String count, LayerDrawable icon_cart) {
        CountDrawable badge;
        Drawable reuse = icon_cart.findDrawableByLayerId(R.id.ic_group_count);
        if (reuse instanceof CountDrawable) {
            badge = (CountDrawable) reuse;
        } else {
            badge = new CountDrawable(context);
        }

        badge.setCount(count);
        icon_cart.mutate();
        icon_cart.setDrawableByLayerId(R.id.ic_group_count, badge);
    }

    public boolean filterList(String filterKeyword) {
        if (!filterKeyword.equals("")) {
            ArrayList<TopProductsModel> searchedProductsList = new ArrayList<>();
            for (TopProductsModel product : topProductsList) {

                if (product.getItem_Name().toLowerCase().contains(filterKeyword.toLowerCase())) {
                    searchedProductsList.add(product);
                }
            }
            rv_topproducts.setAdapter(new GetTopProductsGridAdapter(context, searchedProductsList));
        } else {
            rv_topproducts.setAdapter(new GetTopProductsGridAdapter(context, topProductsList));
        }
        return true;
    }

    public class GetTopProducts extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "[]";
            JsonObject obj = new JsonObject();
            obj.addProperty("type", "GetItemsByPincode");
            obj.addProperty("pincode", "413512");
            res = WebServiceCalls.JSONAPICall(ApplicationConstants.getItem, obj.toString());
            return res.trim();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    JSONObject mainObj = new JSONObject(result);
                    topProductsList = new ArrayList<>();
                    TopProductsPojo empPojo = new Gson().fromJson(result, TopProductsPojo.class);
                    type = empPojo.getType();
                    message = empPojo.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        topProductsList = empPojo.getData();

                        if (topProductsList.size() > 0) {
                            rv_topproducts.setAdapter(new GetTopProductsGridAdapter(context, topProductsList));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

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
            holder.tv_unit.setText(topProductsModel.getUoM_Name());
            holder.edt_totalcount.setText("" + totalCount[0]);
            holder.tv_totalrate.setText("₹ " + 0);

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

                    if (holder.tv_totalrate.getText().toString().trim().equals("₹ 0")) {
                        return;
                    }

                    for (int i = 0; i < packageItemsList.size(); i++) {
                        if (topProductsModel.getItemID().equals(packageItemsList.get(i).getItemID())) {
                            packageItemsList.get(i).setTotalProductCount(holder.edt_totalcount.getText().toString().trim());
                            packageItemsList.get(i).setTotalProductrate(holder.tv_totalrate.getText().toString().trim());
                            setCount(context, String.valueOf(packageItemsList.size()), icon_cart);
                            return;
                        }
                    }


                    packageItemsList.add(new PackageItemsModel(
                            topProductsModel.getItemID(),
                            topProductsModel.getItem_image(),
                            topProductsModel.getUoM_ID(),
                            topProductsModel.getIsActive(),
                            topProductsModel.getUnitPrice(),
                            topProductsModel.getItem_Name(),
                            topProductsModel.getUoM_Name(),
                            holder.edt_totalcount.getText().toString().trim(),
                            holder.tv_totalrate.getText().toString().trim()
                    ));

                    constantData.setPackageItemsList(packageItemsList);
                    setCount(context, String.valueOf(packageItemsList.size()), icon_cart);


                }
            });

        }

        @Override
        public int getItemCount() {
            return resultArrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView tv_productprice, tv_productname, tv_totalrate, tv_nopreview, tv_unit;
            private ImageView imv_productimage, imv_remove, imv_add;
            private EditText edt_totalcount;
            private Button btn_addtocart;

            private MyViewHolder(View view) {
                super(view);
                tv_productprice = view.findViewById(R.id.tv_productprice);
                tv_productname = view.findViewById(R.id.tv_productname);
                tv_totalrate = view.findViewById(R.id.tv_totalrate);
                tv_nopreview = view.findViewById(R.id.tv_nopreview);
                tv_unit = view.findViewById(R.id.tv_unit);
                imv_productimage = view.findViewById(R.id.imv_productimage);
                imv_remove = view.findViewById(R.id.imv_remove);
                imv_add = view.findViewById(R.id.imv_add);
                edt_totalcount = view.findViewById(R.id.edt_totalcount);
                btn_addtocart = view.findViewById(R.id.btn_addtocart);

            }
        }
    }


}
