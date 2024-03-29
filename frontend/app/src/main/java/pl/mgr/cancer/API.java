package pl.mgr.cancer;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface API {
    @Multipart
    @POST("/predict")
    Call<ApiResponse> uploadImage(@Part MultipartBody.Part image);
}
