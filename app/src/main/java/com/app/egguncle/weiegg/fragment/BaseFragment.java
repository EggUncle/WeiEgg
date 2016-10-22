package com.app.egguncle.weiegg.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.app.egguncle.weiegg.R;

/**
 * Created by egguncle on 16.10.10.
 */
public class BaseFragment extends Fragment {
    @Override
    public void startActivity(Intent intent) {
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.anim_in_right_left, R.anim.anim_out_right_left);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        getActivity().startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(R.anim.anim_in_left_right, R.anim.anim_out_left_right);
    }
}
