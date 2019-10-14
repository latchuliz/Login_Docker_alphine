package com.neopharma.datavault.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.neopharma.datavault.data.local.database.AppDatabase;
import com.neopharma.datavault.data.local.entity.FieldTest;
import com.neopharma.datavault.data.local.entity.Image;
import com.neopharma.datavault.data.remote.API;
import com.neopharma.datavault.fragments.module.SharedPreferences;
import com.neopharma.datavault.model.Constants;
import com.neopharma.datavault.model.Constants.HeaderType;
import com.neopharma.datavault.model.Password;
import com.neopharma.datavault.model.area.AreaData;
import com.neopharma.datavault.model.donor.Donor;
import com.neopharma.datavault.model.donor.DonorData;
import com.neopharma.datavault.model.images.ImageData;
import com.neopharma.datavault.model.kit.Kit;
import com.neopharma.datavault.model.kit.KitData;
import com.neopharma.datavault.model.request.LoginRequest;
import com.neopharma.datavault.model.request.RequestObserver;
import com.neopharma.datavault.model.response.AreaResponse;
import com.neopharma.datavault.model.response.ConfigResponse;
import com.neopharma.datavault.model.response.DonorResponse;
import com.neopharma.datavault.model.response.ImageUploadResponse;
import com.neopharma.datavault.model.response.KitResponse;
import com.neopharma.datavault.model.response.LoginResponse;
import com.neopharma.datavault.model.response.Response;
import com.neopharma.datavault.model.response.TaskResponse;
import com.neopharma.datavault.model.response.UserResponse;
import com.neopharma.datavault.model.task.Task;
import com.neopharma.datavault.model.task.TaskData;
import com.neopharma.datavault.utility.Store;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;

import static com.neopharma.datavault.model.Constants.ALL_DONORS;
import static com.neopharma.datavault.model.Constants.FieldTest.INCOMPLETE;
import static com.neopharma.datavault.utility.Utility.md5String;

public class Repository {
    private API api;

    @Inject
    AppDatabase db;

    @Inject
    SharedPreferences pref;

    @Inject
    public Repository(API api) {
        this.api = api;
    }

    public LiveData<LoginResponse> doLogin(LoginRequest body) {
        MutableLiveData<LoginResponse> result = new MutableLiveData<>();
        if (Store.isNetworkAvailable)
            api.doLogin(body).subscribeOn(Schedulers.io()).subscribe(
                    new RequestObserver<LoginResponse>(result) {
                        @Override
                        public void updateDB(LoginResponse value) {

                        }
                    }
            );
        else
            result.postValue(new LoginResponse().noConnectivity());
        return result;
    }

    public LiveData<TaskResponse> getAdminTasks() {
        MutableLiveData<TaskResponse> result = new MutableLiveData<>();
        if (Store.isNetworkAvailable)
            api.getAdminTasks(HeaderType.APPLICATION_JSON, pref.getToken(), Constants.ACTIVE).subscribeOn(Schedulers.io()).subscribe(
                    new RequestObserver<TaskResponse>(result) {
                        @Override
                        public void updateDB(TaskResponse value) {
                            if (Constants.OKAY.equalsIgnoreCase(value.status)) {
                                db.taskDao().deleteAllAdmin(Store.userId);
                                db.taskDao().insertAll(value.data.tasks);
                            }
                        }

                        @Override
                        public void onNext(TaskResponse value) {
                            super.onNext(value);
                            if (value.data != null)
                                for (Task task : value.data.tasks) {
                                    getDonors(task.id, task.name);
                                }
                        }
                    }
            );
        else {
            TaskResponse response = new TaskResponse();
            response.status = Constants.OKAY;
            response.data = new TaskData();
            //TODO GET DATA FROM DB WHERE CREATED BY ADMIN
            response.data.tasks = db.taskDao().getAdminTasks(Store.userId);
            result.postValue(response);
        }
        return result;
    }

    public LiveData<TaskResponse> getSupervisorTasks() {
        MutableLiveData<TaskResponse> result = new MutableLiveData<>();
        if (Store.isNetworkAvailable)
            api.getSupervisorTasks(HeaderType.APPLICATION_JSON, pref.getToken(), Constants.ACTIVE, Store.userId).subscribeOn(Schedulers.io()).subscribe(
                    new RequestObserver<TaskResponse>(result) {
                        @Override
                        public void updateDB(TaskResponse value) {
                            if (Constants.OKAY.equalsIgnoreCase(value.status)) {
                                db.taskDao().deleteAllSupervisor(Store.userId);
                                db.taskDao().insertAll(value.data.tasks);
                            }
                        }

                        @Override
                        public void onNext(TaskResponse value) {
                            super.onNext(value);
                            if (value.data != null)
                                for (Task task : value.data.tasks) {
                                    getDonors(task.id, task.name);
                                }
                        }
                    }
            );
        else {
            TaskResponse response = new TaskResponse();
            response.status = Constants.OKAY;
            response.data = new TaskData();
            response.data.tasks = db.taskDao().getSupervisorTasks(Store.userId);
            result.postValue(response);
        }
        return result;
    }

    public LiveData<AreaResponse> getAreas() {
        MutableLiveData<AreaResponse> result = new MutableLiveData<>();
        if (Store.isNetworkAvailable)
            api.getAreas(HeaderType.APPLICATION_JSON, Store.token).subscribeOn(Schedulers.io()).subscribe(
                    new RequestObserver<AreaResponse>(result) {
                        @Override
                        public void updateDB(AreaResponse value) {
                            if (Constants.OKAY.equalsIgnoreCase(value.status)) {
                                db.areaDao().deleteAll();
                                db.areaDao().insertAll(value.data.areas);
                            }
                        }
                    }
            );
        else {
            AreaResponse response = new AreaResponse();
            response.status = Constants.OKAY;
            response.data = new AreaData();
            response.data.areas = db.areaDao().getAreas();
            result.postValue(response);
        }
        return result;
    }

    public void deleteAllTaskData() {
        db.taskDao().deleteAll();
        db.donorDao().deleteAll();
    }

    public LiveData<KitResponse> getKits() {
        MutableLiveData<KitResponse> result = new MutableLiveData<>();
        if (Store.isNetworkAvailable)
            api.getKits(HeaderType.APPLICATION_JSON, pref.getToken(), 5000).subscribeOn(Schedulers.io()).subscribe(
                    new RequestObserver<KitResponse>(result) {
                        @Override
                        public void updateDB(KitResponse value) {
                            if (Constants.OKAY.equalsIgnoreCase(value.status)) {
                                db.kitDao().deleteAll();
                                db.kitDao().insertAll(value.data.kits);
                            }
                        }
                    }
            );
        else {
            KitResponse response = new KitResponse();
            response.status = Constants.OKAY;
            response.data = new KitData();
            response.data.kits = db.kitDao().getAll();
            result.postValue(response);
        }
        return result;
    }

    public LiveData<UserResponse> getProfile() {
        MutableLiveData<UserResponse> result = new MutableLiveData<>();
        if (Store.isNetworkAvailable)
            api.getProfile(HeaderType.APPLICATION_JSON, pref.getToken()).subscribeOn(Schedulers.io()).subscribe(
                    new RequestObserver<UserResponse>(result) {
                        @Override
                        public void updateDB(UserResponse value) {
                            if (Constants.OKAY.equalsIgnoreCase(value.status) && value.data != null) {
                                db.userDao().deleteAll();
                                db.userDao().insert(value.data);
                            }
                        }
                    }
            );
        else {
            UserResponse response = new UserResponse();
            response.status = Constants.OKAY;
            response.data = db.userDao().getUser();
            result.postValue(response);
        }
        return result;
    }

    public LiveData<DonorResponse> getDonors(String taskId, String name) {
        MutableLiveData<DonorResponse> result = new MutableLiveData<>();
        if (Store.isNetworkAvailable)
            api.getDonors(HeaderType.APPLICATION_JSON, pref.getToken(), taskId).subscribeOn(Schedulers.io()).subscribe(
                    new RequestObserver<DonorResponse>(result) {
                        @Override
                        public void updateDB(DonorResponse value) {
                            if (Constants.OKAY.equalsIgnoreCase(value.status)) {
                                db.donorDao().deleteTaskDonors(taskId);
                                for (Donor donor : value.data.donors) {
                                    donor.task.taskId = taskId;
                                    donor.task.taskName = name;
                                    db.donorDao().insert(donor);
                                }
                                //db.donorDao().insertAll(value.data.donors);
                            }

                        }
                    }
            );
        else {
            DonorResponse response = new DonorResponse();
            response.status = Constants.OKAY;
            response.data = new DonorData();
            response.data.donors = db.donorDao().findDonorByTaskId(taskId);
            result.postValue(response);
        }
        return result;
    }

    public LiveData<DonorResponse> getAllDonors() {
        MutableLiveData<DonorResponse> result = new MutableLiveData<>();
        if (Store.isNetworkAvailable)
            api.getAllDonors(HeaderType.APPLICATION_JSON, pref.getToken()).subscribeOn(Schedulers.io()).subscribe(
                    new RequestObserver<DonorResponse>(result) {
                        @Override
                        public void updateDB(DonorResponse value) {
                            if (Constants.OKAY.equalsIgnoreCase(value.status)) {
                                db.donorDao().deleteAllDonors(ALL_DONORS);
                                List<Donor> donorList = new ArrayList<>();
                                for (Donor donors : value.data.donors) {
                                    donors.donorType = ALL_DONORS;
                                    if (donors.task != null) {
                                        donorList.add(donors);
                                    }
                                }
                                db.donorDao().insertAll(donorList);
                            }
                        }
                    }
            );
        else {
            DonorResponse response = new DonorResponse();
            response.status = Constants.OKAY;
            response.data = new DonorData();
            response.data.donors = db.donorDao().getAllDonors(ALL_DONORS);
            result.postValue(response);
        }
        return result;
    }

    public LiveData<ImageUploadResponse> imageUpload(MultipartBody.Part picture, Image image) {
        MutableLiveData<ImageUploadResponse> result = new MutableLiveData<>();
        if (Store.isNetworkAvailable)
            api.imageUpload(picture, pref.getToken()).subscribeOn(Schedulers.io()).subscribe(
                    new RequestObserver<ImageUploadResponse>(result) {
                        @Override
                        public void updateDB(ImageUploadResponse value) {
                            if (Constants.OKAY.equalsIgnoreCase(value.status)) {
                                for (ImageData data : value.data) {
                                    File file = new File(image.path);
                                    file.delete();
                                    image.path = data.key;
                                    image.status = Constants.FieldTest.PENDING;
                                    db.imageDao().update(image);
                                }
                            }
                        }
                    }
            );
        return result;
    }

    public List<FieldTest> getPendingDonor() {
        return db.fieldTestDao().findAllPending(Store.userId);
    }

    public LiveData<List<FieldTest>> findAllPendingLive() {
        return db.fieldTestDao().findAllPendingLive(Store.userId);
    }

    public LiveData<Integer> getTodayTask() {
        return db.taskDao().getTaskCount(Store.userId);
    }

    public List<Kit> getUnusedKits() {
        List<String> fieldTests = db.fieldTestDao().findAllFieldTestUserId(Store.userId);
        List<Kit> kits = db.kitDao().getAll();
        List<Kit> unUsedKits = new ArrayList<>();
        for (Kit kit : kits) {
            if (!fieldTests.contains(kit.serialNo)) {
                unUsedKits.add(kit);
            }
        }
        return unUsedKits;
    }

    public List<String> getTakenFieldTestDonors() {
        return db.fieldTestDao().findTakenFieldTestDonorsByUserId(Store.userId);
    }

    public Integer getDonorCount() {
        return db.donorDao().findAllDonorCount(ALL_DONORS);
    }

    public LiveData<List<Donor>> getDonorLive() {
        List<String> taskIds = new ArrayList<>();
        List<Task> tasks = db.taskDao().getAdminTasks(Store.userId);
        for (Task task : tasks) {
            taskIds.add(task.id);
        }
        return db.donorDao().findAllDonorLive(taskIds);
    }

    public FieldTest findOrCreateIncompleteRecord() {
        FieldTest fieldTest = db.fieldTestDao().findIncompleteRecord(Store.userId);
        if (fieldTest == null) {
            fieldTest = new FieldTest();
            fieldTest.setSyncCaseId(getCaseId());
            fieldTest.setStatus(Constants.FieldTest.INCOMPLETE);
            fieldTest.setUserId(Store.userId);
            fieldTest.setId(db.fieldTestDao().insert(fieldTest));
        }
        fieldTest.setImages(db.imageDao().findByFieldId(fieldTest.getId()));
        return fieldTest;
    }

    public Image saveImagePath(FieldTest fieldTest, String imagePath, Constants.ImageType type) {
        Image image = new Image();
        image.path = imagePath;
        image.methodName = type.name;
        image.fieldId = fieldTest.getId();
        image.status = INCOMPLETE;
        image.id = db.imageDao().insert(image);
        return image;
    }

    public boolean haveIncomplete() {
        return db.fieldTestDao().findIncompleteRecord(Store.userId) != null;
    }

    public FieldTest haveIncompleteTest() {
        return db.fieldTestDao().findIncompleteRecord(Store.userId);
    }

    public FieldTest findPendingEqualDonor(String donorId) {
        return db.fieldTestDao().findPendingEqualDonor(Store.userId, donorId);
    }

    public Image saveImagePath(String imagePath, Constants.ImageType type) {
        return saveImagePath(findOrCreateIncompleteRecord(), imagePath, type);
    }

    public void update(FieldTest fieldTest) {
        db.fieldTestDao().update(fieldTest);
    }

    public void removeImage(Image image) {
        db.imageDao().delete(image);
    }

    private String getCaseId() {
        return md5String(pref.getToken() + ":" + System.currentTimeMillis());
    }

    public LiveData<Response> syncCloud(FieldTest fieldTest) {
        MutableLiveData<Response> result = new MutableLiveData<>();
        if (Store.isNetworkAvailable)
            api.syncCloud(pref.getToken(), fieldTest, db.userDao().getUser().tenant_id.id).subscribeOn(Schedulers.io()).subscribe(
                    new RequestObserver<Response>(result) {
                        @Override
                        public void updateDB(Response value) {
                            if (Constants.OKAY.equalsIgnoreCase(value.status) ||
                                    Constants.ErrorCode.TASK_ALREADY_EXITS.equals(value.errorCode) ||
                                    Constants.ErrorCode.KIT_ALREADY_EXITS.equals(value.errorCode)) {
                                db.kitDao().deleteSyncKit(fieldTest.getKitId());
                                db.fieldTestDao().delete(fieldTest); // already synced
                            }
                        }
                    }
            );
        return result;
    }

    public List<Image> findByAllImage(long id, String status) {
        return db.imageDao().findByAllImage(id, status);
    }

    public LiveData<Response> updatePassword(Password body) {
        MutableLiveData<Response> result = new MutableLiveData<>();
        if (Store.isNetworkAvailable)
            api.updatePassword(pref.getToken(), Store.userId, body).subscribeOn(Schedulers.io()).subscribe(
                    new RequestObserver<Response>(result) {
                        @Override
                        public void updateDB(Response value) {

                        }
                    }
            );
        else
            result.postValue(new Response().noConnectivity());
        return result;
    }

    public void deleteFieldTestById(FieldTest fieldTest) {
        db.fieldTestDao().delete(fieldTest);
    }

    public LiveData<Response> logout() {
        MutableLiveData<Response> result = new MutableLiveData<>();
        if (Store.isNetworkAvailable)
            api.logout(HeaderType.APPLICATION_JSON, pref.getToken()).subscribeOn(Schedulers.io()).subscribe(
                    new RequestObserver<Response>(result) {
                        @Override
                        public void updateDB(Response value) {

                        }
                    }
            );
        else
            result.postValue(new Response().noConnectivity());
        return result;
    }

    public LiveData<ConfigResponse> getConfig() {
        MutableLiveData<ConfigResponse> result = new MutableLiveData<>();
        api.getConfig(HeaderType.APPLICATION_JSON).subscribeOn(Schedulers.io()).subscribe(
                new RequestObserver<ConfigResponse>(result) {
                    @Override
                    public void updateDB(ConfigResponse value) {

                    }
                }
        );
        return result;

    }
}
