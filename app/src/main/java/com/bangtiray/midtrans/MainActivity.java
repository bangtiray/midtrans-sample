package com.bangtiray.midtrans;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.*;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.*;

import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements TransactionFinishedCallback, View.OnClickListener {

    private static final String TAG = "transactionresult";
    @BindView(R.id.bankClick)
    PorterShapeImageView imageKlikBank;
    @BindView(R.id.gopayClick)
    PorterShapeImageView imageKlikGopay;
    @BindView(R.id.alfamartClick)
    PorterShapeImageView imageKlikAlfa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        initMid();
        setupCLick();
    }

    private void initMid() {
        SdkUIFlowBuilder.init()
                .setClientKey(Constant.CLIENT_KEY) // client_key is mandatory
                .setContext(this) // context is mandatory
                .setTransactionFinishedCallback(new TransactionFinishedCallback() {
                    @Override
                    public void onTransactionFinished(TransactionResult result) {
                        Log.w(TAG, result.getResponse().getStatusMessage());
                    }
                }) // set transaction finish callback (sdk callback)
                .setMerchantBaseUrl(Constant.BASE_URL) //set merchant url (required)
                .enableLog(true) // enable sdk log (optional)
                .setColorTheme(new CustomColorTheme("#FFE51255", "#B61548", "#FFE51255")) // set theme. it will replace theme on snap theme on MAP ( optional)
                .buildSDK();
    }

    private void setupCLick() {
        imageKlikBank.setOnClickListener(this);
        imageKlikGopay.setOnClickListener(this);
        imageKlikAlfa.setOnClickListener(this);

    }

    @Override
    public void onTransactionFinished(TransactionResult transactionResult) {
        Log.w(TAG, transactionResult.getResponse().getStatusMessage());
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onClick(View v) {
        transactionRequester();
        if (v.getId() == R.id.bankClick) {
            MidtransSDK.getInstance().startPaymentUiFlow(this, PaymentMethod.BANK_TRANSFER);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        } else if (v.getId() == R.id.gopayClick) {
            MidtransSDK.getInstance().startPaymentUiFlow(this, PaymentMethod.GO_PAY);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        } else if (v.getId() == R.id.alfamartClick) {
            MidtransSDK.getInstance().startPaymentUiFlow(this, PaymentMethod.ALFAMART);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
    }

    private void transactionRequester() {
        UserDetail userDetail = LocalDataHandler.readObject("user_details", UserDetail.class);
        if (userDetail == null) {
            userDetail = new UserDetail();
            userDetail.setUserFullName("Ahmad Satiri");
            userDetail.setEmail("bangtiray@gmail.com");
            userDetail.setPhoneNumber("08123456789");
            userDetail.setUserId("bangtiray-6789");

            ArrayList<UserAddress> userAddresses = new ArrayList<>();
            UserAddress userAddress = new UserAddress();
            userAddress.setAddress("Jalan Andalas Gang Sebelah No. 1");
            userAddress.setCity("Jakarta");
            userAddress.setAddressType(com.midtrans.sdk.corekit.core.Constants.ADDRESS_TYPE_BOTH);
            userAddress.setZipcode("12345");
            userAddress.setCountry("IDN");
            userAddresses.add(userAddress);
            userDetail.setUserAddresses(userAddresses);
            LocalDataHandler.saveObject("user_details", userDetail);
        }

        TransactionRequest transactionRequest = new TransactionRequest(System.currentTimeMillis() + "", 35000);
        ItemDetails itemDetails1 = new ItemDetails("BP1", 15000, 1, "Bakso Paket 1");
        ItemDetails itemDetails2 = new ItemDetails("BP2", 20000, 1, "Bakso Paket 1");


        ArrayList<ItemDetails> itemDetailsList = new ArrayList<>();
        itemDetailsList.add(itemDetails1);
        itemDetailsList.add(itemDetails2);

        transactionRequest.setItemDetails(itemDetailsList);
        MidtransSDK.getInstance().setTransactionRequest(transactionRequest);


    }
}
