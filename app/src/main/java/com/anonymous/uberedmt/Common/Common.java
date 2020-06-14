package com.anonymous.uberedmt.Common;

import com.anonymous.uberedmt.Retro.IGoogleAPI;
import com.anonymous.uberedmt.Retro.RetroFitClient;

public class Common {

    public static final String baseURL = "https://maps.googleapis.com";

    public static IGoogleAPI getGoogleAPI() {
        return RetroFitClient.getClient(baseURL).create(IGoogleAPI.class);
    }

}
