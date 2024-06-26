/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.tika.parser.sqlite3;


import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.rowset.serial.SerialBlob;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import org.apache.tika.extractor.EmbeddedDocumentUtil;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.jdbc.JDBCTableReader;


/**
 * Concrete class for SQLLite table parsing.  This overrides
 * column type handling from JDBCRowHandler.
 * <p/>
 * For now, this silently skips cells of type CLOB, because xerial's jdbc connector
 * does not currently support them.
 */
public class SQLite3TableReader extends JDBCTableReader {


    public SQLite3TableReader(Connection connection, String tableName,
                              EmbeddedDocumentUtil embeddedDocumentUtil) {
        super(connection, tableName, embeddedDocumentUtil);
    }


    /**
     * No-op for now in {@link SQLite3TableReader}.
     *
     * @param tableName
     * @param fieldName
     * @param rowNum
     * @param resultSet
     * @param columnIndex
     * @param handler
     * @param context
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     * @throws org.xml.sax.SAXException
     */
    @Override
    protected void handleClob(String tableName, String fieldName, int rowNum, ResultSet resultSet,
                              int columnIndex, ContentHandler handler, ParseContext context)
            throws SQLException, IOException, SAXException {
        //no-op for now.
    }

    @Override
    protected Blob getBlob(ResultSet resultSet, int columnIndex, Metadata m) throws SQLException {
        byte[] bytes = resultSet.getBytes(columnIndex);
        if (!resultSet.wasNull()) {
            return new SerialBlob(bytes);
        }
        return null;
    }
}
