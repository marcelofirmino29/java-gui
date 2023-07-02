package br.com.firmino.gestaovenda.modelo.util;

import br.com.firmino.gestaovenda.modelo.entidades.Venda;
import java.util.List;
import javax.swing.table.AbstractTableModel;


public class VendaTableModel extends AbstractTableModel {
    
    private List<Venda> vendas;
    private final String [] colunas = {"ID", "CLIENTE", "TOTAL", "VALOR PAGO", "DESCONTO", "TROCO", "DATA", "VENDIDO POR"};

    public VendaTableModel(List<Venda> vendas) {
        this.vendas = vendas;
    }

    @Override
    public int getRowCount() {
        return vendas.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public Object getValueAt(int linha, int coluna) {
        Venda venda = vendas.get(linha);
        
        return switch (coluna) {
            case 0 -> venda.getId();
            case 1 -> venda.getCliente().getNome();
            case 2 -> venda.getTotalVenda();
            case 3 -> venda.getValorPago();
            case 4 -> venda.getDesconto();
            case 5 -> venda.getTroco();
            case 6 -> venda.getDataHoraCriacao();
            case 7 -> venda.getUsuario().getNome();
            default -> "";
        };
    }

    @Override
    public String getColumnName(int column) {
        return colunas[column];
    }

    public List<Venda> getVendas() {
        return vendas;
    }

    public void setVendas(List<Venda> vendas) {
        this.vendas = vendas;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false; 
    }
    
}
