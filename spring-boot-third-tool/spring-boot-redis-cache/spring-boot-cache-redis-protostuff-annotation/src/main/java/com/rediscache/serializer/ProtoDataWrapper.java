package com.rediscache.serializer;


class ProtoDataWrapper<T> {
    private T data;

    public ProtoDataWrapper() {
    }

    public ProtoDataWrapper(T data) {
        super();
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

