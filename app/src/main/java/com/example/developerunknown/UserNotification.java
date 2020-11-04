package com.example.developerunknown;

import com.google.firebase.Timestamp;

public class UserNotification {
        private String sender;
        private Timestamp timestamp;
        private String type;
        private String book;
        private String description;

        public UserNotification(String sender, Timestamp timestamp, String type, String book) {
            this.sender = sender;
            this.timestamp = timestamp;
            this.type = type;
            this.book = book;
            if (type.equals("accept")) {
                description = sender + " accepted your request of book "+book;
            }
            else if (type.equals("request")){
                description = sender + " requested on your book "+book;
            }
        }

        public String getSender() {
            return sender;
        }

        public Timestamp getTimestamp() {
            return timestamp;
        }

        public String getType() {
            return type;
        }

        public String getBook() {
            return book;
        }

        public String getDescription() {
            return description;
        }

}
