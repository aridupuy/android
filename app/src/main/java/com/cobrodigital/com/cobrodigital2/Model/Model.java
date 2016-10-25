package com.cobrodigital.com.cobrodigital2.Model;

import android.content.Context;
import android.content.Intent;
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
public class Model extends OrmLiteSqliteOpenHelper {
    protected final Boolean ACTIVAR_DEBUG = true;
    private static final String NOMBRE_BASE_DATOS = "CobroDigital.db";
    private static final String PASSWORD="estoescobrodigitalbitch";
    private static final int VERSION_ACTUAL = 1;
    public static OrmLiteSqliteOpenHelper base;
    private Model instance;
    private Dao dao;
    public Model(Context contexto) {
        super(contexto, NOMBRE_BASE_DATOS, null, VERSION_ACTUAL);
    }
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }
    public Dao<Credencial,Integer> getCredencialDao() throws SQLException {
        return getDao(Credencial.class);
    }
    public Dao<Transaccion,Integer> getTransaccionDao() throws SQLException {
        return getDao(Transaccion.class);
    }
    public Dao<Pagador,Integer> getPagadorDao() throws SQLException {
        return getDao(Pagador.class);
    }
    @Override
    public void close() {
        super.close();
    }
}