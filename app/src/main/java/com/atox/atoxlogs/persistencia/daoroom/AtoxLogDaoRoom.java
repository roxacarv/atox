package com.atox.atoxlogs.persistencia.daoroom;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.atox.atoxlogs.AtoxLog;
import com.atox.infra.persistencia.DaoBase;

import java.util.List;

@Dao
public interface AtoxLogDaoRoom extends DaoBase<AtoxLog> {

    @Query("SELECT * FROM atox_log")
    List<AtoxLog> buscarTodosLogs();

    @Query("SELECT * FROM atox_log WHERE usuario_log_id LIKE :ulid")
    List<AtoxLog> buscarLogsDoUsuario(long ulid);

    @Query("SELECT * FROM atox_log WHERE usuario_log_id LIKE :ulid AND tipo LIKE :tipo")
    List<AtoxLog> buscarLogsDoUsuarioPorTipo(long ulid, long tipo);

    @Query("SELECT * FROM atox_log WHERE acao LIKE :acao")
    List<AtoxLog> buscarLogsPorAcao(long acao);

}
