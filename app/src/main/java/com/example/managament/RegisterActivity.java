package com.example.managament;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText idText;
    EditText passwordText;
    EditText passwordTextConfirm;
    EditText nameText;
    Button registerButton1; // 회원가입 버튼
    Button validateButton; // 중복체크 버튼
    private String userID;
    private String userPassword;
    private String userPasswordConfirm;
    private Boolean validate = false;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        idText = findViewById(R.id.idText);
        passwordText = findViewById(R.id.passwordText);
        passwordTextConfirm = findViewById(R.id.passwordTextConfirm);
        nameText = findViewById(R.id.nameText);

        idText.setFilters(new InputFilter[]{new CustomInputFilter()});
        idText.setPrivateImeOptions("defaultInputmode=english;");

        passwordText.setFilters(new InputFilter[]{new CustomInputFilter()});
        passwordText.setPrivateImeOptions("defaultInputmode=english;");

        passwordTextConfirm.setFilters(new InputFilter[]{new CustomInputFilter()});
        passwordTextConfirm.setPrivateImeOptions("defaultInputmode=english;");

        nameText.setFilters(new InputFilter[]{new CustomInputFilter1()});
        nameText.setPrivateImeOptions("defaultInputmode=korean;");

        validateButton = findViewById(R.id.validateButton);
        registerButton1 = findViewById(R.id.registerButton1);

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userID = idText.getText().toString();
                if (validate) {
                    return;
                }

                if (userID.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("아이디는 빈칸일 수 없습니다.").setPositiveButton("확인", null).create();
                    dialog.show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("사용할 수 있는 아이디입니다.").setPositiveButton("확인", null).create();
                                dialog.show();
                                idText.setEnabled(false);
                                validate = true;

                                idText.setBackgroundColor(getResources().getColor(R.color.colorGray));

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("사용할 수 없는 아이디입니다.").setNegativeButton("확인", null).create();
                                dialog.show();
                            }


                        } catch (Exception e) {

                        }
                    }
                };

                ValidateRequest validateRequest = new ValidateRequest(userID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(validateRequest);
            }
        });

        registerButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userID = idText.getText().toString();
                userPassword = passwordText.getText().toString();
                userPasswordConfirm = passwordTextConfirm.getText().toString();

                if (!validate) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("먼저 중복 체크를 해주세요.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }

                if (userID.equals("") || userPassword.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("빈 칸 없이 입력해주세요.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }

                if(!userPassword.equals(userPasswordConfirm)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("비밀번호가 일치하지 않습니다.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("회원 등록에 성공했습니다")
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                                startActivity(i);
                                            }
                                        })
                                        .create();
                                dialog.show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("회원 등록에 실패했습니다")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                            }
                        } catch (Exception e) {

                            e.printStackTrace();

                        }
                    }
                };

                RegisterRequest registerRequest = new RegisterRequest(userID, userPassword, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);

            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    protected class CustomInputFilter implements InputFilter {
        @Override
        public CharSequence filter(CharSequence source, int start,
                                   int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");

            if (source.equals("") || ps.matcher(source).matches()) {
                return source;
            }

            Toast.makeText(RegisterActivity.this,
                    "영문, 숫자만 입력 가능합니다.", Toast.LENGTH_SHORT).show();

            return "";
        }
    }


    protected class CustomInputFilter1 implements InputFilter {
        @Override
        public CharSequence filter(CharSequence source, int start,
                                   int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣]+$");

            if (source.equals("") || ps.matcher(source).matches()) {
                return source;
            }

            Toast.makeText(RegisterActivity.this,
                    "한글, 영문만 입력 가능합니다.", Toast.LENGTH_SHORT).show();

            return "";
        }
    }
}

