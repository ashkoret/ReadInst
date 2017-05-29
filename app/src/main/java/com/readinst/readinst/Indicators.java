package com.readinst.readinst;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.support.constraint.ConstraintSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.readinst.barcode.BarcodeCaptureActivity;
import com.readinst.dbconnector.DBconnect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Indicators extends AppCompatActivity implements AddDevDialog.AddDevDialogListener
{
    private static final String LOG_TAG = Indicators.class.getSimpleName();
    private static final int BARCODE_READER_REQUEST_CODE = 1;
    static int totalEditTexts = 0;
    LinearLayout IndicatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicators);

        // TODO Here we read DEV file
        try {
            File file = new File(this.getFilesDir(), AppConfig.DEV_FILE);
            // Check file exists and read DEVs from file
            if (file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String ReadString;
                while (((ReadString = br.readLine()) != null)) {
                   String[] RS = ReadString.split("-");
                }
                br.close();
            }
        }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        //Here we read DEVs from DB
        HashMap<String, String> UserDevs;
        LinkedHashMap<String, String> DevIndicators;
        DBconnect db_users = new DBconnect();
        UserDevs = db_users.readUser(AppConfig.UserEmail, AppConfig.TABLE_USER_DEVS);
        ArrayList<String> DevList = new ArrayList<>(UserDevs.values());
        ArrayList<String> DevListNames = new ArrayList<>(UserDevs.keySet());
        ArrayList<ArrayList<String>> Indicators = new ArrayList<>();
        IndicatorLayout = (LinearLayout) findViewById(R.id.indicators);
        for (int i = 0; i<DevList.size(); i++)
        {
            String DeviceID = DevList.get(i);
            DevIndicators = db_users.readDeviceIndicators(DeviceID, AppConfig.TABLE_DEVICES);
            Indicators.add(new ArrayList<>(DevIndicators.values()));
        }

        ConstraintSet set = new ConstraintSet();
        ArrayList<EditText> TextViews = new ArrayList<>();

        for (int i = 0; i<DevList.size(); i++) {
            EditText editTextPC = new EditText(getBaseContext());
            editTextPC.setText(DevListNames.get(i));
            editTextPC.setTextColor(Color.rgb(0,0,152));
            editTextPC.setGravity(Gravity.CENTER);
            editTextPC.setEnabled(false);
            TextViews.add(editTextPC);
            IndicatorLayout.addView(editTextPC);
            for (int j = 0; j < Indicators.get(i).size(); j++)
            {
                String DevName = Indicators.get(i).get(j).replace("\r\n","").replace("\r","").replace("\n","");

                if (!DevName.equals("0"))
                {
                    EditText editText = new EditText(getBaseContext());
                    editText.setEnabled(false);
                    editText.setTag("Indicator" + totalEditTexts);
                    if (j == Math.ceil(j/6)*6)
                    {
                        editText.setTextColor(Color.rgb(152,0,0));
                    }
                    else if ((j-1) == Math.ceil((j-1)/6)*6)
                    {
                        editText.setTextColor(Color.rgb(0,102,0));
                    }
                    else
                    {
                        editText.setTextColor(Color.rgb(64,64,64));
                    }
                    editText.setId(i * 6 + j);
                    editText.setText(DevName);
                    TextViews.add(editText);
                    IndicatorLayout.addView(editText);
                    totalEditTexts++;
                }
            }
        }
        // TODO Merge DEV file and DEV DB
        // TODO Here write the DEV file
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.AddPCScan:
                Toast.makeText(Indicators.this, getString(R.string.addPCscan), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
                startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
                return true;
            case R.id.AddPCLocal:
                Toast.makeText(Indicators.this, getString(R.string.addPCphoto), Toast.LENGTH_SHORT).show();
                try
                {
                    //TODO add function here to open file and read barcode
                    Bitmap myQRCode = BitmapFactory.decodeStream(getAssets().open("myqrcode.jpg"));
                    BarcodeDetector barcodeDetector =
                            new BarcodeDetector.Builder(this)
                                    .setBarcodeFormats(Barcode.QR_CODE)
                                    .build();
                    Frame myFrame = new Frame.Builder()
                            .setBitmap(myQRCode)
                            .build();
                    SparseArray<Barcode> barcodes = barcodeDetector.detect(myFrame);
                    // Check if at least one barcode was detected
                    if(barcodes.size() != 0) {

                        // Print the QR code's message
                        Log.d("My QR Code's Data",
                                barcodes.valueAt(0).displayValue
                        );
                    }
                }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            //TODO end
                return true;

            case R.id.DeletePC:
                Toast.makeText(Indicators.this, getString(R.string.deletePC), Toast.LENGTH_SHORT).show();
                Intent delpc_intent = new Intent(this, DelDevDialog.class);
                startActivity(delpc_intent);
                return true;
            case R.id.Logout:
                Toast.makeText(Indicators.this, getString(R.string.logout), Toast.LENGTH_SHORT).show();
                File file = new File(this.getFilesDir(), AppConfig.USR_FILE);
                if (file.exists())
                {
                  file.delete();
                }
                Intent lintent = new Intent(this, MainActivity.class);
                startActivity(lintent);
                return true;
            case R.id.Exit:
                Toast.makeText(Indicators.this, getString(R.string.exit), Toast.LENGTH_SHORT).show();
                finishAffinity();
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Point[] p = barcode.cornerPoints;
                    Toast.makeText(Indicators.this, barcode.displayValue, Toast.LENGTH_SHORT).show();
                    DialogFragment addDevDialog = AddDevDialog.newInstance(barcode.displayValue);
                    addDevDialog.show(getFragmentManager(), "addDevDialog");
                } else Toast.makeText(Indicators.this, R.string.no_barcode_captured, Toast.LENGTH_SHORT).show();
            } else Log.e(LOG_TAG, String.format(getString(R.string.barcode_error_format),
                    CommonStatusCodes.getStatusCodeString(resultCode)));
        } else super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDialogPositiveClick(String DevID , String simpleID) {

        DBconnect db_devs = new DBconnect();
        db_devs.insertDevice(AppConfig.UserEmail, DevID, simpleID, AppConfig.TABLE_USER_DEVS);

        Intent refresh = new Intent(this, Indicators.class);
        startActivity(refresh);
        this.finish(); //
    }
}
