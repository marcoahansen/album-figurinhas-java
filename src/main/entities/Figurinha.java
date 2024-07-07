package main.entities;

public class Figurinha {
    private int id;
    private String nome;
    private int numero;
    private String descricao;
    private int pagina;
    private String tag;
    private byte[] foto;
    private int album_id;

    public Figurinha(int id, String nome, int numero, String descricao, int pagina, String tag, byte[] foto, int album_id) {
        this.id = id;
        this.nome = nome;
        this.numero = numero;
        this.descricao = descricao;
        this.pagina = pagina;
        this.tag = tag;
        this.foto = foto;
        this.album_id = album_id;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public int getPagina() { return pagina; }
    public void setPagina(int pagina) { this.pagina = pagina; }

    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }

    public byte[] getFoto() { return foto; }
    public void setFoto(byte[] foto) { this.foto = foto; }

    public int getAlbumId() { return album_id; }
    public void setAlbumId(int album_id) { this.album_id = album_id; }
}
