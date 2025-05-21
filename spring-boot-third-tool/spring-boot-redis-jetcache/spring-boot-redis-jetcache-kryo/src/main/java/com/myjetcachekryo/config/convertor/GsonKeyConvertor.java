package com.myjetcachekryo.config.convertor;

import com.google.gson.Gson;

import java.util.function.Function;

/**
 * The type Gson key convertor.
 */
public class GsonKeyConvertor implements Function<Object, Object> {
    /**
     * The constant INSTANCE.
     */
    public static final GsonKeyConvertor INSTANCE = new GsonKeyConvertor();

    /**
     * Apply object.
     *
     * @param originalKey the original key
     * @return the object
     */
    @Override
    public Object apply(Object originalKey) {
        if (originalKey == null) {
            return null;
        }
        if (originalKey instanceof String) {
            return originalKey;
        }
        return new Gson().toJson(originalKey);
    }
}
