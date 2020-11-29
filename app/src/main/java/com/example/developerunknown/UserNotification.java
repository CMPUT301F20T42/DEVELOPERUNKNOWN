package com.example.developerunknown;


/**
 *this class is used for displaying notification received by current user
 */
public class UserNotification {
        private String sender;
        private String type;
        private String book;
        private String id;
        private String description;

    /**
     *defines user information
     * @param book book title
     * @param id user id
     * @param sender user's name
     * @param type  status type
     */
        public UserNotification(String sender, String type, String book, String id) {
            this.sender = sender;
            this.type = type;
            this.book = book;
            this.id = id;
            if (type.equals("Accepted")) {
                this.description = sender + " accepted your request of book "+ book;
            }
            else if (type.equals("Request")){
                this.description = sender + " requested on your book "+ book;
            }
            else if (type.equals("Denied")){
                this.description = sender + " denied your request on book " + book;
            }
        }


    /**
     * Return id
     * @return
     * Return id
     */
        public String getId() {
            return id;
            }

    /**
     * Return the sender name
     * @return
     * Return sender
     */
        public String getSender() {
            return sender;
        }

    /**
     * Return the book status type
     * @return
     * Return type
     */
        public String getType() {
            return type;
        }

    /**
     * Return the book
     * @return
     * Return book
     */
        public String getBook() {
            return book;
        }

    /**
     * Return the book description
     * @return
     * Return description
     */
        public String getDescription() {
            return description;
        }

}
