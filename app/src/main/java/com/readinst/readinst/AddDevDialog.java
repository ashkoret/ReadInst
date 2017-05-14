package com.readinst.readinst;

/**
 * Created by dan on 14-05-17.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;



public class AddDevDialog extends DialogFragment
{

   private String mDevID;
   private TextView simpleDevID;

   public AddDevDialog(){}
    public interface AddDevDialogListener
    {
        void onDialogPositiveClick(String DevID, String simpleID);

    }

    AddDevDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (AddDevDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    static AddDevDialog newInstance(String DevID) {
        AddDevDialog f = new AddDevDialog();
        Bundle args = new Bundle();
        args.putString("DevID", DevID);
        f.setArguments(args);

        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //super.onCreate(savedInstanceState);
        mDevID = getArguments().getString("DevID");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        final View dialogView = inflater.inflate(R.layout.dialog_assign_dev_simplename, null);
        simpleDevID = (TextView) dialogView.findViewById(R.id.dev_simplename);
        final TextView tv = (TextView) dialogView.findViewById(R.id.devname);
        builder.setView(dialogView)
                 // Add action buttons
                .setPositiveButton(R.string.add_dev, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogPositiveClick(tv.getText().toString(), simpleDevID.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel_dev, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddDevDialog.this.getDialog().cancel();
                    }
                });
        tv.setText(mDevID.split("\\s")[0]);
        tv.setTextColor(Color.BLACK);
        return builder.create();
    }


}
