package com.xue.parsers;

import com.xue.bean.CityList;

import org.json.JSONArray;
import org.json.JSONObject;

public class CityListParser extends MasterParser<CityList> {

    @Override
    public CityList parse(JSONObject data) throws Exception {
        CityList cityList = null;
        JSONArray array = optJSONArray(data, "city_list");
        int len = getLength(array);
        if (len > 0) {
            cityList = new CityList();
            for (int i = 0; i < len; i++) {
                JSONObject object = optJSONObject(array, i);

                String id = optString(object, "id");
                String name = optString(object, "name");
                String showName = optString(object, "show_name");
                String initial = optString(object, "initial");

                CityList.City city = new CityList.City();
                city.setId(id);
                city.setName(name);
                city.setShowName(showName);
                city.setInitial(initial);
                cityList.add(city);
            }
        }

        return cityList;
    }
}