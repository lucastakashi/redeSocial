package login;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import javax.naming.spi.DirStateFactory;
import javax.swing.JOptionPane;

public class Conexao {

    private String url = "jdbc:postgresql://localhost/MTP";
    //usuario postgres
    private String usuario = "postgres";
    //senha
    private String senha = "jvpdne587363";
    //variavel que guarda a conexao
    private Connection conn;

    public Conexao()  {
        conectar();
    }

    void conectar() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        Properties props = new Properties();
        props.setProperty("user", this.usuario);
        props.setProperty("password", this.senha);
        try {
            this.conn = DriverManager.getConnection(this.url, props);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Usuario Login(String email, String senha) {
        Usuario us = new Usuario();
        try {
            PreparedStatement ps = this.conn
                    .prepareStatement("SELECT id, nome,email,cidade_estado FROM pessoa WHERE email =? AND  senha= ?");
            ps.setString(1, email);// é colocado no primeiro sinal de (?) da linha 27.
            ps.setString(2, senha);// é colocado no segundo sinal de (?) da linha 27.
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {//next anda uma casa adiante da atual

                us.setId(rs.getInt(1));
                us.setNome(rs.getString(2));
                us.setEmail(rs.getString(3));
                us.setCidadeEstado(rs.getString(4));
                return us;
            } else {
                JOptionPane.showMessageDialog(null, "usuario vazio");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return us;
    }

    public ArrayList<ClassePosts> Post(int id) {
        ArrayList<ClassePosts> cp = new ArrayList<ClassePosts>();
        try {
            PreparedStatement ps = this.conn.prepareStatement("SELECT  id,texto,imagem,pessoa_id from post where pessoa_id=?;");
            ps.setInt(1, id);
            ResultSet rd = ps.executeQuery();
            while (rd.next()) {
                ClassePosts pos = new ClassePosts();
                pos.setId(rd.getInt(1));
                pos.setImagem(rd.getBytes(3));
                pos.setTexto(rd.getString(2));
                pos.setPessoaId(rd.getInt(4));
                cp.add(pos);
            }
        } catch (Exception e) {
        }
        return cp;
    }

    public String CadastroBD (String email, String nome, String senha, String cidade_estado) {
        try {
            PreparedStatement ps = this.conn.prepareStatement("INSERT INTO public.pessoa(email,nome,  senha, cidade_estado) values(?,?,?,?)");

            ps.setString(1, email);
            ps.setString(2, nome);
            ps.setString(3, senha);
            ps.setString(4, cidade_estado);

            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void atualizar(String nome, String email, String senha, String cidade_estado) {
        try {
            PreparedStatement ps = this.conn.prepareStatement(" UPDATE public.pessoa SET nome=?, email=?, senha=?, cidade_estado=? WHERE id=?;");
            ps.setString(1, nome);
            ps.setString(2, email);
            ps.setString(3, senha);
            ps.setString(4, cidade_estado);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

        //classe que envia uma imagem ao banco
    public boolean inserirPost(String texto, byte[] imagem) {

        try {
            PreparedStatement ps=this.conn.prepareStatement("INSERT INTO post(texto,imagem) VALUES(?,?)");
            ps.setString(1,texto);
            ps.setBytes(2, imagem);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
        }
        return true;
    }


}
