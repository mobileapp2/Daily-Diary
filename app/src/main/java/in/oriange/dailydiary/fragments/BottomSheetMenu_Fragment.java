package in.oriange.dailydiary.fragments;

import android.app.Dialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.view.View;

import in.oriange.dailydiary.R;

public class BottomSheetMenu_Fragment extends BottomSheetDialogFragment {

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;

    }

    @Override
    public void setupDialog(Dialog dialog, int style) {

        //Set the custom view
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_bottomsheet_menu, null);
        dialog.setContentView(view);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) view.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
    }


}
