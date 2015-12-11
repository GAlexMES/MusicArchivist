package de.brennecke.musicarchivst.dialogs;


import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.gc.materialdesign.views.Button;

import de.brennecke.musicarchivst.R;
import de.brennecke.musicarchivst.activities.EditAlbumDataActivity;

/**
 * Created by Alexander on 27.10.2015.
 */
public class TypeBarcodeDialog extends DialogFragment implements TextView.OnEditorActionListener, View.OnClickListener {
    private EditText mEditText;

    // Empty constructor required for DialogFragment
    public TypeBarcodeDialog() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_type_barcode, container);

        mEditText = (EditText) view.findViewById(R.id.barcode_input);
        mEditText.setOnEditorActionListener(this);
        mEditText.requestFocus();

        ((Button)view.findViewById(R.id.cancleButton)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.acceptButton)).setOnClickListener(this);

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setTitle(R.string.barcode_type_dialog_title);

        Spinner spinner = (Spinner) view.findViewById(R.id.format_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.barcode_formats, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        return view;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        this.dismiss();
        return true;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.acceptButton){
            String contents = mEditText.getText().toString();
            Intent intentMain = new Intent(getActivity(), EditAlbumDataActivity.class);
            intentMain.putExtra("BARCODE", contents);
            startActivity(intentMain);
            this.dismiss();
        }
        if(v.getId() ==  R.id.cancleButton){
            this.dismiss();
        }
    }
}
