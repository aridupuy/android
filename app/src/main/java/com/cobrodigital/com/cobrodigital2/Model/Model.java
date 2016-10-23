package com.cobrodigital.com.cobrodigital2.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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
    void factory(Dao dao, Context context, Class clase) {
        base= OpenHelperManager.getHelper(context,this.getClass());
        try{
            dao=base.getDao(clase);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void guardar(Object objeto) throws SQLException{
        dao.create(objeto);
    }
    public List<Object>select() throws SQLException {
        return dao.queryForAll();
    }
    public List<Object> select(Map<String,Object> objects) throws SQLException{
        return dao.queryForFieldValues(objects);
    }
    public  boolean delete(Object object) throws SQLException {
        if(dao.delete(object)!=0)
            return true;
        return false;
    }

}