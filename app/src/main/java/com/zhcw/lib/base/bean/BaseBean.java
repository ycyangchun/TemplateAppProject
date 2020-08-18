package com.zhcw.lib.base.bean;

/**
 * 使用示例：GsonUtil.<ToastBean>unifiedBeanToBody("json", new TypeToken<BaseBean<ToastBean>>() {}.getType());
 * @param <T>
 */
public class BaseBean<T> {

    private MessageBean<T> message;

    public MessageBean<T> getMessage() {
        return message;
    }

    public void setMessage(MessageBean<T> message) {
        this.message = message;
    }

    public class MessageBean<T> {

        private T body;
        private HeadBean head;

        public T getBody() {
            return body;
        }

        public void setBody(T body) {
            this.body = body;
        }

        public HeadBean getHead() {
            return head;
        }

        public void setHead(HeadBean head) {
            this.head = head;
        }

        public class HeadBean {
            @Override
            public String toString() {
                return "HeadBean{" +
                        "digest='" + digest + '\'' +
                        ", message='" + message + '\'' +
                        ", messageID='" + messageID + '\'' +
                        ", messengerID='" + messengerID + '\'' +
                        ", resCode='" + resCode + '\'' +
                        ", timeStamp='" + timeStamp + '\'' +
                        ", transactionType='" + transactionType + '\'' +
                        '}';
            }
            private String digest;
            private String message;
            private String messageID;
            private String messengerID;
            private String resCode;
            private String timeStamp;
            private String transactionType;

            public String getDigest() {
                return digest;
            }

            public void setDigest(String digest) {
                this.digest = digest;
            }

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public String getMessageID() {
                return messageID;
            }

            public void setMessageID(String messageID) {
                this.messageID = messageID;
            }

            public String getMessengerID() {
                return messengerID;
            }

            public void setMessengerID(String messengerID) {
                this.messengerID = messengerID;
            }

            public String getResCode() {
                return resCode;
            }

            public void setResCode(String resCode) {
                this.resCode = resCode;
            }

            public String getTimeStamp() {
                return timeStamp;
            }

            public void setTimeStamp(String timeStamp) {
                this.timeStamp = timeStamp;
            }

            public String getTransactionType() {
                return transactionType;
            }

            public void setTransactionType(String transactionType) {
                this.transactionType = transactionType;
            }
        }
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "message=" + message +
                '}';
    }
}
