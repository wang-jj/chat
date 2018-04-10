package com.example.dell.chat.base;

/**
 * Created by courageface on 2018/4/8.
 */

public class DbSchema {
    public static final class ChatListTable {
        public static final String NAME = "MSG";

        public static final class Cols {
            public static final String UUID = "id";
            public static final String IMAGE = "image";
            public static final String NAME = "name";
            public static final String MESSAGE = "message";
            public static final String TIME = "time";
            public static final String UNREAD = "unread";
        }
    }

    public static final class ChatTable {
        public static final String NAME = "CHAT";

        public static final class Cols {
            public static final String UUID = "id";
            public static final String TIME = "time";
            public static final String CONTENT = "content";
            public static final String DIRECTION = "direction";

        }
    }

}
