package org.daijie.swagger.result;

public class Result extends ModelResultInitialFactory {

    public static final boolean SUCCESS = true;
    public static final boolean ERROR = false;

    protected static <E> ModelResultWrapper<E> clear(E value){
        return new ModelResultWrapper<E>(Result.SUCCESS, ResultCode.CODE_200).setData(value);
    }
}
