package br.com.firmino.gestaovenda.modelo.util;

import br.com.firmino.gestaovenda.modelo.entidades.Cliente;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class ClienteTableModel extends AbstractTableModel{
    
    private List<Cliente> clientes;
    private final String [] colunas = {"ID", "NOME", "TELEFONE", "MORADA"};

    public ClienteTableModel(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    @Override
    public int getRowCount() {
        return clientes.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public Object getValueAt(int linha, int coluna) {
        Cliente cliente = clientes.get(linha);
        
        return switch (coluna) {
            case 0 -> cliente.getId();
            case 1 -> cliente.getNome();
            case 2 -> cliente.getTelefone();
            case 3 -> cliente.getMorada();
            default -> "";
        };
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    @Override
    public String getColumnName(int column) {
        return colunas[column];
    }

      @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false; 
    }

    public void setCliente(List<Cliente> clientes) {
        this.clientes = clientes;
    }  
    
    
    
}
