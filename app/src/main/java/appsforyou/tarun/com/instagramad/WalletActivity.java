package appsforyou.tarun.com.instagramad;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WalletActivity extends AppCompatActivity implements RewardedVideoAdListener {
    private static final String TAG = "WalletActivity";
    ImageView backButton;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    InterstitialAd mInterstitialAd1, mInterstitialAd2, mInterstitialAd3, mInterstitialAd4;
    CardView cardVedio, cardBrowse;
    private RewardedVideoAd mRewardedVideoAd1, mRewardedVideoAd2;
    ImageView bonus;
    AdView adView1, adView2, adView3;
    AdRequest adRequest;
    int vedioCount = 0;
    int user_vedio_count = 0;
    FirebaseAuth mAuth;
    String currentDate;
    String lastDate;
    int user_total_earning = 0;
    TextView user_total_coinstextview;
    int vedio_coins = 0;
    Button redeem_Coins_Button;
    int maximum_redeem_coins;
    int status_of_user = 0;//non_verified
    Dialog redeemDialog;
    TextView dialogboxpaytmnumber, dialogbuttonredeem;
    ProgressBar progressBar;
    int previous_redeem_coins;
    int redeem_coins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        MobileAds.initialize(this, "ca-app-pub-6073850005981754~3835330398");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        adView1 = (AdView) findViewById(R.id.banneradViewwalletbottom);
        adView2 = (AdView) findViewById(R.id.banneradViewwallettop);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);
        redeem_Coins_Button = (Button) findViewById(R.id.buttonRedeem);
        user_total_coinstextview = (TextView) findViewById(R.id.user_total_coins);
        adRequest = new AdRequest.Builder().build();
        adView1.loadAd(adRequest);
        adRequest = new AdRequest.Builder().build();
        adView2.loadAd(adRequest);

        mInterstitialAd1 = new InterstitialAd(this);
        mInterstitialAd1.setAdUnitId("ca-app-pub-6073850005981754/9035971590");
        mInterstitialAd1.loadAd(new AdRequest.Builder().build());
        mInterstitialAd2 = new InterstitialAd(this);
        mInterstitialAd2.setAdUnitId("ca-app-pub-6073850005981754/2926547179");
        mInterstitialAd2.loadAd(new AdRequest.Builder().build());
        mInterstitialAd3 = new InterstitialAd(this);
        mInterstitialAd3.setAdUnitId("ca-app-pub-6073850005981754/6207316635");
        mInterstitialAd3.loadAd(new AdRequest.Builder().build());
        mInterstitialAd4 = new InterstitialAd(this);
        mInterstitialAd4.setAdUnitId("ca-app-pub-6073850005981754/5484836870");
        mInterstitialAd4.loadAd(new AdRequest.Builder().build());
        mRewardedVideoAd1 = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd1.setRewardedVideoAdListener(WalletActivity.this);
        mRewardedVideoAd2 = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd2.setRewardedVideoAdListener(WalletActivity.this);
        loadRewardedVideoAd1();
        loadRewardedVideoAd2();
        backButton = (ImageView) findViewById(R.id.imageViewbackbutton);
        bonus = (ImageView) findViewById(R.id.imageviewbonus);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd1.isLoaded()) {
                    mInterstitialAd1.show();
                }
                Intent intent = new Intent(WalletActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
        redeemDialog = new Dialog(WalletActivity.this);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.VISIBLE);
                if (dataSnapshot.child("User_Detail").child(mAuth.getCurrentUser().getUid()).hasChild("Total_Coins")) {
                    user_total_earning = Integer.valueOf(dataSnapshot.child("User_Detail")
                            .child(mAuth.getCurrentUser().getUid()).child("Total_Coins").getValue().toString());
                    user_total_coinstextview.setText(String.valueOf(user_total_earning));

                } else {
                    user_total_earning = 0;
                    databaseReference.child("User_Detail").child(mAuth.getCurrentUser().getUid()).child("Total_Coins").setValue(user_total_earning);
                    user_total_coinstextview.setText(String.valueOf(user_total_earning));
                }
                if (dataSnapshot.child("User_Detail").child(mAuth.getCurrentUser().getUid()).hasChild("Redeem_Request_Coins")) {
                    previous_redeem_coins= Integer.valueOf(dataSnapshot.child("User_Detail")
                            .child(mAuth.getCurrentUser().getUid()).child("Redeem_Request_Coins").getValue().toString());

                } else {
                    previous_redeem_coins=0;

                    databaseReference.child("User_Detail").child(mAuth.getCurrentUser().getUid()).child("Redeem_Request_Coins").setValue(previous_redeem_coins);
                }
                if (dataSnapshot.child("Detail").hasChild("Maximum_Redeem_Coins")) {

                    maximum_redeem_coins = Integer.valueOf(dataSnapshot.child("Detail").child("Maximum_Redeem_Coins").getValue().toString());

                } else {
                    maximum_redeem_coins = 1000;
                    databaseReference.child("Detail").child("Maximum_Redeem_Coins").setValue(maximum_redeem_coins);


                }
                if (dataSnapshot.child("User_Detail").child(mAuth.getCurrentUser().getUid()).hasChild("Status_Verified_Non_Verified")) {
                    status_of_user = Integer.valueOf(
                            dataSnapshot.child("User_Detail").child
                                    (mAuth.getCurrentUser().getUid()).child("Status_Verified_Non_Verified").getValue().toString());

                } else {
                    status_of_user = 0;
                    databaseReference.child("User_Detail").
                            child(mAuth.getCurrentUser().getUid()).child("Status_Verified_Non_Verified").setValue(status_of_user);

                }


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);

            }

        });
        redeem_Coins_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.GONE);

               redeem_coins = Integer.valueOf(user_total_earning);
                if (redeem_coins >= maximum_redeem_coins) {
                    if (status_of_user == 1) {
                        redeemDialog.setContentView(R.layout.redeem_layout);
                        dialogboxpaytmnumber = (EditText) redeemDialog.findViewById(R.id.editTextpytmnumber);
                        dialogbuttonredeem = (Button) redeemDialog.findViewById(R.id.buttonwithredeem);
                        dialogbuttonredeem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (dialogboxpaytmnumber.getText().equals("")) {
                                    Toast.makeText(WalletActivity.this, "Enter Valid Mobile Number", Toast.LENGTH_LONG).show();
                                } else {
                                    databaseReference.child("User_Detail").
                                            child(mAuth.getCurrentUser().getUid()).child("PaytmNumber").setValue(dialogboxpaytmnumber.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            redeemDialog.dismiss();
                                            Toast.makeText(WalletActivity.this,"You Redeem Request is Done.Payment is done within 24 hours.",Toast.LENGTH_LONG).show();
                                            final AlertDialog alertDialog = new AlertDialog.Builder(WalletActivity.this).create(); //ead Update
                                            alertDialog.setTitle("Redeem Status");
                                            alertDialog.setMessage("You Redeem Request is Done , we are amount on your paytm number");
                                            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    alertDialog.cancel();

                                                }
                                            });
                                        }
                                    });
                                    redeem_coins=Integer.valueOf(user_total_earning);
                                    user_total_earning=user_total_earning-redeem_coins;
                                    databaseReference.child("User_Detail")
                                            .child(mAuth.getCurrentUser().getUid()).child("Total_Coins").setValue(user_total_earning);
                                    databaseReference.child("User_Detail")
                                            .child(mAuth.getCurrentUser().getUid()).child("Redeem_Request_Coins").setValue(redeem_coins+previous_redeem_coins);

                                    databaseReference.child("User_Detail").
                                            child(mAuth.getCurrentUser().getUid()).child("PaytmNumber").setValue(dialogboxpaytmnumber.getText().toString());




                                }

                            }

                        });
                        redeemDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        redeemDialog.show();


                    } else if (status_of_user == 0) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(WalletActivity.this).create(); //Read Update
                        alertDialog.setTitle("Redeem Status");
                        alertDialog.setMessage("You are not Verified User, Please Verify your account. ," +
                                "For verify your account Contact us " +
                                "Mobile Number -- 9261616155");
                        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.cancel();

                            }
                        });
                        alertDialog.show();

                    } else {
                        Toast.makeText(WalletActivity.this, "SomethingWrong", Toast.LENGTH_LONG).show();
                    }

                } else {
                    final AlertDialog alertDialog = new AlertDialog.Builder(WalletActivity.this).create(); //Read Update
                    alertDialog.setTitle("Redeem Status");
                    alertDialog.setMessage("Maximum Redeem point is" +
                            ""+maximum_redeem_coins + ", You are not eligiable currently. Browse and Watch Vedio to earn more more");
                    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.cancel();

                        }
                    });
                    alertDialog.show();
                }


            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.VISIBLE);
                if (dataSnapshot.child("Detail").hasChild("Daily_Vedio_Count")) {

                    vedioCount = Integer.valueOf(dataSnapshot.child("Detail").child("Daily_Vedio_Count").getValue().toString());

                } else {
                    vedioCount = 5;
                    databaseReference.child("Detail").child("Daily_Vedio_Count").setValue(5);


                }
                if (dataSnapshot.child("Detail").hasChild("Daily_Vedio_Coins")) {

                    vedio_coins = Integer.valueOf(dataSnapshot.child("Detail").child("Daily_Vedio_Coins").getValue().toString());

                } else {
                    vedio_coins = 5;
                    databaseReference.child("Detail").child("Daily_Vedio_Coins").setValue(5);


                }
                if (dataSnapshot.child("Detail").hasChild("Maximum_Redeem_Coins")) {

                    maximum_redeem_coins = Integer.valueOf(dataSnapshot.child("Detail").child("Maximum_Redeem_Coins").getValue().toString());

                } else{
                    maximum_redeem_coins = 1000;
                    databaseReference.child("Detail").child("Maximum_Redeem_Coins").setValue(maximum_redeem_coins);


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);

            }
        });


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.VISIBLE);
                if (dataSnapshot.child("User_Detail").child(mAuth.getCurrentUser().getUid()).hasChild("Last_Login_Date")) {
                    lastDate = dataSnapshot.child("User_Detail").child(mAuth.getCurrentUser().getUid()).child("Last_Login_Date").getValue().toString();

                } else {

                    databaseReference.child("User_Detail").child(mAuth.getCurrentUser().getUid()).child("Last_Login_Date").setValue(getCurrentTimeUsingCalendar());
                    lastDate = getCurrentTimeUsingCalendar();
                }
                if (dataSnapshot.child("User_Detail").child(mAuth.getCurrentUser().getUid()).hasChild("Total_Coins")) {
                    user_total_earning = Integer.valueOf(dataSnapshot.child("User_Detail")
                            .child(mAuth.getCurrentUser().getUid()).child("Total_Coins").getValue().toString());
                    user_total_coinstextview.setText(String.valueOf(user_total_earning));

                } else {
                    user_total_earning = 0;
                    databaseReference.child("User_Detail").child(mAuth.getCurrentUser().getUid()).child("Total_Coins").setValue(user_total_earning);
                    user_total_coinstextview.setText(String.valueOf(user_total_earning));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);

            }
        });


        cardVedio = (CardView) findViewById(R.id.card1);
        cardVedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate = getCurrentTimeUsingCalendar();
                if (currentDate.equals(lastDate)) {
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child("User_Detail").child(mAuth.getCurrentUser().getUid()).hasChild("Vedio_Count")) {
                                user_vedio_count = Integer.valueOf(dataSnapshot.child("User_Detail")
                                        .child(mAuth.getCurrentUser().getUid()).child("Vedio_Count").getValue().toString());


                            } else {
                                user_vedio_count = Integer.valueOf(dataSnapshot.child("Detail").child("Daily_Vedio_Count").getValue().toString());
                                databaseReference.child("User_Detail").child(mAuth.getCurrentUser().getUid()).child("Vedio_Count").setValue(user_vedio_count);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    if (mRewardedVideoAd2.isLoaded()) {
                        mRewardedVideoAd2.show();
                    } else {
                        loadRewardedVideoAd2();
                        mRewardedVideoAd2.show();
                    }

                } else {
                    user_vedio_count = vedioCount;
                    databaseReference.child("User_Detail").child(mAuth.getCurrentUser().getUid()).child("Vedio_Count").setValue(user_vedio_count);
                    databaseReference.child("User_Detail").child(mAuth.getCurrentUser().getUid()).child("Last_Login_Date").setValue(getCurrentTimeUsingCalendar());


                }

//                if (mInterstitialAd2.isLoaded()) {
//                    mInterstitialAd2.show();
//
//
//                }
//                else {
//                    mInterstitialAd2.loadAd(new AdRequest.Builder().build());
//
//                }


            }
        });
        cardBrowse = (CardView) findViewById(R.id.card2);
        cardBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mInterstitialAd2.isLoaded()) {
                    mInterstitialAd2.show();
                    Intent intent = new Intent(WalletActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    if (mRewardedVideoAd1.isLoaded()) {
                        mRewardedVideoAd1.show();
                    } else {
                        loadRewardedVideoAd1();
                        mRewardedVideoAd1.show();
                    }
                    mInterstitialAd2.loadAd(new AdRequest.Builder().build());
                    Intent intent = new Intent(WalletActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }


            }
        });

        bonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRewardedVideoAd1.isLoaded()) {
                    mRewardedVideoAd1.show();
                } else {
                    loadRewardedVideoAd1();
                    mRewardedVideoAd1.show();
                }

            }
        });
        adView1.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (mInterstitialAd4.isLoaded()) {
                    mInterstitialAd4.show();
                }
                adRequest = new AdRequest.Builder().build();
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                if (mInterstitialAd4.isLoaded()) {
                    mInterstitialAd4.show();
                }
                adRequest = new AdRequest.Builder().build();

                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                if (mInterstitialAd4.isLoaded()) {
                    mInterstitialAd4.show();
                }
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                if (mInterstitialAd4.isLoaded()) {
                    mInterstitialAd4.show();
                }
                adRequest = new AdRequest.Builder().build();

                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                if (mInterstitialAd4.isLoaded()) {
                    mInterstitialAd4.show();
                }
                adRequest = new AdRequest.Builder().build();

                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });
        adView2.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (mInterstitialAd2.isLoaded()) {
                    mInterstitialAd2.show();
                }
                adRequest = new AdRequest.Builder().build();
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                if (mInterstitialAd2.isLoaded()) {
                    mInterstitialAd2.show();
                }
                adRequest = new AdRequest.Builder().build();

                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                if (mInterstitialAd2.isLoaded()) {
                    mInterstitialAd2.show();
                }
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                if (mInterstitialAd2.isLoaded()) {
                    mInterstitialAd2.show();
                }
                adRequest = new AdRequest.Builder().build();

                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                if (mInterstitialAd3.isLoaded()) {
                    mInterstitialAd3.show();
                }
                adRequest = new AdRequest.Builder().build();

                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });
        mInterstitialAd1.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {

                mInterstitialAd1.loadAd(new AdRequest.Builder().build());
                // Code to be executed when an ad finishes loading.
            }


            @Override
            public void onAdFailedToLoad(int errorCode) {

                mInterstitialAd1.loadAd(new AdRequest.Builder().build());

                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {

                // mInterstitialAd.loadAd(new AdRequest.Builder().build());

                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {

                mInterstitialAd1.loadAd(new AdRequest.Builder().build());

                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {

                mInterstitialAd1.loadAd(new AdRequest.Builder().build());

                // Code to be executed when when the interstitial ad is closed.
            }
        });
        mInterstitialAd2.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {

                mInterstitialAd2.loadAd(new AdRequest.Builder().build());
                // Code to be executed when an ad finishes loading.
            }


            @Override
            public void onAdFailedToLoad(int errorCode) {
                mInterstitialAd2.loadAd(new AdRequest.Builder().build());

                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // mInterstitialAd.loadAd(new AdRequest.Builder().build());

                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                mInterstitialAd2.loadAd(new AdRequest.Builder().build());

                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                mInterstitialAd2.loadAd(new AdRequest.Builder().build());

                // Code to be executed when when the interstitial ad is closed.
            }
        });
        mInterstitialAd3.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mInterstitialAd3.loadAd(new AdRequest.Builder().build());
                // Code to be executed when an ad finishes loading.
            }


            @Override
            public void onAdFailedToLoad(int errorCode) {
                mInterstitialAd3.loadAd(new AdRequest.Builder().build());

                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // mInterstitialAd.loadAd(new AdRequest.Builder().build());

                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                mInterstitialAd3.loadAd(new AdRequest.Builder().build());

                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {

                mInterstitialAd3.loadAd(new AdRequest.Builder().build());

                // Code to be executed when when the interstitial ad is closed.
            }
        });
        mInterstitialAd4.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mInterstitialAd4.loadAd(new AdRequest.Builder().build());
                // Code to be executed when an ad finishes loading.
            }


            @Override
            public void onAdFailedToLoad(int errorCode) {
                mInterstitialAd4.loadAd(new AdRequest.Builder().build());

                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // mInterstitialAd.loadAd(new AdRequest.Builder().build());

                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                mInterstitialAd4.loadAd(new AdRequest.Builder().build());

                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                mInterstitialAd4.loadAd(new AdRequest.Builder().build());

                // Code to be executed when when the interstitial ad is closed.
            }
        });


    }

    public void getfixVedioCount() {


    }


    private void loadRewardedVideoAd1() {
        mRewardedVideoAd1.loadAd("ca-app-pub-6073850005981754/5012966100",
                new AdRequest.Builder().build());
    }

    private void loadRewardedVideoAd2() {
        mRewardedVideoAd2.loadAd("ca-app-pub-6073850005981754/4305694565",
                new AdRequest.Builder().build());
    }


    @Override
    public void onRewarded(RewardItem reward) {
//        loadRewardedVideoAd1();
//        loadRewardedVideoAd2();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("User_Detail").child(mAuth.getCurrentUser().getUid()).hasChild("Vedio_Count")) {
                    user_vedio_count = Integer.valueOf(dataSnapshot.child("User_Detail")
                            .child(mAuth.getCurrentUser().getUid()).child("Vedio_Count").getValue().toString());


                } else {
                    user_vedio_count = Integer.valueOf(dataSnapshot.child("Detail").child("Daily_Vedio_Count").getValue().toString());
                    databaseReference.child("User_Detail").child(mAuth.getCurrentUser().getUid()).child("Vedio_Count").setValue(user_vedio_count);
                }
                if (dataSnapshot.child("User_Detail").child(mAuth.getCurrentUser().getUid()).hasChild("Total_Coins")) {
                    user_total_earning = Integer.valueOf(dataSnapshot.child("User_Detail")
                            .child(mAuth.getCurrentUser().getUid()).child("Total_Coins").getValue().toString());

                } else {
                    user_total_earning = 0;
                    databaseReference.child("User_Detail").child(mAuth.getCurrentUser().getUid()).child("Total_Coins").setValue(user_total_earning);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Detail").hasChild("Daily_Vedio_Count")) {

                    vedioCount = Integer.valueOf(dataSnapshot.child("Detail").child("Daily_Vedio_Count").getValue().toString());

                } else {
                    vedioCount = 5;
                    databaseReference.child("Detail").child("Daily_Vedio_Count").setValue(5);


                }
                if (dataSnapshot.child("Detail").hasChild("Daily_Vedio_Coins")) {

                    vedio_coins = Integer.valueOf(dataSnapshot.child("Detail").child("Daily_Vedio_Coins").getValue().toString());

                } else {
                    vedio_coins = 5;
                    databaseReference.child("Detail").child("Daily_Vedio_Coins").setValue(5);


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (user_vedio_count <= 0) {
            Toast.makeText(this, "Your Today Vedio Earning is Out of Limit", Toast.LENGTH_SHORT).show();

        } else if (user_vedio_count <= vedioCount) {


            user_vedio_count = user_vedio_count - 1;
            user_total_earning = user_total_earning + vedio_coins;
            databaseReference.child("User_Detail").child(mAuth.getCurrentUser().getUid()).child("Total_Coins").setValue(user_total_earning);
            databaseReference.child("User_Detail").child(mAuth.getCurrentUser().getUid()).child("Vedio_Count").setValue(user_vedio_count);
            user_total_coinstextview.setText(String.valueOf(user_total_earning));

            Toast.makeText(this,"You get "+vedioCount+"Coins", Toast.LENGTH_SHORT).show();


        }
        // Reward the user.
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
//        loadRewardedVideoAd1();
//        loadRewardedVideoAd2();

//        Toast.makeText(this, "onRewardedVideoAdLeftApplication",
//                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
//        loadRewardedVideoAd1();
//        loadRewardedVideoAd2();
        Toast.makeText(this, "VideoAdClosed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
//        loadRewardedVideoAd1();
//        loadRewardedVideoAd2();
        Log.d(TAG, "onRewardedVideoAdFailedToLoad: "+errorCode);
        Toast.makeText(this, "VideoAd not found", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdLoaded() {
//        loadRewardedVideoAd1();
//        loadRewardedVideoAd2();
        Toast.makeText(this, "VideoAdLoaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
//        loadRewardedVideoAd1();
//        loadRewardedVideoAd2();
        Toast.makeText(this, "VideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
//        loadRewardedVideoAd1();
//        loadRewardedVideoAd2();
        Toast.makeText(this, "VideoStarted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoCompleted() {
//        loadRewardedVideoAd1();
//        loadRewardedVideoAd2();
        Toast.makeText(this, "VideoCompleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        mRewardedVideoAd1.resume(this);
        mRewardedVideoAd2.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardedVideoAd1.resume(this);
        mRewardedVideoAd2.resume(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd1.resume(this);
        mRewardedVideoAd2.resume(this);
        super.onDestroy();
    }

    public String getCurrentTimeUsingCalendar() {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = dateFormat.format(date);
        return formattedDate;
    }

    public void comparedate(String currentD, String lastD) throws ParseException {


    }

}
