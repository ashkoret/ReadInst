package com.readinst.readinst;

/**
 * Created by dan on 14-05-17.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;


public class AddDevDialog extends DialogFragment
{

/*    String mDevID;

    static AddDevDialog newInstance(String DevID) {
        AddDevDialog f = new AddDevDialog();
        Bundle args = new Bundle();
        args.putString("DevID", DevID);
        f.setArguments(args);

        return f;
    }*/

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        /*super.onCreate(savedInstanceState);
        mDevID = getArguments().getString("DeID");*/

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_assign_dev_simplename, null))
                 // Add action buttons
                .setPositiveButton(R.string.add_dev, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                })
                .setNegativeButton(R.string.cancel_dev, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddDevDialog.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

}
