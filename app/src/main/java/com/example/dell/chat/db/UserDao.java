package com.example.dell.chat.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.example.dell.chat.bean.User;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER".
*/
public class UserDao extends AbstractDao<User, Long> {

    public static final String TABLENAME = "USER";

    /**
     * Properties of entity User.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Email = new Property(1, String.class, "email", false, "EMAIL");
        public final static Property User_id = new Property(2, int.class, "user_id", false, "USER_ID");
        public final static Property User_name = new Property(3, String.class, "user_name", false, "USER_NAME");
        public final static Property Image_path = new Property(4, String.class, "image_path", false, "IMAGE_PATH");
        public final static Property School = new Property(5, String.class, "school", false, "SCHOOL");
        public final static Property Gender = new Property(6, int.class, "gender", false, "GENDER");
        public final static Property User_motto = new Property(7, String.class, "user_motto", false, "USER_MOTTO");
        public final static Property Picture_path1 = new Property(8, String.class, "picture_path1", false, "PICTURE_PATH1");
        public final static Property Picture_path2 = new Property(9, String.class, "picture_path2", false, "PICTURE_PATH2");
        public final static Property Picture_path3 = new Property(10, String.class, "picture_path3", false, "PICTURE_PATH3");
        public final static Property Password = new Property(11, String.class, "password", false, "PASSWORD");
        public final static Property Birthday = new Property(12, String.class, "birthday", false, "BIRTHDAY");
    };


    public UserDao(DaoConfig config) {
        super(config);
    }
    
    public UserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"EMAIL\" TEXT," + // 1: email
                "\"USER_ID\" INTEGER NOT NULL ," + // 2: user_id
                "\"USER_NAME\" TEXT," + // 3: user_name
                "\"IMAGE_PATH\" TEXT," + // 4: image_path
                "\"SCHOOL\" TEXT," + // 5: school
                "\"GENDER\" INTEGER NOT NULL ," + // 6: gender
                "\"USER_MOTTO\" TEXT," + // 7: user_motto
                "\"PICTURE_PATH1\" TEXT," + // 8: picture_path1
                "\"PICTURE_PATH2\" TEXT," + // 9: picture_path2
                "\"PICTURE_PATH3\" TEXT," + // 10: picture_path3
                "\"PASSWORD\" TEXT," + // 11: password
                "\"BIRTHDAY\" TEXT);"); // 12: birthday
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, User entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(2, email);
        }
        stmt.bindLong(3, entity.getUser_id());
 
        String user_name = entity.getUser_name();
        if (user_name != null) {
            stmt.bindString(4, user_name);
        }
 
        String image_path = entity.getImage_path();
        if (image_path != null) {
            stmt.bindString(5, image_path);
        }
 
        String school = entity.getSchool();
        if (school != null) {
            stmt.bindString(6, school);
        }
        stmt.bindLong(7, entity.getGender());
 
        String user_motto = entity.getUser_motto();
        if (user_motto != null) {
            stmt.bindString(8, user_motto);
        }
 
        String picture_path1 = entity.getPicture_path1();
        if (picture_path1 != null) {
            stmt.bindString(9, picture_path1);
        }
 
        String picture_path2 = entity.getPicture_path2();
        if (picture_path2 != null) {
            stmt.bindString(10, picture_path2);
        }
 
        String picture_path3 = entity.getPicture_path3();
        if (picture_path3 != null) {
            stmt.bindString(11, picture_path3);
        }
 
        String password = entity.getPassword();
        if (password != null) {
            stmt.bindString(12, password);
        }
 
        String birthday = entity.getBirthday();
        if (birthday != null) {
            stmt.bindString(13, birthday);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, User entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(2, email);
        }
        stmt.bindLong(3, entity.getUser_id());
 
        String user_name = entity.getUser_name();
        if (user_name != null) {
            stmt.bindString(4, user_name);
        }
 
        String image_path = entity.getImage_path();
        if (image_path != null) {
            stmt.bindString(5, image_path);
        }
 
        String school = entity.getSchool();
        if (school != null) {
            stmt.bindString(6, school);
        }
        stmt.bindLong(7, entity.getGender());
 
        String user_motto = entity.getUser_motto();
        if (user_motto != null) {
            stmt.bindString(8, user_motto);
        }
 
        String picture_path1 = entity.getPicture_path1();
        if (picture_path1 != null) {
            stmt.bindString(9, picture_path1);
        }
 
        String picture_path2 = entity.getPicture_path2();
        if (picture_path2 != null) {
            stmt.bindString(10, picture_path2);
        }
 
        String picture_path3 = entity.getPicture_path3();
        if (picture_path3 != null) {
            stmt.bindString(11, picture_path3);
        }
 
        String password = entity.getPassword();
        if (password != null) {
            stmt.bindString(12, password);
        }
 
        String birthday = entity.getBirthday();
        if (birthday != null) {
            stmt.bindString(13, birthday);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public User readEntity(Cursor cursor, int offset) {
        User entity = new User( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // email
            cursor.getInt(offset + 2), // user_id
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // user_name
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // image_path
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // school
            cursor.getInt(offset + 6), // gender
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // user_motto
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // picture_path1
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // picture_path2
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // picture_path3
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // password
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12) // birthday
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, User entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setEmail(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setUser_id(cursor.getInt(offset + 2));
        entity.setUser_name(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setImage_path(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setSchool(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setGender(cursor.getInt(offset + 6));
        entity.setUser_motto(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setPicture_path1(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setPicture_path2(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setPicture_path3(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setPassword(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setBirthday(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(User entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(User entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
