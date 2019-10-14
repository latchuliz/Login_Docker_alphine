package com.neopharma.datavault.model.response;

import com.google.gson.annotations.SerializedName;
import com.neopharma.datavault.model.images.ImageData;

import java.util.List;

public class ImageUploadResponse extends Response {
    @SerializedName("data")
    public List<ImageData> data = null;
}
