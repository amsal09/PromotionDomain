package com.danapprentech.promotion.response;

public class BaseResponse<T> {
    private int code;
    private String message;
    private T data;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public static final class BaseResponseBuilder<T>{
        private int code;
        private String message;
        private T data;

        public BaseResponseBuilder<T> withCode(int code){
            this.code = code;
            return this;
        }
        public BaseResponseBuilder<T> withMessage(String message){
            this.message = message;
            return this;
        }
        public BaseResponseBuilder<T> withData(T data){
            this.data = data;
            return this;
        }

        public BaseResponse<T> build(){
            BaseResponse<T> baseResponse = new BaseResponse<> ();
            baseResponse.code = this.code;
            baseResponse.message=this.message;
            baseResponse.data=this.data;
            return baseResponse;
        }
    }
}
