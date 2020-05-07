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
    //private MediaPlayer mediaPlayer;

    private Button buttonCamera;
    private Button buttonGallery;
    private Button buttonSend;

    private boolean isGallery = false;
    private boolean isCamera = false;

    private Intent takePictureIntent = null;

    final int MENU_IMAGE_DELETE = 1;


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        switch (v.getId()) {
            case R.id.imageView:
                menu.add(0, MENU_IMAGE_DELETE, 0, "Delete");
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_IMAGE_DELETE:
                imageView.setImageDrawable(null);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        setContentView(R.layout.tab1_home_layout);

        //mediaPlayer = MediaPlayer.create(this, R.raw.best);

        buttonCamera = findViewById(R.id.btnCamera);
        buttonGallery = findViewById(R.id.btnGallery);
        buttonSend = findViewById(R.id.btnSend);
        imageView = findViewById(R.id.imageView);
        serverUrl = findViewById(R.id.serverUrl);

        registerForContextMenu(imageView);

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

        /*imageView.setOnLongClickListener(view -> {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(200);
            }
            mediaPlayer = MediaPlayer.create(Tab1HomeActivity.this, R.raw.best);
            Toast.makeText(Tab1HomeActivity.this, "Long click", Toast.LENGTH_SHORT).show();
            return true;
        });*/

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
        // Create an image file name
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
                //mediaPlayer = MediaPlayer.create(Tab1HomeActivity.this, Uri.fromFile(musicFile));
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