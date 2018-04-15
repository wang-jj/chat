package com.example.dell.chat.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.example.dell.chat.bean.PersonalState;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "PERSONAL_STATE".
*/
public class PersonalStateDao extends AbstractDao<PersonalState, Long> {

    public static final String TABLENAME = "PERSONAL_STATE";

    /**
     * Properties of entity PersonalState.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Nickname = new Property(1, String.class, "nickname", false, "NICKNAME");
        public final static Property School = new Property(2, String.class, "school", false, "SCHOOL");
        public final static Property Content = new Property(3, String.class, "content", false, "CONTENT");
        public final static Property Location = new Property(4, String.class, "location", false, "LOCATION");
        public final static Property State_time = new Property(5, java.util.Date.class, "state_time", false, "STATE_TIME");
        public final static Property Like = new Property(6, int.class, "like", false, "LIKE");
        public final static Property Comment = new Property(7, int.class, "comment", false, "COMMENT");
        public final static Property ProfileID = new Property(8, int.class, "profileID", false, "PROFILE_ID");
        public final static Property Image1ID = new Property(9, int.class, "image1ID", false, "IMAGE1_ID");
        public final static Property Image2ID = new Property(10, int.class, "image2ID", false, "IMAGE2_ID");
        public final static Property Image3ID = new Property(11, int.class, "image3ID", false, "IMAGE3_ID");
        public final static Property PictureID = new Property(12, int.class, "pictureID", false, "PICTURE_ID");
        public final static Property Img_type = new Property(13, int.class, "img_type", false, "IMG_TYPE");
        public final static Property Personalstate_id = new Property(14, int.class, "personalstate_id", false, "PERSONALSTATE_ID");
        public final static Property User_id = new Property(15, int.class, "user_id", false, "USER_ID");
        public final static Property Holder_id = new Property(16, int.class, "holder_id", false, "HOLDER_ID");
        public final static Property Update_time = new Property(17, java.util.Date.class, "update_time", false, "UPDATE_TIME");
    };


    public PersonalStateDao(DaoConfig config) {
        super(config);
    }
    
    public PersonalStateDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PERSONAL_STATE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"NICKNAME\" TEXT," + // 1: nickname
                "\"SCHOOL\" TEXT," + // 2: school
                "\"CONTENT\" TEXT," + // 3: content
                "\"LOCATION\" TEXT," + // 4: location
                "\"STATE_TIME\" INTEGER," + // 5: state_time
                "\"LIKE\" INTEGER NOT NULL ," + // 6: like
                "\"COMMENT\" INTEGER NOT NULL ," + // 7: comment
                "\"PROFILE_ID\" INTEGER NOT NULL ," + // 8: profileID
                "\"IMAGE1_ID\" INTEGER NOT NULL ," + // 9: image1ID
                "\"IMAGE2_ID\" INTEGER NOT NULL ," + // 10: image2ID
                "\"IMAGE3_ID\" INTEGER NOT NULL ," + // 11: image3ID
                "\"PICTURE_ID\" INTEGER NOT NULL ," + // 12: pictureID
                "\"IMG_TYPE\" INTEGER NOT NULL ," + // 13: img_type
                "\"PERSONALSTATE_ID\" INTEGER NOT NULL ," + // 14: personalstate_id
                "\"USER_ID\" INTEGER NOT NULL ," + // 15: user_id
                "\"HOLDER_ID\" INTEGER NOT NULL ," + // 16: holder_id
                "\"UPDATE_TIME\" INTEGER);"); // 17: update_time
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_PERSONAL_STATE_HOLDER_ID ON PERSONAL_STATE" +
                " (\"HOLDER_ID\" ASC);");
        db.execSQL("CREATE INDEX " + constraint + "IDX_PERSONAL_STATE_UPDATE_TIME ON PERSONAL_STATE" +
                " (\"UPDATE_TIME\" ASC);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PERSONAL_STATE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, PersonalState entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String nickname = entity.getNickname();
        if (nickname != null) {
            stmt.bindString(2, nickname);
        }
 
        String school = entity.getSchool();
        if (school != null) {
            stmt.bindString(3, school);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(4, content);
        }
 
        String location = entity.getLocation();
        if (location != null) {
            stmt.bindString(5, location);
        }
 
        java.util.Date state_time = entity.getState_time();
        if (state_time != null) {
            stmt.bindLong(6, state_time.getTime());
        }
        stmt.bindLong(7, entity.getLike());
        stmt.bindLong(8, entity.getComment());
        stmt.bindLong(9, entity.getProfileID());
        stmt.bindLong(10, entity.getImage1ID());
        stmt.bindLong(11, entity.getImage2ID());
        stmt.bindLong(12, entity.getImage3ID());
        stmt.bindLong(13, entity.getPictureID());
        stmt.bindLong(14, entity.getImg_type());
        stmt.bindLong(15, entity.getPersonalstate_id());
        stmt.bindLong(16, entity.getUser_id());
        stmt.bindLong(17, entity.getHolder_id());
 
        java.util.Date update_time = entity.getUpdate_time();
        if (update_time != null) {
            stmt.bindLong(18, update_time.getTime());
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, PersonalState entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String nickname = entity.getNickname();
        if (nickname != null) {
            stmt.bindString(2, nickname);
        }
 
        String school = entity.getSchool();
        if (school != null) {
            stmt.bindString(3, school);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(4, content);
        }
 
        String location = entity.getLocation();
        if (location != null) {
            stmt.bindString(5, location);
        }
 
        java.util.Date state_time = entity.getState_time();
        if (state_time != null) {
            stmt.bindLong(6, state_time.getTime());
        }
        stmt.bindLong(7, entity.getLike());
        stmt.bindLong(8, entity.getComment());
        stmt.bindLong(9, entity.getProfileID());
        stmt.bindLong(10, entity.getImage1ID());
        stmt.bindLong(11, entity.getImage2ID());
        stmt.bindLong(12, entity.getImage3ID());
        stmt.bindLong(13, entity.getPictureID());
        stmt.bindLong(14, entity.getImg_type());
        stmt.bindLong(15, entity.getPersonalstate_id());
        stmt.bindLong(16, entity.getUser_id());
        stmt.bindLong(17, entity.getHolder_id());
 
        java.util.Date update_time = entity.getUpdate_time();
        if (update_time != null) {
            stmt.bindLong(18, update_time.getTime());
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public PersonalState readEntity(Cursor cursor, int offset) {
        PersonalState entity = new PersonalState( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // nickname
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // school
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // content
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // location
            cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)), // state_time
            cursor.getInt(offset + 6), // like
            cursor.getInt(offset + 7), // comment
            cursor.getInt(offset + 8), // profileID
            cursor.getInt(offset + 9), // image1ID
            cursor.getInt(offset + 10), // image2ID
            cursor.getInt(offset + 11), // image3ID
            cursor.getInt(offset + 12), // pictureID
            cursor.getInt(offset + 13), // img_type
            cursor.getInt(offset + 14), // personalstate_id
            cursor.getInt(offset + 15), // user_id
            cursor.getInt(offset + 16), // holder_id
            cursor.isNull(offset + 17) ? null : new java.util.Date(cursor.getLong(offset + 17)) // update_time
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, PersonalState entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setNickname(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setSchool(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setContent(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setLocation(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setState_time(cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)));
        entity.setLike(cursor.getInt(offset + 6));
        entity.setComment(cursor.getInt(offset + 7));
        entity.setProfileID(cursor.getInt(offset + 8));
        entity.setImage1ID(cursor.getInt(offset + 9));
        entity.setImage2ID(cursor.getInt(offset + 10));
        entity.setImage3ID(cursor.getInt(offset + 11));
        entity.setPictureID(cursor.getInt(offset + 12));
        entity.setImg_type(cursor.getInt(offset + 13));
        entity.setPersonalstate_id(cursor.getInt(offset + 14));
        entity.setUser_id(cursor.getInt(offset + 15));
        entity.setHolder_id(cursor.getInt(offset + 16));
        entity.setUpdate_time(cursor.isNull(offset + 17) ? null : new java.util.Date(cursor.getLong(offset + 17)));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(PersonalState entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(PersonalState entity) {
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