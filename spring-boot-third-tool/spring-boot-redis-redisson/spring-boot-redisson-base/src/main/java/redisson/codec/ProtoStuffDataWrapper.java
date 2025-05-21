package redisson.codec;


/**
 * Protostuff 包装类
 * Protostuff 是基于POJO进行序列化和反序列化操作, 有时候对非定义POJO要用这个类辅助.
 * 作用:
 * 1.主要用于对 Map、List、String、Enum 等进行序列化/反序列化
 * 2.对未知的 Object.class 可以用这个通用包装类
 */
class ProtoStuffDataWrapper<T> {
    private T data;

    public ProtoStuffDataWrapper() {
    }

    public ProtoStuffDataWrapper(T data) {
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

