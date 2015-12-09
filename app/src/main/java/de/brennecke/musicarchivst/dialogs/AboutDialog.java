package de.brennecke.musicarchivst.dialogs;


import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gc.materialdesign.views.Button;

import de.brennecke.musicarchivst.R;

/**
 * Created by Alexander on 27.10.2015.
 */
public class AboutDialog extends DialogFragment implements View.OnClickListener{

    // Empty constructor required for DialogFragment
    public AboutDialog() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_about, container);

        ((Button)view.findViewById(R.id.cancleButton)).setOnClickListener(this);

        getDialog().setTitle(R.string.dialog_about_title);

        return view;
    }


    @Override
    public void onClick(View v) {
        if(v.getId() ==  R.id.cancleButton){
            this.dismiss();
        }
    }
}
