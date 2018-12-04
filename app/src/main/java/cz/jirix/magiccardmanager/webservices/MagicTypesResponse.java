package cz.jirix.magiccardmanager.webservices;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import cz.jirix.magiccardmanager.model.MagicType;

public class MagicTypesResponse {

    @SerializedName("types")
    private List<String> mTypes;

    public MagicTypesResponse(List<String> types) {
        mTypes = types;
    }

    private List<MagicType> convertToObject(List<String> list){
        List<MagicType> converted = new ArrayList<>();
        for(String item : list){
            converted.add(new MagicType(item));
        }
        return converted;
    }

    public List<MagicType> getConvertedTypes(){
        return convertToObject(mTypes);
    }

    public List<String> getTypes() {
        return mTypes;
    }

    public void setTypes(List<String> types) {
        mTypes = types;
    }
}
