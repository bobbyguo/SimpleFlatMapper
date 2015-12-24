package org.sfm.jdbc.impl.setter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PrepareStatementIndexedSetter<T> {

    /**
     *
     * @param ps the preparedStatement to bind against
     * @param value the value to bind
     * @param columnIndex the index to start binding at
     * @throws SQLException if an error occurs
     */
    void set(PreparedStatement ps, T value, int columnIndex) throws SQLException;
}
