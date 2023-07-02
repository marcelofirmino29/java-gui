package br.com.firmino.gestaovenda.modelo.dao;

import br.com.firmino.gestaovenda.modelo.entidades.PERFIL;
import br.com.firmino.gestaovenda.modelo.entidades.Usuario;
import br.com.firmino.gestaovenda.modelo.exception.NegocioException;
import br.com.firmino.gestaovenda.view.modelo.LoginDto;
import javax.swing.JOptionPane;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class AutenticacaoDao {
    
    private final UsuarioDao usuarioDao;

    public AutenticacaoDao() {
        this.usuarioDao = new UsuarioDao();
    }
    
    public boolean temPermissao(Usuario usuario) {
        try {
            permissao(usuario);
            return true;
        } catch (NegocioException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Usuario sem permissao", 0);
            System.out.println("ERROR: " + e.getMessage());
            return false;
        }
    }
    
    private void permissao(Usuario usuario) {
        if(!PERFIL.ADMIN.equals(usuario.getPerfil())) {
            throw new NegocioException("Sem permissao para realizar essa accao.");
        }
    }

    public Usuario login(LoginDto login) {
        Usuario usuario = usuarioDao.buscarUsuarioPeloUsername(login.getUsername());
        
        if(usuario == null || !usuario.isEstado()) 
            return null;
       
        if(usuario.isEstado() && validaSenha(usuario.getSenha(), login.getSenha())) {
            usuarioDao.actualizarUltimoLogin(usuario);
            return usuario;
        }
        return null;
    }
    
  //  private boolean validaSenha(String usuarioSenha, String loginSenha) {
  //      return usuarioSenha.equals(loginSenha);
   // }
    
    private boolean validaSenha(String usuarioSenha, String loginSenha) {
       BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
       return passwordEncoder.matches(loginSenha, usuarioSenha);
  }
    
}
