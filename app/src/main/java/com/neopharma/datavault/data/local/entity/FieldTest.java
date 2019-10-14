package com.neopharma.datavault.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import static com.neopharma.datavault.utility.Utility.toUpperCaseWord;

@Entity(tableName = "tbl_field_test")
public class FieldTest {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "field_id")
    private transient long id;

    @SerializedName("sync_case_id")
    @ColumnInfo(name = "sync_case_id")
    private String syncCaseId;

    @SerializedName("id")
    @ColumnInfo(name = "id")
    private String donorId;

    @SerializedName("name")
    @ColumnInfo(name = "name")
    private String name;

    @SerializedName("dob")
    @ColumnInfo(name = "dob")
    private String dob;

    @SerializedName("gender")
    @ColumnInfo(name = "gender")
    private String gender;

    @SerializedName("email")
    @ColumnInfo(name = "email")
    private String email;

    @SerializedName("ssn")
    @ColumnInfo(name = "ssn")
    private String ssn;

    @SerializedName("mobile_number")
    @ColumnInfo(name = "mobile_number")
    private String mobileNumber;

    @SerializedName("location")
    @ColumnInfo(name = "location")
    private String location;

    @SerializedName("address")
    @ColumnInfo(name = "address")
    private String address;

    @SerializedName("kit_id")
    @ColumnInfo(name = "kit_id")
    private String kitId;

    @SerializedName("tampering")
    @ColumnInfo(name = "tampering")
    private String tamperingType;

    @SerializedName("tl_reference")
    @ColumnInfo(name = "tl_reference")
    private String tamperingReference;

    @SerializedName("officer_comments")
    @ColumnInfo(name = "officer_comments")
    private String officerComments;

    @SerializedName("task")
    @ColumnInfo(name = "task_id")
    private String taskId;

    @SerializedName("officer_result")
    @ColumnInfo(name = "result")
    private String result;

    @Ignore
    private transient Image idImage;

    public String getAdulterantResult() {
        return adulterantResult;
    }

    public void setAdulterantResult(String adulterantResult) {
        this.adulterantResult = adulterantResult;
    }

    @SerializedName("adulterant_result")
    @ColumnInfo(name = "adulterant_result")
    private String adulterantResult;

    @ColumnInfo(name = "area")
    private transient String area;

    @SerializedName("area")
    @ColumnInfo(name = "area_id")
    private String areaId;

    @SerializedName("id_type")
    @ColumnInfo(name = "id_type")
    private String idType;

    @SerializedName("id_no")
    @ColumnInfo(name = "id_no")
    private String idReferenceNo;

    @ColumnInfo(name = "status")
    private transient String status;

    @ColumnInfo(name = "task_name")
    private transient String taskName;

    @ColumnInfo(name = "user_id")
    private transient String userId;

    @ColumnInfo(name = "test_step")
    private transient int testPosition;

    @Ignore
    private transient List<Image> images;

    @Ignore
    @SerializedName("donor_image")
    private List<String> donorImage;

    public List<String> getIdReferenceImage() {
        return idReferenceImage;
    }

    public void setIdReferenceImage(List<String> idReferenceImage) {
        this.idReferenceImage = idReferenceImage;
    }

    @Ignore
    @SerializedName("reference_image")
    private List<String> idReferenceImage;

    @Ignore
    @SerializedName("kit_uploads")
    private List<String> kitUploads;

    @Ignore
    @SerializedName("officer_uploads")
    private List<String> officerUploads;

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    @NonNull
    public long getId() {
        return id;
    }

    public void setId(@NonNull long id) {
        this.id = id;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getName() {
        return toUpperCaseWord(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return toUpperCaseWord(gender);
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getEmail() {
        return email != null && !email.isEmpty() ? email.toLowerCase() : email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return toUpperCaseWord(address);
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getKitId() {
        return kitId;
    }

    public void setKitId(String kitId) {
        this.kitId = kitId;
    }

    public String getOfficerComments() {
        return toUpperCaseWord(officerComments);
    }

    public void setOfficerComments(String officerComments) {
        this.officerComments = officerComments;
    }

    public String getTamperingType() {
        return tamperingType;
    }

    public void setTamperingType(String tamperingType) {
        this.tamperingType = tamperingType;
    }

    public String getTamperingReference() {
        return tamperingReference;
    }

    public void setTamperingReference(String tamperingReference) {
        this.tamperingReference = tamperingReference;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getSyncCaseId() {
        return syncCaseId;
    }

    public void setSyncCaseId(String syncCaseId) {
        this.syncCaseId = syncCaseId;
    }

    public String getDonorId() {
        return donorId;
    }

    public void setDonorId(String donorId) {
        this.donorId = donorId;
    }

    public List<String> getDonorImage() {
        return donorImage;
    }

    public void setDonorImage(List<String> donorImage) {
        this.donorImage = donorImage;
    }

    public List<String> getKitUploads() {
        return kitUploads;
    }

    public void setKitUploads(List<String> kitUploads) {
        this.kitUploads = kitUploads;
    }

    public List<String> getOfficerUploads() {
        return officerUploads;
    }

    public void setOfficerUploads(List<String> officerUploads) {
        this.officerUploads = officerUploads;
    }

    public String getTaskName() {
        return toUpperCaseWord(taskName);
    }

    public String getArea() {
        return toUpperCaseWord(area);
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdReferenceNo() {
        return idReferenceNo;
    }

    public void setIdReferenceNo(String idReferenceNo) {
        this.idReferenceNo = idReferenceNo;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public int getTestPosition() {
        return testPosition;
    }

    public void setTestPosition(int testPosition) {
        if (this.testPosition < testPosition)
            this.testPosition = testPosition;
    }

    public void setIdImage(Image image) {
        this.idImage = image;
    }

    public Image getIdImage() {
        return idImage;
    }
}
