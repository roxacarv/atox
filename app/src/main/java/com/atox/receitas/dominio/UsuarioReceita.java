package com.atox.receitas.dominio;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.atox.usuario.dominio.Usuario;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "usuario_receita", indices = {@Index("usuario_id"), @Index("receita_id")},
        foreignKeys = { @ForeignKey(entity = Usuario.class,
                                    parentColumns = "uid",
                                    childColumns = "usuario_id",
                                    onDelete = CASCADE),
                        @ForeignKey(entity = Receita.class,
                                    parentColumns = "rid",
                                    childColumns = "receita_id",
                                    onDelete = CASCADE)
        })
public class UsuarioReceita {

    @PrimaryKey(autoGenerate = true)
    private Long urid;

    @ColumnInfo(name = "receita_id")
    private Long receitaId;

    @ColumnInfo(name = "usuario_id")
    private Long usuarioId;

    public Long getUrid() {
        return urid;
    }

    public void setUrid(Long urid) {
        this.urid = urid;
    }

    public Long getReceitaId() {
        return receitaId;
    }

    public void setReceitaId(Long receitaId) {
        this.receitaId = receitaId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}
