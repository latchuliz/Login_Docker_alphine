package com.neopharma.datavault.model.user;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.neopharma.datavault.BuildConfig;

import static com.neopharma.datavault.utility.Utility.toUpperCaseWord;

@Entity(tableName = "tbl_user")
public class UserData {
    @PrimaryKey
    @NonNull
    public String id;
    public String firstname;
    public String lastname;
    public String email;
    @SerializedName("mobile_number")
    public String mobileNumber;
    public String address;
    public String avatar;
    public String username;
    @Embedded
    public Role role;
    @Embedded
    public Tenant tenant_id;

    @Ignore
    public String getFullName() {
        if (firstname == null)
            firstname = "";
        if (lastname == null)
            lastname = "";
        return String.format("%s %s", toUpperCaseWord(firstname), toUpperCaseWord(lastname));
    }

    @Ignore
    public String getAvatar() {
        return (avatar != null && !avatar.isEmpty()) ? BuildConfig.IMAGEURL + avatar : "";
    }

    @Ignore
    public String getUsername() {
        return toUpperCaseWord(username);
    }
}
