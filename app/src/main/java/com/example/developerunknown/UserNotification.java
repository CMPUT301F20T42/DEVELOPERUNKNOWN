package com.example.developerunknown;


//this class is used for displaying notification received by current user

public class UserNotification {
        private String sender;
        private String type;
        private String book;
        private String id;
        private String description;


        public UserNotification(String sender, String type, String book,String id) {
            this.sender = sender;
            this.type = type;
            this.book = book;
            this.id = id;
            if (type.equals("Accepted")) {
                description = sender + " accepted your request of book "+book;
            }
            else if (type.equals("Request")){
                description = sender + " requested on your book "+ book;
            }
            else if (type.equals("Denied")){
                description = sender+ " denied your request on book "+book;
            }
        }


        public String getId() {
            return id;
            }

        public String getSender() {
            return sender;
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
