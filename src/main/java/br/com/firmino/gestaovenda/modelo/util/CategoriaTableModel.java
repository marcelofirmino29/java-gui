package br.com.firmino.gestaovenda.modelo.util;

import br.com.firmino.gestaovenda.modelo.entidades.Categoria;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class CategoriaTableModel extends AbstractTableModel {
    
    private final List<Categoria> categorias;
    private final String [] colunas = {"ID", "NOME", "DESCRICAO"};

    public CategoriaTableModel(List<Categoria> categorias) {
        this.categorias = categorias;
    }
    

    @Override
    public int getRowCount() {
        return categorias.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public Object getValueAt(int linha, int coluna) {
        Categoria categoria = categorias.get(linha);
        
        return switch (coluna) {
            case 0 -> categoria.getId();
            case 1 -> categoria.getNome();
            case 2 -> categoria.getDescricao();
            default -> "";
        };
    }

    @Override
    public String getColumnName(int column) {
        return colunas[column]; 
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false; //To change body of generated methods, choose Tools | Templates.
    }
}
