package com.splxtech.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import greendao.Appliance;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table APPLIANCE.
*/
public class ApplianceDao extends AbstractDao<Appliance, Long> {

    public static final String TABLENAME = "APPLIANCE";

    /**
     * Properties of entity Appliance.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property ImageId1 = new Property(2, Integer.class, "imageId1", false, "IMAGE_ID1");
        public final static Property ImageId2 = new Property(3, Integer.class, "imageId2", false, "IMAGE_ID2");
        public final static Property AppId = new Property(4, Integer.class, "appId", false, "APP_ID");
        public final static Property Time = new Property(5, Integer.class, "time", false, "TIME");
        public final static Property Waste = new Property(6, Integer.class, "waste", false, "WASTE");
        public final static Property Online = new Property(7, Boolean.class, "online", false, "ONLINE");
    };


    public ApplianceDao(DaoConfig config) {
        super(config);
    }
    
    public ApplianceDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'APPLIANCE' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'NAME' TEXT," + // 1: name
                "'IMAGE_ID1' INTEGER," + // 2: imageId1
                "'IMAGE_ID2' INTEGER," + // 3: imageId2
                "'APP_ID' INTEGER," + // 4: appId
                "'TIME' INTEGER," + // 5: time
                "'WASTE' INTEGER," + // 6: waste
                "'ONLINE' INTEGER);"); // 7: online
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'APPLIANCE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Appliance entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        Integer imageId1 = entity.getImageId1();
        if (imageId1 != null) {
            stmt.bindLong(3, imageId1);
        }
 
        Integer imageId2 = entity.getImageId2();
        if (imageId2 != null) {
            stmt.bindLong(4, imageId2);
        }
 
        Integer appId = entity.getAppId();
        if (appId != null) {
            stmt.bindLong(5, appId);
        }
 
        Integer time = entity.getTime();
        if (time != null) {
            stmt.bindLong(6, time);
        }
 
        Integer waste = entity.getWaste();
        if (waste != null) {
            stmt.bindLong(7, waste);
        }
 
        Boolean online = entity.getOnline();
        if (online != null) {
            stmt.bindLong(8, online ? 1l: 0l);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Appliance readEntity(Cursor cursor, int offset) {
        Appliance entity = new Appliance( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // imageId1
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // imageId2
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // appId
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5), // time
            cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // waste
            cursor.isNull(offset + 7) ? null : cursor.getShort(offset + 7) != 0 // online
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Appliance entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setImageId1(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setImageId2(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setAppId(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setTime(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
        entity.setWaste(cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6));
        entity.setOnline(cursor.isNull(offset + 7) ? null : cursor.getShort(offset + 7) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Appliance entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Appliance entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
