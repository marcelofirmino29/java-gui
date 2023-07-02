package br.com.firmino.gestaovenda.modelo.controller;

import br.com.firmino.gestaovenda.modelo.conexao.Conexao;
import br.com.firmino.gestaovenda.modelo.conexao.ConexaoMysql;
import br.com.firmino.gestaovenda.modelo.dao.AutenticacaoDao;
import br.com.firmino.gestaovenda.modelo.dao.ClienteDao;
import br.com.firmino.gestaovenda.modelo.entidades.Cliente;
import br.com.firmino.gestaovenda.modelo.entidades.Usuario;
import br.com.firmino.gestaovenda.modelo.exception.NegocioException;
import br.com.firmino.gestaovenda.modelo.util.ClienteTableModel;
import br.com.firmino.gestaovenda.view.formulario.Dashboard;
import br.com.firmino.gestaovenda.modelo.dao.UsuarioDao;
import br.com.firmino.gestaovenda.modelo.entidades.Categoria;
import br.com.firmino.gestaovenda.modelo.entidades.PERFIL;
import br.com.firmino.gestaovenda.modelo.entidades.Produto;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

public class ClienteController implements ActionListener, MouseListener, KeyListener {

    private final Conexao conexao;
    private final Dashboard dashboard;
    private final AutenticacaoDao autenticacaoDao;
    private final UsuarioDao usuarioDao;
    private ClienteTableModel clienteTableModel;
    private Cliente cliente;
    private final ClienteDao clienteDao;

    public ClienteController(Dashboard dashboard) {
        this.conexao = new ConexaoMysql();
        this.usuarioDao = new UsuarioDao();
        this.autenticacaoDao = new AutenticacaoDao();
        this.dashboard = dashboard;
        this.clienteDao = new ClienteDao();
        actualizarTabela(clienteDao.todosClientes());
    }

    private void ocultaTelaCliente() {
        this.dashboard.getDialogCliente().pack();
        this.dashboard.getDialogCliente().setLocationRelativeTo(dashboard);
        this.dashboard.getDialogCliente().setVisible(false);
        limpaCampo();
        mensagemNaTela("", Color.WHITE);
    }

    private void limpaCampo() {
        this.dashboard.getTxtClienteId().setText("0");
        this.dashboard.getTxtClienteNome().setText("");
        actualizarTabela(clienteDao.todosClientes());
        this.cliente = null;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String accao = ae.getActionCommand().toLowerCase();

        switch (accao) {
            case "adicionar" ->
                adicionar();
            case "editar" ->
                editar();
            case "apagar" ->
                remover();
            case "salvar" ->
                salvar();
            case "cancelar" ->
                ocultaTelaCliente();
        }
    }

    private void salvar() {
        Cliente clienteTemp = pegarValoresDoFormulario();
        String mensagem = clienteDao.salvar(clienteTemp);

        if (mensagem.startsWith("Cliente")) {
            mensagemNaTela(mensagem, Color.GREEN);

            limpaCampo();
        } else {
            mensagemNaTela(mensagem, Color.RED);
        }
    }

    private void mensagemNaTela(String mensagem, Color color) {
        this.dashboard.getLabelClienteMensagem().setBackground(color);
        this.dashboard.getLabelClienteMensagem().setText(mensagem);
    }

    private void cancelar() {
        limpar();
        this.dashboard.getDialogCliente().setVisible(false);
    }

    private void limpar() {
        this.dashboard.getTxtClienteId().setText("0");
        this.dashboard.getTxtClienteNome().setText("");
        this.dashboard.getTxtClienteTelefone().setText("");
        this.dashboard.getTxtClienteMorada().setText("");
    }

    private void adicionar() {
        mostrarTela();
    }

    private void actualizarTabela(List<Cliente> clientes) {
        this.clienteTableModel = new ClienteTableModel(clientes);
        this.dashboard.getTabelaCliente().setModel(clienteTableModel);
        this.dashboard.getLabelHomeCliente().setText(String.format("%d", clientes.size()));
    }

    private void mostrarTela() {
        this.dashboard.getDialogCliente().pack();
        this.dashboard.getDialogCliente().setLocationRelativeTo(dashboard);
        this.dashboard.getDialogCliente().setVisible(true);
    }

    private void editar() {
        int linhaSelecionada = dashboard.getTabelaCliente().getSelectedRow();
        if (linhaSelecionada >= 0) {
            Cliente cliente = clienteTableModel.getClientes().get(linhaSelecionada);

            // Preencha os campos relevantes na interface do usuário com os dados do cliente para permitir a edição
            dashboard.getTxtClienteId().setText(String.valueOf(cliente.getId()));
            dashboard.getTxtClienteNome().setText(cliente.getNome());
            dashboard.getTxtClienteTelefone().setText(cliente.getTelefone());
            dashboard.getTxtClienteMorada().setText(cliente.getMorada());

            mostrarTela(); // Mostra a tela de clientes para permitir a edição
        }
    }

    private void validacaoDoCampo(String campo, String nomeDaVariavel) {
        if (campo.isEmpty()) {
            String mensagem = String.format("Você deve preencher o campo %s", nomeDaVariavel);
            mensagemNaTela(mensagem, Color.RED);
            throw new NegocioException(mensagem);
        }
    }

    private Cliente pegarValoresDoFormulario() {

        String idString = this.dashboard.getTxtClienteId().getText();
        String nome = this.dashboard.getTxtClienteNome().getText();
        String telefone = this.dashboard.getTxtClienteTelefone().getText();
        String morada = this.dashboard.getTxtClienteMorada().getText();

        Long id = Long.valueOf(idString);
        validacaoDoCampo(nome);
        validacaoDoCampo(telefone, "telefone");
        validacaoDoCampo(morada, "morada");

        return new Cliente(id, nome, telefone, morada);
    }

    private void validacaoDoCampo(String campo) {
        if (campo.isEmpty()) {
            String mensagem = "Deves preencher o campo nome";
            mensagemNaTela(mensagem, Color.RED);
            throw new NegocioException(mensagem);
        }
    }

    private void remover() {
        Usuario usuario = usuarioLogado();
        if (autenticacaoDao.temPermissao(usuario)) {
            System.out.println("Valor de this.cliente: " + this.cliente);
            if (this.cliente != null) {
                int confirmar = JOptionPane.showConfirmDialog(dashboard,
                        String.format("Você tem certeza que deseja apagar? \nNome: %s", this.cliente.getNome()),
                        "Apagar cliente", JOptionPane.YES_NO_OPTION);

                if (confirmar == JOptionPane.YES_OPTION) {
                    String mensagem = clienteDao.deleteClientePeloId(this.cliente.getId());
                    JOptionPane.showMessageDialog(dashboard, mensagem);
                    limpaCampo();
                }
            } else {
                JOptionPane.showMessageDialog(dashboard, "Você deve selecionar um cliente na tabela", "Seleciona um produto", 0);
            }
        }
    }

    private void preencherOsValoresNoFormularioCliente() {
        this.dashboard.getTxtClienteId().setText(this.cliente.getId().toString());
        this.dashboard.getTxtClienteNome().setText(this.cliente.getNome());
        this.dashboard.getTxtClienteTelefone().setText(this.cliente.getTelefone());
        this.dashboard.getTxtClienteMorada().setText(this.cliente.getMorada());
    }

    public void addKeyListener(ClienteController clienteController) {
        dashboard.getTxtClientePesquisar().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent ke) {
                clienteController.keyReleased(ke);
            }
        }
        );
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        int linhaSelecionada = this.dashboard.getTabelaCliente().getSelectedRow();
        this.cliente = this.clienteTableModel.getClientes().get(linhaSelecionada);
        preencherOsValoresNoFormularioCliente();
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        String pesquisar = this.dashboard.getTxtClientePesquisar().getText();

        if (pesquisar.isEmpty()) {
            actualizarTabela(clienteDao.todosClientes());
        } else {
            List<Cliente> clientesTemp = this.clienteDao.todosClientes()
                    .stream()
                    .filter((Cliente c) -> {
                        return c.getNome().toLowerCase().contains(pesquisar.toLowerCase());
                        //    || c.getTelefone().toLowerCase().contains(pesquisar.toLowerCase())
                        //   || c.getMorada().toLowerCase().contains(pesquisar.toLowerCase());
                    })
                    .collect(Collectors.toList());

            actualizarTabela(clientesTemp);
        }
    }

    private Usuario usuarioLogado() {
        Long usuarioLogadoId = Long.valueOf(this.dashboard.getLabelUsuarioLogadoId().getText());
        return usuarioDao.buscarUsuarioPeloId(usuarioLogadoId);
    }

}
