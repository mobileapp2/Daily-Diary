package in.oriange.dailydiary.fragments;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import in.oriange.dailydiary.R;

public class BottomSheetMenu_Fragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private Context context;
    private Button btn_login, btn_register;

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;

    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_bottomsheet_menu, null);
        dialog.setContentView(view);

        init(view);
        setEventListner();
    }

    private void init(View view) {
        context = getActivity();
        btn_login = view.findViewById(R.id.btn_login);
        btn_register = view.findViewById(R.id.btn_register);

    }

    private void setEventListner() {
        btn_login.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:

                break;

        }
    }
}
