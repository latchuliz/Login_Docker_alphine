package com.neopharma.datavault.model;

public class Constants {

    public static final String NO_INTERNET = "Please make sure you have a valid connection and try again!";
    public static final String ERROR = "Something went wrong.. Please try after some time..";
    public static final String OKAY = "Ok";
    public static final String DISCARD = "Discard";
    public static final String KEEP = "Keep";
    public static final String CANCEL = "Cancel";
    public static final String TASK = "task";
    public static final String LIMIT = "limit";
    public static final String TASK_NAME = "taskname";
    public static final String USER_ID = "userid";
    public static final String CAMERA = "camera";
    public static final String IMAGE_PREVIEW = "image_preview";
    public static final String IMAGE_PATH = "image_path";
    public static final String DONOR = "donor";
    public static final String ERROR_RESULT = "error";
    public static final String PHOTOS = "photos";
    public static final String SESSION_EXPIRED = "310";
    public static final String LOGOUT = "Are you sure want to logout?";
    public static final String KIT_ERROR = "Kit id not found";
    public static final String PASSWORD_CHANGED_SUCCESSFULLY = "Password changed successfully";
    public static final String ASSIGNED = "assigned_to";
    public static final String CREATED = "created_by";
    public static final String STATUS = "status";
    public static final String ACTIVE = "ACTIVE";
    public static final String SUPERVISOR = "supervisor";
    public static final String EMPTY_KIT = "Kit not available, Please contact admin/supervisor";
    public static final String EMPTY_KIT_ERROR = "Kit not available";
    public static final String USER_KIT_ALERT = "Once the kit is mapped, can’t be changed";
    public static final String PENDING_ALERT = "You have a pending test";
    public static final String FIELD_TEST_ALERT = "Do you want to discard this field test?";
    public static final String DELETE_ALERT = "Are you sure to delete?";
    public static final String INVALID_LOGIN = "Username or Password is invalid";
    public static final String FORCE_LOGIN = "Force Login";
    public static final String ENFORCEMENT_OFFICER = "enforcement-officer";
    public static final String DOB_ALERT = "Please select DOB minimum of 13yrs";
    public static final String ALREADY_USED_DONOR_ALERT = "Field Test already taken for this donor. Please try another donor";
    public static final String ALL_DONORS = "all_donors";

    public static class Preference {
        public static final String PREFERENCES_NAME = "preferences";
        public static final String PREFERENCES_TOUR_COMPLETE = "tour_complete";
        public static final String TOKEN = "token";
        public static final String LAST_SYNC_TIME = "LastSyncTime";
        public static final String USER_KIT_ALERT = "kitUserAlert";
        public static final String CONFIG = "config";
    }

    public static class HeaderType {
        public static final String APPLICATION_JSON = "application/json";
        public static final String CONTENT_TYPE = "Content-Type";
        public static final String X_AUTH_TOKEN = "x-auth-token";
    }

    public static class Endpoint {
        public static final String AUTHENTICATE = "authenticate";
        public static final String TASKS = "tasks";
        public static final String KITS = "kits";
        public static final String PROFILE = "profile";
        public static final String DRUG_TESTS = "drugtests";
        public static final String LOGOUT = "logout";
        public static final String AREAS = "areas";
        public static final String UPLOADS = "uploads";
        public static final String SYNC_TESTS = "sync-drugtests";
        public static final String CHANGE_PASS = "users/{userid}";
        public static final String CONFIG = "app-constants";
    }

    public static class FieldTest {
        public static final String INCOMPLETE = "incomplete";
        public static final String PENDING = "pending";
    }

    public static class Settings {
        public static final String AUTO_SYNC = "autoSync";
    }

    public static class Numbers {
        public static final int ZERO = 0;
        public static final int ONE = 1;
        public static final int TWO = 2;
        public static final int THREE = 3;
        public static final int FOUR = 4;
        public static final int FIVE = 5;
        public static final int SIX = 6;
        public static final int SEVEN = 7;
        public static final int HUNDRED = 100;
        public static final int HUNDRED_ONE = 101;
        public static final int THOUSAND = 1000;
    }

    public static class Gender {
        public static final String MALE = "Male";
        public static final String FEMALE = "female";
    }

    public static class Results {
        public static final String POSITIVE = "Positive";
        public static final String NEGATIVE = "negative";
    }

    public enum ImageType {
        DONOR("DONOR"),
        KIT("KIT"),
        TEST("TEST"),
        ID("ID");
        public final String name;

        ImageType(String name) {
            this.name = name;
        }
    }

    public static class DonorStatus {
        public static final String NEW = "NEW";
        public static final String DELETED = "DELETED";
    }

    public static class FragmentType {
        public static final String DASHBOARD = "Dashboard";
        public static final String DONOR_KIT_HISTORY = "Donor Kit History";
        public static final String TASKS = "Task";
        public static final String FIELD_TEST_DONOR_IDENTIFICATION = "Field Test - Donor Identification";
        public static final String FIELD_TEST_KIT_INFO = "Field Test- Kit Info";
        public static final String FIELD_TEST_TEST_RESULT = "Field Test- Test Result";
        public static final String PROFILE = "Profile";
        public static final String SETTINGS = "Settings";
        public static final String CHANGE_PASSWORD = "Change Password";
        public static final String SUMMARY = "Summary";
        public static final String SYNCHRONIZATION = "Synchronization";
        public static final String DIGITAL = "All Donors";
    }

    public static class Temp {
        public static final String DENIED_PERMISSION = "You denied the permission. Camera not working. Make sure the app permission enabled to your settings";
    }

    public class ErrorCode {
        public static final String TASK_ALREADY_EXITS = "601";
        public static final String KIT_ALREADY_EXITS = "602";
    }
}
