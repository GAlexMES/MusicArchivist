package de.brennecke.musicarchivst.dialogs;


import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gc.materialdesign.views.Button;

import de.brennecke.musicarchivst.R;

/**
 * Created by Alexander on 27.10.2015.
 */
public class SimpleDialog extends DialogFragment implements View.OnClickListener{

    private String title,text;
    private Activity activity;

    // Empty constructor required for DialogFragment
    public SimpleDialog() {

    }

    public void setActivity(Activity a){
        activity = a;
    }

    public void setTitle(int title)throws Exception {
        try {
            this.title = activity.getResources().getString(title);
        }
        catch (NullPointerException npee){
            throw new Exception("Please use the setActivity() function to set the Activity. Or set the title directly as String with the setTitle(String title) function.");
        }
    }

    public void setText(int text)throws Exception{
        try {
            this.text = activity.getResources().getString(text);
        }
        catch (NullPointerException npee){
            throw new Exception("Please use the setActivity() function to set the Activity. Or set the displayed Message directly as String with the setText(String text) function.");
        }
    }


    public void setTitle(String title){
        this.title=title;
    }

    public void setText(String text){
        this.text=text;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_simple, container);

        ((Button)view.findViewById(R.id.cancleButton)).setOnClickListener(this);
        TextView textView = ((TextView)view.findViewById(R.id.simple_dialog_text));
        textView.setText(text);
        getDialog().setTitle(title);

        return view;
    }


    @Override
    public void onClick(View v) {
        if(v.getId() ==  R.id.cancleButton){
            this.dismiss();
        }
    }
}
