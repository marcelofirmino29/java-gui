package br.com.firmino.gestaovenda.modelo.util;


import java.util.HashMap;

import java.util.stream.Collectors;


import javax.swing.table.AbstractTableModel;


import br.com.firmino.gestaovenda.modelo.entidades.VendaDetalhes;


public class VendaRegistroTableModel extends AbstractTableModel {
    
    private HashMap<String, VendaDetalhes> vendaDetalhes;
    private final String [] colunas = {"PRODUTO", "PREÇO", "QTD", "DESCONTO", "TOTAL"};
    
    public VendaRegistroTableModel(HashMap<String, VendaDetalhes> vendaDetalhes) {
        this.vendaDetalhes = vendaDetalhes;
    }

    @Override
    public int getRowCount() {
        return vendaDetalhes.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public Object getValueAt(int linha, int coluna) {
        VendaDetalhes vendaDetalhess = vendaDetalhes
                .values()
                .stream()
                .collect(Collectors.toList()).get(linha);
        
        return switch (coluna) {
            case 0 -> vendaDetalhess.getProduto().getNome();
            case 1 -> vendaDetalhess.getProduto().getPreco();
            case 2 -> vendaDetalhess.getQuantidade();
            case 3 -> vendaDetalhess.getDesconto();
            case 4 -> vendaDetalhess.getTotal();
            default -> "";
        };
    }

    @Override
    public String getColumnName(int column) {
        return colunas[column];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public HashMap<String, VendaDetalhes> getVendaDetalhes() {
        return vendaDetalhes;
    }

    public void setVendaDetalhes(HashMap<String, VendaDetalhes> vendaDetalhes) {
        this.vendaDetalhes = vendaDetalhes;
    }
    
    
}
