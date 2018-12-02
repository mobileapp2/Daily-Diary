package in.oriange.dailydiary.fragments;

import android.content.Context;
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
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;

import in.oriange.dailydiary.R;
import in.oriange.dailydiary.adapters.GetTopProductsGridAdapter;
import in.oriange.dailydiary.models.TopProductsModel;
import in.oriange.dailydiary.models.TopProductsPojo;
import in.oriange.dailydiary.utilities.ApplicationConstants;
import in.oriange.dailydiary.utilities.ConstantData;
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
    }

    private void getSessionData() {

    }

    private void setDefault() {

        if (constantData.getTopProductsList() == null) {
            if (Utilities.isInternetAvailable(context)) {
                new GetTopProducts().execute();
            } else {
                Utilities.showSnackBar(main_content, "Please Check Internet Connection");
            }
        } else {
            rv_topproducts.setAdapter(new GetTopProductsGridAdapter(context, constantData.getTopProductsList()));
        }

    }

    private void setEventHandlers() {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.shopping_fragment_menus, menu);

        MenuItem mSearch = menu.findItem(R.id.action_search);

        SearchView mSearchView = (SearchView) mSearch.getActionView();

        EditText searchEditText = (EditText) mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.black));
        searchEditText.setHintTextColor(getResources().getColor(R.color.mediumGray));
        mSearchView.setQueryHint("Search here");

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);


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

}
