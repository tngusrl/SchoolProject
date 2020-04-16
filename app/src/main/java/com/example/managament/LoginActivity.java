package com.example.managament;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    String[] permission_list = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    SignInButton Google_Login;
    EditText idText;
    EditText passwordText;
    Button loginButton;
    TextView registerButton;
    String ID,PASSWORD;
    // 자동로그인 getSharedPreferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    // 로그인 getSharedPreferences
    SharedPreferences pref1;
    SharedPreferences.Editor editor1;

    CheckBox autoLogin;
    Boolean loginChecked = false;

    private static final int RC_SIGN_IN = 1000;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;

    @Override

    public void onStart() {

        super.onStart();

        // 활동을 초기화할 때 사용자가 현재 로그인되어 있는지 확인합니다.

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            // 로그인 되어있으니까 로그인창 말고 바로 서비스 화면으로 이동
            // mUsername = currentUser.getDisplayName();
            // if(currentUser.getPhotoUrl()!=null) mPhotoUrl = currentUser.getPhotoUrl().toString();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkPermission();

        autoLogin = findViewById(R.id.checkBox1);

        pref = getSharedPreferences("autologin",0);
        editor = pref.edit();

        pref1 = getSharedPreferences("login",0);
        editor1 = pref1.edit();


        if(pref.getBoolean("Auto_Login_enabled",false)){

            Intent boardlistIntent = new Intent(LoginActivity.this,BoardListActivity.class);
            boardlistIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            boardlistIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(boardlistIntent);

        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        mAuth = FirebaseAuth.getInstance();

//        Google_Login = findViewById(R.id.Google_Login);
//        Google_Login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//                startActivityForResult(signInIntent,RC_SIGN_IN);
//            }
//        });
//
//        TextView textView = (TextView) Google_Login.getChildAt(0);
//        textView.setText("구글 로그인");

//
//
//        Button logout_Button = findViewById(R.id.button3);
//        logout_Button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder alt_bld = new AlertDialog.Builder(v.getContext());
//                alt_bld.setMessage("로그아웃 하시겠습니까?").setCancelable(false)
//                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                signOut();
//                            }
//                        }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//
//                AlertDialog alert = alt_bld.create();
//                alert.setTitle("로그아웃");
//                alert.show();
//            }
//        });



        autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // 자동로그인 체크
                if(isChecked){
                    loginChecked = true;
                }

                // 자동로그인 체크 해제시, editor에 저장되어있는 정보들 삭제
                else{
                    loginChecked = false;
                    editor.clear();
                    editor.commit();
                }
            }
        });




        idText = findViewById(R.id.idText);
        passwordText = findViewById(R.id.passwordText);

        loginButton = findViewById(R.id.loginButton);

        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent noticeIntent = new Intent(LoginActivity.this,NoticeActivity.class);
                startActivity(noticeIntent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ID = idText.getText().toString();
                PASSWORD = passwordText.getText().toString();

                Response.Listener<String> responseLister = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            // 로그인 성공
                            if(success){

                                // 자동로그인 체크박스 체크
                                if(loginChecked){

                                    editor.putString("ID",ID);
                                    editor.putString("PW",PASSWORD);
                                    editor.putBoolean("Auto_Login_enabled",true);
                                    editor.commit();

                                    editor1.putString("ID",ID);
                                    editor1.putString("PW",PASSWORD);
                                    editor1.commit();

                                    Intent boardlistIntent = new Intent(LoginActivity.this,BoardListActivity.class);
                                    boardlistIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    boardlistIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(boardlistIntent);

                                }

                                // 자동로그인 체크박스 비체크
                                else{

                                    editor1.putString("ID",ID);
                                    editor1.putString("PW",PASSWORD);
                                    editor1.commit();

                                    Intent boardlistIntent = new Intent(LoginActivity.this,BoardListActivity.class);
                                    boardlistIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    boardlistIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(boardlistIntent);
                                }
                            }

                            // 로그인 실패
                            else{

                                Toast.makeText(getApplicationContext(),"입력한 정보가 옳지 않습니다",Toast.LENGTH_LONG).show();

                            }
                        }
                        catch (Exception e){

                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(ID,PASSWORD,responseLister);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);


            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                //구글 로그인 성공해서 파베에 인증
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);


                // 로그인 성공했으므로 메인화면으로 이동하는 intent 코드
            }
            else{
                //구글 로그인 실패
            }
        }
    }

    // 사용자가 정상적으로 로그인한 후에 GoogleSignInAccount 개체에서 ID 토큰을 가져와서
    //Firebase 사용자 인증 정보로 교환하고 Firebase 사용자 인증 정보를 사용해 Firebase에 인증

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct){
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "인증 실패", Toast.LENGTH_SHORT).show();
                        }else{
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "구글 로그인 인증 성공", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void signOut() {

        mGoogleApiClient.connect();

        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {

            @Override

            public void onConnected(@Nullable Bundle bundle) {

                mAuth.signOut();

                if (mGoogleApiClient.isConnected()) {

                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {

                        public void onResult(@NonNull Status status) {

                            if (status.isSuccess()) {

                                Log.v("알림", "로그아웃 성공");

                                setResult(1);

                            } else {

                                setResult(0);

                            }

                            finish();

                        }

                    });

                }

            }

            @Override

            public void onConnectionSuspended(int i) {

                Log.v("알림", "Google API Client Connection Suspended");

                setResult(-1);

                finish();

            }

        });

    }


    public void checkPermission() {
        //현재 안드로이드 버전이 6.0미만이면 메서드를 종료한다.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;

        for (String permission : permission_list) {
            //권한 허용 여부를 확인한다.
            int chk = checkCallingOrSelfPermission(permission);

            if (chk == PackageManager.PERMISSION_DENIED) {
                //권한 허용을여부를 확인하는 창을 띄운다
                requestPermissions(permission_list, 0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            for (int i = 0; i < grantResults.length; i++) {
                //허용됬다면
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(getApplicationContext(), "앱권한설정하세요", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }

    }

}