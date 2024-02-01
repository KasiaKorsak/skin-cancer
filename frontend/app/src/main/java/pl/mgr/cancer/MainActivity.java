package pl.mgr.cancer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "MESSAGE";

    Button BSelectImage;
    ImageView IVPreviewImage;
    File outFile;
    int SELECT_PICTURE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IVPreviewImage = findViewById(R.id.image);
    }

    public void onUpload(View v) {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    IVPreviewImage.setImageURI(selectedImageUri);
                    try {
                        saveImage(IVPreviewImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void saveImage(ImageView ivPreviewImage) throws IOException {
        BitmapDrawable draw = (BitmapDrawable) ivPreviewImage.getDrawable();
        Bitmap bitmap = draw.getBitmap();
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File dir = new File(path.getAbsolutePath() + "/files");
        dir.mkdirs();
        String fileName = String.format("%d.jpg", System.currentTimeMillis());
        outFile = new File(dir, fileName);
        System.out.println("nazwa pliku: " + fileName);
        FileOutputStream outStream = new FileOutputStream(outFile);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
        outStream.flush();
        outStream.close();
    }

    public void onValidate(View view) {
        if (outFile != null) {
            RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-data"), outFile);
            MultipartBody.Part partImage = MultipartBody.Part.createFormData("image_bytes", outFile.getName(), reqBody);
            API api = RetrofitClient.getInstance().getAPI();
            Call<ApiResponse> upload = api.uploadImage(partImage);
            upload.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        String result = response.body().getTyp();

                        Intent intent = new Intent(MainActivity.this, DisplayMessageActivity.class);
                        intent.putExtra(EXTRA_MESSAGE, result);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Context context = getApplicationContext();
                    CharSequence text = "Wystąpił błąd!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            });
        }
    }

}