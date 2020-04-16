package com.example.managament;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    URL url;
    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";
    String result;

    String state = "";

    SellBoardRequest sellBoardRequest;

    ImageView iv1, iv2;
    Button boardRegisterButton;
    EditText et1; // 메모
    EditText et2; // 원하는 거래가

    CheckBox ch1, ch2, ch3, ch4, ch6, ch8;

    String memo;
    String price;

    String path, path1;
    String fileExtension, fileExtension1;

    String loginID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences pref = this.getSharedPreferences("login", 0);

        loginID = pref.getString("ID", "null");

        boardRegisterButton = findViewById(R.id.boardRegisterButton);
        et1 = findViewById(R.id.memo);
        et2 = findViewById(R.id.wantPrice);

        iv1 = findViewById(R.id.image1);
        iv2 = findViewById(R.id.image2);

        ch1 = findViewById(R.id.a1);
        ch2 = findViewById(R.id.a2);
        ch3 = findViewById(R.id.a3);
        ch4 = findViewById(R.id.a4);
        ch6 = findViewById(R.id.a6);
        ch8 = findViewById(R.id.a8);

        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });

        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 2);
            }
        });

        final String 도서명 = getIntent().getExtras().getString("도서명");
        final String 저자명 = getIntent().getExtras().getString("저자명");
        final String 출판사 = getIntent().getExtras().getString("출판사");
        final String 정가 = getIntent().getExtras().getString("정가");
        final String 도서표지 = getIntent().getExtras().getString("도서표지");
        final String 별점 = getIntent().getExtras().getString("별점");
        final String 목차이미지 = getIntent().getExtras().getString("목차이미지");

        boardRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ch1.isChecked()) {
                    state = state.concat("1");
                }

                if (ch2.isChecked()) {
                    state = state.concat("2");
                }

                if (ch3.isChecked()) {
                    state = state.concat("3");
                }

                if (ch4.isChecked()) {
                    state = state.concat("4");
                }

                if (ch6.isChecked()) {
                    state = state.concat("6");
                }

                if (ch8.isChecked()) {
                    state = state.concat("8");
                }

                memo = et1.getText().toString();
                price = et2.getText().toString();

                if (memo == null || price == null) {
                    Toast.makeText(getApplicationContext(), "원하는 가격이나 간단한 메모는 반드시 입력하세요!", Toast.LENGTH_LONG).show();
                    return;
                }


                Response.Listener<String> responseListener1 = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                if (path != null)
                                    fileUpload(path);

                                if (path1 != null)
                                    fileUpload(path1);

                                Intent intent = new Intent(MainActivity.this, BoardListActivity.class);
                                startActivity(intent);
                            }

                        } catch (Exception e) {

                        }
                    }

                };


                // 사진 둘다 선택했을때
                if (path != null) {
                    if (path1 != null) {
                        sellBoardRequest = new SellBoardRequest(loginID, 도서표지, fileExtension, fileExtension1, 도서명, 저자명, 출판사, 정가 + "원", price + "원", state, memo, 별점, 목차이미지, responseListener1);
                    }
                }

                // 사진 첫번째만 선택했을때
                if (path == null) {
                    if (path1 != null) {
                        sellBoardRequest = new SellBoardRequest(loginID, 도서표지, fileExtension, null, 도서명, 저자명, 출판사, 정가 + "원", price + "원", state, memo, 별점, 목차이미지, responseListener1);
                    }
                }

                // 사진 두번째만 선택했을때
                if (path != null) {
                    if (path1 == null) {
                        sellBoardRequest = new SellBoardRequest(loginID, 도서표지, null, fileExtension1, 도서명, 저자명, 출판사, 정가 + "원", price + "원", state, memo, 별점, 목차이미지, responseListener1);
                    }
                }

                // // 사진 하나도 선택 안했을때
                if (path == null) {
                    if (path1 == null) {
                        sellBoardRequest = new SellBoardRequest(loginID, 도서표지, null, null, 도서명, 저자명, 출판사, 정가 + "원", price + "원", state, memo, 별점, 목차이미지, responseListener1);
                    }
                }
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(sellBoardRequest);


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    Uri dataUri = data.getData();

                    path = getPath(getApplicationContext(), dataUri);
                    fileExtension = path.substring(path.lastIndexOf("/") + 1, path.length());

                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지 표시
                    iv1.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (requestCode == 2) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    Uri dataUri = data.getData();

                    path1 = getPath(getApplicationContext(), dataUri);
                    fileExtension1 = path1.substring(path1.lastIndexOf("/") + 1, path1.length());

                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지 표시
                    iv2.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    void fileUpload(final String file) {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                httpFileUpload("https://gurwls1307.cafe24.com/mobile/upload.php", file);
            }
        });
        th.start();
    }

    void httpFileUpload(String urlString, String file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            url = new URL(urlString);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data;name=\"userfile\";filename=\"" + file + "\"" + lineEnd);
            dos.writeBytes(lineEnd);
            int bytesAvailable = fis.available();
            int maxBufferSize = 4096;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];
            int bytesRead = fis.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fis.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fis.read(buffer, 0, bufferSize);
            }

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            fis.close();
            dos.flush();
            int ch;

            InputStream is = conn.getInputStream();
            StringBuffer b = new StringBuffer();

            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }

            result = b.toString().trim();
            dos.close();
            JSONObject jsonObj = new JSONObject(result);

            result = jsonObj.getString("message");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
