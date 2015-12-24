package org.sfm.jdbc.impl.setter;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class URLPreparedStatementIndexedSetter implements PrepareStatementIndexedSetter<URL> {
    @Override
    public void set(PreparedStatement target, URL value, int columnIndex) throws SQLException {
        if (value == null) {
            target.setNull(columnIndex, Types.DATALINK);
        } else {
            target.setURL(columnIndex, value);
        }
    }
}
