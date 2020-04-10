package ru.otus.hw09.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sergey
 * created on 03.02.19.
 */
public class DbExecutor<T> {
  private static Logger logger = LoggerFactory.getLogger(DbExecutor.class);

  @FunctionalInterface
  public interface ParamsAdder {
    void accept(PreparedStatement t) throws SQLException;
  }

  @FunctionalInterface
  public interface ObjectReader<T> {
    T apply(ResultSet rs) throws SQLException;
  }

  public long insertRecord(Connection connection, String sql, ParamsAdder paramsAdder) throws SQLException {
    Savepoint savePoint = connection.setSavepoint("savePointName");
    try (PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      paramsAdder.accept(pst);
      pst.executeUpdate();
      try (ResultSet rs = pst.getGeneratedKeys()) {
        rs.next();
        return rs.getInt(1);
      }
    } catch (SQLException ex) {
      connection.rollback(savePoint);
      logger.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public void updateRecord(Connection connection, String sql, ParamsAdder paramsAdder) throws SQLException {
    Savepoint savePoint = connection.setSavepoint("savePointName");
    try (PreparedStatement pst = connection.prepareStatement(sql)) {
      paramsAdder.accept(pst);
      pst.executeUpdate();
    } catch (SQLException ex) {
      connection.rollback(savePoint);
      logger.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  public Optional<T> selectRecord(Connection connection, String sql, long id, ObjectReader<T> rsHandler) throws SQLException {
    try (PreparedStatement pst = connection.prepareStatement(sql)) {
      pst.setLong(1, id);
      try (ResultSet rs = pst.executeQuery()) {
        return Optional.ofNullable(rsHandler.apply(rs));
      }
    }
  }
}
