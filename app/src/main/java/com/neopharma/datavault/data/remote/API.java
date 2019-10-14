package com.neopharma.datavault.data.remote;

import com.neopharma.datavault.data.local.entity.FieldTest;
import com.neopharma.datavault.model.Constants;
import com.neopharma.datavault.model.Constants.Endpoint;
import com.neopharma.datavault.model.Constants.HeaderType;
import com.neopharma.datavault.model.Password;
import com.neopharma.datavault.model.request.LoginRequest;
import com.neopharma.datavault.model.response.AreaResponse;
import com.neopharma.datavault.model.response.ConfigResponse;
import com.neopharma.datavault.model.response.DonorResponse;
import com.neopharma.datavault.model.response.ImageUploadResponse;
import com.neopharma.datavault.model.response.KitResponse;
import com.neopharma.datavault.model.response.LoginResponse;
import com.neopharma.datavault.model.response.Response;
import com.neopharma.datavault.model.response.TaskResponse;
import com.neopharma.datavault.model.response.UserResponse;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface API {

    @POST(Endpoint.AUTHENTICATE)
    Observable<LoginResponse> doLogin(@Body LoginRequest body);

    @GET(Endpoint.LOGOUT)
    Observable<Response> logout(@Header(HeaderType.CONTENT_TYPE) String type, @Header(HeaderType.X_AUTH_TOKEN) String token);

    @GET(Endpoint.AREAS)
    Observable<AreaResponse> getAreas(@Header(HeaderType.CONTENT_TYPE) String type, @Header(HeaderType.X_AUTH_TOKEN) String token);

    @GET(Endpoint.TASKS)
    Observable<TaskResponse> getAdminTasks(
            @Header(HeaderType.CONTENT_TYPE) String type,
            @Header(HeaderType.X_AUTH_TOKEN) String token,
            @Query(Constants.STATUS) String status);

    @GET(Endpoint.TASKS)
    Observable<TaskResponse> getSupervisorTasks(
            @Header(HeaderType.CONTENT_TYPE) String type,
            @Header(HeaderType.X_AUTH_TOKEN) String token,
            @Query(Constants.STATUS) String status,
            @Query(Constants.CREATED) String userId
    );

    @GET(Endpoint.KITS)
    Observable<KitResponse> getKits(
            @Header(HeaderType.CONTENT_TYPE) String type,
            @Header(HeaderType.X_AUTH_TOKEN) String token,
            @Query(Constants.LIMIT) int limit
    );

    @GET(Endpoint.PROFILE)
    Observable<UserResponse> getProfile(
            @Header(HeaderType.CONTENT_TYPE) String type,
            @Header(HeaderType.X_AUTH_TOKEN) String token
    );

    @GET(Endpoint.DRUG_TESTS)
    Observable<DonorResponse> getDonors(
            @Header(HeaderType.CONTENT_TYPE) String type,
            @Header(HeaderType.X_AUTH_TOKEN) String token,
            @Query(Constants.TASK) String taskId
    );

    @GET(Endpoint.DRUG_TESTS)
    Observable<DonorResponse> getAllDonors(
            @Header(HeaderType.CONTENT_TYPE) String type,
            @Header(HeaderType.X_AUTH_TOKEN) String token
    );

    @Multipart
    @POST(Endpoint.UPLOADS)
    Observable<ImageUploadResponse> imageUpload(
            @Part MultipartBody.Part file_to_upload,
            @Header(HeaderType.X_AUTH_TOKEN) String token
    );

    @POST(Endpoint.SYNC_TESTS)
    Observable<Response> syncCloud(@Header(HeaderType.X_AUTH_TOKEN) String token, @Body FieldTest body, @Query("tenant_id") String tenantId);

    @PUT(Endpoint.CHANGE_PASS)
    Observable<Response> updatePassword(
            @Header(HeaderType.X_AUTH_TOKEN) String token,
            @Path(Constants.USER_ID) String userId,
            @Body Password body
    );

    @GET(Endpoint.CONFIG)
    Observable<ConfigResponse> getConfig(@Header(HeaderType.CONTENT_TYPE) String type);

}
