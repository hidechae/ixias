/*
 * This file is part of the IxiaS services.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */

package net.ixias
package core.port.adapter.persistence.model

import org.joda.time.DateTime
import slick.driver.JdbcProfile
import core.domain.model._
import org.specs2.mutable.Specification

/** テーブル定義 */
case class UserTable[P <: JdbcProfile](val driver: P) extends Table[P] { self =>
  import api._

  /** DNS定義 */
  lazy val dsn = Map(
    "master" -> DataSourceName("ixais.db.slick://master/test"),
    "slave"  -> DataSourceName("ixais.db.slick://slave/test")
  )

  /** レコード定義 */
  case class Record (
    val id:        Long,
    val name:      Option[String],
    val email:     Option[String],
    val updatedAt: DateTime = new DateTime,
    val createdAt: DateTime = new DateTime
  )

  /** テーブル定義 */
  class Table(tag: Tag) extends BasicTable(tag, "user") {
    def id        = column[Long]           ("uid",        O.AsciiChar8, O.PrimaryKey)
    def name      = column[Option[String]] ("name",       O.AsciiChar8, O.PrimaryKey)
    def email     = column[Option[String]] ("email",      O.AsciiChar8, O.PrimaryKey)
    def updatedAt = column[DateTime]       ("updated_at", O.TsCurrent)
    def createdAt = column[DateTime]       ("created_at", O.Ts)
    def * = (id, name, email, updatedAt, createdAt) <> (Record.tupled, Record.unapply)
  }

  /** クエリー定義 */
  class Query extends BasicQuery(new Table(_))
  lazy val query = new Query
}
