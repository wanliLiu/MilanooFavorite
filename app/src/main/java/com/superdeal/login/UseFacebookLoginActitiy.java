package com.superdeal.login;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Locale;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.InjectView;
import butterknife.OnClick;

import com.MaterialsDesign.Ldialogs.CustomDialog;
import com.MaterialsDesign.Ldialogs.CustomDialog.ClickListener;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.superdeal.R;
import com.superdeal.base.BaseActivity;
import com.superdeal.data.ApiCallBack;
import com.superdeal.data.ApiHelper;
import com.superdeal.data.ApiParams;
import com.superdeal.data.Result;
import com.superdeal.util.UserUtil;

public class UseFacebookLoginActitiy extends BaseActivity {

    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;

    @InjectView(R.id.title)
    TextView mTitle;

    @InjectView(R.id.loging)
    ProgressBar mloading;

    @InjectView(R.id.button_accept)
    Button mbtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logOut();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        faceboolLoginOkay();
                    }

                    @Override
                    public void onCancel() {
                        ShowLoginFailureDialog();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        ShowLoginFailureDialog();
                    }
                });

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                faceboolLoginOkay();
            }
        };

    }

    /**
     *
     */
    private void ShowLoginFailureDialog() {
        CustomDialog.Builder builder = new CustomDialog.Builder(ctx, R.string.login_failure, R.string.Again);
        builder.content(getResources().getString(R.string.login_failure_attention));
        builder.positiveColor(getResources().getColor(R.color.color_blue));
        builder.negativeText(R.string.Cancel);
        builder.darkTheme(false);

        CustomDialog customDialog = builder.build();
        customDialog.setClickListener(new ClickListener() {

            @Override
            public void onConfirmClick() {
                loginFackbook();
            }

            @Override
            public void onCancelClick() {
                onBackPressed();
            }
        });
        customDialog.setCancelable(false);
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.show();
    }

    @Override
    protected void getLayoutView() {
        setContentView(R.layout.login_activity);
    }

    @Override
    protected void initView() {

        Typeface ty = Typeface.createFromAsset(getAssets(), "Mission-Script.otf");

        mTitle.setTypeface(ty);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }

    /**
     * fackebook登陆
     */
    @OnClick(R.id.button_accept)
    public void gotoLoginFacebook(View view) {
        mbtnLogin.setEnabled(false);
        loginFackbook();
    }

    private void loginFackbook() {
        LoginManager.getInstance().logInWithReadPermissions(UseFacebookLoginActitiy.this, Arrays.asList("public_profile", "email"));
    }

    /**
     * facebool login success
     */
    private void faceboolLoginOkay() {
        boolean enableButtons = AccessToken.getCurrentAccessToken() != null;
        Profile profile = Profile.getCurrentProfile();

        if (enableButtons && profile != null) {
            loginInOurWay(profile);
        } else {

        }
    }

    /**
     * @param profile
     */
    private void loginInOurWay(final Profile profile) {
        mloading.setVisibility(View.VISIBLE);

        ApiParams params = new ApiParams();
        params.put("module", "user");
        params.put("a", "save_user");
        params.put("uid", profile.getId());
        params.put("email", "");
        params.put("name", URLEncoder.encode(profile.getName()));
        params.put("langcode", Locale.getDefault().getLanguage());

        ApiHelper.get(ctx, "", params, new ApiCallBack() {

            @Override
            public void receive(Result result) {

                mloading.setVisibility(View.GONE);

                if (result.isSuccess()) {
                    UserUtil.saveUserInfo(ctx, profile.getId(), profile.getName(), "");
                    MyToast(R.string.loading_success);
                    setResult(RESULT_OK);
                }
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        profileTracker.stopTracking();

        LoginManager.getInstance().logOut();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
