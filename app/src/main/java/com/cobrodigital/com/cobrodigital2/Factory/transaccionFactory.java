package com.cobrodigital.com.cobrodigital2.Factory;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.cobrodigital.com.cobrodigital2.Model.Model;
import com.cobrodigital.com.cobrodigital2.Model.Transaccion;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Ariel on 23/10/16.
 */
public class transaccionFactory {

    /**
    /**
     * Created by Ariel on 23/10/16.
     */
    private transaccionFactory instance;
    private Dao<Transaccion,Integer> dao;

        public transaccionFactory getInstance(Context context) throws SQLException, ClassNotFoundException {
            if(instance==null)
                instance=new transaccionFactory(context);
            return instance;
        }
        public transaccionFactory(Context context) throws ClassNotFoundException, SQLException {
            OrmLiteSqliteOpenHelper helper= OpenHelperManager.getHelper(context,Model.class);
            TableUtils.createTableIfNotExists(helper.getConnectionSource(),Transaccion.class);
            this.dao=helper.getDao(Transaccion.class);
        }
        public void guardar(Transaccion objeto) throws SQLException {
            dao.create(objeto);
        }
        public List<Transaccion> select() throws SQLException {
            return dao.queryForAll();
        }
        public List<Transaccion> select(Map<String,Object> transacciones) throws SQLException{
            return dao.queryForFieldValues(transacciones);
        }
        public  boolean delete(Transaccion object) throws SQLException {
            if(dao.delete(object)!=0)
                return true;
            return false;
        }
}
