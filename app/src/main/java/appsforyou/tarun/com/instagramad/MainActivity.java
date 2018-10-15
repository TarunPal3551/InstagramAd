package appsforyou.tarun.com.instagramad;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    WebView mWebview;
    private static final String TAG = "MainActivity";
    ImageView gotoWalllet;
    SwipeRefreshLayout refreshLayout;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    AdView adView;
    AdRequest adRequest;
    private InterstitialAd mInterstitialAd1,mInterstitialAd2,mInterstitialAd3,mInterstitialAd4;
    int user_total_earning;
    int browsingPointOnScroling;
    ImageView optionImages;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
        mAuth=FirebaseAuth.getInstance();
        mWebview = (WebView) findViewById(R.id.webView);
        MobileAds.initialize(this,"ca-app-pub-6073850005981754~3835330398");
        adView=(AdView)findViewById(R.id.banneradView);
        adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);
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


        refreshLayout=(SwipeRefreshLayout)findViewById(R.id.swiperefreshlayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
                mWebview.reload();
                if (mInterstitialAd1.isLoaded()){
                    mInterstitialAd1.show();
                }
                else {
                    mInterstitialAd1.loadAd(new AdRequest.Builder().build());
                }
            }
        });
        gotoWalllet=(ImageView)findViewById(R.id.imageViewwallet);
        gotoWalllet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd2.isLoaded()){
                    mInterstitialAd2.show();
                }
                else {
                    mInterstitialAd2.loadAd(new AdRequest.Builder().build());
                }
                Intent intent=new Intent(MainActivity.this,WalletActivity.class);
                startActivity(intent);

            }
        });
        WebSettings webSettings = mWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);// enable javascript
        webSettings.getLoadsImagesAutomatically();
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        webSettings.getMediaPlaybackRequiresUserGesture();
        webSettings.setPluginState(WebSettings.PluginState.ON_DEMAND);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("User_Detail").child(mAuth.getCurrentUser().getUid()).hasChild("Total_Coins")) {
                    user_total_earning = Integer.valueOf(dataSnapshot.child("User_Detail")
                            .child(mAuth.getCurrentUser().getUid()).child("Total_Coins").getValue().toString());

                } else {
                    user_total_earning = 0;
                    databaseReference.child("User_Detail").child(mAuth.getCurrentUser().getUid()).child("Total_Coins").setValue(user_total_earning);
                }
                if (dataSnapshot.child("Detail").hasChild("BrowsingPointOnScrolling")) {

                    browsingPointOnScroling= Integer.valueOf(dataSnapshot.child("Detail").child("BrowsingPointOnScrolling").getValue().toString());

                } else {
                   browsingPointOnScroling=1;
                    databaseReference.child("Detail").child("BrowsingPointOnScrolling").setValue(browsingPointOnScroling);


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mWebview.setWebViewClient(new WebViewClient() {

            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (mInterstitialAd3.isLoaded()){
                    mInterstitialAd3.show();
                }
                else {
                    mInterstitialAd3.loadAd(new AdRequest.Builder().build());
                }
                Log.d(TAG, "onReceivedError: +"+description);
               // Toast.makeText(MainActivity.this, description, Toast.LENGTH_SHORT).show();
            }

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                if (mInterstitialAd4.isLoaded()){
                    mInterstitialAd4.show();
                }
                // Redirect to deprecated method, so you can use it in all SDK versions

                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());

                refreshLayout.setRefreshing(false);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (mInterstitialAd3.isLoaded()){
                    mInterstitialAd3.show();
                }
                else {
                    mInterstitialAd3.loadAd(new AdRequest.Builder().build());

                }
                Log.d(TAG, "onPageStarted: " + url);
                super.onPageStarted(view, url, favicon);
                refreshLayout.setRefreshing(true);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (mInterstitialAd1.isLoaded()){
                    mInterstitialAd1.show();
                }
                else {
                    mInterstitialAd1.loadAd(new AdRequest.Builder().build());


                }
                super.onPageFinished(view, url);
                refreshLayout.setRefreshing(false);
            }

        });

        mWebview.loadUrl("https://www.instagram.com/accounts/login/");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mWebview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY >= oldScrollY) {
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child("User_Detail").child(mAuth.getCurrentUser().getUid()).hasChild("Total_Coins")) {
                                    user_total_earning = Integer.valueOf(dataSnapshot.child("User_Detail")
                                            .child(mAuth.getCurrentUser().getUid()).child("Total_Coins").getValue().toString());

                                } else {
                                    user_total_earning = 0;
                                    databaseReference.child("User_Detail").child(mAuth.getCurrentUser().getUid()).child("Total_Coins").setValue(user_total_earning);
                                }
                                if (dataSnapshot.child("Detail").hasChild("BrowsingPointOnScrolling")) {

                                    browsingPointOnScroling = Integer.valueOf(dataSnapshot.child("Detail").child("BrowsingPointOnScrolling").getValue().toString());

                                } else {
                                    browsingPointOnScroling = 1;
                                    databaseReference.child("Detail").child("BrowsingPointOnScrolling").setValue(browsingPointOnScroling);


                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        user_total_earning = user_total_earning + browsingPointOnScroling;
                        databaseReference.child("User_Detail")
                                .child(mAuth.getCurrentUser().getUid()).child("Total_Coins").setValue(user_total_earning);


                    }

                    }

            });
        }

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (mInterstitialAd1.isLoaded()){
                    mInterstitialAd1.show();
                }
                else {
                    mInterstitialAd1.loadAd(new AdRequest.Builder().build());


                }
                adRequest=new AdRequest.Builder().build();
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                if (mInterstitialAd2.isLoaded()){
                    mInterstitialAd2.show();
                }
                else {
                    mInterstitialAd2.loadAd(new AdRequest.Builder().build());


                }
                adRequest=new AdRequest.Builder().build();

                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                if (mInterstitialAd3.isLoaded()){
                    mInterstitialAd3.show();
                }
                else {
                    mInterstitialAd3.loadAd(new AdRequest.Builder().build());


                }
                adRequest=new AdRequest.Builder().build();
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                if (mInterstitialAd4.isLoaded()){
                    mInterstitialAd4.show();
                }
                else {
                    mInterstitialAd4.loadAd(new AdRequest.Builder().build());


                }
                adRequest=new AdRequest.Builder().build();

                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                if (mInterstitialAd1.isLoaded()){
                    mInterstitialAd1.show();
                }
                else {
                    mInterstitialAd1.loadAd(new AdRequest.Builder().build());


                }
                adRequest=new AdRequest.Builder().build();

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

    @Override
    public void onBackPressed() {
        if (mWebview.canGoBack()){
            mWebview.goBack();

        }
        else {

            super.onBackPressed();

        }

    }

}
