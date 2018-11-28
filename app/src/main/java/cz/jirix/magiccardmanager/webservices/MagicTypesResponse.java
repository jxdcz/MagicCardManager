package cz.jirix.magiccardmanager.webservices;

import com.google.gson.annotations.SerializedName;

import java.util.List;

class MagicTypesResponse {

    @SerializedName("types")
    private List<String> mTypes;

    public MagicTypesResponse(List<String> types) {
        mTypes = types;
    }

    public List<String> getTypes() {
        return mTypes;
    }

    public void setTypes(List<String> types) {
        mTypes = types;
    }
}
