package com.ttknp.api.entity;
// how to @Builder in lombok library works
public class ResponseObject<T> {

    private Short status;
    private String info;
    private T data;

    public ResponseObject(Short status, String info, T data) {
        this.status = status;
        this.info = info;
        this.data = data;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseObject{" +
                "status=" + status +
                ", info='" + info + '\'' +
                ", data=" + data +
                '}';
    }

    // *********************static******************** Importance work like -> ResponseObject.<T>builder() it will return ResponseObjectBuilder it's like Student
    public static ResponseObjectBuilder builder() {
        return new ResponseObjectBuilder();
    }
    // *********************static******************** builder class
    public static class ResponseObjectBuilder<T> {
        private Short status;
        private String info;
        private T data;

        public ResponseObjectBuilder() {} // ** need
        public ResponseObjectBuilder status (Short status) {
            this.status = status;
            return ResponseObjectBuilder.this;
        }
        public ResponseObjectBuilder info (String info) {
            this.info = info;
            return ResponseObjectBuilder.this;
        }
        public ResponseObjectBuilder data (T data) {
            this.data = data;
            return ResponseObjectBuilder.this;
        }

        // *** for get ResponseObject class that you set at all properties
        // Like -> ResponseObject.builder().status(...).info(...).data(...) [Student call Student Builder and set properties in Student Builder]
        //                  .build() [bring properties in Student Builder to Student that's ]
        public ResponseObject build() {
            return new ResponseObject(this.status,this.info,this.data);
        }

    }
}
