package ca.flymile.ModelDelta;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class JsonResponse {
    @SerializedName("data")
    private Info info;

}
