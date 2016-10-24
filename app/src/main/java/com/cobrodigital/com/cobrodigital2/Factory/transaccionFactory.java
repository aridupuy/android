package com.cobrodigital.com.cobrodigital2.Factory;

import android.content.Context;

import com.cobrodigital.com.cobrodigital2.Model.Transaccion;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;

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
    private Dao dao;

        public transaccionFactory getInstance(){
            if(instance==null)
                instance=new transaccionFactory();
            return instance;
        }
        public void factory(Context context) throws ClassNotFoundException {
            OrmLiteSqliteOpenHelper base= OpenHelperManager.getHelper(context,Transaccion.class);
            try{
                this.dao=base.getDao(Transaccion.class);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        public void guardar(Transaccion objeto) throws SQLException {
            dao.create(objeto);

        }
        public List<Transaccion> select() throws SQLException {
            return dao.queryForAll();
        }
        public List<Transaccion> select(Map<String,Transaccion> transacciones) throws SQLException{
            return dao.queryForFieldValues(transacciones);
        }
        public  boolean delete(Transaccion object) throws SQLException {
            if(dao.delete(object)!=0)
                return true;
            return false;
        }
}
