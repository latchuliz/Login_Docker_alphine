package com.neopharma.datavault.model.response;

import com.neopharma.datavault.model.Constants;
import com.neopharma.datavault.model.Data;

public class LoginResponse extends Response {
    public Data data;

    public LoginResponse noConnectivity() {
        this.status = Constants.ERROR_RESULT;
        this.message = Constants.NO_INTERNET;
        return this;
    }
}
