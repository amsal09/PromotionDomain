package com.danapprentech.promotion.services;

import org.json.simple.JSONObject;

public abstract class AbstractMethod {
    public abstract boolean save(JSONObject jsonObject);
    public abstract boolean update(JSONObject jsonObject);
    public abstract boolean delete(String id);
}
