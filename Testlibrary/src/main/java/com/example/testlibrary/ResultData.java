package com.example.testlibrary;

/**
 * Created by 91299 on 2017/3/29   0029.
 */

public class ResultData<T> {

    private Result<T> result;

    public Result<T> getResult() {
        return result;
    }

    public void setResult(Result<T> result) {
        this.result = result;
    }


    public static class Result<T>{
        private int code;
        private String message;
        private T data;


        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }
}
