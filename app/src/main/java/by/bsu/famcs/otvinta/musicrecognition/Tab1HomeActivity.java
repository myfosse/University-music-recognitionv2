package by.bsu.famcs.otvinta.musicrecognition;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class Tab1HomeActivity extends Activity {

    private ImageView imageView;
    private String currentPhotoPath;
    private EditText serverUrl;
    private static final int STANDARD_FILE_CHOOSER = 1000;

    private Button buttonCamera;
    private Button buttonGallery;
    private Button buttonSend;

    private boolean isGallery = false;
    private boolean isCamera = false;

    private Intent takePictureIntent = null;

    private SwipeRefreshLayout swipeLayout;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        setContentView(R.layout.tab1_home_layout);

        buttonCamera = findViewById(R.id.btnCamera);
        buttonGallery = findViewById(R.id.btnGallery);
        buttonSend = findViewById(R.id.btnSend);
        imageView = findViewById(R.id.imageView);
        serverUrl = findViewById(R.id.serverUrl);

        registerForContextMenu(imageView);

        swipeLayout = findViewById(R.id.swipe_container);

        swipeLayout.setOnRefreshListener(() -> {
            imageView.setImageDrawable(null);
            Toast.makeText(getApplicationContext(), "Clear", Toast.LENGTH_LONG).show();
            new Handler().postDelayed(() -> {
                swipeLayout.setRefreshing(false);
            }, 100);
        });

        swipeLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright)
        );

        buttonGallery.setOnClickListener(view -> showFileChooser());

        buttonSend.setOnClickListener(view -> {
            if (isGallery) {
                sendRequest();
            }
            if (isCamera) {
                sendRequest();
            }
        });

        buttonCamera.setOnClickListener(view -> {
            isCamera = false;
            takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                }
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(Tab1HomeActivity.this,
                            "by.bsu.famcs.otvinta.musicrecognition",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, 123);
                }
            }
        });

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1111);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private File createMidiFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "audio_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File image = File.createTempFile(
                imageFileName,
                ".mid",
                storageDir
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void setPic() {
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            setPic();
            isCamera = true;
        } else if (requestCode == STANDARD_FILE_CHOOSER && resultCode == RESULT_OK) {
            currentPhotoPath = getFileNameByUri(data.getData());
            setPic();
            isGallery = true;
        }
    }

    public void sendRequest() {
        File f = new File(currentPhotoPath);
        File myFile = new File(currentPhotoPath);
        RequestParams params = new RequestParams();
        try {
            params.put("file", myFile);
        } catch (FileNotFoundException e) {
        }

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Please Wait...");
        progress.setIndeterminate(false);
        progress.setCancelable(false);
        progress.show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(serverUrl.getText().toString(), params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progress.dismiss();
                File musicFile = null;
                try {
                    musicFile = createMidiFile();
                } catch (IOException ex) {
                }
                try (FileOutputStream fos = new FileOutputStream(musicFile)) {
                    fos.write(responseBody);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(Tab1HomeActivity.this, "midi succes", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progress.dismiss();
                Toast.makeText(Tab1HomeActivity.this, "midi fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getFileNameByUri(Uri uri) {
        String wholeID = DocumentsContract.getDocumentId(uri);
        String id = wholeID.split(":")[1];
        String[] column = {MediaStore.Images.Media.DATA};
        String sel = MediaStore.Images.Media._ID + "=?";
        Cursor cursor = getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{id}, null);

        String filePath = "";
        int columnIndex = cursor.getColumnIndex(column[0]);
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    private void showFileChooser() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                isGallery = false;
                takePictureIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                takePictureIntent.addCategory(Intent.CATEGORY_OPENABLE);
                takePictureIntent.setType("*/*");
                String[] mimeTypes = {"image/*", "application/pdf"};
                takePictureIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                takePictureIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                startActivityForResult(Intent.createChooser(takePictureIntent, "Select a File to Upload"), STANDARD_FILE_CHOOSER);
            } else {
                throw new ActivityNotFoundException();
            }
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(Tab1HomeActivity.this, "No file chooser", Toast.LENGTH_SHORT).show();
        }
    }
}