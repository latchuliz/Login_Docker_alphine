package com.neopharma.datavault.model.response;

import com.neopharma.datavault.model.Constants;

public class Response {
    public String status;
    public String message;
    public String errorCode;

    public Response noConnectivity() {
        this.status = Constants.ERROR_RESULT;
        this.message = Constants.NO_INTERNET;
        return this;
    }
}
