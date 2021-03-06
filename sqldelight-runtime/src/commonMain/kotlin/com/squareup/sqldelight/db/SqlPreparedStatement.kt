/*
 * Copyright (C) 2018 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.squareup.sqldelight.db

interface SqlPreparedStatement {
  // Would be nice to just rename to the types: https://github.com/JetBrains/kotlin-native/issues/2371
  fun bindBytes(index: Int, value: ByteArray?)
  fun bindLong(index: Int, value: Long?)
  fun bindDouble(index: Int, value: Double?)
  fun bindString(index: Int, value: String?)

  fun executeQuery(): SqlCursor

  /**
   * Executes the SQL statement in this [SqlPreparedStatement], which must be an
   * SQL Data Manipulation Language (DML) statement, such as `INSERT`, `UPDATE` or
   * `DELETE`; or an SQL statement that returns nothing, such as a DDL statement.
   */
  fun execute()

  enum class Type {
    INSERT, UPDATE, DELETE, SELECT, EXECUTE
  }
}
