package dao.impl;

import parainfo.sql.ConectaDb;

public class DaoImpl {

    protected final ConectaDb DB;
    protected String message;

    public DaoImpl() {
        this.DB = new ConectaDb();
    }
}
