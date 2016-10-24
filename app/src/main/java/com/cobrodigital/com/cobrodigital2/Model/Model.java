package com.cobrodigital.com.cobrodigital2.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.AvoidXfermode;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Ariel on 6/09/16.
 */
public abstract class Model extends OrmLiteSqliteOpenHelper {
    protected final Boolean ACTIVAR_DEBUG = true;
    private static final String NOMBRE_BASE_DATOS = "CobroDigital.db";
    private static final int VERSION_ACTUAL = 1;
    OrmLiteSqliteOpenHelper base;
    private Dao dao;
    public Model(Context contexto) {
        super(contexto, NOMBRE_BASE_DATOS, null, VERSION_ACTUAL);
        return;
    }
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource,this.getClass());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }


}